-- data.sql: dados iniciais de exemplo (idempotente usando INSERT ... ON DUPLICATE KEY UPDATE)
-- Use apenas em ambiente de desenvolvimento; create-drop irá remover esses dados no shutdown
-- Convertido de sintaxe H2 para MySQL

-- Clientes (conforme DataLoader)
INSERT INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (1, 'João Silva', 'joao@email.com', NULL, NULL, 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

INSERT INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (2, 'Maria Santos', 'maria@email.com', NULL, NULL, 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

INSERT INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (3, 'Pedro Oliveira', 'pedro@email.com', NULL, NULL, 0, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

INSERT INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (4, 'Ana Costa', 'ana@email.com', NULL, NULL, 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

INSERT INTO CLIENTE (id, nome, email, telefone, endereco, ativo, excluido)
VALUES (5, 'Carlos Ferreira', 'carlos@email.com', NULL, NULL, 1, 0)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    email = VALUES(email),
    telefone = VALUES(telefone),
    endereco = VALUES(endereco),
    ativo = VALUES(ativo),
    excluido = VALUES(excluido);

-- Restaurantes (conforme DataLoader)
INSERT INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (1, 'Pizza Express', 'Italiana', NULL, 5.00, NULL, NULL, 30, 1, 4.5, 0)
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

INSERT INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (2, 'Burger King', 'Fast Food', NULL, 4.00, NULL, NULL, 25, 1, 4.2, 0)
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

INSERT INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (3, 'Sushi House', 'Japonesa', NULL, 6.00, NULL, NULL, 40, 1, 4.8, 0)
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

INSERT INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (4, 'Gyros Athenas', 'Grega', NULL, 5.00, NULL, NULL, 35, 1, 4.0, 0)
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

INSERT INTO RESTAURANTE (id, nome, categoria, endereco, taxa_entrega, telefone, email, tempo_entrega_minutos, ativo, avaliacao, excluido)
VALUES (5, 'Chiparia do Porto', 'Frutos do Mar', NULL, 7.00, NULL, NULL, 45, 1, 4.3, 0)
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

-- Produtos (conforme DataLoader)
INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (1, 'Pizza Margherita', 'Pizza', NULL, 25.00, 1, 0, 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (2, 'Pizza Pepperoni', 'Pizza', NULL, 28.00, 1, 0, 1)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (3, 'Big Burger', 'Hambúrguer', NULL, 30.00, 1, 0, 2)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (4, 'Batata Frita Grande', 'Acompanhamento', NULL, 10.00, 1, 0, 2)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (5, 'Sushi Salmão', 'Sushi', NULL, 15.00, 1, 0, 3)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (6, 'Hot Roll', 'Sushi', NULL, 12.00, 1, 0, 3)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (7, 'Gyros de Cordeiro', 'Espeto', NULL, 22.00, 1, 0, 4)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (8, 'Souvlaki de Frango', 'Espeto', NULL, 20.00, 1, 0, 4)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (9, 'Fish & Chips Tradicional', 'Peixe', NULL, 35.00, 1, 0, 5)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

INSERT INTO PRODUTO (id, nome, categoria, descricao, preco, disponivel, excluido, restaurante_id)
VALUES (10, 'Porção de Camarão Empanado', 'Frutos do Mar', NULL, 28.00, 1, 0, 5)
ON DUPLICATE KEY UPDATE
    nome = VALUES(nome),
    categoria = VALUES(categoria),
    descricao = VALUES(descricao),
    preco = VALUES(preco),
    disponivel = VALUES(disponivel),
    excluido = VALUES(excluido),
    restaurante_id = VALUES(restaurante_id);

-- Pedidos (conforme DataLoader: 3 pedidos de exemplo)
INSERT INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido)
VALUES (1, 1, 1, 50.00, 'PED-0001', 50.00, 'Sem lactose', 'Rua A', '100', 'Centro', 'Sao Paulo', 'SP', '01000-000', '', 'CRIADO', NOW())
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
    data_pedido = VALUES(data_pedido);

INSERT INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido)
VALUES (2, 2, 2, 30.00, 'PED-0002', 30.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ENTREGUE', NOW())
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
    data_pedido = VALUES(data_pedido);

INSERT INTO PEDIDO (id, cliente_id, restaurante_id, valor_total, numero_pedido, subtotal, observacoes, logradouro, numero, bairro, cidade, estado, cep, complemento, status, data_pedido)
VALUES (3, 3, 3, 45.00, 'PED-0003', 45.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CANCELADO', NOW())
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
    data_pedido = VALUES(data_pedido);

-- Itens dos pedidos
INSERT INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 2, 25.00, 50.00)
ON DUPLICATE KEY UPDATE
    pedido_id = VALUES(pedido_id),
    produto_id = VALUES(produto_id),
    quantidade = VALUES(quantidade),
    preco_unitario = VALUES(preco_unitario),
    subtotal = VALUES(subtotal);

INSERT INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 3, 1, 30.00, 30.00)
ON DUPLICATE KEY UPDATE
    pedido_id = VALUES(pedido_id),
    produto_id = VALUES(produto_id),
    quantidade = VALUES(quantidade),
    preco_unitario = VALUES(preco_unitario),
    subtotal = VALUES(subtotal);

INSERT INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (3, 3, 5, 3, 15.00, 45.00)
ON DUPLICATE KEY UPDATE
    pedido_id = VALUES(pedido_id),
    produto_id = VALUES(produto_id),
    quantidade = VALUES(quantidade),
    preco_unitario = VALUES(preco_unitario),
    subtotal = VALUES(subtotal);

-- Nota: estas instruções foram adaptadas a partir do DataLoader presente no código-fonte.
-- Convertidas de sintaxe H2 (MERGE INTO) para MySQL (INSERT ... ON DUPLICATE KEY UPDATE)
