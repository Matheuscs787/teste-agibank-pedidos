package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.*;
import br.com.agibank.teste.pedidos.entities.Cliente;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.entities.PedidoProduto;
import br.com.agibank.teste.pedidos.entities.Produto;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.mapper.PedidoMapper;
import br.com.agibank.teste.pedidos.repository.ClienteRepository;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import br.com.agibank.teste.pedidos.repository.ProdutoRepository;
import br.com.agibank.teste.pedidos.service.PagamentoService;
import br.com.agibank.teste.pedidos.service.PedidoService;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final ApplicationContext context;
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;
    private final PagamentoService pagamentoService;

    public PedidoServiceImpl(ApplicationContext context, PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ProdutoRepository produtoRepository, PedidoMapper pedidoMapper, PagamentoService pagamentoService) {
        this.context = context;
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoMapper = pedidoMapper;
        this.pagamentoService = pagamentoService;
    }

    @Override
    public Page<BuscarPedidoResponseDTO> findByClienteCpfCnpj(String cpfCnpj, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findByClienteCpfCnpj(cpfCnpj, pageable);
        return pedidos.map(pedidoMapper::toBuscarPedidoResponseDTO);
    }

    @Override
    public Page<BuscarPedidoResponseDTO> findAll(Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return pedidos.map(pedidoMapper::toBuscarPedidoResponseDTO);
    }

    @Override
    public BuscarPedidoResponseDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o ID: " + id));
        return pedidoMapper.toBuscarPedidoResponseDTO(pedido);
    }

    @Override
    @Transactional
    public RealizarPedidoResponseDTO save(RealizarPedidoRequestDTO realizarPedidoRequestDTO) {
        Cliente cliente = clienteRepository.findByCpfCnpj(realizarPedidoRequestDTO.cliente().cpfCnpj()).orElseThrow(() ->new RecursoNaoEncontradoException("Cliente com o CPF/CNPJ " + realizarPedidoRequestDTO.cliente().cpfCnpj() + " não encontrado"));

        Pedido pedido = montarPedido(realizarPedidoRequestDTO, cliente);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        PedidoServiceImpl self = context.getBean(PedidoServiceImpl.class);
        self.atualizarStatus(pedidoSalvo);

        return pedidoMapper.toRealizarPedidoResponseDTO(pedidoSalvo);
    }

    private Pedido montarPedido(RealizarPedidoRequestDTO dto, Cliente cliente){
        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setFormaPagamento(dto.pagamento().formaPagamento());
        pedido.setQuantidadeParcelas(dto.pagamento().numeroParcelas());
        pedido.setStatusPedido(StatusPedido.CRIADO);

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<PedidoProduto> pedidoProdutos = new ArrayList<>();

        for (ItemPedidoDTO item : dto.itens()) {
            Produto produto = produtoRepository.findById(item.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com ID " + item.produtoId() + " não encontrado"));

            BigDecimal subtotal = produto.getPrecoAtual().multiply(BigDecimal.valueOf(item.quantidade()));
            valorTotal = valorTotal.add(subtotal);

            PedidoProduto pp = new PedidoProduto(produto, item.quantidade(), produto.getPrecoAtual());
            pp.setPedido(pedido);
            pedidoProdutos.add(pp);
        }

        pedido.setValorTotal(valorTotal);
        pedido.setPedidoProduto(pedidoProdutos);
        return pedido;
    }

    @Async
    public void atualizarStatus(Pedido pedido){
        try {
            Thread.sleep((long)(Math.random() * 5000) + 1000);
            pedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);
            pedidoRepository.save(pedido);

            Thread.sleep((long)(Math.random() * 5000) + 1000);

            ProcessarPagamentoDTO processarPagamentoDTO = new ProcessarPagamentoDTO(pedido.getId(), pedido.getFormaPagamento(), pedido.getQuantidadeParcelas(), pedido.getValorTotal(), pedido.getStatusPedido());

            pagamentoService.enviarParaPagamento(processarPagamentoDTO);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
