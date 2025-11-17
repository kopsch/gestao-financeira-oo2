package com.gestaofinanceira.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Conta {

    private UUID id;
    private Usuario usuario; 
    private String nomeDoBanco;
    private String agencia;
    private String numeroDaConta;
    private BigDecimal saldo; 
    private TipoConta tipoConta;

    public Conta() {
    }

    public Conta(UUID id, Usuario usuario, String nomeDoBanco, String agencia, String numeroDaConta, BigDecimal saldo, TipoConta tipoConta) {
        this.id = id;
        this.usuario = usuario;
        this.nomeDoBanco = nomeDoBanco;
        this.agencia = agencia;
        this.numeroDaConta = numeroDaConta;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
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

    public String getNomeDoBanco() {
        return nomeDoBanco;
    }

    public void setNomeDoBanco(String nomeDoBanco) {
        this.nomeDoBanco = nomeDoBanco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroDaConta() {
        return numeroDaConta;
    }

    public void setNumeroDaConta(String numeroDaConta) {
        this.numeroDaConta = numeroDaConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }
}