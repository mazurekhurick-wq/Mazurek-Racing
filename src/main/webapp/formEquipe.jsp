<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="header.jsp" />

<%-- O mesmo formulário serve para CREATE (equipe sem id) e UPDATE (equipe com id) --%>
<c:set var="editando" value="${not empty equipe and equipe.id > 0}"/>

<div class="container">
    <h1>${editando ? 'Editar equipe' : 'Nova equipe'}</h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger"><c:out value="${erro}"/></div>
    </c:if>

    <div class="form-painel">
        <form action="${editando ? 'editarEquipe' : 'novaEquipe'}" method="post" enctype="multipart/form-data">
            <c:if test="${editando}">
                <input type="hidden" name="id" value="${equipe.id}">
            </c:if>

            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label" for="nome">Nome da equipe</label>
                    <input class="form-control" type="text" id="nome" name="nome" maxlength="80" required
                           value="<c:out value='${equipe.nome}'/>">
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="pais">País</label>
                    <input class="form-control" type="text" id="pais" name="pais" maxlength="60" required
                           value="<c:out value='${equipe.pais}'/>">
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="motor">Motor</label>
                    <input class="form-control" type="text" id="motor" name="motor" maxlength="60" required
                           value="<c:out value='${equipe.motor}'/>">
                </div>

                <div class="col-md-3">
                    <label class="form-label" for="anoFundacao">Ano de fundação</label>
                    <input class="form-control" type="number" id="anoFundacao" name="anoFundacao" min="1900" max="2100"
                           value="<c:out value='${equipe.anoFundacao}'/>">
                </div>
                <div class="col-md-3">
                    <label class="form-label" for="cor">Cor da equipe</label>
                    <input class="form-control form-control-color w-100" type="color" id="cor" name="cor"
                           value="<c:out value='${empty equipe.cor ? "#E10600" : equipe.cor}'/>">
                </div>

                <div class="col-md-6">
                    <label class="form-label" for="logo">Imagem da equipe (JPG, PNG, GIF ou WEBP, até 5 MB)</label>
                    <c:if test="${editando}">
                        <img class="previa-imagem" src="<c:out value='${equipe.logoUrl}'/>" alt="Imagem atual">
                    </c:if>
                    <input class="form-control" type="file" id="logo" name="logo" accept="image/*">
                    <c:if test="${editando}">
                        <div class="form-text">Deixe em branco para manter a imagem atual.</div>
                    </c:if>
                </div>
            </div>

            <div class="mt-4">
                <button class="btn btn-primary" type="submit">Salvar</button>
                <a class="btn btn-secondary" href="equipes">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp" />
