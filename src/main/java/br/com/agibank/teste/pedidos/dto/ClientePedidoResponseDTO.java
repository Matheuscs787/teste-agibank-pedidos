package br.com.agibank.teste.pedidos.dto;

public record ClientePedidoResponseDTO(Long id, String nome, String cpfCnpj, String email, String telefone) {
}
