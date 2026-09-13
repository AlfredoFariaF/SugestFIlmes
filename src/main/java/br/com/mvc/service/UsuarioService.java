package br.com.mvc.service;

import br.com.mvc.dao.PerfilDAO;
import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Usuario;

import java.util.List;

/**
 * SERVICE de Usuario — regras de negocio ficam aqui.
 *
 * Controller so chama estes metodos e decide a view.
 * DAO so executa SQL.
 */
public class UsuarioService {

    private static final int SENHA_MINIMA = 6;

    private final UsuarioDAO usuarioDAO;
    private final PerfilDAO perfilDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.perfilDAO = new PerfilDAO();
    }

    /**
     * Regra de autenticacao:
     * - login e senha obrigatorios
     * - so libera acesso se existir usuario com esse login/senha
     */
    public Usuario autenticar(String login, String senha) {
        login = this.normalizar(login);
        senha = this.normalizar(senha);

        if (login == null || senha == null) {
            throw new IllegalArgumentException("Informe login e senha.");
        }

        Usuario usuario = this.usuarioDAO.buscarPorLoginESenha(login, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Login ou senha invalidos.");
        }
        return usuario;
    }

    public List<Usuario> listar() {
        return this.usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return this.usuarioDAO.buscarPorId(id);
    }

    /**
     * Regra de salvamento:
     * - sem id  -> cadastro novo
     * - com id  -> alteracao (usuario precisa existir)
     */
    public void salvar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario e obrigatorio.");
        }

        this.prepararDados(usuario);
        this.validarCamposObrigatorios(usuario);
        this.validarSenha(usuario.getSenha());
        this.validarPerfilExistente(usuario.getPerfilId());
        this.validarLoginUnico(usuario);

        if (usuario.getId() == null) {
            this.usuarioDAO.inserir(usuario);
            return;
        }

        if (this.usuarioDAO.buscarPorId(usuario.getId()) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado para alteracao.");
        }
        this.usuarioDAO.alterar(usuario);
    }

    /**
     * Regra de exclusao:
     * - id obrigatorio
     * - usuario precisa existir
     */
    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        if (this.usuarioDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        this.usuarioDAO.deletar(id);
    }

    private void prepararDados(Usuario usuario) {
        usuario.setNome(this.normalizar(usuario.getNome()));
        usuario.setLogin(this.normalizar(usuario.getLogin()));
        usuario.setSenha(this.normalizar(usuario.getSenha()));
    }

    private void validarCamposObrigatorios(Usuario usuario) {
        if (usuario.getNome() == null) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
        if (usuario.getLogin() == null) {
            throw new IllegalArgumentException("Login e obrigatorio.");
        }
        if (usuario.getSenha() == null) {
            throw new IllegalArgumentException("Senha e obrigatoria.");
        }
        if (usuario.getPerfilId() == null) {
            throw new IllegalArgumentException("Perfil e obrigatorio.");
        }
    }

    private void validarSenha(String senha) {
        if (senha.length() < SENHA_MINIMA) {
            throw new IllegalArgumentException("Senha deve ter no minimo " + SENHA_MINIMA + " caracteres.");
        }
    }

    private void validarPerfilExistente(Long perfilId) {
        if (this.perfilDAO.buscarPorId(perfilId) == null) {
            throw new IllegalArgumentException("Perfil informado nao existe.");
        }
    }

    private void validarLoginUnico(Usuario usuario) {
        Usuario existente = this.usuarioDAO.buscarPorLogin(usuario.getLogin());
        if (existente == null) {
            return;
        }
        // no cadastro, qualquer login repetido e invalido
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("Ja existe um usuario com este login.");
        }
        // na alteracao, so permite se o login for do proprio usuario
        if (!existente.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Ja existe um usuario com este login.");
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
