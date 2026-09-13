<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${empty usuario.id}">Novo usuario</c:when>
            <c:otherwise>Editar usuario</c:otherwise>
        </c:choose>
        - MVC Aula
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header class="topbar">
    <div class="container">
        <strong>MVC Aula</strong>
        <nav>
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
            <a href="${pageContext.request.contextPath}/perfis">Perfis</a>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${empty usuario.id}">Novo usuario</c:when>
                <c:otherwise>Editar usuario</c:otherwise>
            </c:choose>
        </h1>
    </div>

    <div class="card">
        <c:if test="${not empty erro}">
            <div class="alert alert-erro">${erro}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/usuarios">
            <input type="hidden" name="acao" value="salvar">
            <input type="hidden" name="id" value="${usuario.id}">

            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" value="${usuario.nome}" required>
            </div>

            <div class="form-group">
                <label for="login">Login</label>
                <input type="text" id="login" name="login" value="${usuario.login}" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="text" id="senha" name="senha" value="${usuario.senha}" required>
            </div>

            <div class="form-group">
                <label for="perfilId">Perfil</label>
                <select id="perfilId" name="perfilId" required>
                    <option value="">Selecione</option>
                    <c:forEach var="perfil" items="${perfis}">
                        <option value="${perfil.id}"
                                <c:if test="${usuario.perfilId == perfil.id}">selected</c:if>>
                            ${perfil.nome}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="actions">
                <button type="submit" class="btn">Salvar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
