package com.gestaofinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.dao.ContaDAO;
import com.gestaofinanceira.exception.ContaNaoEncontradaException;
import com.gestaofinanceira.exception.SaldoInsuficienteException;
import com.gestaofinanceira.exception.ValorInvalidoException;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Usuario;

public class ContaService {

    private final ContaDAO contaDAO;

    public ContaService() {
        this.contaDAO = new ContaDAO();
    }

    public void salvar(Conta conta) {
        if (conta.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorInvalidoException("O saldo inicial não pode ser negativo.");
        }

        if (conta.getId() == null) {
            conta.setId(UUID.randomUUID());
        }

        contaDAO.salvar(conta);
    }

    public void atualizar(Conta conta) {
        contaDAO.atualizar(conta);
    }

    public void deletar(UUID id) {
        contaDAO.deletar(id);
    }

    public Conta buscarPorId(UUID id) {
        return contaDAO.buscarPorId(id);
    }

    public List<Conta> listarTodos() {
        return contaDAO.listarTodos();
    }

    public List<Conta> listarMinhasContas(Usuario usuario) {
        return contaDAO.listarPorUsuario(usuario.getId());
    }

    public void transferir(UUID idContaOrigem, UUID idContaDestino, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor da transferência deve ser maior que zero.");
        }

        Conta contaOrigem = contaDAO.buscarPorId(idContaOrigem);
        Conta contaDestino = contaDAO.buscarPorId(idContaDestino);

        if (contaOrigem == null || contaDestino == null) {
            throw new ContaNaoEncontradaException();
        }

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaDAO.atualizarSaldo(contaOrigem);
        contaDAO.atualizarSaldo(contaDestino);
    }
}