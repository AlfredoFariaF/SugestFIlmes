package br.com.mvc.service;

import br.com.mvc.dao.PerfilDAO;
import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Perfil;

import java.util.List;

/**
 * SERVICE de Perfil — regras de negocio ficam aqui.
 *
 * Controller so chama estes metodos e decide a view.
 * DAO so executa SQL.
 */
public class PerfilService {

    private final PerfilDAO perfilDAO;
    private final UsuarioDAO usuarioDAO;

    public PerfilService() {
        this.perfilDAO = new PerfilDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public List<Perfil> listar() {
        return this.perfilDAO.listarTodos();
    }

    public Perfil buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return this.perfilDAO.buscarPorId(id);
    }

    /**
     * Regra de salvamento:
     * - sem id -> cadastro novo
     * - com id -> alteracao (perfil precisa existir)
     * - nome obrigatorio e unico
     */
    public void salvar(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Perfil e obrigatorio.");
        }

        perfil.setNome(this.normalizar(perfil.getNome()));
        this.validarNomeObrigatorio(perfil.getNome());
        this.validarNomeUnico(perfil);

        if (perfil.getId() == null) {
            this.perfilDAO.inserir(perfil);
            return;
        }

        if (this.perfilDAO.buscarPorId(perfil.getId()) == null) {
            throw new IllegalArgumentException("Perfil nao encontrado para alteracao.");
        }
        this.perfilDAO.alterar(perfil);
    }

    /**
     * Regra de exclusao:
     * - id obrigatorio
     * - perfil precisa existir
     * - nao pode excluir perfil que ainda tem usuarios vinculados
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        if (this.perfilDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Perfil nao encontrado.");
        }
        if (this.usuarioDAO.contarPorPerfil(id) > 0) {
            throw new IllegalArgumentException("Nao e possivel excluir perfil com usuarios vinculados.");
        }
        this.perfilDAO.deletar(id);
    }

    private void validarNomeObrigatorio(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
    }

    private void validarNomeUnico(Perfil perfil) {
        Perfil existente = this.perfilDAO.buscarPorNome(perfil.getNome());
        if (existente == null) {
            return;
        }
        if (perfil.getId() == null) {
            throw new IllegalArgumentException("Ja existe um perfil com este nome.");
        }
        if (!existente.getId().equals(perfil.getId())) {
            throw new IllegalArgumentException("Ja existe um perfil com este nome.");
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
