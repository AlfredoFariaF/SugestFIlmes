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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (acao(req)) {
            case "novo" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }
                form(req, resp, new Categoria());
            }
            case "editar" -> {
                if (!exigirAdmin(req, resp)) {
                    return;
                }

                Categoria categoria = service.buscarPorId(paramLong(req, "id"));
                if (categoria == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Categoria não encontrada.");
                    return;
                }
                form(req, resp, categoria);
            }
            case "excluir" -> resp.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "A exclusão de categorias deve ser enviada por POST.");
            default -> {
                req.setAttribute("categorias", service.listar());
                req.setAttribute("ehAdmin", isAdmin(req));
                forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Cadastro, alteração e exclusão do catálogo são exclusivos do administrador.
        if (!exigirAdmin(req, resp)) {
            return;
        }

        String acaoPost = param(req, "acao");
        if ("excluir".equalsIgnoreCase(acaoPost)) {
            excluir(req, resp);
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setId(paramLong(req, "id"));
        categoria.setNome(param(req, "nome"));

        try {
            service.salvar(categoria);
            redirect(req, resp, "/categorias");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            form(req, resp, categoria);
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            service.deletar(paramLong(req, "id"));
            redirect(req, resp, "/categorias");
        } catch (RuntimeException e) {
            req.setAttribute(
                    "erro",
                    "Não foi possível excluir a categoria. Verifique se ela está vinculada a filmes.");
            req.setAttribute("categorias", service.listar());
            req.setAttribute("ehAdmin", isAdmin(req));
            forward(req, resp, LISTA);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Categoria categoria)
            throws ServletException, IOException {
        req.setAttribute("categoria", categoria);
        forward(req, resp, FORM);
    }
}
