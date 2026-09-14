package br.com.mvc.service;

import br.com.mvc.dao.AvaliacaoDAO;
import br.com.mvc.dao.FilmeDAO;
import br.com.mvc.model.Avaliacao;

import java.util.List;

public class AvaliacaoService {

    private final AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO();
    private final FilmeDAO filmeDAO = new FilmeDAO();

    public List<Avaliacao> listar() {
        return avaliacaoDAO.listarTodas();
    }

    public List<Avaliacao> listarPorFilme(Long filmeId) {
        return avaliacaoDAO.listarPorFilme(filmeId);
    }

    public List<Avaliacao> listarPorUsuario(Long usuarioId) {
        return avaliacaoDAO.listarPorUsuario(usuarioId);
    }

    public Avaliacao buscarDoUsuario(Long usuarioId, Long filmeId) {
        return avaliacaoDAO.buscarPorUsuarioEFilme(usuarioId, filmeId);
    }

    public Avaliacao buscarPorIdEUsuario(Long id, Long usuarioId) {
        return avaliacaoDAO.buscarPorIdEUsuario(id, usuarioId);
    }

    public void salvar(Avaliacao a) {
        validar(a);
        avaliacaoDAO.salvar(a);
    }

    public void alterar(Avaliacao a) {
        validar(a);

        if (a.getId() == null) {
            throw new IllegalArgumentException("A avaliação é obrigatória para alteração.");
        }

        if (avaliacaoDAO.buscarPorIdEUsuario(a.getId(), a.getUsuarioId()) == null) {
            throw new IllegalArgumentException("Avaliação não encontrada ou não pertence ao usuário.");
        }

        avaliacaoDAO.alterar(a);
    }

    public void deletar(Long id, Long usuarioId) {
        if (id == null || usuarioId == null) {
            throw new IllegalArgumentException("Avaliação inválida.");
        }

        if (avaliacaoDAO.buscarPorIdEUsuario(id, usuarioId) == null) {
            throw new IllegalArgumentException("Avaliação não encontrada ou não pertence ao usuário.");
        }

        avaliacaoDAO.deletar(id, usuarioId);
    }

    private void validar(Avaliacao a) {
        if (a == null || a.getFilmeId() == null || a.getUsuarioId() == null) {
            throw new IllegalArgumentException("Filme e usuário são obrigatórios.");
        }

        if (filmeDAO.buscarPorId(a.getFilmeId()) == null) {
            throw new IllegalArgumentException("Filme não encontrado.");
        }

        if (a.getNota() < 1 || a.getNota() > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
        }

        if (a.getComentario() != null && a.getComentario().length() > 500) {
            throw new IllegalArgumentException("Comentário deve ter no máximo 500 caracteres.");
        }
    }
}
