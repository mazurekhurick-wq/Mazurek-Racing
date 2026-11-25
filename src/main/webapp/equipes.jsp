<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Equipes</h1>

    <jsp:include page="mensagens.jsp" />

    <div class="text-end mb-3">
        <a class="btn btn-primary" href="novaEquipe">+ Nova equipe</a>
    </div>

    <c:if test="${empty equipes}">
        <div class="alert alert-info">Nenhuma equipe cadastrada.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="e" items="${equipes}">
            <div class="col-md-6 col-xl-4">
                <article class="card-equipe" style="border-top-color: <c:out value='${e.cor}'/>;">
                    <div class="imagem">
                        <img src="<c:out value='${e.logoUrl}'/>" alt="Imagem da equipe <c:out value='${e.nome}'/>">
                    </div>
                    <div class="corpo">
                        <h2><c:out value="${e.nome}"/></h2>
                        <dl>
                            <dt>País</dt>
                            <dd><c:out value="${e.pais}"/></dd>
                            <dt>Motor</dt>
                            <dd><c:out value="${e.motor}"/></dd>
                            <dt>Fundação</dt>
                            <dd><c:out value="${empty e.anoFundacao ? '-' : e.anoFundacao}"/></dd>
                            <dt>Pilotos</dt>
                            <dd><a href="pilotos?equipe=${e.id}">${e.totalPilotos}</a></dd>
                        </dl>
                        <div class="acoes">
                            <a class="btn btn-sm btn-primary" href="editarEquipe?id=${e.id}">Editar</a>
                            <form id="formExcluirEquipe${e.id}" action="excluirEquipe" method="post">
                                <input type="hidden" name="id" value="${e.id}">
                                <button class="btn btn-sm btn-danger" type="button"
                                        data-form="formExcluirEquipe${e.id}"
                                        data-nome="<c:out value='${e.nome}'/>"
                                        onclick="confirmarExclusao(this)">Excluir</button>
                            </form>
                        </div>
                    </div>
                </article>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="modalExcluir.jsp" />
<jsp:include page="footer.jsp" />
