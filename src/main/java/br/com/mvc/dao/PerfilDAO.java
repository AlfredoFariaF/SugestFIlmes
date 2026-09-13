package br.com.mvc.dao;

import br.com.mvc.model.Perfil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO = Data Access Object (acesso ao banco)
 *
 * Somente SQL e conversao ResultSet -> {@link Perfil}.
 * Quem decide "quando" chamar cada metodo e o Service / Controller.
 */
public class PerfilDAO extends MysqlDAO {

    public PerfilDAO() {
        super();
    }

    public List<Perfil> listarTodos() {
        String sql = "SELECT id, nome FROM perfis ORDER BY id";
        List<Perfil> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql)) {
            while (rs.next()) {
                lista.add(this.mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar perfis.", e);
        }
        return lista;
    }

    public Perfil buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM perfis WHERE id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfil por id.", e);
        }
        return null;
    }

    public Perfil buscarPorNome(String nome) {
        String sql = "SELECT id, nome FROM perfis WHERE nome = ?";
        try (ResultSet rs = super.executar(sql, nome)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar perfil por nome.", e);
        }
        return null;
    }

    public void inserir(Perfil perfil) {
        String sql = "INSERT INTO perfis (nome) VALUES (?)";
        try {
            super.executarUpdate(sql, perfil.getNome());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir perfil.", e);
        }
    }

    public void alterar(Perfil perfil) {
        String sql = "UPDATE perfis SET nome = ? WHERE id = ?";
        try {
            super.executarUpdate(sql, perfil.getNome(), perfil.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar perfil.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM perfis WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar perfil.", e);
        }
    }

    private Perfil mapear(ResultSet rs) throws SQLException {
        Perfil perfil = new Perfil();
        perfil.setId(rs.getLong("id"));
        perfil.setNome(rs.getString("nome"));
        return perfil;
    }
}
