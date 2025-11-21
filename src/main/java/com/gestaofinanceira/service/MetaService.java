package com.gestaofinanceira.service;

import com.gestaofinanceira.dao.MetaDAO;
import com.gestaofinanceira.exception.ValorInvalidoException;
import com.gestaofinanceira.model.Meta;
import com.gestaofinanceira.model.Usuario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class MetaService {

    private final MetaDAO metaDAO;

    public MetaService() {
        this.metaDAO = new MetaDAO();
    }

    public void salvar(Meta meta) {
        if (meta.getValorMeta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor da meta deve ser positivo.");
        }

        if (meta.getDataLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data limite da meta deve ser no futuro.");
        }

        if (meta.getDescricao() == null || meta.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da meta é obrigatória.");
        }
        
        if (meta.getTipo() == null) {
            throw new IllegalArgumentException("É necessário definir o tipo da meta (Longo Prazo ou Ocasional).");
        }

        if (meta.getId() == null) {
            meta.setId(UUID.randomUUID());
        }
        
        if (meta.getValorAtual() == null) {
            meta.setValorAtual(BigDecimal.ZERO);
        }

        metaDAO.salvar(meta);
    }

    public void atualizar(Meta meta) {
        if (meta.getId() == null) {
            throw new IllegalArgumentException("O ID da meta é obrigatório.");
        }
        metaDAO.atualizar(meta);
    }

    public void deletar(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID é obrigatório para exclusão.");
        }
        metaDAO.deletar(id);
    }

    public Meta buscarPorId(UUID id) {
        return metaDAO.buscarPorId(id);
    }

    public List<Meta> listarTodos() {
        return metaDAO.listarTodos();
    }

    public List<Meta> listarPorUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuário inválido para listar metas.");
        }
        return metaDAO.listarPorUsuario(usuario.getId());
    }

    public void adicionarValor(UUID idMeta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor a ser adicionado deve ser positivo.");
        }

        Meta meta = metaDAO.buscarPorId(idMeta);
        if (meta == null) {
            throw new IllegalArgumentException("Meta não encontrada.");
        }

        meta.setValorAtual(meta.getValorAtual().add(valor));
        metaDAO.atualizar(meta);
    }

    public BigDecimal calcularAporteMensalIdeal(Meta meta) {
        BigDecimal faltaGuardar = meta.getValorMeta().subtract(meta.getValorAtual());

        if (faltaGuardar.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        long mesesRestantes = ChronoUnit.MONTHS.between(LocalDate.now(), meta.getDataLimite());

        if (mesesRestantes <= 0) {
            return faltaGuardar;
        }

        return faltaGuardar.divide(BigDecimal.valueOf(mesesRestantes), 2, RoundingMode.HALF_UP);
    }
}