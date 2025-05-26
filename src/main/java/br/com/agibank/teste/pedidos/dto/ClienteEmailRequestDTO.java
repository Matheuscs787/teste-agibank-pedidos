package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ClienteEmailRequestDTO(@Email @NotNull String email) {
}
