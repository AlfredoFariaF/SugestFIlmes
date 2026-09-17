package br.com.mvc.controller;

import br.com.mvc.model.Filme;
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
    private final CategoriaService categoriaService = new CategoriaService();
    private static final String LISTA = "/WEB-INF/jsp/filmes/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/filmes/form.jsp";
    private static final String DETALHES = "/WEB-INF/jsp/filmes/detalhes.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (acao(req)) {
            case "novo" -> form(req, resp, new Filme(), List.of());
            case "editar" -> {

                if (!exigirAdmin(req, resp)) {
                    return;
                }

                Filme f = filmeService.buscarPorId(
                        paramLong(req, "id"));

                if (f == null) {
                    redirect(req, resp, "/filmes");
                    return;
                }

                List<Long> ids = f.getCategorias()
                        .stream()
                        .map(c -> c.getId())
                        .toList();

                form(req, resp, f, ids);
            }
            case "detalhes" -> {
                Filme f = filmeService.buscarPorId(paramLong(req, "id"));
                if (f == null) {
                    redirect(req, resp, "/filmes");
                    return;
                }
                req.setAttribute("filme", f);
                this.forward(req, resp, DETALHES);
            }
            case "excluir" -> {

                if (!exigirAdmin(req, resp)) {
                    return;
                }

                try {

                    filmeService.deletar(
                            paramLong(req, "id"));

                    redirect(req, resp, "/filmes");

                } catch (IllegalArgumentException e) {

                    req.setAttribute(
                            "erro",
                            e.getMessage());

                    req.setAttribute(
                            "filmes",
                            filmeService.listar());

                    this.forward(
                            req,
                            resp,
                            LISTA);
                }
            }
            default -> {
                String busca = param(req, "busca");
                req.setAttribute("filmes", filmeService.pesquisar(busca));
                req.setAttribute("busca", busca);
                this.forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");

        Filme f = fromRequest(req);
        List<Long> categorias = listaLong(
                req.getParameterValues("categoria"));
        try {
            // Só administrador pode alterar um filme existente
            if (f.getId() != null && !isAdmin(req)) {
                resp.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Somente administradores podem editar filmes.");
                return;
            }
            filmeService.salvar(f, categorias);
            redirect(req, resp, "/filmes");
        } catch (IllegalArgumentException e) {
            req.setAttribute(
                    "erro",
                    e.getMessage());
            form(
                    req,
                    resp,
                    f,
                    categorias);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Filme f, List<Long> selecionadas)
            throws ServletException, IOException {
        req.setAttribute("filme", f);
        req.setAttribute("categorias", categoriaService.listar());
        req.setAttribute("categoriasSelecionadas", selecionadas);
        this.forward(req, resp, FORM);
    }

    private Filme fromRequest(HttpServletRequest req) {
        Filme f = new Filme();
        f.setId(paramLong(req, "id"));
        f.setTitulo(param(req, "titulo"));
        f.setDescricao(param(req, "descricao"));
        f.setGenero(param(req, "genero"));
        f.setDiretor(param(req, "diretor"));
        try {
            f.setAno(Integer.valueOf(param(req, "ano")));
        } catch (Exception e) {
            f.setAno(null);
        }
        return f;
    }

    private List<Long> listaLong(String[] valores) {
        List<Long> lista = new ArrayList<>();
        if (valores != null)
            for (String v : valores)
                try {
                    lista.add(Long.valueOf(v));
                } catch (Exception ignored) {
                }
        return lista;
    }
}
