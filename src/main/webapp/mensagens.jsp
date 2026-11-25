<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Mostra (uma única vez) as mensagens guardadas na sessão pelos controllers --%>
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success"><c:out value="${sessionScope.successMessage}"/></div>
    <c:remove var="successMessage" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-danger"><c:out value="${sessionScope.errorMessage}"/></div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>
