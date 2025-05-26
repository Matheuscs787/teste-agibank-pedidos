package br.com.agibank.teste.pedidos.dto;

import br.com.agibank.teste.pedidos.enums.StatusPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record RealizarPedidoResponseDTO(@NotNull(message = "Os dados do cliente são obrigatórios")
                                        ClientePedidoResponseDTO cliente,
                                        @NotEmpty(message = "O pedido deve conter ao menos um produto")
                                        List<ItemPedidoResponseDTO> itens,
                                        @NotNull(message = "É necessário informar a forma de pagamento.")
                                        PagamentoDTO pagamento,
                                        BigDecimal valorTotal,
                                        StatusPedido statusPedido) {
}
