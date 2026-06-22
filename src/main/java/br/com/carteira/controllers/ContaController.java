package br.com.carteira.controllers;

// IMPORTS CRÍTICOS ADICIONADOS PARA RESOLVER OS ERROS VERMELHOS
import br.com.carteira.model.ContaBancaria;
import br.com.carteira.services.ContaBancariaService;

import io.javalin.http.Context;
import java.util.Map;
import br.com.carteira.strategy.*;
import io.javalin.http.Context;
import java.math.BigDecimal;

public class ContaController {

    private final ContaBancariaService contaService;

    public ContaController(ContaBancariaService contaService) {
        this.contaService = contaService;
    }

    public void criarConta(Context ctx) {
        try {
            Map<?, ?> body = ctx.bodyAsClass(Map.class);
            Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
            String numeroConta = body.get("numeroConta").toString();

            ContaBancaria novaConta = contaService.criarConta(usuarioId, numeroConta);
            ctx.status(201).json(novaConta);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("erro", "Erro interno no servidor"));
        }
    }

    public void buscarConta(Context ctx) {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            ctx.json(contaService.buscarConta(id));
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(Map.of("erro", e.getMessage()));
        }
    }

    public void listarContasDoUsuario(Context ctx) {
        Long usuarioId = Long.valueOf(ctx.pathParam("usuarioId"));
        ctx.json(contaService.listarContasDoUsuario(usuarioId));
    }
    public void simularJuros(Context ctx) {
        // 1. Pegamos os dados em texto da URL de forma segura
        String saldoParam = ctx.queryParam("saldo");
        String taxaParam = ctx.queryParam("taxa");
        String tipoJuros = ctx.queryParam("tipo");

        // 2. Convertemos os textos para os números corretos (evitando o aviso amarelo)
        BigDecimal saldo = (saldoParam != null && !saldoParam.isEmpty()) ? new BigDecimal(saldoParam) : BigDecimal.ZERO;
        double taxa = (taxaParam != null && !taxaParam.isEmpty()) ? Double.parseDouble(taxaParam) : 0.0;

        // 3. Declaramos a Interface (Desacoplada)
        JurosStrategy strategy;

        // 4. Escolha dinâmica da estratégia
        if ("composto".equalsIgnoreCase(tipoJuros)) {
            strategy = new JurosCompostosStrategy();
        } else {
            strategy = new JurosSimplesStrategy();
        }

        // 5. Executa a estratégia escolhida
        BigDecimal resultadoJuros = strategy.calcularJuros(saldo, taxa);

        // 6. Retorna o resultado para o usuário
        ctx.result("Valor dos juros calculados: R$ " + resultadoJuros).status(200);
    }
}
