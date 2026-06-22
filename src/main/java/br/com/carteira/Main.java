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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        // 1. Configurar Pool de Conexão com o Banco de Dados (HikariCP) via Variáveis de Ambiente
        HikariConfig cfg = new HikariConfig();

        // Em produção, o servidor injeta estas variáveis.
        // Em desenvolvimento local, usa o fallback automático após o sinal de ':'
        String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/carteira_db";
        String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "admin";
        String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "123";

        cfg.setJdbcUrl(dbUrl);
        cfg.setUsername(dbUser);
        cfg.setPassword(dbPassword);
        cfg.setMaximumPoolSize(5); // REQUISITO: Banco compartilhado, limite estrito de 5 conexões

        // Driver do PostgreSQL
        //cfg.setDriverClassName("org.postgresql.Driver");

        HikariDataSource dataSource = new HikariDataSource(cfg);

        // 2. Instanciar as camadas usando o DataSource do Hikari
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

        // 3. Inicializar o Javalin (Configurações básicas)
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        });

        // 4. Mapear as rotas da API

        // REQUISITO: Endpoint /ping público para o portal de monitoramento (Aparecer "no ar")
        app.get("/ping", ctx -> ctx.json(java.util.Map.of("status", "ok")));

        // OPCIONAL: Rota raiz para evitar o erro "Not Found" ao acessar o endereço puro
        app.get("/", ctx -> ctx.result("Bem-vindo à API da Carteira Bancária! Acesse /ping para testar a saúde do sistema."));
        // --- CRUD DE USUÁRIOS ---
        app.post("/usuarios", usuarioController::criar);
        app.get("/usuarios", usuarioController::listar);
        app.get("/usuarios/{id}", usuarioController::buscarPorId);
        app.put("/usuarios/{id}", usuarioController::atualizar);
        app.delete("/usuarios/{id}", usuarioController::deletar);

        // --- CRUD DE CONTAS BANCÁRIAS ---
        // ⚠️ CORREÇÃO: Rota estática declarada ANTES da rota dinâmica com parâmetro {id}
        app.get("/contas/simular-juros", contaController::simularJuros);
        app.post("/contas", contaController::criarConta);
        app.get("/contas/{id}", contaController::buscarConta);
        app.get("/usuarios/{usuarioId}/contas", contaController::listarContasDoUsuario);

        // --- MOVIMENTAÇÕES / TRANSAÇÕES ---
        app.post("/transacoes", transacaoController::registrarTransacao);
        app.get("/contas/{contaId}/transacoes", transacaoController::listarTransacoesDaConta);

        // REQUISITO: Iniciar explicitamente na porta 8080 APÓS as rotas/configurações
        app.start(8080);

        System.out.println("Servidor de API iniciado com sucesso na porta 8080!");
    }
}
