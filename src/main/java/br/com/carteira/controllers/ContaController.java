package br.com.carteira.controllers;

// IMPORTS CRÍTICOS ADICIONADOS PARA RESOLVER OS ERROS VERMELHOS
import br.com.carteira.model.ContaBancaria;
import br.com.carteira.services.ContaBancariaService;

import io.javalin.http.Context;
import java.util.Map;

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
}
