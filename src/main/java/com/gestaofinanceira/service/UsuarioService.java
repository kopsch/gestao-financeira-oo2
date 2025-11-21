package com.gestaofinanceira.service;

import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.gestaofinanceira.dao.UsuarioDAO;
import com.gestaofinanceira.exception.UsuarioJaCadastradoException;
import com.gestaofinanceira.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void salvar(Usuario usuario) {
        if (usuario.getNomeUsuario() == null || usuario.getNomeUsuario().isEmpty()) {
            throw new IllegalArgumentException("O nome de usuário é obrigatório.");
        }

        if (usuarioDAO.existeLogin(usuario.getNomeUsuario())) {
            throw new UsuarioJaCadastradoException(usuario.getNomeUsuario());
        }

        if (usuario.getId() == null) {
            usuario.setId(UUID.randomUUID());
        }

        String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaHash);

        usuarioDAO.salvar(usuario);
    }

    public void atualizar(Usuario usuario) {
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
            String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            usuario.setSenha(senhaHash);
        }
        usuarioDAO.atualizar(usuario);
    }

    public void deletar(UUID id) {
        usuarioDAO.deletar(id);
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario autenticar(String nomeUsuario, String senhaDigitada) {
        Usuario usuarioDoBanco = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);

        if (usuarioDoBanco != null) {
            if (BCrypt.checkpw(senhaDigitada, usuarioDoBanco.getSenha())) {
                return usuarioDoBanco;
            }
        }
        return null;
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        return usuarioDAO.buscarPorNomeUsuario(nomeUsuario);
    }
}