package br.com.agibank.teste.pedidos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_pedido_produto")
public class PedidoProduto {

    @EmbeddedId
    private PedidoProdutoId id = new PedidoProdutoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    private Produto produto;

    private Integer quantidade;

    @NotNull(message = "O valor do produto não pode ter o valor zerado.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do produto deve ser maior que 0.0")
    private BigDecimal precoUnitario;

    public PedidoProduto() {
    }

    public PedidoProduto(Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public PedidoProduto(PedidoProdutoId id, Pedido pedido, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public PedidoProdutoId getId() {
        return id;
    }

    public void setId(PedidoProdutoId id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
