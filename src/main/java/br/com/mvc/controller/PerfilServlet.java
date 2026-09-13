package br.com.mvc.controller;

import br.com.mvc.model.Perfil;
import br.com.mvc.service.PerfilService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller de Perfil.
 * Ponte entre rota, service e view — sem regra de negocio.
 */
@WebServlet("/perfis")
public class PerfilServlet extends BaseServlet {

    private static final String LISTA = "/WEB-INF/jsp/perfis/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/perfis/form.jsp";

    private final PerfilService perfilService = new PerfilService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        switch (this.acao(req)) {
            case "novo" -> this.form(req, resp, null);
            case "editar" -> this.form(req, resp, this.perfilService.buscarPorId(this.paramLong(req, "id")));
            case "excluir" -> {
                try {
                    this.perfilService.deletar(this.paramLong(req, "id"));
                } catch (IllegalArgumentException e) {
                    req.setAttribute("erro", e.getMessage());
                    req.setAttribute("perfis", this.perfilService.listar());
                    this.forward(req, resp, LISTA);
                    return;
                }
                this.redirect(req, resp, "/perfis");
            }
            default -> {
                req.setAttribute("perfis", this.perfilService.listar());
                this.forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Perfil perfil = this.fromRequest(req);

        try {
            this.perfilService.salvar(perfil);
            this.redirect(req, resp, "/perfis");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            this.form(req, resp, perfil);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Perfil perfil)
            throws ServletException, IOException {

        if ("editar".equals(this.acao(req)) && perfil == null) {
            this.redirect(req, resp, "/perfis");
            return;
        }

        req.setAttribute("perfil", perfil);
        this.forward(req, resp, FORM);
    }

    private Perfil fromRequest(HttpServletRequest req) {
        Perfil perfil = new Perfil();
        perfil.setId(this.paramLong(req, "id"));
        perfil.setNome(this.param(req, "nome"));
        return perfil;
    }
}
