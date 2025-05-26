package br.com.agibank.teste.pedidos.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RealizarPedidoRequestDTO(
        @NotNull(message = "Os dados do cliente são obrigatórios")
        ClientePedidoDTO cliente,
        @NotEmpty(message = "O pedido deve conter ao menos um produto")
        List<ItemPedidoDTO> itens,
        @NotNull(message = "É necessário informar a forma de pagamento.")
        PagamentoDTO pagamento
    ){}
