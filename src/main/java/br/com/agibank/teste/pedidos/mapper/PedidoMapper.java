package br.com.agibank.teste.pedidos.mapper;

import br.com.agibank.teste.pedidos.dto.*;
import br.com.agibank.teste.pedidos.entities.Pedido;
import br.com.agibank.teste.pedidos.entities.PedidoProduto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoMapper {

    public RealizarPedidoResponseDTO toRealizarPedidoResponseDTO(Pedido pedido) {

        ClientePedidoResponseDTO cliente = new ClientePedidoResponseDTO(
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getCliente().getCpfCnpj(),
                pedido.getCliente().getEmail(),
                pedido.getCliente().getTelefone());

        PagamentoDTO pagamentoDTO = new PagamentoDTO(
                pedido.getFormaPagamento(),
                pedido.getQuantidadeParcelas()
        );

        List<ItemPedidoResponseDTO> itens = new ArrayList<>();

        for(PedidoProduto pedidoProduto: pedido.getPedidoProduto()){
            ItemPedidoResponseDTO item = new ItemPedidoResponseDTO(
                    pedidoProduto.getProduto().getId(),
                    pedidoProduto.getProduto().getNome(),
                    pedidoProduto.getQuantidade(),
                    pedidoProduto.getPrecoUnitario()
            );

            itens.add(item);
        }

        return new RealizarPedidoResponseDTO(cliente,
                itens,
                pagamentoDTO,
                pedido.getValorTotal(),
                pedido.getStatusPedido());
    }

    public BuscarPedidoResponseDTO toBuscarPedidoResponseDTO(Pedido pedido) {
        return new BuscarPedidoResponseDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getFormaPagamento(),
                pedido.getStatusPedido(),
                pedido.getValorTotal()
        );
    }
}
