<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Perfis - MVC Aula</title>
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
        <h1>Perfis</h1>
        <a class="btn" href="${pageContext.request.contextPath}/perfis?acao=novo">Novo perfil</a>
    </div>

    <div class="table-wrap">
        <c:choose>
            <c:when test="${empty perfis}">
                <p class="empty">Nenhum perfil cadastrado.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Acoes</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="perfil" items="${perfis}">
                        <tr>
                            <td>${perfil.id}</td>
                            <td>${perfil.nome}</td>
                            <td class="links">
                                <a href="${pageContext.request.contextPath}/perfis?acao=editar&id=${perfil.id}">Editar</a>
                                <a href="${pageContext.request.contextPath}/perfis?acao=excluir&id=${perfil.id}"
                                   onclick="return confirm('Excluir este perfil?');">Excluir</a>
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
