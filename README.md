# Mazurek Racing — CRUD de Pilotos e Equipes de Fórmula 1

Projeto da disciplina **Programação I** (Ensino Médio Integrado em Informática — IFC Campus Videira).
Aplicação web em **Java (Servlets + JSP)** com **MariaDB**, rodando no **Apache Tomcat**, seguindo a
mesma estrutura do repositório-base do professor (`Portfolio1`).

> Veja como o sistema fica **sem instalar nada**: abra `previa/previa.html` no navegador.

## O que o sistema faz

Não existe login nem painel de usuário: qualquer pessoa que abrir o site pode gerenciar os dados.

| Página | Rota | Operação |
|--------|------|----------|
| Início | `/index` | — |
| Lista de pilotos (com filtro por equipe) | `/pilotos` | **R**ead |
| Novo piloto | `/novoPiloto` | **C**reate |
| Editar piloto | `/editarPiloto?id=` | **U**pdate |
| Excluir piloto (botão com confirmação) | `/excluirPiloto` | **D**elete |
| Lista de equipes | `/equipes` | **R**ead |
| Nova equipe | `/novaEquipe` | **C**reate |
| Editar equipe | `/editarEquipe?id=` | **U**pdate |
| Excluir equipe (botão com confirmação) | `/excluirEquipe` | **D**elete |

- **Imagens:** cada piloto tem uma foto e cada equipe tem uma imagem. Ao cadastrar ou editar, escolha um arquivo
  (JPG, PNG, GIF ou WEBP, até 5 MB). Na edição, se nenhuma imagem nova for escolhida, a atual é mantida.
  Os dados de exemplo usam fotos e logos em `.webp` que já vêm no projeto (`assets/img/`).
- Uma equipe que ainda tem pilotos **não pode ser excluída** (o sistema avisa).
- Número de piloto repetido e nome de equipe repetido também geram aviso.

## Tecnologias

Java 21 · Jakarta Servlet/JSP/JSTL · Maven (WAR) · MariaDB (XAMPP) · Tomcat 10.1+ ·
Bootstrap 5 + CSS próprio (`assets/css/estilos.css`) com cores lisas: vermelho, preto e branco (sem degradê).

## Estrutura

```
MazurekRacing/
├── mazurek_racing.sql            <- script do banco (cria o banco, as tabelas e os dados de exemplo)
├── previa/previa.html            <- prévia visual (abre direto no navegador, sem servidor)
├── pom.xml                       <- dependências Maven
├── src/main/java
│   ├── model/                    <- Equipe, Piloto
│   ├── dao/                      <- ConexaoDB, EquipeDAO, PilotoDAO   (SQL do CRUD)
│   ├── controller/               <- EquipeController, PilotoController, ImagemController
│   └── util/                     <- Arquivos (upload), Web, NegocioException
├── src/main/webapp               <- JSPs, CSS, JS, imagens, WEB-INF (web.xml, urlrewrite.xml, erros)
├── .project / .classpath / .settings   <- projeto Eclipse
└── .vscode/                      <- configuração do VS Code
```

## Pré-requisitos

1. **JDK 21**
2. **XAMPP** (só o **MySQL/MariaDB** é necessário)
3. **Apache Tomcat 10.1 ou 11** (Jakarta EE 10+; *não* use o Tomcat 9)
4. **Eclipse IDE for Enterprise Java and Web Developers** *ou* **VS Code**

## 1) Banco de dados

1. Abra o **XAMPP Control Panel** e inicie o **MySQL** (e o **Apache**, para usar o phpMyAdmin).
2. Acesse <http://localhost/phpmyadmin> → aba **Importar**.
3. Selecione `mazurek_racing.sql` → **Importar**. O script cria o banco `mazurek_racing` sozinho.

A conexão padrão é `localhost:3306`, usuário `root`, **sem senha** (padrão do XAMPP).
Se o seu MySQL for diferente, altere os campos no início de `src/main/java/dao/ConexaoDB.java`.

> As imagens enviadas pelo sistema ficam **fora do projeto**, na pasta `mazurek-uploads` dentro da pasta do seu usuário
> (ex.: `C:\Users\Aluno\mazurek-uploads`). Por isso não há caminho fixo no código e o projeto funciona em qualquer
> computador depois do `git clone`.

## 2) Rodando no Eclipse

1. `git clone <URL-do-seu-repositorio>`
2. **File → Import → Existing Projects into Workspace** (ou *Maven → Existing Maven Projects*) e escolha a pasta clonada.
3. Botão direito no projeto → **Maven → Update Project…** (marque *Force Update*) para baixar as dependências.
4. Cadastre o Tomcat em **Window → Preferences → Server → Runtime Environments → Add → Apache Tomcat v10.1**.
5. Botão direito no projeto → **Run As → Run on Server** → escolha o Tomcat.
6. Acesse <http://localhost:8080/MazurekRacing/>

## 3) Rodando no VS Code

1. `git clone <URL-do-seu-repositorio>` e abra a pasta (**File → Open Folder**).
2. Aceite instalar as extensões recomendadas (*Extension Pack for Java*, *Maven for Java*, *Community Server Connectors*).
3. Gere o WAR: no terminal, `mvn clean package` (cria `target/MazurekRacing.war`).
4. Na aba **Servers** (Community Server Connectors), adicione o **Apache Tomcat** instalado, clique com o botão direito em
   *Add Deployment* e escolha `target/MazurekRacing.war`, depois **Start Server**.
   (Alternativa: copie `target/MazurekRacing.war` para a pasta `webapps` do Tomcat e inicie-o.)
5. Acesse <http://localhost:8080/MazurekRacing/>

## Problemas comuns

| Sintoma | Solução |
|---------|---------|
| "Erro ao conectar com o banco de dados" | Ligue o MySQL no XAMPP e confirme que importou `mazurek_racing.sql` |
| `ClassNotFoundException: jakarta.servlet...` | Está usando Tomcat 9. Use Tomcat 10.1 ou 11 |
| Erros de dependência no Eclipse | Botão direito → *Maven → Update Project* (Force Update) |
| Imagem enviada não aparece | Verifique se a pasta `mazurek-uploads` (na pasta do seu usuário) pode ser criada/escrita |
| Página 404 | O endereço deve começar com `/MazurekRacing` (nome do projeto/WAR) |
| Acentos errados nos dados | Importe o `.sql` pelo phpMyAdmin (ele já usa utf8mb4) |

## Autor

Hurick Mazurek
