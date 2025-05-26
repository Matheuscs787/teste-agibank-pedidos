package br.com.agibank.teste.pedidos.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PedidoProdutoId implements Serializable {

    private Long pedidoId;
    private Long produtoId;

    public PedidoProdutoId() {
    }

    public PedidoProdutoId(Long pedidoId, Long produtoId) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}
