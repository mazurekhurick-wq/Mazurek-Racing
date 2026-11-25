<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<jsp:include page="/header.jsp" />
<div class="container">
    <h1>Algo deu errado</h1>
    <div class="form-painel">
        <p>Ocorreu um erro no servidor. Confirme se o MySQL do XAMPP está ligado e se o banco
           <strong>mazurek_racing</strong> foi importado.</p>
        <a class="btn btn-primary" href="index">Voltar ao início</a>
    </div>
</div>
<jsp:include page="/footer.jsp" />
