package br.com.agibank.teste.pedidos.dto;

import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BuscarPedidoResponseDTO(Long id, LocalDateTime dataPedido, FormaPagamento formaPagamento, StatusPedido statusPedido, BigDecimal valorTotal) {
}
