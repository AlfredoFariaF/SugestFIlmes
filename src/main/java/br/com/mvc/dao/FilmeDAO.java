package br.com.mvc.dao;

import br.com.mvc.model.Categoria;
import br.com.mvc.model.Filme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO extends MysqlDAO {
    public List<Filme> listarTodos() {
        String sql = "SELECT f.*, COALESCE(AVG(a.nota),0) media_avaliacao, COUNT(a.id) total_avaliacoes " +
                "FROM filmes f LEFT JOIN avaliacoes a ON a.filme_id = f.id " +
                "GROUP BY f.id ORDER BY f.titulo";
        return consultarFilmes(sql);
    }

    public List<Filme> listarDestaques(int limite) {
        String sql = "SELECT f.*, COALESCE(AVG(a.nota),0) media_avaliacao, COUNT(a.id) total_avaliacoes " +
                "FROM filmes f LEFT JOIN avaliacoes a ON a.filme_id = f.id " +
                "GROUP BY f.id ORDER BY media_avaliacao DESC, total_avaliacoes DESC, f.titulo LIMIT ?";
        return consultarFilmes(sql, limite);
    }

    public Filme buscarPorId(Long id) {
        String sql = "SELECT f.*, COALESCE(AVG(a.nota),0) media_avaliacao, COUNT(a.id) total_avaliacoes " +
                "FROM filmes f LEFT JOIN avaliacoes a ON a.filme_id = f.id WHERE f.id = ? GROUP BY f.id";
        List<Filme> lista = consultarFilmes(sql, id);
        if (lista.isEmpty())
            return null;
        Filme filme = lista.get(0);
        filme.setCategorias(buscarCategorias(id));
        return filme;
    }

    public List<Filme> buscarPorTexto(String termo) {
        String sql = "SELECT f.*, COALESCE(AVG(a.nota),0) media_avaliacao, COUNT(a.id) total_avaliacoes " +
                "FROM filmes f LEFT JOIN avaliacoes a ON a.filme_id = f.id " +
                "WHERE f.titulo LIKE ? OR f.genero LIKE ? OR f.diretor LIKE ? " +
                "GROUP BY f.id ORDER BY f.titulo";
        String busca = "%" + termo.trim() + "%";
        return consultarFilmes(sql, busca, busca, busca);
    }

    public void inserir(Filme filme, List<Long> categorias) {
        String sql = "INSERT INTO filmes (titulo, descricao, genero, ano, diretor) VALUES (?, ?, ?, ?, ?)";
        try {
            super.executarUpdate(sql, filme.getTitulo(), filme.getDescricao(), filme.getGenero(), filme.getAno(),
                    filme.getDiretor());
            Filme salvo = buscarUltimoPorTitulo(filme.getTitulo());
            substituirCategorias(salvo.getId(), categorias);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir filme.", e);
        }
    }

    public void alterar(Filme filme, List<Long> categorias) {
        String sql = "UPDATE filmes SET titulo = ?, descricao = ?, genero = ?, ano = ?, diretor = ? WHERE id = ?";
        try {
            super.executarUpdate(sql, filme.getTitulo(), filme.getDescricao(), filme.getGenero(), filme.getAno(),
                    filme.getDiretor(), filme.getId());
            substituirCategorias(filme.getId(), categorias);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar filme.", e);
        }
    }

    public void deletar(Long id) {
        try {
            super.executarUpdate("DELETE FROM filmes WHERE id = ?", id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir filme.", e);
        }
    }

    public List<Filme> recomendarPorCategorias(List<Long> categorias) {
        if (categorias == null || categorias.isEmpty())
            return List.of();
        String placeholders = String.join(",", java.util.Collections.nCopies(categorias.size(), "?"));
        String sql = "SELECT f.*, COUNT(DISTINCT fc.categoria_id) correspondencias, " +
                "COALESCE(AVG(a.nota),0) media_avaliacao, COUNT(DISTINCT a.id) total_avaliacoes " +
                "FROM filmes f INNER JOIN filmes_categorias fc ON fc.filme_id = f.id " +
                "LEFT JOIN avaliacoes a ON a.filme_id = f.id " +
                "WHERE fc.categoria_id IN (" + placeholders + ") " +
                "GROUP BY f.id ORDER BY correspondencias DESC, media_avaliacao DESC, f.titulo";
        return consultarFilmes(sql, categorias.toArray());
    }

    private Filme buscarUltimoPorTitulo(String titulo) {
        String sql = "SELECT * FROM filmes WHERE titulo = ? ORDER BY id DESC LIMIT 1";
        try (ResultSet rs = super.executar(sql, titulo)) {
            if (rs.next())
                return mapear(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar filme inserido.", e);
        }
    }

    private List<Filme> consultarFilmes(String sql, Object... parametros) {
        List<Filme> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, parametros)) {
            while (rs.next())
                lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar filmes.", e);
        }
        return lista;
    }

    private Filme mapear(ResultSet rs) throws SQLException {
        Filme f = new Filme();
        f.setId(rs.getLong("id"));
        f.setTitulo(rs.getString("titulo"));
        f.setDescricao(rs.getString("descricao"));
        f.setGenero(rs.getString("genero"));
        f.setAno(rs.getInt("ano"));
        f.setDiretor(rs.getString("diretor"));
        try {
            f.setMediaAvaliacao(rs.getDouble("media_avaliacao"));
        } catch (SQLException ignored) {
        }
        try {
            f.setTotalAvaliacoes(rs.getInt("total_avaliacoes"));
        } catch (SQLException ignored) {
        }
        try {
            f.setCorrespondencias(rs.getInt("correspondencias"));
        } catch (SQLException ignored) {
        }
        return f;
    }

    private List<Categoria> buscarCategorias(Long filmeId) {
        String sql = "SELECT c.id, c.nome FROM categorias c INNER JOIN filmes_categorias fc ON fc.categoria_id = c.id WHERE fc.filme_id = ? ORDER BY c.nome";
        List<Categoria> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, filmeId)) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categorias do filme.", e);
        }
        return lista;
    }

    private void substituirCategorias(Long filmeId, List<Long> categorias) throws SQLException {
        super.executarUpdate("DELETE FROM filmes_categorias WHERE filme_id = ?", filmeId);
        if (categorias == null)
            return;
        for (Long categoriaId : categorias)
            super.executarUpdate("INSERT INTO filmes_categorias (filme_id, categoria_id) VALUES (?, ?)", filmeId,
                    categoriaId);
    }
}
