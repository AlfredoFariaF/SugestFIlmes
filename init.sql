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