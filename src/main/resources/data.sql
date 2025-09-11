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
    desconto DECIMAL(10,2),
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
INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 1, 1, 1, 50.00, NULL, 'PED-0001', 50.00, 'Sem lactose', 'Rua A', '100', 'Centro', 'Sao Paulo', 'SP', '01000-000', '', 'CRIADO', NOW(), FALSE 
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 1);

INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 2, 2, 2, 30.00, NULL, 'PED-0002', 30.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', NOW(), FALSE 
WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 2);

INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido) 
SELECT 3, 3, 3, 45.00, NULL, 'PED-0003', 45.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CANCELADO', NOW(), FALSE 
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

-- ========= Dados adicionais para enriquecer a massa (clientes, restaurantes, usuários, produtos, pedidos históricos) =========

-- -- Clientes adicionais
-- INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
-- SELECT 6, 'Bruna Lima', 'bruna.lima@email.com', '11999990006', 'Rua F, 45', TRUE, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 6);

-- INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
-- SELECT 7, 'Rafael Gomes', 'rafael.gomes@email.com', '11999990007', 'Av. B, 200', TRUE, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 7);

-- INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
-- SELECT 8, 'Mariana Ribeiro', 'mariana.ribeiro@email.com', '11999990008', 'Rua C, 123', TRUE, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 8);

-- INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
-- SELECT 9, 'Lucas Almeida', 'lucas.almeida@email.com', '11999990009', 'Praça D, 10', TRUE, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 9);

-- INSERT INTO cliente (id, nome, email, telefone, endereco, ativo, excluido)
-- SELECT 10, 'Fernanda Castro', 'fernanda.castro@email.com', '11999990010', 'Rua E, 77', TRUE, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE id = 10);

-- -- Restaurantes adicionais
-- INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
-- SELECT 6, 'Taco Town', 'Mexicana', 'Av. Mex, 50', 4.50, '1122223336', 'tacotown@rest.com', 28, TRUE, 4.1, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 6);

-- INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
-- SELECT 7, 'Padaria Central', 'Padaria', 'Rua Pao, 12', 2.50, '1122223337', 'padariacentral@rest.com', 20, TRUE, 4.4, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 7);

-- INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
-- SELECT 8, 'Veggie Corner', 'Vegetariana', 'Av. Verde, 300', 3.00, '1122223338', 'veggiec@rest.com', 30, TRUE, 4.6, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 8);

-- INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
-- SELECT 9, 'Café do Museu', 'Café', 'Rua Historia, 5', 3.50, '1122223339', 'cafedomuseu@rest.com', 18, TRUE, 4.3, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 9);

-- INSERT INTO restaurante (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
-- SELECT 10, 'Açougue do Bairro', 'Carnes', 'Av. Churrasco, 9', 5.50, '1122223340', 'acougue@rest.com', 35, TRUE, 4.0, FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM restaurante WHERE id = 10);

-- -- Usuários adicionais (ADMIN já existe)
-- INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao)
-- SELECT 5, 'Cliente Extra', 'cliente.extra@delivery.com', '$2a$10$ExampleHashForClient5xxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'CLIENTE', TRUE, NOW()
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 5);

-- INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao, restaurante_id)
-- SELECT 6, 'Dono Taco', 'dono@tacotown.com', '$2a$10$ExampleHashForOwner6xxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'RESTAURANTE', TRUE, NOW(), 6
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 6);

-- INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao)
-- SELECT 7, 'Entregador Extra', 'entregador.extra@delivery.com', '$2a$10$ExampleHashForDelivery7xxxxxxxxxxxxxxxxxxxxxxxx', 'ENTREGADOR', TRUE, NOW()
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 7);

-- INSERT INTO usuario (id, nome, email, senha, role, ativo, data_criacao, restaurante_id)
-- SELECT 8, 'Padaria Owner', 'owner@padaria.com', '$2a$10$ExampleHashForOwner8xxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'RESTAURANTE', TRUE, NOW(), 7
-- WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE id = 8);

