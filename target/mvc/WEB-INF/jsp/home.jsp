<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Home - MVC Aula</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header class="topbar">
    <div class="container">
        <strong>MVC Aula</strong>
        <nav>
            <span>${usuarioLogado.nome} (${usuarioLogado.perfil.nome})</span>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="page-header">
        <h1>Painel</h1>
    </div>

    <div class="grid-cards">
        <a class="menu-card" href="${pageContext.request.contextPath}/usuarios">
            <strong>Usuarios</strong>
            <span>Listar, cadastrar, editar e excluir usuarios.</span>
        </a>
        <a class="menu-card" href="${pageContext.request.contextPath}/perfis">
            <strong>Perfis</strong>
            <span>Listar, cadastrar, editar e excluir perfis.</span>
        </a>
    </div>
</main>
</body>
</html>
