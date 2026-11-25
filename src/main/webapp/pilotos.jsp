<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="header.jsp" />

<div class="container">
    <h1>Pilotos</h1>

    <jsp:include page="mensagens.jsp" />

    <div class="d-flex flex-wrap justify-content-between align-items-start mb-3">
        <div class="filtro-equipes">
            <a class="btn btn-sm ${equipeFiltro == 0 ? 'btn-primary' : 'btn-outline-dark'}" href="pilotos">Todos</a>
            <c:forEach var="e" items="${equipes}">
                <a class="btn btn-sm ${equipeFiltro == e.id ? 'btn-primary' : 'btn-outline-dark'}"
                   href="pilotos?equipe=${e.id}"><c:out value="${e.nome}"/></a>
            </c:forEach>
        </div>
        <a class="btn btn-primary" href="novoPiloto">+ Novo piloto</a>
    </div>

    <c:if test="${empty pilotos}">
        <div class="alert alert-info">Nenhum piloto cadastrado.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="p" items="${pilotos}">
            <div class="col-sm-6 col-lg-4 col-xl-3">
                <article class="card-piloto">
                    <div class="foto">
                        <img src="<c:out value='${p.fotoUrl}'/>" alt="Foto de <c:out value='${p.nome}'/>">
                        <span class="numero-selo">${p.numero}</span>
                    </div>
                    <div class="corpo">
                        <h2><c:out value="${p.nome}"/></h2>
                        <div class="equipe-nome">
                            <span class="selo-equipe" style="background-color: <c:out value='${p.equipeCor}'/>;"></span>
                            <c:out value="${p.equipeNome}"/>
                        </div>
                        <div class="detalhe">
                            <c:out value="${p.nacionalidade}"/>
                            <c:if test="${not empty p.dataNascimento}">
                                &middot; <fmt:formatDate value="${p.dataNascimento}" pattern="dd/MM/yyyy"/>
                            </c:if>
                        </div>
                        <div class="estatisticas">
                            <div><strong>${p.vitorias}</strong><span>Vitórias</span></div>
                            <div><strong>${p.titulos}</strong><span>Títulos</span></div>
                        </div>
                        <div class="acoes">
                            <a class="btn btn-sm btn-primary" href="editarPiloto?id=${p.id}">Editar</a>
                            <form id="formExcluirPiloto${p.id}" action="excluirPiloto" method="post">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="btn btn-sm btn-danger" type="button"
                                        data-form="formExcluirPiloto${p.id}"
                                        data-nome="<c:out value='${p.nome}'/>"
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
