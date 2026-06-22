package br.com.carteira.repositories;

import br.com.carteira.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela comunicação com o banco de dados (DAO).
 */
public class UsuarioRepository {

    private final Connection connection;

    public UsuarioRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, cpf, senha) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getSenha());
            stmt.executeUpdate();

            // Atualiza o ID do usuário após a inserção
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Usuario> findAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    public Usuario findById(Long id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToUsuario(rs);
            }
        }
        return null;
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToUsuario(rs);
            }
        }
        return null;
    }

    public Usuario findByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToUsuario(rs);
            }
        }
        return null;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, cpf = ?, senha = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getSenha());
            stmt.setLong(5, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Método auxiliar para mapear o ResultSet para o Modelo
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("cpf"),
            rs.getString("senha")
        );
    }
}
