CREATE DATABASE IF NOT EXISTS mvc_java
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mvc_java;

-- =========================================
-- TABELA DE PERFIS
-- =========================================

CREATE TABLE perfis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

-- =========================================
-- TABELA DE USUÁRIOS
-- =========================================

CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_usuario_login
        UNIQUE (login),

    CONSTRAINT fk_usuario_perfil
        FOREIGN KEY (perfil_id)
        REFERENCES perfis(id)
);

CREATE TABLE filmes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    genero VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    diretor VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

CREATE table categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE filmes_categorias (
    filme_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,

    PRIMARY KEY (filme_id, categoria_id),

    CONSTRAINT fk_filmes_categorias_filme
        FOREIGN KEY (filme_id)
        REFERENCES filmes(id),

    CONSTRAINT fk_filmes_categorias_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categorias(id)
);

CREATE TABLE avaliacoes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    filme_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nota INT NOT NULL,
    comentario TEXT,

    PRIMARY KEY (id),

    CONSTRAINT fk_avaliacoes_filme
        FOREIGN KEY (filme_id)
        REFERENCES filmes(id),

    CONSTRAINT fk_avaliacoes_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

-- =========================================
-- DADOS PARA TESTE
-- =========================================

INSERT INTO perfis (nome)
VALUES
    ('Administrador'),
    ('Professor'),
    ('Aluno');

INSERT INTO usuarios (
    nome,
    login,
    senha,
    perfil_id
)
VALUES
    ('Administrador do Sistema', 'admin', '123456', 1),
    ('João Professor', 'joao', '123456', 2),
    ('Maria Aluna', 'maria', '123456', 3);


INSERT INTO categorias (nome) VALUES
('Ação'),
('Aventura'),
('Comédia'),
('Drama'),
('Terror'),
('Ficção Científica'),
('Romance'),
('Suspense'),
('Fantasia'),
('Animação');

INSERT INTO filmes (titulo, descricao, genero, ano, diretor) VALUES

(
    'Interestelar',
    'Em um futuro em que a Terra enfrenta problemas ambientais e escassez de alimentos, um grupo de astronautas viaja através de um buraco de minhoca em busca de um novo planeta habitável para a humanidade.',
    'Ficção Científica',
    2014,
    'Christopher Nolan'
),

(
    'Matrix',
    'Um programador descobre que a realidade em que vive é uma simulação criada por máquinas e se junta a um grupo de rebeldes para lutar pela liberdade da humanidade.',
    'Ficção Científica',
    1999,
    'Lana Wachowski e Lilly Wachowski'
),

(
    'O Senhor dos Anéis: A Sociedade do Anel',
    'Um jovem hobbit recebe a missão de destruir um poderoso anel antes que ele caia nas mãos do Senhor do Escuro e cause a destruição da Terra-média.',
    'Fantasia',
    2001,
    'Peter Jackson'
),

(
    'O Senhor dos Anéis: As Duas Torres',
    'Enquanto a Sociedade do Anel está dividida, seus integrantes enfrentam novos perigos e tentam impedir o avanço das forças de Sauron.',
    'Fantasia',
    2002,
    'Peter Jackson'
),

(
    'O Senhor dos Anéis: O Retorno do Rei',
    'As forças de Sauron se preparam para o confronto final enquanto os heróis da Terra-média lutam para destruir o Um Anel.',
    'Fantasia',
    2003,
    'Peter Jackson'
),

(
    'Vingadores: Ultimato',
    'Após um acontecimento que elimina metade da vida no universo, os Vingadores restantes tentam encontrar uma maneira de reverter a situação e derrotar Thanos.',
    'Ação',
    2019,
    'Anthony Russo e Joe Russo'
),

(
    'Homem de Ferro',
    'Um bilionário e inventor é sequestrado e constrói uma armadura tecnológica para escapar. Após retornar para casa, decide usar sua tecnologia para combater ameaças.',
    'Ação',
    2008,
    'Jon Favreau'
),

(
    'Pantera Negra',
    'Após a morte de seu pai, TChalla retorna para Wakanda e precisa assumir o trono enquanto enfrenta um adversário que ameaça o futuro do país.',
    'Ação',
    2018,
    'Ryan Coogler'
),

(
    'Jurassic Park',
    'Um parque temático com dinossauros geneticamente recriados sofre uma falha de segurança, colocando visitantes e funcionários em perigo.',
    'Aventura',
    1993,
    'Steven Spielberg'
),

(
    'De Volta para o Futuro',
    'Um adolescente viaja acidentalmente para o passado usando uma máquina do tempo construída por um cientista e precisa encontrar uma maneira de voltar para sua época.',
    'Ficção Científica',
    1985,
    'Robert Zemeckis'
),

