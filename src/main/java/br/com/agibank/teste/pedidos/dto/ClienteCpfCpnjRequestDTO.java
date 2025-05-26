package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteCpfCpnjRequestDTO(@NotNull
                                       @Size(min = 11, max = 14, message = "O número do cpfCnpj deve ter entre 11 e 14 caracteres")
                                       String cpfCnpj) {
}
