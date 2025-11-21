CREATE DATABASE IF NOT EXISTS gestao_financeira;
USE gestao_financeira;

CREATE TABLE usuarios (
    id CHAR(36) PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    sexo VARCHAR(20) NOT NULL,
    nome_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE contas (
    id CHAR(36) PRIMARY KEY,
    id_usuario CHAR(36) NOT NULL,
    nome_banco VARCHAR(100) NOT NULL,
    agencia VARCHAR(20) NOT NULL,
    numero_conta VARCHAR(20) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    tipo_conta VARCHAR(30) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE categorias_despesas (
    id CHAR(36) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    id_usuario CHAR(36),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE despesas (
    id CHAR(36) PRIMARY KEY,
    id_usuario CHAR(36) NOT NULL,
    id_conta CHAR(36) NOT NULL,
    id_categoria CHAR(36) NOT NULL,
    tipo_despesa VARCHAR(20) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    data_despesa DATE NOT NULL,
    descricao VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_conta) REFERENCES contas(id),
    FOREIGN KEY (id_categoria) REFERENCES categorias_despesas(id)
);

CREATE TABLE receitas (
    id CHAR(36) PRIMARY KEY,
    id_usuario CHAR(36) NOT NULL,
    id_conta CHAR(36) NOT NULL,
    tipo_receita VARCHAR(20) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    data_receita DATE NOT NULL,
    descricao VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_conta) REFERENCES contas(id)
);

CREATE TABLE metas (
    id CHAR(36) PRIMARY KEY,
    id_usuario CHAR(36) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor_meta DECIMAL(15, 2) NOT NULL,
    valor_atual DECIMAL(15, 2) DEFAULT 0.00,
    data_limite DATE NOT NULL,
    tipo_meta VARCHAR(50) NOT NULL, -- Nova coluna obrigatória
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);