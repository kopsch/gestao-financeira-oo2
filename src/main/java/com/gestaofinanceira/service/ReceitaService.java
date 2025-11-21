package com.gestaofinanceira.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.dao.ContaDAO;
import com.gestaofinanceira.dao.ReceitaDAO;
import com.gestaofinanceira.exception.ValorInvalidoException;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Receita;

public class ReceitaService {

    private final ReceitaDAO receitaDAO;
    private final ContaDAO contaDAO;

    public ReceitaService() {
        this.receitaDAO = new ReceitaDAO();
        this.contaDAO = new ContaDAO();
    }

    public void salvar(Receita receita) {
        if (receita.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor da receita deve ser maior que zero.");
        }

        Conta conta = contaDAO.buscarPorId(receita.getConta().getId());
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada.");
        }

        conta.setSaldo(conta.getSaldo().add(receita.getValor()));

        if (receita.getId() == null) {
            receita.setId(UUID.randomUUID());
        }

        contaDAO.atualizarSaldo(conta);
        receitaDAO.salvar(receita);
    }

    public void atualizar(Receita receita) {
        receitaDAO.atualizar(receita);
    }

    public void deletar(UUID id) {
        receitaDAO.deletar(id);
    }

    public Receita buscarPorId(UUID id) {
        return receitaDAO.buscarPorId(id);
    }

    public List<Receita> listarTodos() {
        return receitaDAO.listarTodos();
    }

    public List<Receita> listarPorUsuario(UUID idUsuario) {
        return receitaDAO.listarPorUsuario(idUsuario);
    }
}