-- -- Produtos adicionais (IDs 11..30)
-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 11, 'Taco de Carnitas', 'Taco', 'Taco tradicional com carne suculenta', 12.00, TRUE, FALSE, 6, 40
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 11);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 12, 'Taco Veggie', 'Taco', 'Taco vegetariano com feijão e abacate', 11.00, TRUE, FALSE, 6, 30
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 12);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 13, 'Pão Francês (6 uni)', 'Padaria', 'Pacote com 6 pães franceses frescos', 8.00, TRUE, FALSE, 7, 100
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 13);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 14, 'Pão de Queijo (8 uni)', 'Padaria', 'Porção com 8 pães de queijo', 15.00, TRUE, FALSE, 7, 80
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 14);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 15, 'Salada Caesar', 'Salada', 'Salada Caesar com croutons', 18.00, TRUE, FALSE, 8, 25
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 15);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 16, 'Burger Veggie', 'Hambúrguer', 'Hambúrguer vegetal com molho especial', 22.00, TRUE, FALSE, 8, 30
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 16);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 17, 'Cappuccino', 'Bebida', 'Cappuccino cremoso 300ml', 9.00, TRUE, FALSE, 9, 60
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 17);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 18, 'Sanduíche Natural', 'Salgado', 'Sanduíche natural com peito de peru', 14.00, TRUE, FALSE, 9, 50
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 18);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 19, 'Bife Ancho (500g)', 'Carnes', 'Corte especial para grelhar', 45.00, TRUE, FALSE, 10, 20
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 19);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 20, 'Linguiça artesanal (kg)', 'Carnes', 'Linguiça suína temperada', 25.00, TRUE, FALSE, 10, 15
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 20);

-- -- Produtos adicionais para restaurantes já existentes (aumentar variedade)
-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 21, 'Pizza Quatro Queijos', 'Pizza', NULL, 32.00, TRUE, FALSE, 1, 12
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 21);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 22, 'Sushi Combo 10', 'Sushi', NULL, 60.00, TRUE, FALSE, 3, 10
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 22);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 23, 'Porção de Camarão Grelhado', 'Frutos do Mar', NULL, 35.00, TRUE, FALSE, 5, 8
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 23);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 24, 'Gyros Vegetariano', 'Espeto', NULL, 18.00, TRUE, FALSE, 4, 10
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 24);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 25, 'Big Burger XL', 'Hambúrguer', NULL, 38.00, TRUE, FALSE, 2, 6
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 25);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 26, 'Fish & Chips Premium', 'Peixe', NULL, 42.00, TRUE, FALSE, 5, 4
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 26);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 27, 'Sobremesa Brownie', 'Sobremesa', NULL, 12.00, TRUE, FALSE, 7, 30
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 27);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 28, 'Suco Natural Laranja', 'Bebida', NULL, 7.00, TRUE, FALSE, 9, 40
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 28);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 29, 'Combo Café + Pão', 'Combo', NULL, 12.00, TRUE, FALSE, 9, 50
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 29);

-- INSERT INTO produto (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id, quantidade_estoque)
-- SELECT 30, 'Porção Mista (2 pessoas)', 'Compartilhamento', NULL, 55.00, TRUE, FALSE, 6, 10
-- WHERE NOT EXISTS (SELECT 1 FROM produto WHERE id = 30);

