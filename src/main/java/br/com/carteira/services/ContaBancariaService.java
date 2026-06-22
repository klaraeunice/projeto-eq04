package br.com.carteira.services;

import br.com.carteira.model.ContaBancaria;
import br.com.carteira.repositories.ContaBancariaRepository;

import java.math.BigDecimal;
import java.util.List;

public class ContaBancariaService {

    private final ContaBancariaRepository repository;

    public ContaBancariaService(ContaBancariaRepository repository) {
        this.repository = repository;
    }

    public ContaBancaria criarConta(Long usuarioId, String numeroConta) {
        if (usuarioId == null || numeroConta == null || numeroConta.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID do usuário e o número da conta são obrigatórios.");
        }

        ContaBancaria novaConta = new ContaBancaria();
        novaConta.setUsuarioId(usuarioId);
        novaConta.setNumeroConta(numeroConta);
        novaConta.setSaldoAtual(BigDecimal.ZERO); // Contas sempre iniciam zeradas

        return repository.salvar(novaConta);
    }

    public ContaBancaria buscarConta(Long id) {
        // Alinhado perfeitamente com o Optional retornado pelo ContaBancariaRepository
        return repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Conta bancária não encontrada."));
    }

    public List<ContaBancaria> listarContasDoUsuario(Long usuarioId) {
        return repository.buscarPorUsuarioId(usuarioId);
    }
}
