package br.com.agibank.teste.pedidos.dto;

import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;

import java.math.BigDecimal;

public record ProcessarPagamentoDTO(Long idPedido, FormaPagamento formaPagamento, Integer quantidadeParcelas, BigDecimal valorTotal, StatusPedido statusPedido) {
}
