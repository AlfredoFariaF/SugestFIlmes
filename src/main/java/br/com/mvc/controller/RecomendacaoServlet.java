package br.com.mvc.controller;

import br.com.mvc.service.CategoriaService;
import br.com.mvc.service.RecomendacaoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/recomendacoes")
public class RecomendacaoServlet extends BaseServlet {
    private final RecomendacaoService service=new RecomendacaoService();
    private final CategoriaService categoriaService=new CategoriaService();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        List<Long> categorias=new ArrayList<>();String[] values=req.getParameterValues("categoria");if(values!=null)for(String v:values)try{categorias.add(Long.valueOf(v));}catch(Exception ignored){}
        req.setAttribute("categorias",categoriaService.listar()); req.setAttribute("selecionadas",categorias); req.setAttribute("filmes",service.recomendar(categorias));
        forward(req,resp,"/WEB-INF/jsp/recomendacoes/lista.jsp");
    }
}
