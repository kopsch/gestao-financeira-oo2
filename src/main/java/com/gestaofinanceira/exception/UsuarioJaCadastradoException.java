package com.gestaofinanceira.exception;

public class UsuarioJaCadastradoException extends RuntimeException {
    public UsuarioJaCadastradoException(String nomeUsuario) {
        super("O nome de usuário '" + nomeUsuario + "' já está em uso.");
    }
}