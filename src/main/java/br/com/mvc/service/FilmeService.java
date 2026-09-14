package br.com.mvc.service;

import br.com.mvc.dao.FilmeDAO;
import br.com.mvc.model.Filme;
import java.util.List;

public class FilmeService {
    private final FilmeDAO filmeDAO = new FilmeDAO();
    public List<Filme> listar() { return filmeDAO.listarTodos(); }
    public List<Filme> destaques(int limite) { return filmeDAO.listarDestaques(limite); }
    public List<Filme> pesquisar(String termo) { return termo == null || termo.isBlank() ? listar() : filmeDAO.buscarPorTexto(termo); }
    public Filme buscarPorId(Long id) { return id == null ? null : filmeDAO.buscarPorId(id); }
    public void salvar(Filme filme, List<Long> categorias) {
        validar(filme, categorias);
        if (filme.getId() == null) filmeDAO.inserir(filme, categorias); else filmeDAO.alterar(filme, categorias);
    }
    public void deletar(Long id) {
        if (id == null || filmeDAO.buscarPorId(id) == null) throw new IllegalArgumentException("Filme não encontrado.");
        filmeDAO.deletar(id);
    }
    private void validar(Filme f, List<Long> categorias) {
        if (f == null) throw new IllegalArgumentException("Filme obrigatório.");
        if (f.getTitulo() == null || f.getTitulo().isBlank()) throw new IllegalArgumentException("Título obrigatório.");
        if (f.getDescricao() == null || f.getDescricao().isBlank()) throw new IllegalArgumentException("Descrição obrigatória.");
        if (f.getGenero() == null || f.getGenero().isBlank()) throw new IllegalArgumentException("Gênero obrigatório.");
        if (f.getDiretor() == null || f.getDiretor().isBlank()) throw new IllegalArgumentException("Diretor obrigatório.");
        if (f.getAno() == null || f.getAno() < 1888 || f.getAno() > 2100) throw new IllegalArgumentException("Ano inválido.");
        if (categorias == null || categorias.isEmpty()) throw new IllegalArgumentException("Selecione ao menos uma categoria.");
    }
}
