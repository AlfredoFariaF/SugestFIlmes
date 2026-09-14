package br.com.mvc.dao;

import br.com.mvc.model.Categoria;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CategoriaDAO extends MysqlDAO {
    public List<Categoria> listarTodos() {

    String sql = "SELECT id, nome FROM categorias ORDER BY nome";

    System.out.println("SQL: " + sql);

    List<Categoria> lista = new ArrayList<>();

    try (ResultSet rs = super.executar(sql)) {

        while (rs.next()) {

            String nome = rs.getString("nome");

            System.out.println(
                    "ID: " + rs.getLong("id")
                    + " | NOME: [" + nome + "]"
            );

            System.out.println("NOME ORIGINAL: [" + nome + "]");
            System.out.println("BYTES: " + Arrays.toString(nome.getBytes(StandardCharsets.UTF_8)));


            lista.add(mapear(rs));
        }

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao listar categorias.", e);
    }

    return lista;
}
    public Categoria buscarPorId(Long id) {
        try (ResultSet rs = super.executar("SELECT id, nome FROM categorias WHERE id = ?", id)) {
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { throw new RuntimeException("Erro ao buscar categoria.", e); }
        return null;
    }
    public void inserir(Categoria categoria) {
        try { super.executarUpdate("INSERT INTO categorias (nome) VALUES (?)", categoria.getNome()); }
        catch (SQLException e) { throw new RuntimeException("Erro ao inserir categoria.", e); }
    }
    public void alterar(Categoria categoria) {
        try { super.executarUpdate("UPDATE categorias SET nome = ? WHERE id = ?", categoria.getNome(), categoria.getId()); }
        catch (SQLException e) { throw new RuntimeException("Erro ao alterar categoria.", e); }
    }
    public void deletar(Long id) {
        try { super.executarUpdate("DELETE FROM categorias WHERE id = ?", id); }
        catch (SQLException e) { throw new RuntimeException("Erro ao excluir categoria. Verifique se ela está em uso.", e); }
    }
    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria(); c.setId(rs.getLong("id")); c.setNome(rs.getString("nome")); return c;
    }
}
