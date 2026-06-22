package br.com.carteira;

import br.com.carteira.controllers.UsuarioController;
import br.com.carteira.repositories.UsuarioRepository;
import br.com.carteira.services.UsuarioService;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Configurar Conexão com o Banco de Dados (Exemplo com PostgreSQL)
            String url = "jdbc:postgresql://localhost:5432/carteira_db";
            String user = "admin";
            String password = "123";
            Connection connection = DriverManager.getConnection(url, user, password);

            // 2. Instanciar as camadas
            UsuarioRepository usuarioRepository = new UsuarioRepository(connection);
            UsuarioService usuarioService = new UsuarioService(usuarioRepository);
            UsuarioController usuarioController = new UsuarioController(usuarioService);

            // 3. Inicializar o Javalin
            Javalin app = Javalin.create(config -> {
                config.http.defaultContentType = "application/json";
            }).start(7070);
            app.get("/", ctx -> {
                ctx.result("Bem-vindo à API da Carteira Financeira! O servidor e o banco estão funcionando.");
            });

            // 4. Mapear as rotas
            app.post("/usuarios", usuarioController::criar);
            app.get("/usuarios", usuarioController::listar);
            app.get("/usuarios/{id}", usuarioController::buscarPorId);
            app.put("/usuarios/{id}", usuarioController::atualizar);
            app.delete("/usuarios/{id}", usuarioController::deletar);

            System.out.println("Servidor iniciado na porta 7070!");

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
