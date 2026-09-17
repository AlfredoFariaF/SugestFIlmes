package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Base dos controllers.
 * So helpers de rota/view: ler parametro, encaminhar JSP e redirecionar.
 * Regra de negocio fica no Service.
 */
public abstract class BaseServlet extends HttpServlet {

    protected boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null || usuario.getPerfil() == null) {
            return false;
        }
        return "Administrador".equalsIgnoreCase(
                usuario.getPerfil().getNome());
    }

    protected boolean exigirAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAdmin(req)) {
            resp.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Acesso permitido somente para administradores.");
            return false;
        }
        return true;
    }

    protected String acao(HttpServletRequest req) {
        String acao = req.getParameter("acao");
        if (acao == null || acao.isBlank()) {
            return "listar";
        }
        return acao;
    }

    protected String param(HttpServletRequest req, String nome) {
        return req.getParameter(nome);
    }

    protected Long paramLong(HttpServletRequest req, String nome) {
        String valor = req.getParameter(nome);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String caminho)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + caminho);
    }
}
