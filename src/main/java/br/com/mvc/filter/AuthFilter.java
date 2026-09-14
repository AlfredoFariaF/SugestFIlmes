package br.com.mvc.filter;

import br.com.mvc.model.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/home", "/filmes", "/categorias", "/avaliacoes", "/recomendacoes",
        "/usuarios", "/usuarios/*", "/perfis", "/perfis/*"
})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        Usuario usuarioLogado = session == null ? null : (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
