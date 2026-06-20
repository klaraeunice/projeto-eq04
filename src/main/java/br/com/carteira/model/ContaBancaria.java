package br.com.carteira.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ContaBancaria.java
public class ContaBancaria {
    private Long id;
    private Long usuarioId;
    private String numeroConta;
    private BigDecimal saldoAtual;
    private LocalDateTime criadoEm;

    public ContaBancaria() {}

    public ContaBancaria(Long id, Long usuarioId, String numeroConta, BigDecimal saldoAtual, LocalDateTime criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.numeroConta = numeroConta;
        this.saldoAtual = saldoAtual;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }

    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
