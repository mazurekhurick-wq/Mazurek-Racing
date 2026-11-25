<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="header.jsp" />

<section class="hero">
    <div class="container">
        <h1>Mazurek Racing</h1>
        <p>
            Gerencie os pilotos e as equipes da Fórmula 1: cadastre, consulte, edite e exclua
            cada registro, com foto do piloto e imagem da equipe.
        </p>
        <a class="btn btn-primary btn-lg me-2" href="pilotos">Ver pilotos</a>
        <a class="btn btn-light btn-lg" href="equipes">Ver equipes</a>
    </div>
</section>

<div class="container">
    <div class="row g-4">
        <div class="col-md-6">
            <div class="caixa-info">
                <h2>Pilotos</h2>
                <p>Nome, número, nacionalidade, nascimento, vitórias, títulos, equipe e foto.</p>
                <a class="btn btn-primary" href="pilotos">Abrir pilotos</a>
            </div>
        </div>
        <div class="col-md-6">
            <div class="caixa-info">
                <h2>Equipes</h2>
                <p>Nome, país, motor, ano de fundação, cor e imagem da equipe.</p>
                <a class="btn btn-primary" href="equipes">Abrir equipes</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />
