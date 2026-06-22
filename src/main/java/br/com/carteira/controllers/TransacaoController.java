package br.com.carteira.controllers;

import br.com.carteira.model.Transacao;
import br.com.carteira.services.TransacaoService;

import io.javalin.http.Context;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public void registrarTransacao(Context ctx) {
        try {
            TransacaoDTO dto = ctx.bodyAsClass(TransacaoDTO.class);

            Transacao transacao = new Transacao(
                null,
                dto.getContaId(),
                Transacao.TipoTransacao.valueOf(dto.getTipo().toUpperCase()),
                dto.getValor(),
                LocalDate.parse(dto.getDataTransacao()),
                dto.getDescricao(),
                null
            );

            Transacao salva = transacaoService.registrarTransacao(transacao);
            ctx.status(201).json(salva);

        } catch (DateTimeParseException | NullPointerException e) {
            ctx.status(400).json(Map.of("erro", "Data inválida ou em branco. Formato aceito: YYYY-MM-DD."));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("erro", e.getMessage()));
        } catch (RuntimeException e) {
            ctx.status(422).json(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(Map.of("erro", "Erro interno no servidor"));
        }
    }

    public void listarTransacoesDaConta(Context ctx) {
        Long contaId = Long.valueOf(ctx.pathParam("contaId"));
        ctx.json(transacaoService.listarTransacoesDaConta(contaId));
    }
}
