package br.com.mvc.controller;

import br.com.mvc.model.Filme;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.AvaliacaoService;
import br.com.mvc.service.CategoriaService;
import br.com.mvc.service.FilmeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/filmes")
public class FilmeServlet extends BaseServlet {

    private final FilmeService filmeService = new FilmeService();
    private final AvaliacaoService avaliacaoService = new AvaliacaoService();
    private final CategoriaService categoriaService = new CategoriaService();

    private static final String LISTA = "/WEB-INF/jsp/filmes/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/filmes/form.jsp";
    private static final String DETALHES = "/WEB-INF/jsp/filmes/detalhes.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (acao(req)) {
            case "novo" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }
                form(req, resp, new Filme(), List.of());
            }
            case "editar" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }

                Long id = paramLong(req, "id");
                Filme filme = filmeService.buscarPorId(id);
                if (filme == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Filme não encontrado.");
                    return;
                }

                List<Long> ids = filme.getCategorias()
                        .stream()
                        .map(categoria -> categoria.getId())
                        .toList();
                form(req, resp, filme, ids);
            }
            case "detalhes" -> {
                Filme filme = filmeService.buscarPorId(paramLong(req, "id"));
                if (filme == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Filme não encontrado.");
                    return;
                }

                Usuario usuario = obterUsuarioLogado(req);
                req.setAttribute("filme", filme);
                req.setAttribute(
                        "minhaAvaliacao",
                        avaliacaoService.buscarDoUsuario(usuario.getId(), filme.getId()));
                req.setAttribute("ehAdmin", isAdmin(req));
                forward(req, resp, DETALHES);
            }
            case "excluir" -> resp.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "A exclusão de filmes deve ser enviada por POST.");
            default -> {
                String busca = param(req, "busca");
                req.setAttribute("filmes", filmeService.pesquisar(busca));
                req.setAttribute("busca", busca);
                req.setAttribute("ehAdmin", isAdmin(req));
                forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Todas as operações de escrita no catálogo são exclusivas do administrador.
        if (!exigirAdmin(req, resp)) {
            return;
        }

        String acaoPost = param(req, "acao");
        if ("excluir".equalsIgnoreCase(acaoPost)) {
            excluir(req, resp);
            return;
        }

        Filme filme = fromRequest(req);
        List<Long> categorias = listaLong(req.getParameterValues("categoria"));

        try {
            filmeService.salvar(filme, categorias);
            redirect(req, resp, "/filmes");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            form(req, resp, filme, categorias);
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = paramLong(req, "id");
        try {
            filmeService.deletar(id);
            redirect(req, resp, "/filmes");
        } catch (RuntimeException e) {
            req.setAttribute(
                    "erro",
                    "Não foi possível excluir o filme. Tente novamente ou verifique os vínculos existentes.");
            req.setAttribute("filmes", filmeService.listar());
            req.setAttribute("ehAdmin", isAdmin(req));
            forward(req, resp, LISTA);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp,
            Filme filme, List<Long> selecionadas)
            throws ServletException, IOException {
        req.setAttribute("filme", filme);
        req.setAttribute("categorias", categoriaService.listar());
        req.setAttribute("categoriasSelecionadas", selecionadas);
        forward(req, resp, FORM);
    }

    private Filme fromRequest(HttpServletRequest req) {
        Filme filme = new Filme();
        filme.setId(paramLong(req, "id"));
        filme.setTitulo(param(req, "titulo"));
        filme.setDescricao(param(req, "descricao"));
        filme.setGenero(param(req, "genero"));
        filme.setDiretor(param(req, "diretor"));
        try {
            filme.setAno(Integer.valueOf(param(req, "ano")));
        } catch (Exception e) {
            filme.setAno(null);
        }
        return filme;
    }

    private List<Long> listaLong(String[] valores) {
        List<Long> lista = new ArrayList<>();
        if (valores != null) {
            for (String valor : valores) {
                try {
                    lista.add(Long.valueOf(valor));
                } catch (Exception ignored) {
                    // Valores inválidos são ignorados; o Service valida se há categorias.
                }
            }
        }
        return lista;
    }
}
