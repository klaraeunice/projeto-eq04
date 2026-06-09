package br.com.carteira;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // Inicia o servidor Javalin na porta 8080
        Javalin app = Javalin.create().start(8080);

        // Rota inicial - O famoso "Hello World"
        app.get("/", ctx -> {
            ctx.result("Hello World! Sistema de Carteira Financeira rodando!");
        });
        
        System.out.println("Servidor iniciado! Acesse: http://localhost:8080");
    }
}
