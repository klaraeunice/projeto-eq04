package br.com.carteira.services;

// IMPORTS CRÍTICOS ADICIONADOS PARA RESOLVER OS ERROS DE NOME
import br.com.carteira.model.ContaBancaria;
import br.com.carteira.model.Transacao;
import br.com.carteira.repositories.ContaBancariaRepository;
import br.com.carteira.repositories.TransacaoRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaBancariaRepository contaRepository;
    private final DataSource dataSource;

    public TransacaoService(TransacaoRepository transacaoRepository, ContaBancariaRepository contaRepository, DataSource dataSource) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.dataSource = dataSource;
    }

    public Transacao registrarTransacao(Transacao transacao) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false); // Inicia transação atômica

            try {
                // Busca a conta utilizando a conexão da transação e extrai o Optional com segurança
                ContaBancaria conta = contaRepository.buscarPorId(transacao.getContaId(), conn)
                    .orElseThrow(() -> new IllegalArgumentException("Conta bancária referenciada não encontrada."));

                BigDecimal novoSaldo = conta.getSaldoAtual();

                // CORRIGIDO: Vinculado corretamente ao Enum interno de Transacao
                if (transacao.getTipo() == Transacao.TipoTransacao.ENTRADA) {
                    novoSaldo = novoSaldo.add(transacao.getValor());
                } else if (transacao.getTipo() == Transacao.TipoTransacao.SAIDA) {
                    if (novoSaldo.compareTo(transacao.getValor()) < 0) {
                        throw new IllegalArgumentException("Saldo insuficiente para realizar a transação.");
                    }
                    novoSaldo = novoSaldo.subtract(transacao.getValor());
                }

                // Alinhado perfeitamente com os métodos existentes nos repositórios
                contaRepository.atualizarSaldo(conta.getId(), novoSaldo, conn);
                Transacao transacaoSalva = transacaoRepository.salvar(transacao, conn);

                conn.commit(); // Efetiva no banco de dados se tudo deu certo
                return transacaoSalva;

            } catch (Exception e) {
                conn.rollback(); // Cancela tudo se der erro em qualquer etapa
                throw new RuntimeException("Transação recusada ou falhou. Rollback executado: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro de conexão/falha crítica de banco de dados.", e);
        }
    }

    public List<Transacao> listarTransacoesDaConta(Long contaId) {
        return transacaoRepository.buscarPorContaId(contaId);
    }
}