(
    'O Poderoso Chefão',
    'A história de uma poderosa família envolvida no crime organizado e das disputas internas e externas que ameaçam seus negócios e sua estrutura familiar.',
    'Drama',
    1972,
    'Francis Ford Coppola'
),

(
    'Forrest Gump: O Contador de Histórias',
    'Um homem com uma vida simples acaba participando de importantes acontecimentos da história dos Estados Unidos enquanto enfrenta desafios pessoais e familiares.',
    'Drama',
    1994,
    'Robert Zemeckis'
),

(
    'Titanic',
    'Uma jovem de uma família rica se apaixona por um passageiro de origem humilde durante a viagem inaugural do navio Titanic.',
    'Romance',
    1997,
    'James Cameron'
),

(
    'O Iluminado',
    'Um escritor aceita trabalhar como zelador de um hotel isolado durante o inverno, mas acontecimentos sobrenaturais começam a afetar sua família e sua própria sanidade.',
    'Terror',
    1980,
    'Stanley Kubrick'
),

(
    'It: A Coisa',
    'Um grupo de crianças enfrenta uma entidade sobrenatural que assume a forma de um palhaço e aterroriza a cidade de Derry.',
    'Terror',
    2017,
    'Andy Muschietti'
),

(
    'Toy Story',
    'Um grupo de brinquedos ganha vida quando os humanos não estão por perto. Woody precisa lidar com a chegada de um novo brinquedo chamado Buzz Lightyear.',
    'Animação',
    1995,
    'John Lasseter'
),

(
    'O Rei Leão',
    'Um jovem leão precisa superar a perda de seu pai e assumir seu lugar como rei da savana.',
    'Animação',
    1994,
    'Roger Allers e Rob Minkoff'
),

(
    'Shrek',
    'Um ogro que vive tranquilamente em seu pântano precisa embarcar em uma aventura para resgatar uma princesa e recuperar sua tranquilidade.',
    'Animação',
    2001,
    'Andrew Adamson e Vicky Jenson'
),

(
    'Se Beber, Não Case!',
    'Três amigos viajam para Las Vegas para uma despedida de solteiro, mas acordam após uma noite de festa sem lembrar do que aconteceu.',
    'Comédia',
    2009,
    'Todd Phillips'
),

(
    'As Branquelas',
    'Dois agentes do FBI se disfarçam como duas socialites para protegê-las de uma ameaça e acabam envolvidos em diversas situações inesperadas.',
    'Comédia',
    2004,
    'Keenen Ivory Wayans'
),

(
    'Missão: Impossível - Efeito Fallout',
    'O agente Ethan Hunt e sua equipe precisam impedir uma organização criminosa de realizar um ataque que pode provocar uma catástrofe mundial.',
    'Ação',
    2018,
    'Christopher McQuarrie'
),

(
    'Gladiador',
    'Um general romano é traído e escravizado, tornando-se um gladiador determinado a buscar vingança contra aqueles que destruíram sua família.',
    'Ação',
    2000,
    'Ridley Scott'
),

(
    'Harry Potter e a Pedra Filosofal',
    'Um garoto descobre que é um bruxo e começa seus estudos em Hogwarts, onde conhece seus melhores amigos e descobre segredos sobre seu passado.',
    'Fantasia',
    2001,
    'Chris Columbus'
),

(
    'Homem-Aranha: No Aranhaverso',
    'Miles Morales se torna o Homem-Aranha e conhece diferentes versões do herói vindas de outras dimensões.',
    'Animação',
    2018,
    'Bob Persichetti, Peter Ramsey e Rodney Rothman'
),

(
    'O Exterminador do Futuro 2: O Julgamento Final',
    'Um ciborgue volta ao passado para proteger um jovem que terá um papel importante no futuro da humanidade.',
    'Ficção Científica',
    1991,
    'James Cameron'
),

(
    'Avatar',
    'Um ex-fuzileiro naval participa de uma missão em um planeta distante e acaba se envolvendo com o povo local e com a defesa de seu mundo.',
    'Ficção Científica',
    2009,
    'James Cameron'
),

(
    'Clube da Luta',
    'Um homem insatisfeito com sua vida conhece uma figura misteriosa e juntos criam um clube secreto de lutas que acaba tomando proporções inesperadas.',
    'Drama',
    1999,
    'David Fincher'
),

(
    'O Batman',
    'Batman investiga uma série de crimes cometidos por um assassino misterioso enquanto descobre uma rede de corrupção em Gotham City.',
    'Ação',
    2022,
    'Matt Reeves'
),

