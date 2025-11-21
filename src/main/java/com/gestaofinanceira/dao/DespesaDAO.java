package com.gestaofinanceira.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.model.CategoriaDespesa;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Despesa;
import com.gestaofinanceira.model.TipoDespesa;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

public class DespesaDAO implements GenericDAO<Despesa> {

    @Override
    public void salvar(Despesa despesa) {
        String sql = "INSERT INTO despesas (id, id_usuario, id_conta, id_categoria, tipo_despesa, valor, data_despesa, descricao) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, despesa.getId().toString());
            stmt.setString(2, despesa.getUsuario().getId().toString());
            stmt.setString(3, despesa.getConta().getId().toString());
            stmt.setString(4, despesa.getCategoria().getId().toString());
            stmt.setString(5, despesa.getTipoDespesa().name());
            stmt.setBigDecimal(6, despesa.getValor());
            stmt.setDate(7, Date.valueOf(despesa.getData()));
            stmt.setString(8, despesa.getDescricao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Despesa despesa) {
        String sql = "UPDATE despesas SET id_conta = ?, id_categoria = ?, tipo_despesa = ?, valor = ?, data_despesa = ?, descricao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, despesa.getConta().getId().toString());
            stmt.setString(2, despesa.getCategoria().getId().toString());
            stmt.setString(3, despesa.getTipoDespesa().name());
            stmt.setBigDecimal(4, despesa.getValor());
            stmt.setDate(5, Date.valueOf(despesa.getData()));
            stmt.setString(6, despesa.getDescricao());
            stmt.setString(7, despesa.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM despesas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Despesa buscarPorId(UUID id) {

        String sql = "SELECT d.*, cat.nome AS nome_categoria, c.nome_banco " +
                "FROM despesas d " +
                "INNER JOIN categorias_despesas cat ON d.id_categoria = cat.id " +
                "INNER JOIN contas c ON d.id_conta = c.id " +
                "WHERE d.id = ?";
        Despesa despesa = null;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                despesa = montarDespesaCompleta(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return despesa;
    }

    @Override
    public List<Despesa> listarTodos() {
        String sql = "SELECT d.*, cat.nome AS nome_categoria, c.nome_banco " +
                "FROM despesas d " +
                "INNER JOIN categorias_despesas cat ON d.id_categoria = cat.id " +
                "INNER JOIN contas c ON d.id_conta = c.id " +
                "ORDER BY d.data_despesa DESC";
        List<Despesa> despesas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                despesas.add(montarDespesaCompleta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return despesas;
    }

    public List<Despesa> listarPorUsuario(UUID idUsuario) {
        String sql = "SELECT d.*, cat.nome AS nome_categoria, c.nome_banco " +
                "FROM despesas d " +
                "INNER JOIN categorias_despesas cat ON d.id_categoria = cat.id " +
                "INNER JOIN contas c ON d.id_conta = c.id " +
                "WHERE d.id_usuario = ? " +
                "ORDER BY d.data_despesa DESC";
        List<Despesa> despesas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                despesas.add(montarDespesaCompleta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return despesas;
    }

    public List<Despesa> listarPorPeriodo(UUID idUsuario, LocalDate inicio, LocalDate fim) {
        String sql = "SELECT d.*, cat.nome AS nome_categoria, c.nome_banco " +
                "FROM despesas d " +
                "INNER JOIN categorias_despesas cat ON d.id_categoria = cat.id " +
                "INNER JOIN contas c ON d.id_conta = c.id " +
                "WHERE d.id_usuario = ? AND d.data_despesa BETWEEN ? AND ? " +
                "ORDER BY d.data_despesa DESC";

        List<Despesa> despesas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario.toString());
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                despesas.add(montarDespesaCompleta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return despesas;
    }

    private Despesa montarDespesaCompleta(ResultSet rs) throws SQLException {
        Despesa d = new Despesa();
        d.setId(UUID.fromString(rs.getString("id")));
        d.setValor(rs.getBigDecimal("valor"));
        d.setData(rs.getDate("data_despesa").toLocalDate());
        d.setDescricao(rs.getString("descricao"));
        d.setTipoDespesa(TipoDespesa.valueOf(rs.getString("tipo_despesa")));

        Usuario u = new Usuario();
        u.setId(UUID.fromString(rs.getString("id_usuario")));
        d.setUsuario(u);

        Conta c = new Conta();
        c.setId(UUID.fromString(rs.getString("id_conta")));
        c.setNomeDoBanco(rs.getString("nome_banco"));
        d.setConta(c);

        CategoriaDespesa cat = new CategoriaDespesa();
        cat.setId(UUID.fromString(rs.getString("id_categoria")));
        cat.setNome(rs.getString("nome_categoria"));
        d.setCategoria(cat);

        return d;
    }
}