package br.com.mvc.controller;

import jakarta.servlet.ServletException;
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
