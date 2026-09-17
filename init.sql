-- ============================================================
-- BANCO DE DADOS
-- ============================================================

CREATE DATABASE IF NOT EXISTS sugestfilmes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sugestfilmes;

SET NAMES utf8mb4;


-- ============================================================
-- TABELA: PERFIS
-- ============================================================

CREATE TABLE IF NOT EXISTS perfis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- TABELA: USUÁRIOS
-- ============================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_login (login),
    CONSTRAINT fk_usuario_perfil
        FOREIGN KEY (perfil_id)
        REFERENCES perfis(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- TABELA: FILMES
-- ============================================================

CREATE TABLE IF NOT EXISTS filmes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    genero VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    diretor VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- TABELA: CATEGORIAS
-- ============================================================

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categoria_nome (nome)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- TABELA: FILMES_CATEGORIAS
-- ============================================================

CREATE TABLE IF NOT EXISTS filmes_categorias (
    filme_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (filme_id, categoria_id),
    CONSTRAINT fk_fc_filme
        FOREIGN KEY (filme_id)
        REFERENCES filmes(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_fc_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categorias(id)
        ON DELETE CASCADE
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- TABELA: AVALIAÇÕES
-- ============================================================

CREATE TABLE IF NOT EXISTS avaliacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    filme_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nota INT NOT NULL,
    comentario VARCHAR(500),
    data_avaliacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT ck_avaliacao_nota
        CHECK (nota BETWEEN 1 AND 5),
    CONSTRAINT fk_avaliacao_filme
        FOREIGN KEY (filme_id)
        REFERENCES filmes(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_avaliacao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE,
    UNIQUE KEY uk_avaliacao_usuario_filme (usuario_id, filme_id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- ============================================================
-- PERFIS
-- ============================================================

INSERT IGNORE INTO perfis (id, nome) VALUES
    (1, 'Administrador'),
    (2, 'Professor'),
    (3, 'Aluno');


-- ============================================================
-- USUÁRIOS
-- ============================================================

INSERT IGNORE INTO usuarios
    (id, nome, login, senha, perfil_id)
VALUES
    (1, 'Administrador do Sistema', 'admin', '123456', 1),
    (2, 'João Professor', 'joao', '123456', 2),
    (3, 'Maria Aluna', 'maria', '123456', 3);


-- ============================================================
-- CATEGORIAS
-- ============================================================

INSERT IGNORE INTO categorias (id, nome) VALUES
    (1, 'Ação'),
    (2, 'Aventura'),
    (3, 'Comédia'),
    (4, 'Drama'),
    (5, 'Terror'),
    (6, 'Ficção Científica'),
    (7, 'Romance'),
    (8, 'Suspense'),
    (9, 'Fantasia'),
    (10, 'Animação');


-- ============================================================
-- FILMES
-- ============================================================

INSERT IGNORE INTO filmes
    (id, titulo, descricao, genero, ano, diretor)
VALUES
(1, 'Interestelar',
 'Em um futuro em que a Terra enfrenta problemas ambientais e escassez de alimentos, um grupo de astronautas viaja através de um buraco de minhoca em busca de um novo planeta habitável para a humanidade.',
 'Ficção Científica', 2014, 'Christopher Nolan'),

(2, 'Matrix',
 'Um programador descobre que a realidade em que vive é uma simulação criada por máquinas e se junta a um grupo de rebeldes para lutar pela liberdade da humanidade.',
 'Ficção Científica', 1999, 'Lana Wachowski e Lilly Wachowski'),

(3, 'O Senhor dos Anéis: A Sociedade do Anel',
 'Um jovem hobbit recebe a missão de destruir um poderoso anel antes que ele caia nas mãos do Senhor do Escuro e cause a destruição da Terra-média.',
 'Fantasia', 2001, 'Peter Jackson'),

(4, 'O Senhor dos Anéis: As Duas Torres',
 'Enquanto a Sociedade do Anel está dividida, seus integrantes enfrentam novos perigos e tentam impedir o avanço das forças de Sauron.',
 'Fantasia', 2002, 'Peter Jackson'),

(5, 'O Senhor dos Anéis: O Retorno do Rei',
 'As forças de Sauron se preparam para o confronto final enquanto os heróis da Terra-média lutam para destruir o Um Anel.',
 'Fantasia', 2003, 'Peter Jackson'),

(6, 'Vingadores: Ultimato',
 'Após um acontecimento que elimina metade da vida no universo, os Vingadores restantes tentam encontrar uma maneira de reverter a situação e derrotar Thanos.',
 'Ação', 2019, 'Anthony Russo e Joe Russo'),

(7, 'Homem de Ferro',
 'Um bilionário e inventor é sequestrado e constrói uma armadura tecnológica para escapar. Após retornar para casa, decide usar sua tecnologia para combater ameaças.',
 'Ação', 2008, 'Jon Favreau'),

(8, 'Pantera Negra',
 'Após a morte de seu pai, T’Challa retorna para Wakanda e precisa assumir o trono enquanto enfrenta um adversário que ameaça o futuro do país.',
 'Ação', 2018, 'Ryan Coogler'),

(9, 'Jurassic Park',
 'Um parque temático com dinossauros geneticamente recriados sofre uma falha de segurança, colocando visitantes e funcionários em perigo.',
 'Aventura', 1993, 'Steven Spielberg'),

(10, 'De Volta para o Futuro',
 'Um adolescente viaja acidentalmente para o passado usando uma máquina do tempo construída por um cientista e precisa encontrar uma maneira de voltar para sua época.',
 'Ficção Científica', 1985, 'Robert Zemeckis'),

(11, 'O Poderoso Chefão',
 'A história de uma poderosa família envolvida no crime organizado e das disputas internas e externas que ameaçam seus negócios e sua estrutura familiar.',
 'Drama', 1972, 'Francis Ford Coppola'),

(12, 'Forrest Gump: O Contador de Histórias',
 'Um homem com uma vida simples acaba participando de importantes acontecimentos da história dos Estados Unidos enquanto enfrenta desafios pessoais e familiares.',
 'Drama', 1994, 'Robert Zemeckis'),

(13, 'Titanic',
 'Uma jovem de uma família rica se apaixona por um passageiro de origem humilde durante a viagem inaugural do navio Titanic.',
 'Romance', 1997, 'James Cameron'),

(14, 'O Iluminado',
 'Um escritor aceita trabalhar como zelador de um hotel isolado durante o inverno, mas acontecimentos sobrenaturais começam a afetar sua família e sua própria sanidade.',
 'Terror', 1980, 'Stanley Kubrick'),

(15, 'It: A Coisa',
 'Um grupo de crianças enfrenta uma entidade sobrenatural que assume a forma de um palhaço e aterroriza a cidade de Derry.',
 'Terror', 2017, 'Andy Muschietti'),

(16, 'Toy Story',
 'Um grupo de brinquedos ganha vida quando os humanos não estão por perto. Woody precisa lidar com a chegada de um novo brinquedo chamado Buzz Lightyear.',
 'Animação', 1995, 'John Lasseter'),

(17, 'O Rei Leão',
 'Um jovem leão precisa superar a perda de seu pai e assumir seu lugar como rei da savana.',
 'Animação', 1994, 'Roger Allers e Rob Minkoff'),

(18, 'Shrek',
 'Um ogro que vive tranquilamente em seu pântano precisa embarcar em uma aventura para resgatar uma princesa e recuperar sua tranquilidade.',
 'Animação', 2001, 'Andrew Adamson e Vicky Jenson'),

(19, 'Se Beber, Não Case!',
 'Três amigos viajam para Las Vegas para uma despedida de solteiro, mas acordam após uma noite de festa sem lembrar do que aconteceu.',
 'Comédia', 2009, 'Todd Phillips'),

(20, 'As Branquelas',
 'Dois agentes do FBI se disfarçam como duas socialites para protegê-las de uma ameaça e acabam envolvidos em diversas situações inesperadas.',
 'Comédia', 2004, 'Keenen Ivory Wayans'),

(21, 'Missão: Impossível - Efeito Fallout',
 'O agente Ethan Hunt e sua equipe precisam impedir uma organização criminosa de realizar um ataque que pode provocar uma catástrofe mundial.',
 'Ação', 2018, 'Christopher McQuarrie'),

(22, 'Gladiador',
 'Um general romano é traído e escravizado, tornando-se um gladiador determinado a buscar vingança contra aqueles que destruíram sua família.',
 'Ação', 2000, 'Ridley Scott'),

(23, 'Harry Potter e a Pedra Filosofal',
 'Um garoto descobre que é um bruxo e começa seus estudos em Hogwarts, onde conhece seus melhores amigos e descobre segredos sobre seu passado.',
 'Fantasia', 2001, 'Chris Columbus'),

(24, 'Homem-Aranha: No Aranhaverso',
 'Miles Morales se torna o Homem-Aranha e conhece diferentes versões do herói vindas de outras dimensões.',
 'Animação', 2018, 'Bob Persichetti, Peter Ramsey e Rodney Rothman'),

(25, 'O Exterminador do Futuro 2: O Julgamento Final',
 'Um ciborgue volta ao passado para proteger um jovem que terá um papel importante no futuro da humanidade.',
 'Ficção Científica', 1991, 'James Cameron'),

(26, 'Avatar',
 'Um ex-fuzileiro naval participa de uma missão em um planeta distante e acaba se envolvendo com o povo local e com a defesa de seu mundo.',
 'Ficção Científica', 2009, 'James Cameron'),

(27, 'Clube da Luta',
 'Um homem insatisfeito com sua vida conhece uma figura misteriosa e juntos criam um clube secreto de lutas que acaba tomando proporções inesperadas.',
 'Drama', 1999, 'David Fincher'),

(28, 'The Batman',
 'Batman investiga uma série de crimes cometidos por um assassino misterioso enquanto descobre uma rede de corrupção em Gotham City.',
 'Ação', 2022, 'Matt Reeves'),

(29, 'Top Gun: Maverick',
 'Um experiente piloto retorna para treinar uma nova geração de pilotos e precisa enfrentar desafios relacionados ao seu passado.',
 'Ação', 2022, 'Joseph Kosinski'),

(30, 'Divertida Mente',
 'Dentro da mente de uma garota, suas emoções tentam lidar com as mudanças provocadas por uma nova fase de sua vida.',
 'Animação', 2015, 'Pete Docter');


-- ============================================================
-- RELAÇÃO ENTRE FILMES E CATEGORIAS
-- ============================================================

INSERT IGNORE INTO filmes_categorias
    (filme_id, categoria_id)
VALUES
    (1,6),(1,4),(1,2),
    (2,6),(2,1),(2,8),
    (3,9),(3,2),
    (4,9),(4,2),(4,1),
    (5,9),(5,2),(5,1),
    (6,1),(6,6),(6,2),
    (7,1),(7,6),(7,2),
    (8,1),(8,2),(8,4),
    (9,2),(9,6),(9,8),
    (10,6),(10,2),(10,3),
    (11,4),(11,8),
    (12,4),(12,7),
    (13,7),(13,4),
    (14,5),(14,8),
    (15,5),(15,8),
    (16,10),(16,3),(16,2),
    (17,10),(17,2),(17,4),
    (18,10),(18,3),(18,2),(18,9),
    (19,3),
    (20,3),
    (21,1),(21,8),(21,2),
    (22,1),(22,4),(22,2),
    (23,9),(23,2),
    (24,10),(24,1),(24,2),
    (25,6),(25,1),(25,8),
    (26,6),(26,2),(26,1),
    (27,4),(27,8),
    (28,1),(28,8),(28,4),
    (29,1),(29,4),(29,2),
    (30,10),(30,3),(30,4);
