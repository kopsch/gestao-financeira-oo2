package com.gestaofinanceira.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Receita;
import com.gestaofinanceira.model.TipoReceita;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

public class ReceitaDAO implements GenericDAO<Receita> {

    @Override
    public void salvar(Receita receita) {
        String sql = "INSERT INTO receitas (id, id_usuario, id_conta, tipo_receita, valor, data_receita, descricao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, receita.getId().toString());
            stmt.setString(2, receita.getUsuario().getId().toString());
            stmt.setString(3, receita.getConta().getId().toString());
            stmt.setString(4, receita.getTipo().name());
            stmt.setBigDecimal(5, receita.getValor());
            stmt.setDate(6, Date.valueOf(receita.getData()));
            stmt.setString(7, receita.getDescricao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Receita receita) {
        String sql = "UPDATE receitas SET id_conta = ?, tipo_receita = ?, valor = ?, data_receita = ?, descricao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, receita.getConta().getId().toString());
            stmt.setString(2, receita.getTipo().name());
            stmt.setBigDecimal(3, receita.getValor());
            stmt.setDate(4, Date.valueOf(receita.getData()));
            stmt.setString(5, receita.getDescricao());
            stmt.setString(6, receita.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM receitas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Receita buscarPorId(UUID id) {
        String sql = "SELECT * FROM receitas WHERE id = ?";
        Receita receita = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                receita = montarReceita(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receita;
    }

    @Override
    public List<Receita> listarTodos() {
        String sql = "SELECT * FROM receitas";
        List<Receita> receitas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                receitas.add(montarReceita(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receitas;
    }

    public List<Receita> listarPorUsuario(UUID idUsuario) {
        String sql = "SELECT * FROM receitas WHERE id_usuario = ? ORDER BY data_receita DESC";
        List<Receita> receitas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                receitas.add(montarReceita(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receitas;
    }

    private Receita montarReceita(ResultSet rs) throws SQLException {
        Receita r = new Receita();
        r.setId(UUID.fromString(rs.getString("id")));
        r.setTipo(TipoReceita.valueOf(rs.getString("tipo_receita")));
        r.setValor(rs.getBigDecimal("valor"));
        r.setData(rs.getDate("data_receita").toLocalDate());
        r.setDescricao(rs.getString("descricao"));

        Usuario u = new Usuario();
        u.setId(UUID.fromString(rs.getString("id_usuario")));
        r.setUsuario(u);

        Conta c = new Conta();
        c.setId(UUID.fromString(rs.getString("id_conta")));
        r.setConta(c);

        return r;
    }
}