package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoDTO(
        @NotNull Long produtoId,
        @Min(value = 1, message = "A quantidade deve ser maior que 0") int quantidade
) {}