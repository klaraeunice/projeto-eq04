package br.com.carteira.services;

import br.com.carteira.model.Usuario;
import br.com.carteira.repositories.UsuarioRepository;
import util.CpfValidator;
import org.mindrot.jbcrypt.BCrypt;
import util.CpfValidator;

import java.sql.SQLException;
import java.util.List;

/**
 * Camada responsável pelas regras de negócio e validações.
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void criarUsuario(Usuario usuario) throws Exception {
        // 1. Valida se nenhum campo está vazio
        if (isNullOrEmpty(usuario.getNome()) || isNullOrEmpty(usuario.getEmail()) ||
            isNullOrEmpty(usuario.getCpf()) || isNullOrEmpty(usuario.getSenha())) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios.");
        }

        // 2. Valida o CPF usando a classe utilitária
        if (!util.CpfValidator.isValid(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        // 3. Limpa o CPF para manter apenas números
        String cpfLimpo = usuario.getCpf().replaceAll("\\D", "");
        usuario.setCpf(cpfLimpo);

        // 4. Verifica duplicação de E-mail
        if (repository.findByEmail(usuario.getEmail()) != null) {
            throw new IllegalStateException("E-mail já está em uso.");
        }

        // 5. Verifica duplicação de CPF
        if (repository.findByCpf(usuario.getCpf()) != null) {
            throw new IllegalStateException("CPF já cadastrado.");
        }

        // 6. Criptografa a senha com BCrypt
        String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaCriptografada);

        // 7. Salva o usuário no banco
        repository.save(usuario);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        return repository.findById(id);
    }

    public void atualizarUsuario(Long id, Usuario atualizacoes) throws Exception {
        Usuario existente = repository.findById(id);
        if (existente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Atualiza campos permitidos (Apenas exemplo simples, ideal seria validar)
        if (!isNullOrEmpty(atualizacoes.getNome())) existente.setNome(atualizacoes.getNome());
        if (!isNullOrEmpty(atualizacoes.getEmail())) existente.setEmail(atualizacoes.getEmail());

        // Se enviou nova senha, criptografar antes de atualizar
        if (!isNullOrEmpty(atualizacoes.getSenha())) {
            existente.setSenha(BCrypt.hashpw(atualizacoes.getSenha(), BCrypt.gensalt()));
        }

        repository.update(existente);
    }

    public void deletarUsuario(Long id) throws Exception {
        boolean deletado = repository.delete(id);
        if (!deletado) {
            throw new IllegalArgumentException("Usuário não encontrado para deleção.");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
