<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="header.jsp" />

<%-- O mesmo formulário serve para CREATE (piloto sem id) e UPDATE (piloto com id) --%>
<c:set var="editando" value="${not empty piloto and piloto.id > 0}"/>

<div class="container">
    <h1>${editando ? 'Editar piloto' : 'Novo piloto'}</h1>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger"><c:out value="${erro}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${empty equipes}">
            <div class="alert alert-info">
                Cadastre pelo menos uma equipe antes de cadastrar pilotos.
                <a href="novaEquipe">Nova equipe</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-painel">
                <form action="${editando ? 'editarPiloto' : 'novoPiloto'}" method="post" enctype="multipart/form-data">
                    <c:if test="${editando}">
                        <input type="hidden" name="id" value="${piloto.id}">
                    </c:if>

                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label" for="nome">Nome</label>
                            <input class="form-control" type="text" id="nome" name="nome" maxlength="100" required
                                   value="<c:out value='${piloto.nome}'/>">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="numero">Número (1 a 99)</label>
                            <input class="form-control" type="number" id="numero" name="numero" min="1" max="99" required
                                   value="${empty piloto or piloto.numero == 0 ? '' : piloto.numero}">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="nacionalidade">Nacionalidade</label>
                            <input class="form-control" type="text" id="nacionalidade" name="nacionalidade" maxlength="60" required
                                   value="<c:out value='${piloto.nacionalidade}'/>">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="dataNascimento">Data de nascimento</label>
                            <input class="form-control" type="date" id="dataNascimento" name="dataNascimento"
                                   value="<c:if test='${not empty piloto.dataNascimento}'><fmt:formatDate value='${piloto.dataNascimento}' pattern='yyyy-MM-dd'/></c:if>">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="equipeId">Equipe</label>
                            <select class="form-select" id="equipeId" name="equipeId" required>
                                <option value="">Selecione...</option>
                                <c:forEach var="e" items="${equipes}">
                                    <option value="${e.id}" ${piloto.equipeId == e.id ? 'selected' : ''}><c:out value="${e.nome}"/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="vitorias">Vitórias</label>
                            <input class="form-control" type="number" id="vitorias" name="vitorias" min="0"
                                   value="${empty piloto ? 0 : piloto.vitorias}">
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="titulos">Títulos</label>
                            <input class="form-control" type="number" id="titulos" name="titulos" min="0"
                                   value="${empty piloto ? 0 : piloto.titulos}">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="foto">Foto do piloto (JPG, PNG, GIF ou WEBP, até 5 MB)</label>
                            <c:if test="${editando}">
                                <img class="previa-imagem" src="<c:out value='${piloto.fotoUrl}'/>" alt="Foto atual">
                            </c:if>
                            <input class="form-control" type="file" id="foto" name="foto" accept="image/*">
                            <c:if test="${editando}">
                                <div class="form-text">Deixe em branco para manter a foto atual.</div>
                            </c:if>
                        </div>
                    </div>

                    <div class="mt-4">
                        <button class="btn btn-primary" type="submit">Salvar</button>
                        <a class="btn btn-secondary" href="pilotos">Cancelar</a>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp" />
