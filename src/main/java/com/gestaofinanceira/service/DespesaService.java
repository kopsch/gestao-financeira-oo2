package com.gestaofinanceira.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.dao.ContaDAO;
import com.gestaofinanceira.dao.DespesaDAO;
import com.gestaofinanceira.exception.ContaNaoEncontradaException;
import com.gestaofinanceira.exception.SaldoInsuficienteException;
import com.gestaofinanceira.exception.ValorInvalidoException;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Despesa;
import com.gestaofinanceira.model.Usuario;

public class DespesaService {

    private final DespesaDAO despesaDAO;
    private final ContaDAO contaDAO;

    public DespesaService() {
        this.despesaDAO = new DespesaDAO();
        this.contaDAO = new ContaDAO();
    }

    public void salvar(Despesa despesa) {
        if (despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor da despesa deve ser maior que zero.");
        }

        if (despesa.getConta() == null || despesa.getConta().getId() == null) {
            throw new IllegalArgumentException("A despesa deve estar vinculada a uma conta válida.");
        }

        Conta contaAtualizada = contaDAO.buscarPorId(despesa.getConta().getId());

        if (contaAtualizada == null) {
            throw new ContaNaoEncontradaException();
        }

        if (contaAtualizada.getSaldo().compareTo(despesa.getValor()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para registrar esta despesa.");
        }

        BigDecimal novoSaldo = contaAtualizada.getSaldo().subtract(despesa.getValor());
        contaAtualizada.setSaldo(novoSaldo);

        if (despesa.getId() == null) {
            despesa.setId(UUID.randomUUID());
        }

        contaDAO.atualizarSaldo(contaAtualizada);
        despesaDAO.salvar(despesa);
    }

    public void atualizar(Despesa despesa) {
        despesaDAO.atualizar(despesa);
    }

    public void deletar(UUID id) {
        despesaDAO.deletar(id);
    }

    public Despesa buscarPorId(UUID id) {
        return despesaDAO.buscarPorId(id);
    }

    public List<Despesa> listarTodos() {
        return despesaDAO.listarTodos();
    }

    public List<Despesa> listarDespesasPorPeriodo(Usuario usuario, LocalDate inicio, LocalDate fim) {
        return despesaDAO.listarPorPeriodo(usuario.getId(), inicio, fim);
    }

    public List<Despesa> listarPorUsuario(Usuario usuario) {
        return despesaDAO.listarPorUsuario(usuario.getId());
    }
}