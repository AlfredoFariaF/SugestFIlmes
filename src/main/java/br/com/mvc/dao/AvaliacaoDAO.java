package br.com.mvc.dao;

import br.com.mvc.model.Avaliacao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDAO extends MysqlDAO {
    public List<Avaliacao> listarTodas() {
        String sql = "SELECT a.*, f.titulo titulo_filme, u.nome nome_usuario FROM avaliacoes a " +
                "INNER JOIN filmes f ON f.id=a.filme_id INNER JOIN usuarios u ON u.id=a.usuario_id " +
                "ORDER BY a.data_avaliacao DESC";
        return consultar(sql);
    }
    public Avaliacao buscarPorUsuarioEFilme(Long usuarioId, Long filmeId) {
        String sql = "SELECT a.*, f.titulo titulo_filme, u.nome nome_usuario FROM avaliacoes a " +
                "INNER JOIN filmes f ON f.id=a.filme_id INNER JOIN usuarios u ON u.id=a.usuario_id " +
                "WHERE a.usuario_id=? AND a.filme_id=?";
        List<Avaliacao> lista = consultar(sql, usuarioId, filmeId);
        return lista.isEmpty() ? null : lista.get(0);
    }
    public List<Avaliacao> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT a.*, f.titulo titulo_filme, u.nome nome_usuario FROM avaliacoes a " +
                "INNER JOIN filmes f ON f.id=a.filme_id INNER JOIN usuarios u ON u.id=a.usuario_id " +
                "WHERE a.usuario_id=? ORDER BY a.data_avaliacao DESC";
        return consultar(sql, usuarioId);
    }

    public List<Avaliacao> listarPorFilme(Long filmeId) {
        String sql = "SELECT a.*, f.titulo titulo_filme, u.nome nome_usuario FROM avaliacoes a " +
                "INNER JOIN filmes f ON f.id=a.filme_id INNER JOIN usuarios u ON u.id=a.usuario_id " +
                "WHERE a.filme_id=? ORDER BY a.data_avaliacao DESC";
        return consultar(sql, filmeId);
    }
    public void salvar(Avaliacao a) {
        String sql = "INSERT INTO avaliacoes (filme_id, usuario_id, nota, comentario) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nota=VALUES(nota), comentario=VALUES(comentario), data_avaliacao=CURRENT_TIMESTAMP";
        try { super.executarUpdate(sql, a.getFilmeId(), a.getUsuarioId(), a.getNota(), a.getComentario()); }
        catch (SQLException e) { throw new RuntimeException("Erro ao salvar avaliação.", e); }
    }
    public void deletar(Long id) {
        try { super.executarUpdate("DELETE FROM avaliacoes WHERE id=?", id); }
        catch (SQLException e) { throw new RuntimeException("Erro ao excluir avaliação.", e); }
    }
    private List<Avaliacao> consultar(String sql, Object... parametros) {
        List<Avaliacao> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, parametros)) {
            while (rs.next()) {
                Avaliacao a = new Avaliacao();
                a.setId(rs.getLong("id")); a.setFilmeId(rs.getLong("filme_id")); a.setUsuarioId(rs.getLong("usuario_id"));
                a.setNota(rs.getInt("nota")); a.setComentario(rs.getString("comentario")); a.setDataAvaliacao(rs.getTimestamp("data_avaliacao"));
                a.setTituloFilme(rs.getString("titulo_filme")); a.setNomeUsuario(rs.getString("nome_usuario")); lista.add(a);
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao consultar avaliações.", e); }
        return lista;
    }
}
