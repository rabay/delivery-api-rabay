-- data.sql: Schema and initial data for MySQL database
-- This script creates tables, relationships, and initial data needed for testing

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
    quantidade_estoque INTEGER NOT NULL,
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

-- Insert initial data using standard SQL that works with both MySQL and H2
-- For idempotent inserts, we'll use a conditional approach

-- Clientes
INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido) 
SELECT 1, 'João Silva', 'joao@email.com', NULL, NULL, TRUE, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 1);

INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido) 
SELECT 2, 'Maria Santos', 'maria@email.com', NULL, NULL, TRUE, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 2);

INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido) 
SELECT 3, 'Pedro Oliveira', 'pedro@email.com', NULL, NULL, FALSE, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 3);

INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido) 
SELECT 4, 'Ana Costa', 'ana@email.com', NULL, NULL, TRUE, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 4);

INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido) 
SELECT 5, 'Carlos Ferreira', 'carlos@email.com', NULL, NULL, TRUE, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 5);

-- Restaurantes
INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) 
SELECT 1, 'Pizza Express', 'Italiana', NULL, 5.00, NULL, NULL, 30, TRUE, 4.5, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 1);

INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) 
SELECT 2, 'Burger King', 'Fast Food', NULL, 4.00, NULL, NULL, 25, TRUE, 4.2, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 2);

INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) 
SELECT 3, 'Sushi House', 'Japonesa', NULL, 6.00, NULL, NULL, 40, TRUE, 4.8, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 3);

INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) 
SELECT 4, 'Gyros Athenas', 'Grega', NULL, 5.00, NULL, NULL, 35, TRUE, 4.0, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 4);

INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) 
SELECT 5, 'Chiparia do Porto', 'Frutos do Mar', NULL, 7.00, NULL, NULL, 45, TRUE, 4.3, FALSE 
WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 5);

-- Usuarios
INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao) 
SELECT 1, 'Administrator', 'admin@deliveryapi.com', '$2a$10$JR5F1bHMJWaItjsch8eLIujaLx.RyaCRP5Oy7K.c0qW7OneVGmHQa', 'ADMIN', TRUE, NOW() 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 1);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao) 
SELECT 2, 'Cliente Teste', 'cliente@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'CLIENTE', TRUE, NOW() 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 2);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao, restaurante_id) 
SELECT 3, 'Restaurante Teste', 'restaurante@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'RESTAURANTE', TRUE, NOW(), 1 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 3);

INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao) 
SELECT 4, 'Entregador Teste', 'entregador@test.com', '$2a$10$rOzJmZKz.5d5z.5d5z.5d5uz5z5z5z5z5z5z5z5z5z5z5z5z5z5z', 'ENTREGADOR', TRUE, NOW() 
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 4);

-- Produtos
INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 1, 'Pizza Margherita', 'Pizza', NULL, 25.00, TRUE, FALSE, 1, 10 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 1);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 2, 'Pizza Pepperoni', 'Pizza', NULL, 28.00, TRUE, FALSE, 1, 8 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 2);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 3, 'Big Burger', 'Hambúrguer', NULL, 30.00, TRUE, FALSE, 2, 15 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 3);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 4, 'Batata Frita Grande', 'Acompanhamento', NULL, 10.00, TRUE, FALSE, 2, 20 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 4);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 5, 'Sushi Salmão', 'Sushi', NULL, 15.00, TRUE, FALSE, 3, 12 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 5);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 6, 'Hot Roll', 'Sushi', NULL, 12.00, TRUE, FALSE, 3, 15 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 6);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 7, 'Gyros de Cordeiro', 'Espeto', NULL, 22.00, TRUE, FALSE, 4, 7 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 7);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 8, 'Souvlaki de Frango', 'Espeto', NULL, 20.00, TRUE, FALSE, 4, 9 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 8);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 9, 'Fish & Chips Tradicional', 'Peixe', NULL, 35.00, TRUE, FALSE, 5, 5 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 9);

INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque) 
SELECT 10, 'Porção de Camarão Empanado', 'Frutos do Mar', NULL, 28.00, TRUE, FALSE, 5, 6 
WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 10);

-- Pedidos
INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 1, 1, 1, 50.00, 'PED-0001', 50.00, 'Sem lactose', 'Rua A', '100', 'Centro', 'Sao Paulo', 'SP', '01000-000', '', 'CRIADO', NOW(), FALSE 
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 1);

INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 2, 2, 2, 30.00, 'PED-0002', 30.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', NOW(), FALSE 
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 2);

INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 3, 3, 3, 45.00, 'PED-0003', 45.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CANCELADO', NOW(), FALSE 
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 3);

-- Itens dos pedidos
INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) 
SELECT 1, 1, 1, 2, 25.00, 50.00 
WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 1);

INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) 
SELECT 2, 2, 3, 1, 30.00, 30.00 
WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 2);

INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) 
SELECT 3, 3, 5, 3, 15.00, 45.00 
WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 3);