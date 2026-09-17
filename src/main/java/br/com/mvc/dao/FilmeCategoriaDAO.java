package br.com.mvc.dao;

import java.sql.SQLException;

public class FilmeCategoriaDAO extends MysqlDAO {
    public void inserir(Long filmeId, Long categoriaId) {
        try {
            super.executarUpdate("INSERT INTO filmes_categorias (filme_id, categoria_id) VALUES (?, ?)", filmeId,
                    categoriaId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao relacionar filme e categoria.", e);
        }
    }

    public void removerPorFilme(Long filmeId) {
        try {
            super.executarUpdate("DELETE FROM filmes_categorias WHERE filme_id = ?", filmeId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover categorias do filme.", e);
        }
    }
}
