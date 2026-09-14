package br.com.mvc.service;

import br.com.mvc.dao.AvaliacaoDAO;
import br.com.mvc.dao.FilmeDAO;
import br.com.mvc.model.Avaliacao;
import java.util.List;

public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    private final FilmeDAO filmeDAO = new FilmeDAO();
    public List<Avaliacao> listar() { return avaliacaoDAO.listarTodas(); }
    public List<Avaliacao> listarPorFilme(Long filmeId) { return avaliacaoDAO.listarPorFilme(filmeId); }
    public List<Avaliacao> listarPorUsuario(Long usuarioId) { return avaliacaoDAO.listarPorUsuario(usuarioId); }
    public Avaliacao buscarDoUsuario(Long usuarioId, Long filmeId) { return avaliacaoDAO.buscarPorUsuarioEFilme(usuarioId, filmeId); }
    public void salvar(Avaliacao a) {
        if (a == null || a.getFilmeId() == null || a.getUsuarioId() == null) throw new IllegalArgumentException("Filme e usuário são obrigatórios.");
        if (filmeDAO.buscarPorId(a.getFilmeId()) == null) throw new IllegalArgumentException("Filme não encontrado.");
        if (a.getNota() < 1 || a.getNota() > 5) throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
        if (a.getComentario() != null && a.getComentario().length() > 500) throw new IllegalArgumentException("Comentário deve ter no máximo 500 caracteres.");
        avaliacaoDAO.salvar(a);
    }
    public void deletar(Long id) { if (id != null) avaliacaoDAO.deletar(id); }
}
