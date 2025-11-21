package com.gestaofinanceira.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.TipoConta;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

public class ContaDAO implements GenericDAO<Conta> {

    @Override
    public void salvar(Conta conta) {
        String sql = "INSERT INTO contas (id, id_usuario, nome_banco, agencia, numero_conta, saldo, tipo_conta) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getId().toString());
            stmt.setString(2, conta.getUsuario().getId().toString());
            stmt.setString(3, conta.getNomeDoBanco());
            stmt.setString(4, conta.getAgencia());
            stmt.setString(5, conta.getNumeroDaConta());
            stmt.setBigDecimal(6, conta.getSaldo());
            stmt.setString(7, conta.getTipoConta().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar conta: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Conta conta) {
        String sql = "UPDATE contas SET nome_banco = ?, agencia = ?, numero_conta = ?, saldo = ?, tipo_conta = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getNomeDoBanco());
            stmt.setString(2, conta.getAgencia());
            stmt.setString(3, conta.getNumeroDaConta());
            stmt.setBigDecimal(4, conta.getSaldo());
            stmt.setString(5, conta.getTipoConta().name());
            stmt.setString(6, conta.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar conta: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM contas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar conta: " + e.getMessage(), e);
        }
    }

    @Override
    public Conta buscarPorId(UUID id) {
        String sql = "SELECT * FROM contas WHERE id = ?";
        Conta conta = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                conta = montarConta(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar conta por ID: " + e.getMessage(), e);
        }
        return conta;
    }

    @Override
    public List<Conta> listarTodos() {
        String sql = "SELECT * FROM contas";
        List<Conta> contas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contas.add(montarConta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as contas: " + e.getMessage(), e);
        }
        return contas;
    }

    public List<Conta> listarPorUsuario(UUID idUsuario) {
        String sql = "SELECT * FROM contas WHERE id_usuario = ?";
        List<Conta> contas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(montarConta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contas por usuário: " + e.getMessage(), e);
        }

        return contas;
    }

    public void atualizarSaldo(Conta conta) {
        String sql = "UPDATE contas SET saldo = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, conta.getSaldo());
            stmt.setString(2, conta.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar saldo: " + e.getMessage(), e);
        }
    }

    private Conta montarConta(ResultSet rs) throws SQLException {
        Conta conta = new Conta();
        conta.setId(UUID.fromString(rs.getString("id")));
        conta.setNomeDoBanco(rs.getString("nome_banco"));
        conta.setAgencia(rs.getString("agencia"));
        conta.setNumeroDaConta(rs.getString("numero_conta"));
        conta.setSaldo(rs.getBigDecimal("saldo"));
        conta.setTipoConta(TipoConta.valueOf(rs.getString("tipo_conta")));

        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString(rs.getString("id_usuario")));
        conta.setUsuario(usuario);

        return conta;
    }
}