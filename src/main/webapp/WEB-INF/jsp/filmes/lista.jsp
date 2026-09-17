<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Filmes - CineVault</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
        </head>

        <body>
            <header class="topbar">
                <div class="container topbar-content"><a class="logo" href="${pageContext.request.contextPath}/home">
                        CineVault</a>
                    <nav class="nav"><a href="${pageContext.request.contextPath}/home">Início</a><a class="active"
                            href="${pageContext.request.contextPath}/filmes">Filmes</a><a
                            href="${pageContext.request.contextPath}/categorias">Categorias</a><a
                            href="${pageContext.request.contextPath}/recomendacoes">Recomendações</a><a
                            href="${pageContext.request.contextPath}/avaliacoes?minhas=true">Minhas avaliações</a></nav>
                    <div class="user-nav"><span>${usuarioLogado.nome}</span><a
                            href="${pageContext.request.contextPath}/logout">Sair</a></div>
                </div>
            </header>
            <main class="container page-space">
                <div class="page-header">
                    <div>
                        <h1>Filmes</h1>
                        <p>Consulte e gerencie os filmes do catálogo.</p>
                    </div><a class="btn" href="${pageContext.request.contextPath}/filmes?acao=novo">+ Novo filme</a>
                </div>
                <form class="search-bar" method="get" action="${pageContext.request.contextPath}/filmes"><input
                        type="text" name="busca" placeholder="Pesquisar por título, gênero ou diretor"
                        value="${busca}"><button class="btn" type="submit">Pesquisar</button></form>
                <c:if test="${not empty erro}">
                    <div class="alert alert-erro">${erro}</div>
                </c:if>
                <div class="movie-grid">
                    <c:forEach var="filme" items="${filmes}">
                        <article class="movie-card">
                            <div class="movie-poster"><span></span><span class="year-badge">${filme.ano}</span></div>
                            <div class="movie-body">
                                <h3>${filme.titulo}</h3>
                                <p class="movie-meta">${filme.genero} · ${filme.diretor}</p>
                                <div class="rating">Nota: <strong>
                                        <c:choose>
                                            <c:when test="${filme.totalAvaliacoes > 0}">${filme.mediaAvaliacao}</c:when>
                                            <c:otherwise>Sem avaliações</c:otherwise>
                                        </c:choose>
                                    </strong></div>
                                <div class="card-actions">
                                    <a class="btn btn-secondary btn-small"
                                        href="${pageContext.request.contextPath}/filmes?acao=detalhes&id=${filme.id}">
                                        Detalhes
                                    </a>
                                    <c:if test="${sessionScope.usuarioLogado.perfil.nome == 'Administrador'}">
                                        <a class="btn btn-small"
                                            href="${pageContext.request.contextPath}/filmes?acao=editar&id=${filme.id}">
                                            Editar
                                        </a>
                                        <a class="btn btn-danger btn-small"
                                            href="${pageContext.request.contextPath}/filmes?acao=excluir&id=${filme.id}"
                                            onclick="return confirm('Excluir este filme?');">
                                            Excluir
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </div>
                <c:if test="${empty filmes}">
                    <div class="empty">Nenhum filme encontrado.</div>
                </c:if>
            </main>
        </body>

        </html>