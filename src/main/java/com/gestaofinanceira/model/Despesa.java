package com.gestaofinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Despesa {

    private UUID id;
    private Usuario usuario;
    private Conta conta;
    private CategoriaDespesa categoria;
    private TipoDespesa tipoDespesa;
    private BigDecimal valor;
    private LocalDate data;
    private String descricao;

    public Despesa() {
    }

    public Despesa(UUID id, Usuario usuario, Conta conta, CategoriaDespesa categoria,
            TipoDespesa tipoDespesa, BigDecimal valor, LocalDate data, String descricao) {
        this.id = id;
        this.usuario = usuario;
        this.conta = conta;
        this.categoria = categoria;
        this.tipoDespesa = tipoDespesa;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
