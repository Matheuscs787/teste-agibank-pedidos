package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.producer.PagamentoRequestProducer;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoServiceImplTest {
    private PagamentoRequestProducer pagamentoRequestProducer;
    private PedidoRepository pedidoRepository;
    private PagamentoServiceImpl pagamentoService;

    @BeforeEach
    void setUp() {
        pagamentoRequestProducer = mock(PagamentoRequestProducer.class);
        pedidoRepository = mock(PedidoRepository.class);
        pagamentoService = new PagamentoServiceImpl(pagamentoRequestProducer, pedidoRepository);
    }

    @Test
    void deveEnviarParaPagamentoQuandoPedidoEstiverAguardandoPagamento() throws JsonProcessingException {
        Long idPedido = 1L;
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(idPedido, FormaPagamento.CARTAO_CREDITO, 3, new BigDecimal("150.00"), StatusPedido.AGUARDANDO_PAGAMENTO);
        Pedido pedido = new Pedido();
        pedido.setId(idPedido);
        pedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));
        when(pagamentoRequestProducer.enviarParaPagamento(dto)).thenReturn("Pagamento enviado");

        String resultado = pagamentoService.enviarParaPagamento(dto);

        assertEquals("Pagamento enviado", resultado);
        verify(pagamentoRequestProducer, times(1)).enviarParaPagamento(dto);
    }

    @Test
    void deveRetornarMensagemQuandoStatusPedidoNaoForAguardandoPagamento() throws JsonProcessingException {
        Long idPedido = 2L;
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(idPedido, FormaPagamento.CARTAO_CREDITO, 3, new BigDecimal("150.00"), StatusPedido.AGUARDANDO_PAGAMENTO);
        Pedido pedido = new Pedido();
        pedido.setId(idPedido);
        pedido.setStatusPedido(StatusPedido.CRIADO);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        String resultado = pagamentoService.enviarParaPagamento(dto);

        assertEquals("O pedido não está aguardando pagamento, status atual: CRIADO", resultado);
        verify(pagamentoRequestProducer, never()).enviarParaPagamento(any());
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() throws JsonProcessingException {
        Long idPedido = 3L;
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(idPedido, FormaPagamento.CARTAO_CREDITO, 3, new BigDecimal("150.00"), StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(RecursoNaoEncontradoException.class, () ->
                pagamentoService.enviarParaPagamento(dto));

        assertEquals("Pedido não encontrado para o ID: 3", ex.getMessage());
        verify(pagamentoRequestProducer, never()).enviarParaPagamento(any());
    }

    @Test
    void deveRetornarMensagemDeErroQuandoJsonProcessingExceptionForLancada() throws JsonProcessingException {
        Long idPedido = 4L;
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(idPedido, FormaPagamento.CARTAO_CREDITO, 3, new BigDecimal("150.00"), StatusPedido.AGUARDANDO_PAGAMENTO);
        Pedido pedido = new Pedido();
        pedido.setId(idPedido);
        pedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));
        when(pagamentoRequestProducer.enviarParaPagamento(dto)).thenThrow(new JsonProcessingException("Erro JSON") {});

        String resultado = pagamentoService.enviarParaPagamento(dto);

        assertTrue(resultado.contains("Houve um erro ao solicitar o pagamento"));
        assertTrue(resultado.contains("Erro JSON"));
    }
}