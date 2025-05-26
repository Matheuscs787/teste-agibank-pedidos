package br.com.agibank.teste.pedidos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;
    private String nome;
    private String descricao;
    @NotNull(message = "O valor do produto não pode ter o valor zerado.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do produto deve ser maior que 0.0")
    private BigDecimal precoAtual;

    public Produto() {
    }

    public Produto(Long id, String nome, String descricao, BigDecimal precoAtual) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoAtual = precoAtual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }
}