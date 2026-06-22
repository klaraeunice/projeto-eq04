package br.com.carteira;

import br.com.carteira.controllers.ContaController;
import br.com.carteira.controllers.UsuarioController;
import br.com.carteira.controllers.TransacaoController;
import br.com.carteira.repositories.ContaBancariaRepository;
import br.com.carteira.repositories.TransacaoRepository;
import br.com.carteira.repositories.UsuarioRepository;
import br.com.carteira.services.ContaBancariaService;
import br.com.carteira.services.TransacaoService;
import br.com.carteira.services.UsuarioService;

import io.javalin.Javalin;
import org.postgresql.ds.PGSimpleDataSource;

public class Main {
    public static void main(String[] args) {

        // 1. Configurar Conexão com o Banco de Dados (PostgreSQL)
        String url = "jdbc:postgresql://localhost:5432/carteira_db";
        String user = "admin";
        String password = "123";

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        // 2. Instanciar as camadas de forma uniforme usando o DataSource
        // Usuário
        UsuarioRepository usuarioRepository = new UsuarioRepository(dataSource);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        // Conta Bancária
        ContaBancariaRepository contaRepository = new ContaBancariaRepository(dataSource);
        ContaBancariaService contaService = new ContaBancariaService(contaRepository);
        ContaController contaController = new ContaController(contaService);

        // Transação
        TransacaoRepository transacaoRepository = new TransacaoRepository(dataSource);
        TransacaoService transacaoService = new TransacaoService(transacaoRepository, contaRepository, dataSource);
        TransacaoController transacaoController = new TransacaoController(transacaoService);

        // 3. Inicializar o Javalin puro para APIs (Porta 7070)
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(7070);

        // 4. Mapear as rotas da API

        // --- CRUD DE USUÁRIOS ---
        app.post("/usuarios", usuarioController::criar);
        app.get("/usuarios", usuarioController::listar);
        app.get("/usuarios/{id}", usuarioController::buscarPorId);
        app.put("/usuarios/{id}", usuarioController::atualizar);
        app.delete("/usuarios/{id}", usuarioController::deletar);

        // --- CRUD DE CONTAS BANCÁRIAS ---
        app.post("/contas", contaController::criarConta);
        app.get("/contas/{id}", contaController::buscarConta);
        app.get("/usuarios/{usuarioId}/contas", contaController::listarContasDoUsuario);
        // ADICIONADO: Rota do Padrão Strategy para simulação de juros dinâmicos
        app.get("/contas/simular-juros", contaController::simularJuros);

        // --- MOVIMENTAÇÕES / TRANSAÇÕES ---
        app.post("/transacoes", transacaoController::registrarTransacao);
        app.get("/contas/{contaId}/transacoes", transacaoController::listarTransacoesDaConta);

        System.out.println("Servidor de API iniciado com sucesso na porta 7070!");
    }
}
