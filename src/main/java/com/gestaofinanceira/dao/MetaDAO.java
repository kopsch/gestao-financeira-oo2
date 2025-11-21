package com.gestaofinanceira.dao;

import com.gestaofinanceira.model.Meta;
import com.gestaofinanceira.model.TipoMeta;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MetaDAO implements GenericDAO<Meta> {

    @Override
    public void salvar(Meta meta) {
        String sql = "INSERT INTO metas (id, id_usuario, descricao, valor_meta, valor_atual, data_limite, tipo_meta) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getId().toString());
            stmt.setString(2, meta.getUsuario().getId().toString());
            stmt.setString(3, meta.getDescricao());
            stmt.setBigDecimal(4, meta.getValorMeta());
            stmt.setBigDecimal(5, meta.getValorAtual());
            stmt.setDate(6, Date.valueOf(meta.getDataLimite()));
            stmt.setString(7, meta.getTipo().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void atualizar(Meta meta) {
        String sql = "UPDATE metas SET descricao = ?, valor_meta = ?, valor_atual = ?, data_limite = ?, tipo_meta = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, meta.getDescricao());
            stmt.setBigDecimal(2, meta.getValorMeta());
            stmt.setBigDecimal(3, meta.getValorAtual());
            stmt.setDate(4, Date.valueOf(meta.getDataLimite()));
            stmt.setString(5, meta.getTipo().name());
            stmt.setString(6, meta.getId().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(UUID id) {
        String sql = "DELETE FROM metas WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Meta buscarPorId(UUID id) {
        String sql = "SELECT * FROM metas WHERE id = ?";
        Meta meta = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                meta = montarMeta(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meta;
    }

    @Override
    public List<Meta> listarTodos() {
        String sql = "SELECT * FROM metas";
        List<Meta> metas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                metas.add(montarMeta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metas;
    }

    public List<Meta> listarPorUsuario(UUID idUsuario) {
        String sql = "SELECT * FROM metas WHERE id_usuario = ?";
        List<Meta> metas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                metas.add(montarMeta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metas;
    }

    private Meta montarMeta(ResultSet rs) throws SQLException {
        Meta meta = new Meta();
        meta.setId(UUID.fromString(rs.getString("id")));
        meta.setDescricao(rs.getString("descricao"));
        meta.setValorMeta(rs.getBigDecimal("valor_meta"));
        meta.setValorAtual(rs.getBigDecimal("valor_atual"));
        meta.setDataLimite(rs.getDate("data_limite").toLocalDate());
        meta.setTipo(TipoMeta.valueOf(rs.getString("tipo_meta")));

        Usuario u = new Usuario();
        u.setId(UUID.fromString(rs.getString("id_usuario")));
        meta.setUsuario(u);

        return meta;
    }
}