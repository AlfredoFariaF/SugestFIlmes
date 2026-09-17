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
                    <a class="logo" href="${pageContext.request.contextPath}/home"> CineVault</a>
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
                        <h1>Minhas avaliações</h1>
                        <p>Edite ou exclua as avaliações que você registrou.</p>
                    </div>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/filmes">Explorar filmes</a>
                </div>

                <c:if test="${not empty erro}">
                    <div class="alert alert-erro">${erro}</div>
                </c:if>

                <c:if test="${modoEdicao}">
                    <section class="card review-edit-card">
                        <div class="section-heading">
                            <div>
                                <h2>Editar avaliação</h2>
                                <p>${edicao.tituloFilme}</p>
                            </div>
                        </div>

                        <form method="post" action="${pageContext.request.contextPath}/avaliacoes?acao=editar">
                            <input type="hidden" name="id" value="${edicao.id}">
                            <input type="hidden" name="filmeId" value="${edicao.filmeId}">

                            <div class="form-group">
                                <label for="nota">Nota</label>
                                <select id="nota" name="nota" required>
                                    <option value="">Selecione</option>
                                    <c:forEach var="n" begin="1" end="5">
                                        <option value="${n}" <c:if test="${edicao.nota == n}">selected
                </c:if>>${n} estrela(s)</option>
                </c:forEach>
                </select>
                </div>

                <div class="form-group">
                    <label for="comentario">Comentário</label>
                    <textarea id="comentario" name="comentario" rows="4" maxlength="500">${edicao.comentario}</textarea>
                </div>

                <div class="actions-row">
                    <button class="btn" type="submit">Salvar alterações</button>
                    <a class="btn btn-secondary"
                        href="${pageContext.request.contextPath}/avaliacoes?minhas=true">Cancelar</a>
                </div>
                </form>
                </section>
                </c:if>

                <div class="review-list">
                    <c:forEach var="avaliacao" items="${avaliacoes}">
                        <article class="review-card">
                            <div class="review-head">
                                <div>
                                    <strong>${avaliacao.tituloFilme}</strong>
                                    <span class="review-user">por ${avaliacao.nomeUsuario}</span>
                                </div>
                                <div class="stars">Nota: ${avaliacao.nota}</div>
                            </div>

                            <c:if test="${not empty avaliacao.comentario}">
                                <p>${avaliacao.comentario}</p>
                            </c:if>

                            <small>${avaliacao.dataAvaliacao}</small>

                            <div class="review-actions">
                                <a class="btn btn-secondary btn-small"
                                    href="${pageContext.request.contextPath}/avaliacoes?acao=editar&id=${avaliacao.id}">
                                    Editar
                                </a>

                                <form method="post" action="${pageContext.request.contextPath}/avaliacoes?acao=excluir"
                                    onsubmit="return confirm('Deseja realmente excluir esta avaliação?');">
                                    <input type="hidden" name="id" value="${avaliacao.id}">
                                    <button class="btn btn-danger btn-small" type="submit">
                                        Excluir
                                    </button>
                                </form>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <c:if test="${empty avaliacoes}">
                    <div class="empty">
                        Você ainda não possui avaliações.
                    </div>
                </c:if>

            </main>

        </body>

        </html>