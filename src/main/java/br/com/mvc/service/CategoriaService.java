package br.com.mvc.service;

import br.com.mvc.dao.CategoriaDAO;
import br.com.mvc.model.Categoria;
import java.util.List;

public class CategoriaService {
    private final CategoriaDAO dao = new CategoriaDAO();
    public List<Categoria> listar() { return dao.listarTodos(); }
    public Categoria buscarPorId(Long id) { return id == null ? null : dao.buscarPorId(id); }
    public void salvar(Categoria categoria) {
        if (categoria == null || categoria.getNome() == null || categoria.getNome().isBlank()) throw new IllegalArgumentException("Nome da categoria é obrigatório.");
        categoria.setNome(categoria.getNome().trim());
        if (categoria.getId() == null) dao.inserir(categoria); else dao.alterar(categoria);
    }
    public void deletar(Long id) { if (id == null) throw new IllegalArgumentException("Id obrigatório."); dao.deletar(id); }
}
