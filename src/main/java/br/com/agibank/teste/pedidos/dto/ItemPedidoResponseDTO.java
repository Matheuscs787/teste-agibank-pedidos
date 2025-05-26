package br.com.agibank.teste.pedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(Long id, String nome, Integer quantidade, BigDecimal valorUnitario) {
}
