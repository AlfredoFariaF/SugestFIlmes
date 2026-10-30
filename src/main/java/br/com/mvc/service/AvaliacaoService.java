package br.com.mvc.service;

import br.com.mvc.dao.AvaliacaoDAO;
import br.com.mvc.dao.FilmeDAO;
import br.com.mvc.model.Avaliacao;

import java.util.List;

/** Regras de negócio e validações das avaliações. */
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

    /** Não retorna avaliações pertencentes a outro usuário. */
    public Avaliacao buscarPorIdDoUsuario(Long id, Long usuarioId) {
        return avaliacaoDAO.buscarPorIdEUsuario(id, usuarioId);
    }

    /** Cria ou atualiza a avaliação do próprio usuário para um filme. */
    public void salvar(Avaliacao avaliacao) {
        validar(avaliacao);

        if (filmeDAO.buscarPorId(avaliacao.getFilmeId()) == null) {
            throw new IllegalArgumentException("Filme não encontrado.");
        }

        avaliacaoDAO.salvar(avaliacao);
    }

    /** Atualiza apenas nota/comentário da avaliação pertencente ao usuário. */
    public void atualizar(Long id, Long usuarioId, int nota, String comentario) {
        if (id == null || usuarioId == null) {
            throw new IllegalArgumentException("Avaliação não informada.");
        }

        Avaliacao existente = avaliacaoDAO.buscarPorIdEUsuario(id, usuarioId);
        if (existente == null) {
            throw new IllegalArgumentException(
                    "Avaliação não encontrada ou você não tem permissão para alterá-la.");
        }

        validarNotaEComentario(nota, comentario);

        boolean atualizada = avaliacaoDAO.atualizar(
                id,
                usuarioId,
                nota,
                normalizarComentario(comentario));

        if (!atualizada) {
            throw new IllegalArgumentException(
                    "Não foi possível atualizar a avaliação. Atualize a página e tente novamente.");
        }
    }

    /** Exclui apenas a avaliação do usuário logado. */
    public boolean deletarDoUsuario(Long id, Long usuarioId) {
        if (id == null || usuarioId == null) {
            return false;
        }

        return avaliacaoDAO.deletar(id, usuarioId);
    }

    /**
     * Exclui qualquer avaliação. O Controller deve chamar este método
     * somente depois de confirmar que o usuário da sessão é administrador.
     */
    public boolean deletarComoAdmin(Long id) {
        if (id == null) {
            return false;
        }

        return avaliacaoDAO.deletarPorId(id);
    }

    private void validar(Avaliacao avaliacao) {
        if (avaliacao == null || avaliacao.getFilmeId() == null || avaliacao.getUsuarioId() == null) {
            throw new IllegalArgumentException("Filme e usuário são obrigatórios.");
        }

        validarNotaEComentario(avaliacao.getNota(), avaliacao.getComentario());
        avaliacao.setComentario(normalizarComentario(avaliacao.getComentario()));
    }

    private void validarNotaEComentario(int nota, String comentario) {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
        }

        if (comentario != null && comentario.length() > 500) {
            throw new IllegalArgumentException("Comentário deve ter no máximo 500 caracteres.");
        }
    }

    private String normalizarComentario(String comentario) {
        if (comentario == null) {
            return null;
        }

        String texto = comentario.trim();
        return texto.isEmpty() ? null : texto;
    }
}
