<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>${filme.titulo} - SugestFilmes</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
        </head>

        <body>
            <header class="topbar">
                <div class="container topbar-content"><a class="logo" href="${pageContext.request.contextPath}/home">
                        SugestFilmes</a>
                    <nav class="nav"><a href="${pageContext.request.contextPath}/home">Início</a><a class="active"
                            href="${pageContext.request.contextPath}/filmes">Filmes</a><a
                            href="${pageContext.request.contextPath}/categorias">Categorias</a><a
                            href="${pageContext.request.contextPath}/recomendacoes">Recomendações</a><a
                            href="${pageContext.request.contextPath}/avaliacoes?minhas=true">Minhas avaliações</a></nav>
                    <a href="${pageContext.request.contextPath}/logout">Sair</a>
                </div>
            </header>
            <main class="container page-space">
                <div class="detail-layout">
                    <div class="detail-poster"></div>
                    <div class="detail-content"><span class="eyebrow">${filme.ano} · ${filme.genero}</span>
                        <h1>${filme.titulo}</h1>
                        <p>${filme.descricao}</p>
                        <p><strong>Diretor:</strong> ${filme.diretor}</p>
                        <div class="chips">
                            <c:forEach var="categoria" items="${filme.categorias}"><span
                                    class="chip">${categoria.nome}</span></c:forEach>
                        </div>
                        <div class="rating-large">⭐ <c:choose>
                                <c:when test="${filme.totalAvaliacoes > 0}"><strong>${filme.mediaAvaliacao}</strong>
                                    (${filme.totalAvaliacoes} avaliações)</c:when>
                                <c:otherwise>Sem avaliações</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="detail-columns">
                    <section class="card">
                        <h2>Avalie este filme</h2>
                        <c:if test="${not empty erro}">
                            <div class="alert alert-erro">${erro}</div>
                        </c:if>
                        <form method="post" action="${pageContext.request.contextPath}/avaliacoes"><input type="hidden"
                                name="filmeId" value="${filme.id}">
                            <div class="form-group"><label for="nota">Nota</label><select id="nota" name="nota"
                                    required>
                                    <option value="">Selecione</option>
                                    <c:forEach var="n" begin="1" end="5">
                                        <option value="${n}" <c:if test="${minhaAvaliacao.nota == n}">selected</c:if>
                                            >${n} estrela(s)</option>
                                    </c:forEach>
                                </select></div>
                            <div class="form-group"><label for="comentario">Comentário</label><textarea id="comentario"
                                    name="comentario" rows="4" maxlength="500"
                                    placeholder="Conte o que achou do filme...">${minhaAvaliacao.comentario}</textarea>
                            </div><button class="btn" type="submit">Salvar avaliação</button>
                        </form>
                    </section>
                    <section>
                        <div class="section-heading">
                            <div>
                                <h2>Informações</h2>
                                <p>Veja outras avaliações no sistema.</p>
                            </div><a href="${pageContext.request.contextPath}/avaliacoes?filmeId=${filme.id}">Ver
                                avaliações</a>
                        </div>
                        <div class="card">
                            <p><strong>Título:</strong> ${filme.titulo}</p>
                            <p><strong>Ano:</strong> ${filme.ano}</p>
                            <p><strong>Diretor:</strong> ${filme.diretor}</p>
                            <p><strong>Gênero:</strong> ${filme.genero}</p>
                        </div>
                    </section>
                </div>
            </main>
        </body>

        </html>