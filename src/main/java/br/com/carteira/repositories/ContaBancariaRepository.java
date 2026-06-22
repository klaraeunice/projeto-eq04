package br.com.carteira.repositories;

import br.com.carteira.model.ContaBancaria; // IMPORT ADICIONADO

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class ContaBancariaRepository {

    private final DataSource dataSource;

    public ContaBancariaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // CORRIGIDO: Agora retorna o tipo correto ContaBancaria
    public ContaBancaria salvar(ContaBancaria conta) {
        String sql = "INSERT INTO contas_bancarias (usuario_id, numero_conta, saldo_atual) VALUES (?, ?, ?) RETURNING *";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, conta.getUsuarioId());
            stmt.setString(2, conta.getNumeroConta());
            stmt.setBigDecimal(3, conta.getSaldoAtual());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar conta bancária", e);
        }
        return null;
    }

    public Optional<ContaBancaria> buscarPorId(Long id, Connection transacaoConn) throws SQLException {
        String sql = "SELECT * FROM contas_bancarias WHERE id = ?";
        Connection conn = (transacaoConn != null) ? transacaoConn : dataSource.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } finally {
            if (transacaoConn == null) conn.close();
        }
        return Optional.empty();
    }

    public Optional<ContaBancaria> buscarPorId(Long id) {
        try {
            return buscarPorId(id, null);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta", e);
        }
    }

    public List<ContaBancaria> buscarPorUsuarioId(Long usuarioId) {
        String sql = "SELECT * FROM contas_bancarias WHERE usuario_id = ?";
        List<ContaBancaria> contas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) contas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar contas do usuário", e);
        }
        return contas;
    }

    public void atualizarSaldo(Long contaId, BigDecimal novoSaldo, Connection conn) throws SQLException {
        String sql = "UPDATE contas_bancarias SET saldo_atual = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setLong(2, contaId);
            stmt.executeUpdate();
        }
    }

    private ContaBancaria mapRow(ResultSet rs) throws SQLException {
        return new ContaBancaria(
            rs.getLong("id"),
            rs.getLong("usuario_id"),
            rs.getString("numero_conta"),
            rs.getBigDecimal("saldo_atual"),
            rs.getTimestamp("criado_em").toLocalDateTime()
        );
    }
}
