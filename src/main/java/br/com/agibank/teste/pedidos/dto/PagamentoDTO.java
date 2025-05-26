package br.com.agibank.teste.pedidos.dto;

import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PagamentoDTO(FormaPagamento formaPagamento,
                           @Min(value = 1, message = "Número de parcelas deve ser maior que 0.")
                           @Max(value = 10, message = "O número máximo de parcelas é 10.")
                           Integer numeroParcelas) {
}
