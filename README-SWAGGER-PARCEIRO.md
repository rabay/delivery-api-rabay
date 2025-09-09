# Guia de Integração para Parceiros — Delivery API Rabay

Este documento orienta restaurantes, desenvolvedores de frontend (MVC, SPA) e apps mobile sobre como integrar seus sistemas à Delivery API Rabay.

## Visão Geral
A Delivery API Rabay expõe endpoints RESTful para cadastro, consulta e gestão de clientes, restaurantes, produtos, pedidos e relatórios. A autenticação é feita via JWT (Bearer Token).

## Passos para Integração

### 1. Cadastro de Parceiro
- Solicite credenciais de acesso à equipe Rabay (admin@deliveryapi.com).
- Receba seu usuário, senha e role (RESTAURANTE, CLIENTE, ADMIN, ENTREGADOR).

### 2. Autenticação JWT
- Realize login via endpoint:
  - `POST /api/auth/login`
  - Body: `{ "username": "seu_email", "password": "sua_senha" }`
- O retorno será um token JWT. Use-o no header `Authorization: Bearer <token>` em todas as requisições protegidas.

### 3. Fluxos Comuns

#### Cadastro de Restaurante
- `POST /api/restaurantes`
- Body: Dados do restaurante (ver exemplo no Swagger)
- Requer role RESTAURANTE ou ADMIN

#### Cadastro de Produto
- `POST /api/produtos`
- Body: Dados do produto
- Requer autenticação

#### Consulta de Produtos
- `GET /api/produtos`
- Parâmetros: `page`, `size`
- Retorna lista paginada

#### Criação de Pedido
- `POST /api/pedidos`
- Body: Dados do pedido (cliente, restaurante, itens, endereço)
- Requer autenticação

#### Consulta de Pedidos
- `GET /api/pedidos`
- Parâmetros: `page`, `size`
- Retorna pedidos do cliente ou restaurante

#### Relatórios
- `GET /api/relatorios/vendas-por-restaurante`
- Parâmetros: `inicio`, `fim`, `page`, `size`
- Retorna vendas agrupadas por restaurante

### 4. Tratamento de Erros
- Respostas seguem o padrão:
  - Sucesso: `{ "data": ..., "message": "...", "success": true }`
  - Erro: `{ "data": null, "message": "Motivo do erro", "success": false }`
- Códigos comuns: 200, 201, 400, 401, 404, 409, 428, 500

### 5. Exemplos de Integração

#### Frontend (JavaScript)
```js
fetch('http://localhost:8080/api/produtos', {
  headers: { 'Authorization': 'Bearer <token>' }
})
  .then(res => res.json())
  .then(data => console.log(data));
```

#### Mobile (Kotlin/Swift)
- Use bibliotecas HTTP nativas e inclua o header Authorization.

#### Backend (Java/Spring)
- Use RestTemplate/WebClient e configure o header Authorization.

### 6. Boas Práticas
- Sempre valide o token antes de cada requisição.
- Utilize paginação para consultas grandes.
- Trate todos os códigos de erro conforme documentação.
- Consulte o Swagger UI para exemplos detalhados: http://localhost:8080/swagger-ui/index.html

## Referências
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [Documentação OpenAPI JSON](http://localhost:8080/api-docs)
- [Postman Collection](entregaveis/delivery-api-rabay.postman_collection.json)
- [Postman Environment](entregaveis/delivery-api-rabay.postman_environment.json)

---
**Dúvidas ou suporte:** contato@rabay.com.br
