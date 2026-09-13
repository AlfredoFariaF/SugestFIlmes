<%--
  Redireciona a raiz da aplicacao para o login.
--%>
<%
    response.sendRedirect(request.getContextPath() + "/login");
%>
