package com.gestaofinanceira.model;

import java.util.UUID;

public class CategoriaDespesa {

    private UUID id;
    private String nome;
    private Usuario usuario;

    public CategoriaDespesa() {
    }

    public CategoriaDespesa(UUID id, String nome, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
