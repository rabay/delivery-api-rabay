# Tabela de Endpoints — Delivery API Rabay

| Método | Endpoint | Parâmetros | Autenticação | Descrição | Resposta | Códigos |
|--------|----------|------------|--------------|-----------|----------|--------|
| POST   | /api/auth/login | username, password (body) | Não | Login e obtenção de JWT | { token, message, success } | 200, 400, 401 |
| POST   | /api/auth/register | nome, email, senha, role (body) | Não | Cadastro de usuário | { id, nome, email, message, success } | 201, 400, 409 |
| GET    | /api/restaurantes | page, size (query) | Sim | Listar restaurantes ativos | Lista paginada | 200, 400 |
| POST   | /api/restaurantes | dados restaurante (body) | Sim (RESTAURANTE/ADMIN) | Cadastrar restaurante | Restaurante cadastrado | 201, 400, 409 |
| GET    | /api/restaurantes/{id} | id (path) | Sim | Buscar restaurante por ID | Dados do restaurante | 200, 404 |
| PUT    | /api/restaurantes/{id} | id (path), dados (body) | Sim | Atualizar restaurante | Restaurante atualizado | 200, 400, 404 |
| DELETE | /api/restaurantes/{id} | id (path) | Sim | Inativar restaurante | Mensagem de sucesso | 200, 404 |
| GET    | /api/produtos | page, size (query) | Sim | Listar produtos | Lista paginada | 200, 400 |
| POST   | /api/produtos | dados produto (body) | Sim | Cadastrar produto | Produto cadastrado | 201, 400, 409 |
| GET    | /api/produtos/{id} | id (path) | Sim | Buscar produto por ID | Dados do produto | 200, 404 |
| PUT    | /api/produtos/{id} | id (path), dados (body) | Sim | Atualizar produto | Produto atualizado | 200, 400, 404 |
| DELETE | /api/produtos/{id} | id (path) | Sim | Deletar produto | Mensagem de sucesso | 200, 404 |
| GET    | /api/pedidos | page, size (query) | Sim | Listar pedidos | Lista paginada | 200, 400 |
| POST   | /api/pedidos | dados pedido (body) | Sim | Criar pedido | Pedido criado | 201, 400, 409 |
| GET    | /api/pedidos/{id} | id (path) | Sim | Buscar pedido por ID | Dados do pedido | 200, 404 |
| PUT    | /api/pedidos/{id}/status | id (path), status (body) | Sim | Atualizar status do pedido | Pedido atualizado | 200, 400, 404 |
| PATCH  | /api/pedidos/{id}/status | id (path), status (body) | Sim | Atualizar status do pedido (parcial) | Pedido atualizado | 200, 400, 404 |
| DELETE | /api/pedidos/{id} | id (path) | Sim | Cancelar pedido | Mensagem de sucesso | 200, 404 |
| GET    | /api/clientes | page, size (query) | Sim | Listar clientes | Lista paginada | 200, 400 |
| POST   | /api/clientes | dados cliente (body) | Não | Cadastrar cliente | Cliente cadastrado | 201, 400, 409 |
| GET    | /api/clientes/{id} | id (path) | Sim | Buscar cliente por ID | Dados do cliente | 200, 404 |
| PUT    | /api/clientes/{id} | id (path), dados (body) | Sim | Atualizar cliente | Cliente atualizado | 200, 400, 404 |
| DELETE | /api/clientes/{id} | id (path) | Sim | Inativar cliente | Mensagem de sucesso | 200, 404 |
| GET    | /api/relatorios/vendas-por-restaurante | inicio, fim, page, size (query) | Sim | Relatório de vendas por restaurante | Dados do relatório | 200, 400 |
| GET    | /api/relatorios/produtos-mais-vendidos | limite, inicio, fim, page, size (query) | Sim | Produtos mais vendidos | Dados do relatório | 200, 400 |
| GET    | /api/relatorios/clientes-ativos | limite, inicio, fim, page, size (query) | Sim | Clientes mais ativos | Dados do relatório | 200, 400 |
| GET    | /health | - | Não | Verificar saúde da aplicação | Status | 200 |
| GET    | /info | - | Não | Obter informações da aplicação | Dados | 200 |
