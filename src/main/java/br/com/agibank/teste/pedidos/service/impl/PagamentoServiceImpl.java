package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.producer.PagamentoRequestProducer;
import br.com.agibank.teste.pedidos.repository.PedidoRepository;
import br.com.agibank.teste.pedidos.service.PagamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRequestProducer pagamentoRequestProducer;
    private final PedidoRepository pedidoRepository;

    public PagamentoServiceImpl(PagamentoRequestProducer pagamentoRequestProducer, PedidoRepository pedidoRepository) {
        this.pagamentoRequestProducer = pagamentoRequestProducer;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public String enviarParaPagamento(ProcessarPagamentoDTO pagamento) {
        try {
            Pedido pedido = pedidoRepository.findById(pagamento.idPedido()).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o ID: " + pagamento.idPedido()));

            if(pedido.getStatusPedido() == StatusPedido.AGUARDANDO_PAGAMENTO){
                return pagamentoRequestProducer.enviarParaPagamento(pagamento);
            }else{
                return "O pedido não está aguardando pagamento, status atual: " + pedido.getStatusPedido();
            }
        } catch (JsonProcessingException e) {
            return "Houve um erro ao solicitar o pagamento " + e.getMessage();
        }
    }
}
