package br.com.agibank.teste.pedidos.enums;

public enum FormaPagamento {
    CARTAO_CREDITO(1, "Cartão de crédito"),
    CARTAO_DEBITO(2, "Cartão de débito"),
    PIX(3, "Pix");

    private Integer id;
    private String descricao;

    FormaPagamento(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}
