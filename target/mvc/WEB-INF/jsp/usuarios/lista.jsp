<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuarios - MVC Aula</title>
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
        <h1>Usuarios</h1>
        <a class="btn" href="${pageContext.request.contextPath}/usuarios?acao=novo">Novo usuario</a>
    </div>

    <div class="table-wrap">
        <c:choose>
            <c:when test="${empty usuarios}">
                <p class="empty">Nenhum usuario cadastrado.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Login</th>
                        <th>Perfil</th>
                        <th>Acoes</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr>
                            <td>${usuario.id}</td>
                            <td>${usuario.nome}</td>
                            <td>${usuario.login}</td>
                            <td>${usuario.perfil.nome}</td>
                            <td class="links">
                                <a href="${pageContext.request.contextPath}/usuarios?acao=editar&id=${usuario.id}">Editar</a>
                                <a href="${pageContext.request.contextPath}/usuarios?acao=excluir&id=${usuario.id}"
                                   onclick="return confirm('Excluir este usuario?');">Excluir</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
