package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        Long id,
        @NotNull(message = "O nome do produto deve ser informado")
        @NotEmpty(message = "O nome do produto deve ser informado")
        String nome,
        String descricao,
        @NotNull(message = "O valor do produto não pode ter o valor zerado.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O valor do produto deve ser maior que 0.0")
        BigDecimal preco) {
}
