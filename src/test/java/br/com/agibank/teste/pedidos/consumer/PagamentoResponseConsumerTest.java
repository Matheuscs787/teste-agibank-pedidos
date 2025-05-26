package br.com.agibank.teste.pedidos.consumer;

import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PagamentoResponseConsumerTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PagamentoResponseConsumer consumer;

    private ProcessarPagamentoDTO dto;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        dto = new ProcessarPagamentoDTO(1L, FormaPagamento.CARTAO_CREDITO, 1, new BigDecimal("100.0"), StatusPedido.PAGAMENTO_APROVADO);
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatusPedido(StatusPedido.PAGAMENTO_APROVADO);
    }

    @Test
    void deveConsumirMensagemKafkaComSucesso() throws JsonProcessingException {
        String mensagemJson = "{\"idPedido\":1,\"statusPedido\":\"PAGAMENTO_APROVADO\"}";

        when(objectMapper.readValue(mensagemJson, ProcessarPagamentoDTO.class)).thenReturn(dto);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consumer.consume(mensagemJson);

        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoForEncontrado() throws JsonProcessingException {
        String mensagemJson = "{\"idPedido\":1,\"statusPedido\":\"PAGAMENTO_APROVADO\"}";
        when(objectMapper.readValue(mensagemJson, ProcessarPagamentoDTO.class)).thenReturn(dto);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> consumer.consume(mensagemJson)
        );

        assertTrue(exception.getMessage().contains("Pedido não encontrado"));
    }

    @Test
    void deveEvoluirStatusAteConcluido() throws JsonProcessingException {
        pedido.setStatusPedido(StatusPedido.PAGAMENTO_APROVADO);
        when(objectMapper.readValue(anyString(), eq(ProcessarPagamentoDTO.class))).thenReturn(dto);
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);

            switch (p.getStatusPedido()) {
                case PAGAMENTO_APROVADO -> p.setStatusPedido(StatusPedido.EM_PROCESSAMENTO);
                case EM_PROCESSAMENTO -> p.setStatusPedido(StatusPedido.ENVIADO);
                case ENVIADO -> p.setStatusPedido(StatusPedido.ENTREGUE);
                case ENTREGUE -> p.setStatusPedido(StatusPedido.CONCLUIDO);
            }
            return p;
        });

        consumer.consume("{\"idPedido\":1,\"statusPedido\":\"PAGAMENTO_APROVADO\"}");

        assertEquals(StatusPedido.CONCLUIDO, pedido.getStatusPedido());
    }

    @Test
    void deveCancelarPedidoComStatusRecusado() throws JsonProcessingException {
        dto = new ProcessarPagamentoDTO(1L, FormaPagamento.CARTAO_CREDITO, 1, new BigDecimal("100.0"), StatusPedido.PAGAMENTO_RECUSADO);
        pedido.setStatusPedido(StatusPedido.PAGAMENTO_RECUSADO);

        when(objectMapper.readValue(anyString(), eq(ProcessarPagamentoDTO.class))).thenReturn(dto);
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            if (p.getStatusPedido() == StatusPedido.PAGAMENTO_RECUSADO) {
                p.setStatusPedido(StatusPedido.CANCELADO);
            }
            return p;
        });

        consumer.consume("{\"idPedido\":1,\"statusPedido\":\"PAGAMENTO_RECUSADO\"}");

        assertEquals(StatusPedido.CANCELADO, pedido.getStatusPedido());
    }
}