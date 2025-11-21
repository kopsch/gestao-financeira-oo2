package com.gestaofinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Receita {

    private UUID id;
    private Usuario usuario;
    private Conta conta;
    private TipoReceita tipo;
    private BigDecimal valor;
    private LocalDate data;
    private String descricao;

    public Receita() {
    }

    public Receita(UUID id, Usuario usuario, Conta conta, TipoReceita tipo,
            BigDecimal valor, LocalDate data, String descricao) {
        this.id = id;
        this.usuario = usuario;
        this.conta = conta;
        this.tipo = tipo;
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

    public TipoReceita getTipo() {
        return tipo;
    }

    public void setTipo(TipoReceita tipo) {
        this.tipo = tipo;
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
