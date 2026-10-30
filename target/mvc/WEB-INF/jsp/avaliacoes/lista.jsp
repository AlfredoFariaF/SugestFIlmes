<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Avaliações - CineVault</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
        </head>

        <body>
            <header class="topbar">
                <div class="container topbar-content">
                    <a class="logo" href="${pageContext.request.contextPath}/home">🎬 CineVault</a>
                    <nav class="nav">
                        <a href="${pageContext.request.contextPath}/home">Início</a>
                        <a href="${pageContext.request.contextPath}/filmes">Filmes</a>
                        <a href="${pageContext.request.contextPath}/categorias">Categorias</a>
                        <a href="${pageContext.request.contextPath}/recomendacoes">Recomendações</a>
                        <a class="active" href="${pageContext.request.contextPath}/avaliacoes?minhas=true">Minhas
                            avaliações</a>
                    </nav>
                    <a href="${pageContext.request.contextPath}/logout">Sair</a>
                </div>
            </header>

            <main class="container page-space">
                <div class="page-header">
                    <div>
                        <h1>
                            <c:choose>
                                <c:when test="${not empty filme}">Avaliações:
                                    <c:out value="${filme.titulo}" />
                                </c:when>
                                <c:when test="${minhas}">Minhas avaliações</c:when>
                                <c:otherwise>Avaliações dos usuários</c:otherwise>
                            </c:choose>
                        </h1>
                        <p>
                            <c:choose>
                                <c:when test="${not empty filme}">Veja as avaliações de todos os usuários para este
                                    filme. Você só pode editar ou excluir a sua própria avaliação.</c:when>
                                <c:when test="${minhas}">Consulte, edite ou exclua as avaliações que você registrou.
                                </c:when>
                                <c:otherwise>Veja as avaliações registradas pelos usuários no CineVault.</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/filmes">Explorar filmes</a>
                </div>

                <c:if test="${not empty erro}">
                    <div class="alert alert-erro">
                        <c:out value="${erro}" />
                    </div>
                </c:if>

                <div class="review-list">
                    <c:forEach var="avaliacao" items="${avaliacoes}">
                        <article class="review-card">
                            <div class="review-head">
                                <div>
                                    <strong>
                                        <c:out value="${avaliacao.tituloFilme}" />
                                    </strong>
                                    <span class="review-user">por
                                        <c:out value="${avaliacao.nomeUsuario}" />
                                    </span>
                                </div>
                                <div class="stars">
                                    <c:out value="${avaliacao.nota}" /> ⭐
                                </div>
                            </div>

                            <c:if test="${not empty avaliacao.comentario}">
                                <p>
                                    <c:out value="${avaliacao.comentario}" />
                                </p>
                            </c:if>

                            <small>
                                <c:out value="${avaliacao.dataAvaliacao}" />
                            </small>

                            <div class="review-actions">
                                <%-- O proprietário pode editar a própria avaliação. --%>
                                    <c:if test="${avaliacao.usuarioId eq sessionScope.usuarioLogado.id}">
                                        <a class="btn btn-secondary btn-small"
                                            href="${pageContext.request.contextPath}/avaliacoes?acao=editar&amp;id=${avaliacao.id}">
                                            Editar
                                        </a>
                                    </c:if>

                                    <%-- O proprietário ou um administrador pode excluir. --%>
                                        <c:if test="${avaliacao.usuarioId eq sessionScope.usuarioLogado.id or ehAdmin}">
                                            <form method="post"
                                                action="${pageContext.request.contextPath}/avaliacoes?acao=excluir"
                                                onsubmit="return confirm('Deseja realmente excluir esta avaliação?');">
                                                <input type="hidden" name="id" value="${avaliacao.id}">
                                                <c:if test="${not empty filme}">
                                                    <input type="hidden" name="filmeId" value="${filme.id}">
                                                </c:if>
                                                <c:if test="${minhas}">
                                                    <input type="hidden" name="minhas" value="true">
                                                </c:if>
                                                <button class="btn btn-danger btn-small" type="submit">Excluir</button>
                                            </form>
                                        </c:if>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <c:if test="${empty avaliacoes}">
                    <div class="empty">Nenhuma avaliação encontrada.</div>
                </c:if>
            </main>
        </body>

        </html>