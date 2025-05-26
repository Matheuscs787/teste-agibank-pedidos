package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.*;

public record ClienteRequestDTO(Long id,
                                @NotNull
                                String nome,
                                @Size(min = 11, max = 14, message = "O número do cpfCnpj deve ter entre 11 e 14 caracteres")
                                @NotNull
                                String cpfCnpj,
                                @NotNull
                                @Email
                                String email,
                                @NotNull
                                String telefone) {
}
