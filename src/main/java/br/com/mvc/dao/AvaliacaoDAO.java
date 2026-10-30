package br.com.mvc.dao;

import br.com.mvc.model.Avaliacao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso aos dados das avaliações.
 *
 * As operações comuns de alteração/exclusão restringem o usuarioId no WHERE.
 * A exclusão administrativa é separada e só deve ser chamada após a validação
 * do perfil de administrador no fluxo da aplicação.
 */
public class AvaliacaoDAO extends MysqlDAO {

    private static final String SELECT_BASE = "SELECT a.*, f.titulo AS titulo_filme, u.nome AS nome_usuario " +
            "FROM avaliacoes a " +
            "INNER JOIN filmes f ON f.id = a.filme_id " +
            "INNER JOIN usuarios u ON u.id = a.usuario_id ";

    public List<Avaliacao> listarTodas() {
        return consultar(
                SELECT_BASE + "ORDER BY a.data_avaliacao DESC");
    }

    public Avaliacao buscarPorUsuarioEFilme(Long usuarioId, Long filmeId) {
        String sql = SELECT_BASE + "WHERE a.usuario_id = ? AND a.filme_id = ?";
        List<Avaliacao> lista = consultar(sql, usuarioId, filmeId);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /** Busca uma avaliação somente quando ela pertence ao usuário informado. */
    public Avaliacao buscarPorIdEUsuario(Long id, Long usuarioId) {
        if (id == null || usuarioId == null) {
            return null;
        }

        String sql = SELECT_BASE + "WHERE a.id = ? AND a.usuario_id = ?";
        List<Avaliacao> lista = consultar(sql, id, usuarioId);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<Avaliacao> listarPorUsuario(Long usuarioId) {
        String sql = SELECT_BASE +
                "WHERE a.usuario_id = ? ORDER BY a.data_avaliacao DESC";
        return consultar(sql, usuarioId);
    }

    /** Lista todas as avaliações de um filme, independentemente do autor. */
    public List<Avaliacao> listarPorFilme(Long filmeId) {
        String sql = SELECT_BASE +
                "WHERE a.filme_id = ? ORDER BY a.data_avaliacao DESC";
        return consultar(sql, filmeId);
    }

    /**
     * Cria a avaliação do usuário logado ou atualiza a avaliação dele para
     * aquele filme, respeitando a chave única (usuario_id, filme_id).
     */
    public void salvar(Avaliacao avaliacao) {
        String sql = "INSERT INTO avaliacoes " +
                "(filme_id, usuario_id, nota, comentario) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "nota = VALUES(nota), " +
                "comentario = VALUES(comentario), " +
                "data_avaliacao = CURRENT_TIMESTAMP";

        try {
            super.executarUpdate(
                    sql,
                    avaliacao.getFilmeId(),
                    avaliacao.getUsuarioId(),
                    avaliacao.getNota(),
                    avaliacao.getComentario());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar avaliação.", e);
        }
    }

    /** Atualiza nota e comentário somente da avaliação pertencente ao usuário. */
    public boolean atualizar(Long id, Long usuarioId, int nota, String comentario) {
        String sql = "UPDATE avaliacoes " +
                "SET nota = ?, comentario = ?, data_avaliacao = CURRENT_TIMESTAMP " +
                "WHERE id = ? AND usuario_id = ?";

        try {
            int alteradas = super.executarUpdate(sql, nota, comentario, id, usuarioId);

            // Se os mesmos dados forem enviados novamente, o MySQL pode informar
            // zero linhas alteradas. Nesse caso, confirma se o registro do usuário
            // ainda existe para não tratar uma atualização sem mudanças como erro.
            return alteradas > 0 || buscarPorIdEUsuario(id, usuarioId) != null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar avaliação.", e);
        }
    }

    /** Exclui somente se o ID e o proprietário corresponderem. */
    public boolean deletar(Long id, Long usuarioId) {
        String sql = "DELETE FROM avaliacoes WHERE id = ? AND usuario_id = ?";

        try {
            return super.executarUpdate(sql, id, usuarioId) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir avaliação.", e);
        }
    }

    /**
     * Exclui uma avaliação pelo ID, sem restringir o proprietário.
     * Este método deve ser chamado exclusivamente após a autorização de
     * administrador ser confirmada no Controller/Service.
     */
    public boolean deletarPorId(Long id) {
        String sql = "DELETE FROM avaliacoes WHERE id = ?";

        try {
            return super.executarUpdate(sql, id) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir avaliação como administrador.", e);
        }
    }

    private List<Avaliacao> consultar(String sql, Object... parametros) {
        List<Avaliacao> lista = new ArrayList<>();

        try (ResultSet rs = super.executar(sql, parametros)) {
            while (rs.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId(rs.getLong("id"));
                avaliacao.setFilmeId(rs.getLong("filme_id"));
                avaliacao.setUsuarioId(rs.getLong("usuario_id"));
                avaliacao.setNota(rs.getInt("nota"));
                avaliacao.setComentario(rs.getString("comentario"));
                avaliacao.setDataAvaliacao(rs.getTimestamp("data_avaliacao"));
                avaliacao.setTituloFilme(rs.getString("titulo_filme"));
                avaliacao.setNomeUsuario(rs.getString("nome_usuario"));
                lista.add(avaliacao);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar avaliações.", e);
        }

        return lista;
    }
}
