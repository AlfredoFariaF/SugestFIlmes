package br.com.mvc.controller;

import br.com.mvc.model.Perfil;
import br.com.mvc.model.Usuario;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Classe base dos controllers.
 * Centraliza helpers de rota, view, sessão e autorização.
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

    /** Retorna o usuário autenticado na sessão, ou null se não houver sessão. */
    protected Usuario obterUsuarioLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }

        Object usuario = session.getAttribute("usuarioLogado");
        return usuario instanceof Usuario ? (Usuario) usuario : null;
    }

    /**
     * Verifica o perfil carregado durante a autenticação.
     * O nome do perfil é obtido do relacionamento usuarios -> perfis.
     */
    protected boolean isAdmin(HttpServletRequest req) {
        Usuario usuario = obterUsuarioLogado(req);
        if (usuario == null) {
            return false;
        }

        Perfil perfil = usuario.getPerfil();
        return perfil != null
                && perfil.getNome() != null
                && "Administrador".equalsIgnoreCase(perfil.getNome().trim());
    }

    /**
     * Interrompe a requisição com HTTP 403 quando o usuário não é administrador.
     * Deve ser chamado antes de qualquer operação de cadastro, edição ou exclusão.
     */
    protected boolean exigirAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (isAdmin(req)) {
            return true;
        }

        resp.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Acesso permitido somente para administradores.");
        return false;
    }

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws jakarta.servlet.ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String caminho)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + caminho);
    }
}
