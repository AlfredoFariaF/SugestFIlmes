package br.com.mvc.controller;

import br.com.mvc.service.CategoriaService;
import br.com.mvc.service.FilmeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {
    private final FilmeService filmeService = new FilmeService();
    private final CategoriaService categoriaService = new CategoriaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categorias", categoriaService.listar());
        req.setAttribute("filmes", filmeService.destaques(6));
        this.forward(req, resp, "/WEB-INF/jsp/home.jsp");
    }
}
