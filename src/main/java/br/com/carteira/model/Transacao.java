package br.com.carteira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transacao {

    // Enumeração embutida na classe
    public enum TipoTransacao {
        ENTRADA, SAIDA
    }

    private Long id;
    private Long contaId;
    private TipoTransacao tipo;
    private BigDecimal valor;
    private LocalDate dataTransacao;
    private String descricao;
    private LocalDateTime criadoEm;

    public Transacao() {
    }

    // Construtor completo com todos os 7 parâmetros
    public Transacao(Long id, Long contaId, TipoTransacao tipo, BigDecimal valor, LocalDate dataTransacao, String descricao, LocalDateTime criadoEm) {
        this.id = id;
        this.contaId = contaId;
        this.tipo = tipo;
        setValor(valor); // Setter possui regras de negócio para barrar < 0
        setDataTransacao(dataTransacao); // Setter possui regras de negócio para barrar datas futuras
        this.descricao = descricao;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        if (dataTransacao == null) {
            throw new IllegalArgumentException("A data da transação não pode ser nula.");
        }
        if (dataTransacao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data da transação não pode ser futura.");
        }
        this.dataTransacao = dataTransacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
