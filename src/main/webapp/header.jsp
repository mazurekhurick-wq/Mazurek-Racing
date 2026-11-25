<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="pt-br">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <base href="${pageContext.request.contextPath}/">
        <title>Mazurek Racing | Pilotos e Equipes de Fórmula 1</title>
        <script src="assets/js/jquery-3.7.1.min.js"></script>
        <script src="assets/js/bootstrap.bundle.min.js"></script>
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/css/estilos.css" rel="stylesheet" type="text/css"/>
        <link rel="icon" href="assets/img/favicon.svg" type="image/svg+xml">
    </head>
    <body>
        <header>
            <nav class="navbar navbar-expand-lg navbar-mr">
                <div class="container">
                    <a class="navbar-brand" href="index">
                        <span class="logo-mr">MR</span> MAZUREK RACING
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#menuPrincipal" aria-controls="menuPrincipal"
                            aria-expanded="false" aria-label="Abrir menu">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="menuPrincipal">
                        <ul class="navbar-nav ms-auto">
                            <li class="nav-item"><a class="nav-link" href="index">Início</a></li>
                            <li class="nav-item"><a class="nav-link" href="pilotos">Pilotos</a></li>
                            <li class="nav-item"><a class="nav-link" href="equipes">Equipes</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <main>
