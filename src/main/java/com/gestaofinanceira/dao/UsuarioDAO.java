package com.gestaofinanceira.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.model.Sexo;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

public class UsuarioDAO implements GenericDAO<Usuario> {

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nome_completo, data_nascimento, sexo, nome_usuario, senha) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getId().toString());
            stmt.setString(2, usuario.getNomeCompleto());
            stmt.setDate(3, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(4, usuario.getSexo().name());
            stmt.setString(5, usuario.getNomeUsuario());
            stmt.setString(6, usuario.getSenha());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_completo = ?, data_nascimento = ?, sexo = ?, senha = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setDate(2, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getSexo().name());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(UUID id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = montarUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return usuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY nome_completo";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nome_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = montarUsuario(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por login: " + e.getMessage(), e);
        }
        return usuario;
    }

    public boolean existeLogin(String nomeUsuario) {
        return buscarPorNomeUsuario(nomeUsuario) != null;
    }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString(rs.getString("id")));
        usuario.setNomeCompleto(rs.getString("nome_completo"));
        usuario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        usuario.setSexo(Sexo.valueOf(rs.getString("sexo")));
        usuario.setNomeUsuario(rs.getString("nome_usuario"));
        usuario.setSenha(rs.getString("senha"));
        return usuario;
    }
}