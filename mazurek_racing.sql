-- ============================================================
--  Mazurek Racing  |  Banco de dados: mazurek_racing  (MariaDB / MySQL)
--
--  Como importar (XAMPP):
--    1) Inicie o MySQL no XAMPP Control Panel (e o Apache, para usar o phpMyAdmin)
--    2) Abra http://localhost/phpmyadmin  ->  aba "Importar"
--    3) Escolha este arquivo e clique em "Importar"
--  (o script já cria o banco "mazurek_racing" sozinho)
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mazurek_racing`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `mazurek_racing`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `piloto`;
DROP TABLE IF EXISTS `equipe`;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------
-- Equipes (construtores)
-- logo = caminho da imagem (assets/img/equipes/... ou assets/uploads/... se enviada pelo sistema)
-- ------------------------------------------------------------
CREATE TABLE `equipe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(80) NOT NULL,
  `pais` varchar(60) NOT NULL,
  `motor` varchar(60) NOT NULL,
  `ano_fundacao` int(11) DEFAULT NULL,
  `cor` varchar(7) NOT NULL DEFAULT '#E10600',
  `logo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_equipe_nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ------------------------------------------------------------
-- Pilotos
-- foto = caminho da imagem (assets/img/pilotos/... ou assets/uploads/... se enviada pelo sistema)
-- ------------------------------------------------------------
CREATE TABLE `piloto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `numero` int(11) NOT NULL,
  `nacionalidade` varchar(60) NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  `vitorias` int(11) NOT NULL DEFAULT 0,
  `titulos` int(11) NOT NULL DEFAULT 0,
  `foto` varchar(255) DEFAULT NULL,
  `equipe_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_piloto_numero` (`numero`),
  KEY `idx_piloto_equipe` (`equipe_id`),
  CONSTRAINT `fk_piloto_equipe` FOREIGN KEY (`equipe_id`) REFERENCES `equipe` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ------------------------------------------------------------
-- Dados de exemplo (são só um ponto de partida: edite ou exclua pelo próprio sistema)
-- As imagens de exemplo (fotos e logos em .webp) já vêm no projeto, em assets/img.
-- ------------------------------------------------------------
INSERT INTO `equipe` (`id`,`nome`,`pais`,`motor`,`ano_fundacao`,`cor`,`logo`) VALUES
(1,'Scuderia Ferrari','Itália','Ferrari',1950,'#E10600','assets/img/equipes/ferrari.webp'),
(2,'McLaren','Reino Unido','Mercedes',1966,'#FF8000','assets/img/equipes/mclaren.webp'),
(3,'Mercedes','Alemanha','Mercedes',1954,'#00A19B','assets/img/equipes/mercedes.webp'),
(4,'Red Bull Racing','Áustria','Red Bull Ford',2005,'#1E41FF','assets/img/equipes/redbull.webp'),
(5,'Williams','Reino Unido','Mercedes',1977,'#005AFF','assets/img/equipes/williams.webp');

INSERT INTO `piloto` (`nome`,`numero`,`nacionalidade`,`data_nascimento`,`vitorias`,`titulos`,`foto`,`equipe_id`) VALUES
('Lewis Hamilton',44,'Britânico','1985-01-07',105,7,'assets/img/pilotos/hamilton.webp',1),
('Charles Leclerc',16,'Monegasco','1997-10-16',8,0,'assets/img/pilotos/leclerc.webp',1),
('Lando Norris',1,'Britânico','1999-11-13',11,1,'assets/img/pilotos/norris.webp',2),
('Oscar Piastri',81,'Australiano','2001-04-06',9,0,'assets/img/pilotos/piastri.webp',2),
('George Russell',63,'Britânico','1998-02-15',5,0,'assets/img/pilotos/russell.webp',3),
('Max Verstappen',3,'Holandês','1997-09-30',71,4,'assets/img/pilotos/verstappen.webp',4),
('Carlos Sainz',55,'Espanhol','1994-09-01',4,0,'assets/img/pilotos/sainz.webp',5),
('Alexander Albon',23,'Tailandês','1996-03-23',0,0,'assets/img/pilotos/albon.webp',5);
