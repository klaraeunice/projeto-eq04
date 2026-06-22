package br.com.carteira.repositories;

import br.com.carteira.model.Transacao; // IMPORT ADICIONADO

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class TransacaoRepository {

    private final DataSource dataSource;

    public TransacaoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Transacao salvar(Transacao transacao, Connection conn) throws SQLException {
        // CORRIGIDO: Removido o cast '::tipo_transacao' para salvar como VARCHAR puro
        String sql = "INSERT INTO transacoes (conta_id, tipo, valor, data_transacao, descricao) VALUES (?, ?, ?, ?, ?) RETURNING *";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transacao.getContaId());
            stmt.setString(2, transacao.getTipo().name());
            stmt.setBigDecimal(3, transacao.getValor());
            stmt.setDate(4, Date.valueOf(transacao.getDataTransacao()));
            stmt.setString(5, transacao.getDescricao());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        throw new SQLException("Falha ao salvar transação");
    }

    public List<Transacao> buscarPorContaId(Long contaId) {
        String sql = "SELECT * FROM transacoes WHERE conta_id = ? ORDER BY data_transacao DESC, criado_em DESC";
        List<Transacao> transacoes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, contaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) transacoes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transações", e);
        }
        return transacoes;
    }

    private Transacao mapRow(ResultSet rs) throws SQLException {
        // CORRIGIDO: Apontado para o Enum embutido correto da classe Transacao
        return new Transacao(
            rs.getLong("id"),
            rs.getLong("conta_id"),
            Transacao.TipoTransacao.valueOf(rs.getString("tipo")),
            rs.getBigDecimal("valor"),
            rs.getDate("data_transacao").toLocalDate(),
            rs.getString("descricao"),
            rs.getTimestamp("criado_em").toLocalDateTime()
        );
    }
}