-- -- Pedidos históricos adicionais (4..15)
-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 4, 6, 6, 36.00, NULL, 'PED-0004', 36.00, 'Sem cebola', 'Rua F', '45', 'Centro', 'Sao Paulo', 'SP', '01100-000', '', 'ENTREGUE', '2025-08-10 12:15:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 4);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 5, 7, 7, 23.00, NULL, 'PED-0005', 23.00, NULL, 'Rua Pao', '12', 'Bela Vista', 'Sao Paulo', 'SP', '01200-000', '', 'ENTREGUE', '2025-08-12 09:30:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 5);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 6, 8, 8, 40.00, 5.00, 'PED-0006', 45.00, 'Sem croutons', NULL, NULL, NULL, NULL, NULL, NULL, 'CANCELADO', '2025-07-20 19:45:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 6);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 7, 9, 9, 16.00, NULL, 'PED-0007', 16.00, NULL, 'Rua Historia', '5', 'Centro', 'Sao Paulo', 'SP', '01300-000', '', 'CRIADO', '2025-09-01 08:10:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 7);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 8, 10, 10, 95.00, NULL, 'PED-0008', 95.00, 'Pedido grande', 'Av. Churrasco', '9', 'Bairro Alto', 'Sao Paulo', 'SP', '01400-000', '', 'ENTREGUE', '2025-06-30 20:00:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 8);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 9, 1, 2, 45.00, NULL, 'PED-0009', 45.00, NULL, 'Rua A', '100', 'Centro', 'Sao Paulo', 'SP', '01000-000', '', 'ENTREGUE', '2025-08-01 13:20:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 9);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 10, 2, 1, 60.00, NULL, 'PED-0010', 60.00, 'Com borda recheada', NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-07-05 18:00:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 10);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 11, 3, 3, 80.00, NULL, 'PED-0011', 80.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-05-12 21:30:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 11);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 12, 4, 4, 28.00, NULL, 'PED-0012', 28.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-08-15 14:00:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 12);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 13, 5, 5, 70.00, NULL, 'PED-0013', 70.00, 'Com bebida', NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-07-01 19:00:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 13);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 14, 6, 1, 30.00, NULL, 'PED-0014', 30.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-08-20 12:00:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 14);

-- INSERT INTO pedido (id, cliente_id, restaurante_id, valor_total, desconto, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido, excluido)
-- SELECT 15, 7, 6, 22.00, NULL, 'PED-0015', 22.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', '2025-08-22 11:30:00', FALSE
-- WHERE NOT EXISTS (SELECT 1 FROM pedido WHERE id = 15);

-- -- Itens para os pedidos adicionais (id 4..30)
-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 4, 4, 11, 3, 12.00, 36.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 4);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 5, 5, 13, 1, 8.00, 8.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 5);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 6, 6, 16, 2, 22.00, 44.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 6);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 7, 7, 18, 1, 14.00, 14.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 7);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 8, 8, 19, 2, 45.00, 90.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 8);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 9, 9, 3, 1, 30.00, 30.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 9);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 10, 10, 21, 1, 32.00, 32.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 10);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 11, 11, 22, 1, 60.00, 60.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 11);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 12, 12, 24, 1, 18.00, 18.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 12);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 13, 13, 23, 2, 35.00, 70.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 13);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 14, 14, 2, 1, 28.00, 28.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 14);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 15, 15, 11, 1, 12.00, 12.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 15);

-- -- Itens adicionais mix para maior realismo
-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 16, 4, 30, 1, 55.00, 55.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 16);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 17, 8, 20, 2, 25.00, 50.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 17);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 18, 10, 1, 2, 25.00, 50.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 18);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 19, 11, 6, 1, 12.00, 12.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 19);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 20, 13, 26, 1, 42.00, 42.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 20);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 21, 5, 14, 2, 15.00, 30.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 21);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 22, 9, 4, 1, 10.00, 10.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 22);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 23, 12, 24, 1, 18.00, 18.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 23);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 24, 14, 3, 1, 30.00, 30.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 24);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 25, 15, 12, 2, 11.00, 22.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 25);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 26, 8, 23, 1, 35.00, 35.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 26);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 27, 11, 22, 1, 60.00, 60.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 27);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 28, 13, 5, 2, 15.00, 30.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 28);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 29, 2, 4, 1, 10.00, 10.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 29);

-- INSERT INTO item_pedido (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
-- SELECT 30, 15, 30, 1, 55.00, 55.00
-- WHERE NOT EXISTS (SELECT 1 FROM item_pedido WHERE id = 30);

-- -- Fim dos dados adicionais