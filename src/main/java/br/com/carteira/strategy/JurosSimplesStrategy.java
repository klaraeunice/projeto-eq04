package br.com.carteira.strategy;

import java.math.BigDecimal;

public class JurosSimplesStrategy implements JurosStrategy {
    @Override
    public BigDecimal calcularJuros(BigDecimal saldo, double taxa) {
        // Fórmula básica: Saldo * Taxa
        return saldo.multiply(BigDecimal.valueOf(taxa));
    }
}
