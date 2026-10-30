package br.com.mvc.service;

import br.com.mvc.dao.CategoriaDAO;
import br.com.mvc.model.Categoria;

import java.util.List;

public class CategoriaService {

    private final CategoriaDAO dao = new CategoriaDAO();

    public List<Categoria> listar() {
        return dao.listarTodos();
    }

    public Categoria buscarPorId(Long id) {
        return id == null ? null : dao.buscarPorId(id);
    }

    public void salvar(Categoria categoria) {
        if (categoria == null
                || categoria.getNome() == null
                || categoria.getNome().isBlank()) {
            throw new IllegalArgumentException(
                    "Nome da categoria é obrigatório.");
        }

        categoria.setNome(categoria.getNome().trim());

        if (categoria.getId() == null) {
            dao.inserir(categoria);
        } else {
            dao.alterar(categoria);
        }
    }

    /**
     * Exclui uma categoria somente quando ela não está associada a filmes.
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da categoria é obrigatório.");
        }

        Categoria categoria = dao.buscarPorId(id);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada.");
        }

        int quantidadeFilmes = dao.contarFilmesVinculados(id);
        if (quantidadeFilmes > 0) {
            String mensagem = String.format(
                    "A categoria '%s' está vinculada a %d filme(s) e não pode ser excluída. "
                            + "Remova essa categoria dos filmes antes de tentar novamente.",
                    categoria.getNome(),
                    quantidadeFilmes);
            throw new IllegalArgumentException(mensagem);
        }

        dao.deletar(id);
    }
}
