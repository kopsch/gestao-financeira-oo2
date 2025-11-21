package com.gestaofinanceira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Meta {

    private UUID id;
    private Usuario usuario;
    private String descricao;
    private BigDecimal valorMeta;
    private BigDecimal valorAtual;
    private LocalDate dataLimite;
    private TipoMeta tipo;

    public Meta() {
        this.valorAtual = BigDecimal.ZERO;
    }

    public Meta(UUID id, Usuario usuario, String descricao, BigDecimal valorMeta, BigDecimal valorAtual, LocalDate dataLimite, TipoMeta tipo) {
        this.id = id;
        this.usuario = usuario;
        this.descricao = descricao;
        this.valorMeta = valorMeta;
        this.valorAtual = valorAtual;
        this.dataLimite = dataLimite;
        this.tipo = tipo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValorMeta() { return valorMeta; }
    public void setValorMeta(BigDecimal valorMeta) { this.valorMeta = valorMeta; }

    public BigDecimal getValorAtual() { return valorAtual; }
    public void setValorAtual(BigDecimal valorAtual) { this.valorAtual = valorAtual; }

    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }

    public TipoMeta getTipo() { return tipo; }
    public void setTipo(TipoMeta tipo) { this.tipo = tipo; }
}