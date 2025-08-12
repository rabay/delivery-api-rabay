# 📮 Coleção Postman - Delivery Tech API

Esta pasta contém uma coleção completa de testes para a **Delivery Tech API**, uma aplicação moderna de delivery construída com Spring Boot 3.2.x e Java 21.

## 📋 Conteúdo

### 📄 Arquivos Principais

- **`Delivery-API-Collection.postman_collection.json`** - Coleção principal com todos os endpoints
- **`Delivery-API-Local.postman_environment.json`** - Ambiente de desenvolvimento local
- **`Delivery-API-LoadTest.postman_collection.json`** - Coleção específica para testes de carga
- **`README.md`** - Este arquivo de documentação

## 🚀 Como Usar

### 1. Importar no Postman

1. Abra o Postman
2. Clique em **Import**
3. Selecione os arquivos `.json` desta pasta
4. Confirme a importação

### 2. Configurar Ambiente

1. Selecione o ambiente **"Delivery API - Local Development"**
2. Verifique se a variável `baseUrl` está configurada como `http://localhost:8080`
3. Certifique-se de que a aplicação esteja rodando localmente

### 3. Executar Testes

#### 🧪 Execução Manual
- Execute os testes seguindo a ordem das pastas
- Comece com **Health & System** para verificar se a API está funcionando
- Continue com **Customers**, **Restaurants**, **Products** e **Orders**
- Termine com **Cenários de Teste Negativos**

#### 🔄 Execução Automatizada com Newman
```bash
# Instalar Newman (CLI do Postman)
npm install -g newman

# Executar coleção principal
newman run "Delivery-API-Collection.postman_collection.json" \
  --environment "Delivery-API-Local.postman_environment.json" \
  --reporters html,cli \
  --reporter-html-export delivery-api-test-results.html

# Executar testes de carga
newman run "Delivery-API-LoadTest.postman_collection.json" \
  --environment "Delivery-API-Local.postman_environment.json" \
  --iteration-count 100 \
  --delay-request 50 \
  --reporters html,cli \
  --reporter-html-export delivery-api-load-test-results.html
```

## 🏗️ Estrutura da Coleção Principal

### 🏠 Health & System
- **GET** `/` - Página inicial da API
- **GET** `/health` - Health check da aplicação

### 👥 Customers (Clientes)
- **POST** `/api/v1/customers` - Criar cliente
- **GET** `/api/v1/customers` - Listar todos os clientes
- **GET** `/api/v1/customers/{id}` - Buscar cliente por ID
- **GET** `/api/v1/customers/email/{email}` - Buscar cliente por email
- **PUT** `/api/v1/customers/{id}` - Atualizar cliente
- **DELETE** `/api/v1/customers/{id}` - Deletar cliente (soft delete)

### 🍽️ Restaurants (Restaurantes)
- **POST** `/api/v1/restaurants` - Criar restaurante
- **GET** `/api/v1/restaurants` - Listar restaurantes com filtros
- **GET** `/api/v1/restaurants/{id}` - Buscar restaurante por ID
- **GET** `/api/v1/restaurants/search?q=` - Pesquisar restaurantes
- **GET** `/api/v1/restaurants/categories` - Listar categorias
- **PUT** `/api/v1/restaurants/{id}` - Atualizar restaurante
- **PATCH** `/api/v1/restaurants/{id}/status` - Alterar status ativo/inativo
- **PATCH** `/api/v1/restaurants/{id}/open-status` - Alterar status aberto/fechado
- **DELETE** `/api/v1/restaurants/{id}` - Deletar restaurante

### 🍕 Products (Produtos)
- **POST** `/api/v1/products` - Criar produto
- **GET** `/api/v1/products` - Listar produtos com filtros
- **GET** `/api/v1/products/{id}` - Buscar produto por ID
- **GET** `/api/v1/products/restaurant/{restaurantId}` - Produtos por restaurante
- **GET** `/api/v1/products/restaurant/{restaurantId}/available` - Produtos disponíveis
- **PUT** `/api/v1/products/{id}` - Atualizar produto
- **PATCH** `/api/v1/products/{id}/availability` - Alterar disponibilidade
- **DELETE** `/api/v1/products/{id}` - Deletar produto

