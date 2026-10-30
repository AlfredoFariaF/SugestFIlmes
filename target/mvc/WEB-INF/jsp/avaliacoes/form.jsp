<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Editar avaliação - CineVault</title>
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
                        <h1>Editar avaliação</h1>
                        <p>Altere somente a nota e o comentário da sua avaliação.</p>
                    </div>
                </div>

                <c:if test="${not empty erro}">
                    <div class="alert alert-erro">
                        <c:out value="${erro}" />
                    </div>
                </c:if>

                <section class="card" style="max-width: 680px;">
                    <p><strong>Filme:</strong>
                        <c:out value="${avaliacao.tituloFilme}" />
                    </p>

                    <form method="post" action="${pageContext.request.contextPath}/avaliacoes?acao=atualizar">
                        <input type="hidden" name="id" value="${avaliacao.id}">

                        <div class="form-group">
                            <label for="nota">Nota</label>
                            <select id="nota" name="nota" required>
                                <option value="">Selecione uma nota</option>
                                <c:forEach var="n" begin="1" end="5">
                                    <option value="${n}" <c:if test="${avaliacao.nota eq n}">selected</c:if>>${n}
                                        estrela(s)</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="comentario">Comentário</label>
                            <textarea id="comentario" name="comentario" rows="5" maxlength="500"
                                placeholder="Conte o que achou do filme..."><c:out value="${avaliacao.comentario}"/></textarea>
                        </div>

                        <div class="actions">
                            <button class="btn" type="submit">Salvar alterações</button>
                            <a class="btn btn-secondary"
                                href="${pageContext.request.contextPath}/avaliacoes?minhas=true">Cancelar</a>
                        </div>
                    </form>
                </section>
            </main>
        </body>

        </html>