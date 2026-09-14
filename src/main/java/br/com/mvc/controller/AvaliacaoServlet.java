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

    private static final String LISTA = "/WEB-INF/jsp/avaliacoes/lista.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = usuarioLogado(req);
        if (usuario == null) {
            redirect(req, resp, "/login");
            return;
        }

        String acao = acao(req);

        if ("editar".equalsIgnoreCase(acao)) {
            editarFormulario(req, resp, usuario);
            return;
        }

        boolean minhas = "true".equalsIgnoreCase(param(req, "minhas"));

        req.setAttribute(
                "avaliacoes",
                minhas ? service.listarPorUsuario(usuario.getId()) : service.listar()
        );

        Filme filme = filmeService.buscarPorId(paramLong(req, "filmeId"));
        req.setAttribute("filme", filme);

        if (filme != null) {
            req.setAttribute(
                    "minhaAvaliacao",
                    service.buscarDoUsuario(usuario.getId(), filme.getId())
            );
        }

        forward(req, resp, LISTA);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Usuario usuario = usuarioLogado(req);
        if (usuario == null) {
            redirect(req, resp, "/login");
            return;
        }

        String acao = acao(req);

        try {
            switch (acao) {
                case "editar" -> alterar(req, resp, usuario);
                case "excluir" -> excluir(req, resp, usuario);
                default -> salvar(req, resp, usuario);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            carregarMinhasAvaliacoes(req, usuario);
            forward(req, resp, LISTA);
        }
    }

    private void salvar(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws IOException {

        Avaliacao avaliacao = montarAvaliacao(req, usuario);

        service.salvar(avaliacao);

        redirect(
                req,
                resp,
                "/filmes?acao=detalhes&id=" + avaliacao.getFilmeId()
        );
    }

    private void alterar(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws IOException {

        Avaliacao avaliacao = montarAvaliacao(req, usuario);
        avaliacao.setId(paramLong(req, "id"));

        service.alterar(avaliacao);

        redirect(req, resp, "/avaliacoes?minhas=true");
    }

    private void excluir(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws IOException {

        Long id = paramLong(req, "id");

        service.deletar(id, usuario.getId());

        redirect(req, resp, "/avaliacoes?minhas=true");
    }

    private void editarFormulario(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws ServletException, IOException {

        Long id = paramLong(req, "id");
        Avaliacao avaliacao = service.buscarPorIdEUsuario(id, usuario.getId());

        if (avaliacao == null) {
            redirect(req, resp, "/avaliacoes?minhas=true");
            return;
        }

        req.setAttribute("edicao", avaliacao);
        req.setAttribute("avaliacoes", service.listarPorUsuario(usuario.getId()));
        req.setAttribute("modoEdicao", true);

        forward(req, resp, LISTA);
    }

    private Avaliacao montarAvaliacao(
            HttpServletRequest req,
            Usuario usuario) {

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setFilmeId(paramLong(req, "filmeId"));
        avaliacao.setUsuarioId(usuario.getId());

        try {
            avaliacao.setNota(Integer.parseInt(param(req, "nota")));
        } catch (Exception e) {
            avaliacao.setNota(0);
        }

        avaliacao.setComentario(param(req, "comentario"));

        return avaliacao;
    }

    private void carregarMinhasAvaliacoes(
            HttpServletRequest req,
            Usuario usuario) {

        req.setAttribute(
                "avaliacoes",
                service.listarPorUsuario(usuario.getId())
        );
    }

    private Usuario usuarioLogado(HttpServletRequest req) {
        if (req.getSession(false) == null) {
            return null;
        }

        return (Usuario) req.getSession(false).getAttribute("usuarioLogado");
    }
}
