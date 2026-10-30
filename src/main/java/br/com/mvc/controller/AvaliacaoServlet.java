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

    private static final String LISTA = "/WEB-INF/jsp/avaliacoes/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/avaliacoes/form.jsp";

    private final AvaliacaoService service = new AvaliacaoService();
    private final FilmeService filmeService = new FilmeService();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        Usuario usuario = usuarioLogado(req);
        if (usuario == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String acao = acao(req);

        if ("editar".equals(acao)) {
            abrirEdicao(req, resp, usuario);
            return;
        }

        if ("excluir".equals(acao) || "atualizar".equals(acao)) {
            resp.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Esta operação deve ser enviada por POST.");
            return;
        }

        listar(req, resp, usuario);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        Usuario usuario = usuarioLogado(req);
        if (usuario == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String acao = param(req, "acao");

        if ("excluir".equals(acao)) {
            excluir(req, resp, usuario);
            return;
        }

        if ("atualizar".equals(acao)) {
            atualizar(req, resp, usuario);
            return;
        }

        registrar(req, resp, usuario);
    }

    /**
     * Lista todas as avaliações quando há filmeId, ou somente as do usuário
     * quando minhas=true. Os botões são condicionados ao proprietário na JSP.
     */
    private void listar(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws ServletException, IOException {

        Long filmeId = paramLong(req, "filmeId");
        boolean minhas = "true".equalsIgnoreCase(param(req, "minhas"));

        Filme filme = null;

        if (filmeId != null) {
            filme = filmeService.buscarPorId(filmeId);
            if (filme == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Filme não encontrado.");
                return;
            }

            // Mostra avaliações de todos os usuários para o filme selecionado.
            req.setAttribute("avaliacoes", service.listarPorFilme(filmeId));
            req.setAttribute("minhaAvaliacao", service.buscarDoUsuario(usuario.getId(), filmeId));
        } else if (minhas) {
            req.setAttribute("avaliacoes", service.listarPorUsuario(usuario.getId()));
        } else {
            req.setAttribute("avaliacoes", service.listar());
        }

        req.setAttribute("filme", filme);
        req.setAttribute("minhas", minhas);
        req.setAttribute("ehAdmin", ehAdministrador(usuario));
        forward(req, resp, LISTA);
    }

    /** Abre o formulário somente se a avaliação pertencer ao usuário da sessão. */
    private void abrirEdicao(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws ServletException, IOException {

        Long id = paramLong(req, "id");
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da avaliação inválido.");
            return;
        }

        Avaliacao avaliacao = service.buscarPorIdDoUsuario(id, usuario.getId());
        if (avaliacao == null) {
            // 404 evita revelar se a avaliação existe, mas pertence a outra pessoa.
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avaliação não encontrada.");
            return;
        }

        req.setAttribute("avaliacao", avaliacao);
        req.setAttribute("filme", filmeService.buscarPorId(avaliacao.getFilmeId()));
        forward(req, resp, FORM);
    }

    /** Cadastro inicial: o usuarioId sempre vem da sessão, nunca do formulário. */
    private void registrar(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws ServletException, IOException {

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setFilmeId(paramLong(req, "filmeId"));
        avaliacao.setUsuarioId(usuario.getId());
        avaliacao.setNota(lerNota(req));
        avaliacao.setComentario(param(req, "comentario"));

        try {
            service.salvar(avaliacao);
            redirect(req, resp, "/filmes?acao=detalhes&id=" + avaliacao.getFilmeId());
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            Filme filme = filmeService.buscarPorId(avaliacao.getFilmeId());
            req.setAttribute("filme", filme);
            req.setAttribute("minhaAvaliacao", avaliacao);
            if (avaliacao.getFilmeId() != null) {
                req.setAttribute("avaliacoes", service.listarPorFilme(avaliacao.getFilmeId()));
            }
            forward(req, resp, "/WEB-INF/jsp/filmes/detalhes.jsp");
        }
    }

    /** Atualiza nota/comentário com ID e proprietário conferidos no backend. */
    private void atualizar(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws ServletException, IOException {

        Long id = paramLong(req, "id");
        int nota = lerNota(req);
        String comentario = param(req, "comentario");

        try {
            Avaliacao existente = service.buscarPorIdDoUsuario(id, usuario.getId());
            if (existente == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avaliação não encontrada.");
                return;
            }

            service.atualizar(id, usuario.getId(), nota, comentario);
            redirect(req, resp, "/avaliacoes?filmeId=" + existente.getFilmeId());

        } catch (IllegalArgumentException e) {
            Avaliacao avaliacao = service.buscarPorIdDoUsuario(id, usuario.getId());
            if (avaliacao == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Avaliação não encontrada.");
                return;
            }

            avaliacao.setNota(nota);
            avaliacao.setComentario(comentario);
            req.setAttribute("avaliacao", avaliacao);
            req.setAttribute("filme", filmeService.buscarPorId(avaliacao.getFilmeId()));
            req.setAttribute("erro", e.getMessage());
            forward(req, resp, FORM);
        }
    }

    /**
     * Exclui por POST. O proprietário pode excluir a própria avaliação;
     * o administrador pode excluir qualquer avaliação para moderação.
     */
    private void excluir(
            HttpServletRequest req,
            HttpServletResponse resp,
            Usuario usuario) throws IOException {

        Long id = paramLong(req, "id");
        boolean admin = ehAdministrador(usuario);

        boolean excluida = admin
                ? service.deletarComoAdmin(id)
                : service.deletarDoUsuario(id, usuario.getId());

        if (!excluida) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Avaliação não encontrada ou você não tem permissão para excluí-la.");
            return;
        }

        Long filmeId = paramLong(req, "filmeId");
        if (filmeId != null) {
            redirect(req, resp, "/avaliacoes?filmeId=" + filmeId);
        } else if ("true".equalsIgnoreCase(param(req, "minhas"))) {
            redirect(req, resp, "/avaliacoes?minhas=true");
        } else {
            redirect(req, resp, "/avaliacoes");
        }
    }

    private boolean ehAdministrador(Usuario usuario) {
        return usuario != null
                && usuario.getPerfil() != null
                && "Administrador".equalsIgnoreCase(usuario.getPerfil().getNome());
    }

    private Usuario usuarioLogado(HttpServletRequest req) {
        if (req.getSession(false) == null) {
            return null;
        }
        return (Usuario) req.getSession(false).getAttribute("usuarioLogado");
    }

    private int lerNota(HttpServletRequest req) {
        try {
            return Integer.parseInt(param(req, "nota"));
        } catch (Exception e) {
            return 0;
        }
    }
}
