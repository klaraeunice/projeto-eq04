package br.com.carteira.controllers;

import br.com.carteira.model.Usuario;
import br.com.carteira.services.UsuarioService;
import io.javalin.http.Context;

import java.sql.SQLException;

/**
 * Responsável por receber requisições HTTP e retornar respostas.
 */
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void criar(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuarioService.criarUsuario(usuario);
            ctx.status(201).json(usuario); // 201 Created
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 400 Bad Request (ex: CPF inválido, campos vazios, dados duplicados)
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Erro interno no servidor: " + e.getMessage());
        }
    }

    public void listar(Context ctx) {
        try {
            ctx.status(200).json(usuarioService.listarTodos());
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao consultar o banco de dados.");
        }
    }

    public void buscarPorId(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Usuario usuario = usuarioService.buscarPorId(id);
            if (usuario != null) {
                ctx.status(200).json(usuario);
            } else {
                ctx.status(404).result("Usuário não encontrado."); // 404 Not Found
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID inválido.");
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao consultar o banco de dados.");
        }
    }

    public void atualizar(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuarioService.atualizarUsuario(id, usuario);
            ctx.status(200).result("Usuário atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Erro interno no servidor.");
        }
    }

    public void deletar(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            usuarioService.deletarUsuario(id);
            ctx.status(204); // 204 No Content
        } catch (IllegalArgumentException e) {
            ctx.status(404).result(e.getMessage()); // 404 se não achar pra deletar
        } catch (Exception e) {
            ctx.status(500).result("Erro interno no servidor.");
        }
    }
}