### 📦 Orders (Pedidos)
- **POST** `/api/v1/orders` - Criar pedido
- **GET** `/api/v1/orders/{id}` - Buscar pedido por ID
- **GET** `/api/v1/orders/customer/{customerId}` - Pedidos por cliente
- **GET** `/api/v1/orders/restaurant/{restaurantId}` - Pedidos por restaurante
- **GET** `/api/v1/orders` - Listar todos os pedidos
- **PATCH** `/api/v1/orders/{id}/status` - Atualizar status do pedido
- **PATCH** `/api/v1/orders/{id}/cancel` - Cancelar pedido

### 🧪 Cenários de Teste Negativos
- Validações de entrada inválida
- Testes com recursos inexistentes
- Cenários de erro esperados

## 🔧 Variáveis de Ambiente

A coleção utiliza as seguintes variáveis que são automaticamente gerenciadas:

- `baseUrl` - URL base da API (padrão: `http://localhost:8080`)
- `customerId` - ID do cliente criado (preenchido automaticamente)
- `restaurantId` - ID do restaurante criado (preenchido automaticamente)
- `productId` - ID do produto criado (preenchido automaticamente)
- `orderId` - ID do pedido criado (preenchido automaticamente)

## 📊 Testes Automatizados

Cada requisição inclui testes automatizados que verificam:

- ✅ Status codes apropriados
- ✅ Estrutura da resposta JSON
- ✅ Presença de campos obrigatórios
- ✅ Tipos de dados corretos
- ✅ Lógica de negócio específica

## ⚡ Testes de Performance

A coleção de **Load Test** inclui:

- 🚀 Testes de tempo de resposta
- 📈 Verificações de performance
- 🔄 Simulação de carga com dados aleatórios
- 📊 Métricas de throughput

### Executar Testes de Carga
```bash
# Teste básico de carga (100 iterações)
newman run "Delivery-API-LoadTest.postman_collection.json" \
  --iteration-count 100 \
  --delay-request 50

# Teste de stress (1000 iterações com menor delay)
newman run "Delivery-API-LoadTest.postman_collection.json" \
  --iteration-count 1000 \
  --delay-request 10
```

## 🛠️ Configurações Avançadas

### Executar com Dados Externos
```bash
# Usar arquivo CSV com dados de teste
newman run "Delivery-API-Collection.postman_collection.json" \
  --data "test-data.csv" \
  --iteration-count 10
```

### Executar em Modo CI/CD
```bash
# Para pipelines de CI/CD
newman run "Delivery-API-Collection.postman_collection.json" \
  --environment "Delivery-API-Local.postman_environment.json" \
  --reporters junit,cli \
  --reporter-junit-export delivery-api-test-results.xml \
  --suppress-exit-code
```

## 📝 Logs e Debugging

Os testes incluem logs detalhados que podem ser visualizados:

- No console do Postman durante execução manual
- No terminal ao usar Newman
- Em relatórios HTML gerados automaticamente

## 🔍 Validações Específicas

### Delivery Tech API Features Testadas

- ✅ **Idempotência** - Testes de criação com dados duplicados
- ✅ **Soft Delete** - Verificação de remoção lógica
- ✅ **Filtros Avançados** - Testes com múltiplos parâmetros
- ✅ **Validações de Negócio** - CNPJ, email, preços, etc.
- ✅ **Estados de Recursos** - Ativo/inativo, aberto/fechado, disponível
- ✅ **Relacionamentos** - Integridade entre entidades

## 📚 Recursos Adicionais

- [Documentação do Postman](https://learning.postman.com/)
- [Newman CLI Guide](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/)
- [Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

## 🐛 Resolução de Problemas

### API não responde
1. Verifique se a aplicação está rodando: `curl http://localhost:8080/health`
2. Confirme a porta no arquivo `application.properties`
3. Verifique logs da aplicação

### Testes falham
1. Execute os testes na ordem correta
2. Verifique se as variáveis de ambiente estão configuradas
3. Confirme se o banco H2 está funcionando

### Newman não encontrado
```bash
npm install -g newman
# ou
yarn global add newman
```

---

**Desenvolvido para a Delivery Tech API** 🚀  
Versão da API: `0.0.1-SNAPSHOT`  
Spring Boot: `3.2.x` | Java: `21`