(
    'Top Gun: Maverick',
    'Um experiente piloto retorna para treinar uma nova geração de pilotos e precisa enfrentar desafios relacionados ao seu passado.',
    'Ação',
    2022,
    'Joseph Kosinski'
),

(
    'Divertida Mente',
    'Dentro da mente de uma garota, suas emoções tentam lidar com as mudanças provocadas por uma nova fase de sua vida.',
    'Animação',
    2015,
    'Pete Docter'
);

INSERT INTO filmes_categorias (filme_id, categoria_id) VALUES

-- 1. Interestelar
(1, 6), -- Ficção Científica
(1, 4), -- Drama
(1, 2), -- Aventura

-- 2. Matrix
(2, 6), -- Ficção Científica
(2, 1), -- Ação
(2, 8), -- Suspense

-- 3. O Senhor dos Anéis: A Sociedade do Anel
(3, 9), -- Fantasia
(3, 2), -- Aventura

-- 4. O Senhor dos Anéis: As Duas Torres
(4, 9), -- Fantasia
(4, 2), -- Aventura
(4, 1), -- Ação

-- 5. O Senhor dos Anéis: O Retorno do Rei
(5, 9), -- Fantasia
(5, 2), -- Aventura
(5, 1), -- Ação

-- 6. Vingadores: Ultimato
(6, 1), -- Ação
(6, 6), -- Ficção Científica
(6, 2), -- Aventura

-- 7. Homem de Ferro
(7, 1), -- Ação
(7, 6), -- Ficção Científica
(7, 2), -- Aventura

-- 8. Pantera Negra
(8, 1), -- Ação
(8, 2), -- Aventura
(8, 4), -- Drama

-- 9. Jurassic Park
(9, 2), -- Aventura
(9, 6), -- Ficção Científica
(9, 8), -- Suspense

-- 10. De Volta para o Futuro
(10, 6), -- Ficção Científica
(10, 2), -- Aventura
(10, 3), -- Comédia

-- 11. O Poderoso Chefão
(11, 4), -- Drama
(11, 8), -- Suspense

-- 12. Forrest Gump: O Contador de Histórias
(12, 4), -- Drama
(12, 7), -- Romance

-- 13. Titanic
(13, 7), -- Romance
(13, 4), -- Drama

-- 14. O Iluminado
(14, 5), -- Terror
(14, 8), -- Suspense

-- 15. It: A Coisa
(15, 5), -- Terror
(15, 8), -- Suspense

-- 16. Toy Story
(16, 10), -- Animação
(16, 3),  -- Comédia
(16, 2),  -- Aventura

-- 17. O Rei Leão
(17, 10), -- Animação
(17, 2),  -- Aventura
(17, 4),  -- Drama

-- 18. Shrek
(18, 10), -- Animação
(18, 3),  -- Comédia
(18, 2),  -- Aventura
(18, 9),  -- Fantasia

-- 19. Se Beber, Não Case!
(19, 3), -- Comédia

-- 20. As Branquelas
(20, 3), -- Comédia

-- 21. Missão: Impossível - Efeito Fallout
(21, 1), -- Ação
(21, 8), -- Suspense
(21, 2), -- Aventura

-- 22. Gladiador
(22, 1), -- Ação
(22, 4), -- Drama
(22, 2), -- Aventura

-- 23. Harry Potter e a Pedra Filosofal
(23, 9), -- Fantasia
(23, 2), -- Aventura

-- 24. Homem-Aranha: No Aranhaverso
(24, 10), -- Animação
(24, 1),  -- Ação
(24, 2),  -- Aventura

-- 25. O Exterminador do Futuro 2: O Julgamento Final
(25, 6), -- Ficção Científica
(25, 1), -- Ação
(25, 8), -- Suspense

-- 26. Avatar
(26, 6), -- Ficção Científica
(26, 2), -- Aventura
(26, 1), -- Ação

-- 27. Clube da Luta
(27, 4), -- Drama
(27, 8), -- Suspense

-- 28. O Batman
(28, 1), -- Ação
(28, 8), -- Suspense
(28, 4), -- Drama

-- 29. Top Gun: Maverick
(29, 1), -- Ação
(29, 4), -- Drama
(29, 2), -- Aventura

-- 30. Divertida Mente
(30, 10), -- Animação
(30, 3),  -- Comédia
(30, 4);  -- Drama

-- =========================================
-- CONSULTA DE EXEMPLO
-- =========================================

SELECT
    u.id,
    u.nome,
    u.login,
    p.nome AS perfil
FROM usuarios u
INNER JOIN perfis p
    ON p.id = u.perfil_id
ORDER BY u.nome;