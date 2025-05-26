package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.*;
import br.com.agibank.teste.pedidos.entities.Cliente;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.entities.Produto;
import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.mapper.PedidoMapper;
import br.com.agibank.teste.pedidos.repository.ClienteRepository;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import br.com.agibank.teste.pedidos.repository.ProdutoRepository;
import br.com.agibank.teste.pedidos.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private PagamentoService pagamentoService;

    @Mock
    private ApplicationContext context;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarPedidoComSucesso() {
        String cpfCnpj = "12345678900";
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setCpfCnpj(cpfCnpj);

        Produto produto = new Produto();
        produto.setId(10L);
        produto.setPrecoAtual(new BigDecimal("100.00"));

        RealizarPedidoRequestDTO requestDTO = new RealizarPedidoRequestDTO(
                new ClientePedidoDTO(cpfCnpj),
                List.of(new ItemPedidoDTO(produto.getId(), 2)),
                new PagamentoDTO(FormaPagamento.CARTAO_CREDITO, 2)
        );

        when(clienteRepository.findByCpfCnpj(cpfCnpj)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        Pedido pedidoCriado = new Pedido();
        pedidoCriado.setId(99L);
        pedidoCriado.setCliente(cliente);
        pedidoCriado.setValorTotal(new BigDecimal("200.00"));

        when(pedidoRepository.save(any())).thenReturn(pedidoCriado);
        when(context.getBean(PedidoServiceImpl.class)).thenReturn(pedidoService);

        when(pedidoMapper.toRealizarPedidoResponseDTO(any())).thenReturn(new RealizarPedidoResponseDTO(
                new ClientePedidoResponseDTO(1l, "teste de cliente", "12345678901", "email@hotmail.com", "43990001122"),
                List.of(new ItemPedidoResponseDTO(10L, "Produto teste", 2, new BigDecimal("100.0"))),
                new PagamentoDTO(FormaPagamento.CARTAO_CREDITO, 2),
                new BigDecimal("1000.0"),
                StatusPedido.CRIADO));


        RealizarPedidoResponseDTO response = pedidoService.save(requestDTO);

        assertNotNull(response);
        assertEquals(StatusPedido.CRIADO, response.statusPedido());
        assertEquals(new BigDecimal("1000.0"), response.valorTotal());
        verify(pedidoRepository, times(2)).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        String cpfCnpj = "00000000000";
        when(clienteRepository.findByCpfCnpj(cpfCnpj)).thenReturn(Optional.empty());

        RealizarPedidoRequestDTO requestDTO = new RealizarPedidoRequestDTO(
                new ClientePedidoDTO(cpfCnpj),
                List.of(),
                new PagamentoDTO(FormaPagamento.CARTAO_DEBITO, 1)
        );

        assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.save(requestDTO));
        verify(clienteRepository, times(1)).findByCpfCnpj(cpfCnpj);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        String cpfCnpj = "12345678900";
        Cliente cliente = new Cliente();
        when(clienteRepository.findByCpfCnpj(cpfCnpj)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RealizarPedidoRequestDTO requestDTO = new RealizarPedidoRequestDTO(
                new ClientePedidoDTO(cpfCnpj),
                List.of(new ItemPedidoDTO(99L, 1)),
                new PagamentoDTO(FormaPagamento.CARTAO_DEBITO, 1)
        );

        assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.save(requestDTO));
    }

    @Test
    void deveAtualizarStatusEChamarPagamento() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
        pedido.setQuantidadeParcelas(2);
        pedido.setValorTotal(new BigDecimal("300.00"));
        pedido.setStatusPedido(StatusPedido.CRIADO);

        pedidoService.atualizarStatus(pedido);

        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatusPedido());
        verify(pagamentoService, times(1)).enviarParaPagamento(any());
    }
}
