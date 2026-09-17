package br.com.mvc.service;

import br.com.mvc.model.Avaliacao;
import br.com.mvc.model.Filme;
import br.com.mvc.model.Usuario;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RegraDeNegocioTest {

    @Test
    public void autenticarUsuarioValido_deveRetornarUsuario() {
        UsuarioService service = new UsuarioService();

        Usuario usuario = service.autenticar("admin", "123456");

        assertNotNull(usuario);
        assertEquals("Administrador do Sistema", usuario.getNome());
        assertEquals("admin", usuario.getLogin());
    }

    @Test
    public void autenticarUsuarioInvalido_deveLancarErro() {
        UsuarioService service = new UsuarioService();

        try {
            service.autenticar("admin", "senhaErrada");
            fail("Deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Login ou senha invalidos"));
        }
    }

    @Test
    public void salvarUsuarioComSenhaCurta_deveLancarErro() {
        UsuarioService service = new UsuarioService();

        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setLogin("usuarioTesteSenhaCurta" + System.currentTimeMillis());
        usuario.setSenha("123");
        usuario.setPerfilId(3L);

        try {
            service.salvar(usuario);
            fail("Deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Senha deve ter no minimo 6 caracteres"));
        }
    }

    @Test
    public void recomendarComCategoriasValidas_deveRetornarFilmes() {
        RecomendacaoService service = new RecomendacaoService();

        List<Filme> filmes = service.recomendar(Arrays.asList(1L, 2L));

        assertNotNull(filmes);
        assertFalse(filmes.isEmpty());
        assertTrue(filmes.stream().anyMatch(f -> f.getTitulo() != null));
    }

    @Test
    public void salvarAvaliacaoComNotaInvalida_deveLancarErro() {
        AvaliacaoService service = new AvaliacaoService();

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setFilmeId(1L);
        avaliacao.setUsuarioId(1L);
        avaliacao.setNota(0);
        avaliacao.setComentario("Nota inválida");

        try {
            service.salvar(avaliacao);
            fail("Deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("A nota deve estar entre 1 e 5"));
        }
    }
}
