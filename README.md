Mazurek Racing 🏁

CRUD de pilotos e equipes de Fórmula 1 para a disciplina de Programação I (IFC Campus Videira). Feito com Java (Servlets + JSP), MariaDB e Tomcat.

Prévia sem instalar nada: abra previa/previa.html no navegador.

Funções
Pilotos: cadastrar, listar (com filtro por equipe), editar e excluir
Equipes: cadastrar, listar, editar e excluir
Upload de imagem (JPG, PNG, GIF ou WEBP, até 5 MB)
Equipe com pilotos não pode ser excluída
Número de piloto e nome de equipe não se repetem
Requisitos

JDK 21, XAMPP (MySQL), Tomcat 10.1 ou 11 e Eclipse ou VS Code.

Instalação
Inicie o MySQL no XAMPP e importe o mazurek_racing.sql pelo phpMyAdmin.
Clone o projeto:
bash
git clone https://github.com/mazurekhurick-wq/Mazurek-Racing.git
Rode no Tomcat:
Eclipse: importe como Maven Project, faça Maven → Update Project e use Run on Server.
VS Code: rode mvn clean package e coloque target/MazurekRacing.war na pasta webapps do Tomcat.
Acesse http://localhost:8080/MazurekRacing/

Conexão padrão do banco: localhost:3306, usuário root, sem senha. Para mudar, edite src/main/java/dao/ConexaoDB.java.

As imagens enviadas ficam em mazurek-uploads, na pasta do usuário.

Hurick Mazurek, 2º Info B
