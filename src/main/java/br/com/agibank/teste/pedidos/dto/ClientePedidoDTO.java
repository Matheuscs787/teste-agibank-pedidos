package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientePedidoDTO(@NotNull(message = "O CPF/CNPJ do cliente é obrigatório")
                               @Size(min = 11, max = 14, message = "O número do CPF/CNPJ deve ter entre 11 e 14 caracteres")
                               String cpfCnpj) {}
