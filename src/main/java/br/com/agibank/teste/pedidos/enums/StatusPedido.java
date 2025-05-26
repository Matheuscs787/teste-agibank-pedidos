package br.com.agibank.teste.pedidos.enums;

public enum StatusPedido {
    CRIADO(1, "Pedido criado"),
    AGUARDANDO_PAGAMENTO(2, "Aguardando o pagamento do pedido"),
    PAGAMENTO_APROVADO(3, "Pagamento aprovado"),
    EM_PROCESSAMENTO(4, "Em processamento"),
    ENVIADO(5, "Pedido enviado"),
    ENTREGUE(6, "Pedido entregue"),
    CONCLUIDO(7, "Pedido concluído"),
    CANCELADO(8, "Pedido cancelado"),
    PAGAMENTO_RECUSADO(9, "Pagamento recusado");

    private int codigoStatusPedido;
    private String descricaoStatusPedido;

    StatusPedido() {
    }

    StatusPedido(int codigoStatusPedido, String descricaoStatusPedido) {
        this.codigoStatusPedido = codigoStatusPedido;
        this.descricaoStatusPedido = descricaoStatusPedido;
    }

    public int getCodigoStatusPedido() {
        return codigoStatusPedido;
    }

    public String getDescricaoStatusPedido() {
        return descricaoStatusPedido;
    }
}
