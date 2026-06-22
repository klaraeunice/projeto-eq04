package br.com.carteira.controllers;

import java.math.BigDecimal;

public class TransacaoDTO {
    private Long contaId;
    private String tipo;
    private BigDecimal valor;
    private String dataTransacao;
    private String descricao;

    public TransacaoDTO() {}

    public Long getContaId() { return contaId; }
    public void setContaId(Long contaId) { this.contaId = contaId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(String dataTransacao) { this.dataTransacao = dataTransacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
