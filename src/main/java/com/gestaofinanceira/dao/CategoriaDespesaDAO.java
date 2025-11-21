package com.gestaofinanceira.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.model.CategoriaDespesa;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

public class CategoriaDespesaDAO implements GenericDAO<CategoriaDespesa> {

    @Override
    public void salvar(CategoriaDespesa categoriaDespesa) {
        String sql = "INSERT INTO categorias_despesas (id, nome, id_usuario) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, categoriaDespesa.getId().toString());
            preparedStatement.setString(2, categoriaDespesa.getNome());
            
            if (categoriaDespesa.getUsuario() != null) {
                preparedStatement.setString(3, categoriaDespesa.getUsuario().getId().toString());
            } else {
                preparedStatement.setObject(3, null);
            }

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void atualizar(CategoriaDespesa categoriaDespesa) {
        String sql = "UPDATE categorias_despesas SET nome = ? WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, categoriaDespesa.getNome());
            preparedStatement.setString(2, categoriaDespesa.getId().toString());

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM categorias_despesas WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CategoriaDespesa buscarPorId(UUID id) {
        String sql = "SELECT * FROM categorias_despesas WHERE id = ?";
        CategoriaDespesa categoria = null;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoria = montarCategoria(resultSet);
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return categoria;
    }

    @Override
    public List<CategoriaDespesa> listarTodos() {
        String sql = "SELECT * FROM categorias_despesas";
        List<CategoriaDespesa> listaCategorias = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listaCategorias.add(montarCategoria(resultSet));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return listaCategorias;
    }

    public List<CategoriaDespesa> listarPorUsuario(UUID idUsuario) {
        String sql = "SELECT * FROM categorias_despesas WHERE id_usuario = ? OR id_usuario IS NULL ORDER BY nome ASC";
        List<CategoriaDespesa> listaCategorias = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, idUsuario.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listaCategorias.add(montarCategoria(resultSet));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return listaCategorias;
    }

    private CategoriaDespesa montarCategoria(ResultSet rs) throws SQLException {
        CategoriaDespesa categoria = new CategoriaDespesa();
        categoria.setId(UUID.fromString(rs.getString("id")));
        categoria.setNome(rs.getString("nome"));
        
        String idUsuarioBanco = rs.getString("id_usuario");
        if (idUsuarioBanco != null) {
            Usuario usuario = new Usuario();
            usuario.setId(UUID.fromString(idUsuarioBanco));
            categoria.setUsuario(usuario);
        }
        return categoria;
    }
}