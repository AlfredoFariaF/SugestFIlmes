package br.com.mvc.dao;

import br.com.mvc.model.Categoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso aos dados das categorias.
 */
public class CategoriaDAO extends MysqlDAO {

    public List<Categoria> listarTodos() {
        String sql = "SELECT id, nome FROM categorias ORDER BY nome";
        List<Categoria> lista = new ArrayList<>();

        try (ResultSet rs = super.executar(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias.", e);
        }

        return lista;
    }

    public Categoria buscarPorId(Long id) {
        String sql = "SELECT id, nome FROM categorias WHERE id = ?";

        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria.", e);
        }

        return null;
    }

    /**
     * Retorna quantos filmes estão associados à categoria.
     * Uma categoria em uso não pode ser excluída.
     */
    public int contarFilmesVinculados(Long categoriaId) {
        String sql = "SELECT COUNT(*) AS total "
                + "FROM filmes_categorias WHERE categoria_id = ?";

        try (ResultSet rs = super.executar(sql, categoriaId)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao verificar os filmes vinculados à categoria.", e);
        }
    }

    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome) VALUES (?)";

        try {
            super.executarUpdate(sql, categoria.getNome());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir categoria.", e);
        }
    }

    public void alterar(Categoria categoria) {
        String sql = "UPDATE categorias SET nome = ? WHERE id = ?";

        try {
            super.executarUpdate(sql, categoria.getNome(), categoria.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar categoria.", e);
        }
    }

    public void deletar(Long id) {
        // A condição NOT EXISTS protege inclusive bancos antigos cuja FK ainda
        // esteja configurada com ON DELETE CASCADE.
        String sql = "DELETE FROM categorias "
                + "WHERE id = ? "
                + "AND NOT EXISTS ("
                + "    SELECT 1 FROM filmes_categorias "
                + "    WHERE categoria_id = ?"
                + ")";

        try {
            int removidas = super.executarUpdate(sql, id, id);
            if (removidas == 0 && contarFilmesVinculados(id) > 0) {
                throw new IllegalArgumentException(
                        "A categoria está vinculada a filmes e não pode ser excluída.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria.", e);
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNome(rs.getString("nome"));
        return categoria;
    }
}
