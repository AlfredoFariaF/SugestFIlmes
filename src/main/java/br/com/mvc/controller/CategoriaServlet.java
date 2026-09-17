package br.com.mvc.controller;

import br.com.mvc.model.Categoria;
import br.com.mvc.service.CategoriaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/categorias")
public class CategoriaServlet extends BaseServlet {
    private final CategoriaService service = new CategoriaService();
    private static final String LISTA = "/WEB-INF/jsp/categorias/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/categorias/form.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (acao(req)) {
            case "novo" -> form(req, resp, new Categoria());
            case "editar" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }
                Categoria c = service.buscarPorId(
                        paramLong(req, "id"));
                if (c == null) {
                    redirect(req, resp, "/categorias");
                    return;
                }
                form(req, resp, c);
            }
            case "excluir" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }
                try {
                    service.deletar(
                            paramLong(req, "id"));
                    redirect(req, resp, "/categorias");
                } catch (RuntimeException e) {
                    req.setAttribute(
                            "erro",
                            "Não foi possível excluir a categoria. Verifique se ela está vinculada a filmes.");
                    req.setAttribute(
                            "categorias",
                            service.listar());
                    forward(
                            req,
                            resp,
                            LISTA);
                }
            }
            default -> {
                req.setAttribute("categorias", service.listar());
                forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Categoria c = new Categoria();
        c.setId(paramLong(req, "id"));
        c.setNome(param(req, "nome"));
        try {
            if (c.getId() != null && !isAdmin(req)) {
                resp.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Somente administradores podem editar categorias.");
                return;
            }
            service.salvar(c);
            redirect(
                    req,
                    resp,
                    "/categorias");
        } catch (IllegalArgumentException e) {
            req.setAttribute(
                    "erro",
                    e.getMessage());
            form(req, resp, c);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Categoria c)
            throws ServletException, IOException {
        req.setAttribute("categoria", c);
        forward(req, resp, FORM);
    }
}
