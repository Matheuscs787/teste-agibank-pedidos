package br.com.agibank.teste.pedidos.service;

import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;

public interface PagamentoService {

    String enviarParaPagamento(ProcessarPagamentoDTO pagamento);

}
