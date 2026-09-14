package br.com.mvc.controller;

import br.com.mvc.model.Avaliacao;
import br.com.mvc.model.Filme;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.AvaliacaoService;
import br.com.mvc.service.FilmeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/avaliacoes")
public class AvaliacaoServlet extends BaseServlet {
    private final AvaliacaoService service = new AvaliacaoService();
    private final FilmeService filmeService = new FilmeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession(false).getAttribute("usuarioLogado");
        boolean minhas = "true".equalsIgnoreCase(param(req, "minhas"));
        req.setAttribute("avaliacoes", minhas ? service.listarPorUsuario(usuario.getId()) : service.listar());

        Filme filme = filmeService.buscarPorId(paramLong(req, "filmeId"));
        req.setAttribute("filme", filme);
        if (filme != null) {
            req.setAttribute("minhaAvaliacao", service.buscarDoUsuario(usuario.getId(), filme.getId()));
        }
        forward(req, resp, "/WEB-INF/jsp/avaliacoes/lista.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Usuario usuario = (Usuario) req.getSession(false).getAttribute("usuarioLogado");

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setFilmeId(paramLong(req, "filmeId"));
        avaliacao.setUsuarioId(usuario.getId());
        try {
            avaliacao.setNota(Integer.parseInt(param(req, "nota")));
        } catch (Exception e) {
            avaliacao.setNota(0);
        }
        avaliacao.setComentario(param(req, "comentario"));

        try {
            service.salvar(avaliacao);
            redirect(req, resp, "/filmes?acao=detalhes&id=" + avaliacao.getFilmeId());
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            Filme filme = filmeService.buscarPorId(avaliacao.getFilmeId());
            req.setAttribute("filme", filme);
            req.setAttribute("minhaAvaliacao", avaliacao);
            req.setAttribute("avaliacoes", service.listarPorFilme(avaliacao.getFilmeId()));
            forward(req, resp, "/WEB-INF/jsp/avaliacoes/lista.jsp");
        }
    }
}
