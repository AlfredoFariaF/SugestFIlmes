package br.com.mvc.service;

import br.com.mvc.dao.FilmeDAO;
import br.com.mvc.model.Filme;
import java.util.List;

public class RecomendacaoService {
    private final FilmeDAO filmeDAO = new FilmeDAO();
    public List<Filme> recomendar(List<Long> categorias) { return filmeDAO.recomendarPorCategorias(categorias); }
}
