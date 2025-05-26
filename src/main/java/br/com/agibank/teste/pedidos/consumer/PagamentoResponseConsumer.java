package br.com.agibank.teste.pedidos.consumer;

import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoResponseConsumer {

    private final PedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public PagamentoResponseConsumer(PedidoRepository pedidoRepository, ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${topicos.pagamento.response.topic}",
            groupId = "pagamento-response-consumer-1"
    )
    public void consume(String message) throws JsonProcessingException {
        ProcessarPagamentoDTO pagamentoProcessado = objectMapper.readValue(message, ProcessarPagamentoDTO.class);
        processarRetornoPagamento(pagamentoProcessado);
    }

    private void processarRetornoPagamento(ProcessarPagamentoDTO pagamentoProcessado) {
        Pedido pedido = pedidoRepository.findById(pagamentoProcessado.idPedido()).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com o ID: " + pagamentoProcessado.idPedido()));

        pedido.setStatusPedido(pagamentoProcessado.statusPedido());
        pedidoRepository.save(pedido);

        while (pedido.getStatusPedido() != StatusPedido.CONCLUIDO &&
                pedido.getStatusPedido() != StatusPedido.CANCELADO) {
            evoluirPedido(pedido);
        }
    }

    private void evoluirPedido(Pedido pedido) {
        StatusPedido proximo = proximoStatus(pedido.getStatusPedido());

        if (proximo != null) {
            pedido.setStatusPedido(proximo);
            pedidoRepository.save(pedido);
        }
    }

    private StatusPedido proximoStatus(StatusPedido atual) {
        return switch (atual) {
            case PAGAMENTO_APROVADO -> StatusPedido.EM_PROCESSAMENTO;
            case EM_PROCESSAMENTO -> StatusPedido.ENVIADO;
            case ENVIADO -> StatusPedido.ENTREGUE;
            case ENTREGUE -> StatusPedido.CONCLUIDO;
            case PAGAMENTO_RECUSADO -> StatusPedido.CANCELADO;
            default -> null;
        };
    }
}
