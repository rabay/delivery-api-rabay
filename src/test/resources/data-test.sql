-- data-test.sql: Schema and initial test data for MySQL database
-- This script creates tables, relationships, and initial test data needed for testing

-- Drop tables if they exist (for clean initialization)
DROP TABLE IF EXISTS item_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS restaurante;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;

-- Create tables with proper relationships

-- Cliente table
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(15),
    endereco VARCHAR(255),
    ativo BOOLEAN,
    excluido BOOLEAN NOT NULL DEFAULT FALSE
);

-- Restaurante table
CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    endereco VARCHAR(255),
    taxa_entrega DECIMAL(8,2),
    telefone VARCHAR(15),
    email VARCHAR(100) UNIQUE,
    tempo_entrega_minutos INTEGER,
    ativo BOOLEAN,
    avaliacao DECIMAL(3,1),
    excluido BOOLEAN NOT NULL DEFAULT FALSE
);

-- Usuario table
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    ativo BOOLEAN,
    data_criacao DATETIME,
    restaurante_id BIGINT
);

-- Produto table (depends on restaurante)
CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    descricao VARCHAR(500),
    preco DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    excluido BOOLEAN NOT NULL DEFAULT FALSE,
    restaurante_id BIGINT NOT NULL,
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);

-- Pedido table (depends on cliente and restaurante)
CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    numero_pedido VARCHAR(50) UNIQUE,
    subtotal DECIMAL(10,2),
    observacoes VARCHAR(500),
    logradouro VARCHAR(100),
    numero VARCHAR(10),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(10),
    complemento VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    data_pedido DATETIME NOT NULL,
    excluido BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
);

-- ItemPedido table (depends on pedido and produto)
CREATE TABLE item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- Insert initial test data (idempotent using INSERT ... ON DUPLICATE KEY UPDATE)

-- Clientes para testes
INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (1, 'Cliente Teste 1', 'cliente1@test.com', '11999999999', 'Rua Teste 1, 123', 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (2, 'Cliente Teste 2', 'cliente2@test.com', '11888888888', 'Rua Teste 2, 456', 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

-- Restaurantes para testes
INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (1, 'Restaurante Teste 1', 'Teste', 'Rua Restaurante 1, 789', 5.00, '11777777777', 'rest1@test.com', 30, 1, 4.5, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    endereco = VALUES(endereco),
    taxa_entrega = VALUES(taxa_entrega),
    telefone = VALUES(telefone),
    email = VALUES(email),
    tempo_entrega_minutos = VALUES(tempo_entrega_minutos),
    ativo = VALUES(ativo),
    avaliacao = VALUES(avaliacao),
    excluido = VALUES(excluido);

INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (2, 'Restaurante Teste 2', 'Teste', 'Rua Restaurante 2, 321', 7.50, '11666666666', 'rest2@test.com', 45, 1, 4.0, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    endereco = VALUES(endereco),
    taxa_entrega = VALUES(taxa_entrega),
    telefone = VALUES(telefone),
    email = VALUES(email),
    tempo_entrega_minutos = VALUES(tempo_entrega_minutos),
    ativo = VALUES(ativo),
    avaliacao = VALUES(avaliacao),
    excluido = VALUES(excluido);

-- Usuarios para testes
INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao)
VALUES (1, 'Administrator', 'admin@deliveryapi.com', '$2a$10$JR5F1bHMJWaItjsch8eLIujaLx.RyaCRP5Oy7K.c0qW7OneVGmHQa', 'ADMIN', 1, NOW())
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    senha = VALUES(senha),
    role = VALUES(role),
    ativo = VALUES(ativo),
    data_criacao = VALUES(data_criacao);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao)
VALUES (2, 'Cliente Teste', 'cliente@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'CLIENTE', 1, NOW())
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    senha = VALUES(senha),
    role = VALUES(role),
    ativo = VALUES(ativo),
    data_criacao = VALUES(data_criacao);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao, restaurante_id)
VALUES (3, 'Restaurante Teste', 'restaurante@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'RESTAURANTE', 1, NOW(), 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    senha = VALUES(senha),
    role = VALUES(role),
    ativo = VALUES(ativo),
    data_criacao = VALUES(data_criacao),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao)
VALUES (4, 'Entregador Teste', 'entregador@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'ENTREGADOR', 1, NOW())
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    senha = VALUES(senha),
    role = VALUES(role),
    ativo = VALUES(ativo),
    data_criacao = VALUES(data_criacao);

-- Produtos para testes
INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (1, 'Produto Teste 1', 'Teste', 'Descrição do Produto Teste 1', 25.00, 1, 0, 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (2, 'Produto Teste 2', 'Teste', 'Descrição do Produto Teste 2', 30.00, 1, 0, 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

-- Pedidos para testes
INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
VALUES (1, 1, 1, 50.00, 'PED-TEST-001', 50.00, 'Pedido de teste 1', 'Rua Teste 1', '123', 'Bairro Teste', 'Cidade Teste', 'CT', '01000-000', '', 'CRIADO', NOW(), 0)
ON DUPLICATE KEY UPDATE
    cliente_id = VALUES(cliente_id),
    restaurante_id = VALUES(restaurante_id),
    valor_total = VALUES(valor_total),
    numero_pedido = VALUES(numero_pedido),
    subtotal = VALUES(subtotal),
    observacoes = VALUES(observacoes),
    logradouro = VALUES(logradouro),
    numero = VALUES(numero),
    bairro = VALUES(bairro),
    cidade = VALUES(cidade),
    estado = VALUES(estado),
    cep = VALUES(cep),
    complemento = VALUES(complemento),
    status = VALUES(status),
    data_pedido = VALUES(data_pedido),
    excluido = VALUES(excluido);

-- Itens dos pedidos
INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 2, 25.00, 50.00)
ON DUPLICATE KEY UPDATE
    pedido_id = VALUES(pedido_id),
    produto_id = VALUES(produto_id),
    quantidade = VALUES(quantidade),
    preco_unitario = VALUES(preco_unitario),
    subtotal = VALUES(subtotal);