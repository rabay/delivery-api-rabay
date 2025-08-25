-- data.sql: dados iniciais de exemplo (idempotente usando MERGE INTO)
-- Use apenas em ambiente de desenvolvimento; create-drop irá remover esses dados no shutdown
-- data.sql: dados iniciais de exemplo (idempotente usando MERGE INTO)
-- Use apenas em ambiente de desenvolvimento; create-drop irá remover esses dados no shutdown

-- Clientes (conforme DataLoader)
MERGE INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido) KEY(id)
VALUES (1, 'João Silva', 'joao@email.com', NULL, NULL, TRUE, FALSE);

MERGE INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido) KEY(id)
VALUES (2, 'Maria Santos', 'maria@email.com', NULL, NULL, TRUE, FALSE);

MERGE INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido) KEY(id)
VALUES (3, 'Pedro Oliveira', 'pedro@email.com', NULL, NULL, FALSE, FALSE);

MERGE INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido) KEY(id)
VALUES (4, 'Ana Costa', 'ana@email.com', NULL, NULL, TRUE, FALSE);

MERGE INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido) KEY(id)
VALUES (5, 'Carlos Ferreira', 'carlos@email.com', NULL, NULL, TRUE, FALSE);

-- Restaurantes (conforme DataLoader)
MERGE INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) KEY(id)
VALUES (1, 'Pizza Express', 'Italiana', NULL, 5.00, NULL, NULL, 30, TRUE, 4.5, FALSE);

MERGE INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) KEY(id)
VALUES (2, 'Burger King', 'Fast Food', NULL, 4.00, NULL, NULL, 25, TRUE, 4.2, FALSE);

MERGE INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) KEY(id)
VALUES (3, 'Sushi House', 'Japonesa', NULL, 6.00, NULL, NULL, 40, TRUE, 4.8, FALSE);

MERGE INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) KEY(id)
VALUES (4, 'Gyros Athenas', 'Grega', NULL, 5.00, NULL, NULL, 35, TRUE, 4.0, FALSE);

MERGE INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido) KEY(id)
VALUES (5, 'Chiparia do Porto', 'Frutos do Mar', NULL, 7.00, NULL, NULL, 45, TRUE, 4.3, FALSE);

-- Produtos (conforme DataLoader)
MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (1, 'Pizza Margherita', 'Pizza', NULL, 25.00, TRUE, FALSE, 1);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (2, 'Pizza Pepperoni', 'Pizza', NULL, 28.00, TRUE, FALSE, 1);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (3, 'Big Burger', 'Hambúrguer', NULL, 30.00, TRUE, FALSE, 2);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (4, 'Batata Frita Grande', 'Acompanhamento', NULL, 10.00, TRUE, FALSE, 2);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (5, 'Sushi Salmão', 'Sushi', NULL, 15.00, TRUE, FALSE, 3);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (6, 'Hot Roll', 'Sushi', NULL, 12.00, TRUE, FALSE, 3);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (7, 'Gyros de Cordeiro', 'Espeto', NULL, 22.00, TRUE, FALSE, 4);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (8, 'Souvlaki de Frango', 'Espeto', NULL, 20.00, TRUE, FALSE, 4);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (9, 'Fish & Chips Tradicional', 'Peixe', NULL, 35.00, TRUE, FALSE, 5);

MERGE INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id) KEY(id)
VALUES (10, 'Porção de Camarão Empanado', 'Frutos do Mar', NULL, 28.00, TRUE, FALSE, 5);

-- Pedidos (conforme DataLoader: 3 pedidos de exemplo)
MERGE INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido) KEY(id)
VALUES (1, 1, 1, 50.00, 'PED-0001', 50.00, 'Sem lactose', 'Rua A', '100', 'Centro', 'Sao Paulo', 'SP', '01000-000', '', 'CRIADO', CURRENT_TIMESTAMP());

MERGE INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido) KEY(id)
VALUES (2, 2, 2, 30.00, 'PED-0002', 30.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', CURRENT_TIMESTAMP());

MERGE INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido) KEY(id)
VALUES (3, 3, 3, 45.00, 'PED-0003', 45.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CANCELADO', CURRENT_TIMESTAMP());

-- Itens dos pedidos
MERGE INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) KEY(id)
VALUES (1, 1, 1, 2, 25.00, 50.00);

MERGE INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) KEY(id)
VALUES (2, 2, 3, 1, 30.00, 30.00);

MERGE INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal) KEY(id)
VALUES (3, 3, 5, 3, 15.00, 45.00);

-- Nota: estas instruções foram adaptadas a partir do DataLoader presente no código-fonte.
