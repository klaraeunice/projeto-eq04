package br.com.carteira.strategy;

import java.math.BigDecimal;

public class JurosCompostosStrategy implements JurosStrategy {
    @Override
    public BigDecimal calcularJuros(BigDecimal saldo, double taxa) {
        // Fórmula: Saldo * ((1 + taxa)^2) - Saldo (Simulando 2 períodos/meses fixos para simplificar)
        double fator = Math.pow(1 + taxa, 2);
        BigDecimal valorFinal = saldo.multiply(BigDecimal.valueOf(fator));
        return valorFinal.subtract(saldo);
    }
}
