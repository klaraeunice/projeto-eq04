package br.com.carteira.strategy;

import java.math.BigDecimal;

public interface JurosStrategy {
    // O contrato padrão: recebe o saldo e a taxa, devolve o valor dos juros calculados
    BigDecimal calcularJuros(BigDecimal saldo, double taxa);
}
