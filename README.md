# Delivery Tech API

API RESTful para sistema de delivery desenvolvida em Java 21 com Spring Boot 3.2.x.

## Tecnologias

- **Java 21** - Virtual Threads habilitadas para alta concorrência
- **Spring Boot 3.2.x** - Framework principal
- **Spring Data JPA** - Acesso a dados
- **Hibernate 6.3.1** - ORM
- **H2 Database** - Banco em memória para desenvolvimento
- **Maven** - Gerenciamento de dependências
- **Swagger/OpenAPI 3.0** - Documentação interativa

## Arquitetura

A aplicação segue o padrão de arquitetura em camadas:

```
Controllers (REST API) → Services (Regras de Negócio) → Repositories (Dados) → Database
```

### Entidades Principais

- **User** - Usuários do sistema (clientes, restaurantes, admin)
- **Restaurant** - Estabelecimentos com endereço e horários
- **Product** - Produtos do cardápio
- **Order** - Pedidos com itens e status de entrega
- **Payment** - Pagamentos com diferentes métodos
- **Address** - Value object para endereços

## Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+ (opcional, usar wrapper incluído)

### Execução Local
```bash
# Clonar repositório
git clone <repository-url>
cd fat-arq/delivery-api-rabay

# Executar aplicação
./mvnw spring-boot:run

# Ou compilar e executar JAR
./mvnw clean package
java -jar target/delivery-api-0.0.1-SNAPSHOT.jar
```

### Verificação da Instalação
```bash
# Health check
curl http://localhost:8080/actuator/health

# Endpoint de boas-vindas
curl http://localhost:8080/

# Testar API de restaurantes
curl http://localhost:8080/api/v1/restaurants
```

## Acessos

- **Homepage**: http://localhost:8080/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (vazio)

## API Endpoints

### Clientes (Customers)
```bash
# Listar clientes
GET /api/v1/customers

# Buscar por ID
GET /api/v1/customers/{id}

# Criar cliente
POST /api/v1/customers
{
  "name": "João Silva",
  "email": "joao@example.com",
  "phone": "(11) 99999-9999",
  "password": "senha123",
  "address": {
    "street": "Rua Exemplo",
    "number": "123",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "postalCode": "01234-567"
  }
}

# Atualizar cliente
PUT /api/v1/customers/{id}

# Remover cliente
DELETE /api/v1/customers/{id}
```

### Restaurantes (Restaurants)
```bash
# Listar restaurantes
GET /api/v1/restaurants
GET /api/v1/restaurants?includeInactive=true
GET /api/v1/restaurants?onlyOpen=true
GET /api/v1/restaurants?search=pizza

# Buscar por ID
GET /api/v1/restaurants/{id}

# Criar restaurante
POST /api/v1/restaurants
{
  "name": "Pizzaria do João",
  "description": "Pizzas artesanais",
  "cnpj": "12.345.678/0001-90",
  "phone": "(11) 98765-4321",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "postalCode": "01234-567"
  },
  "deliveryFee": 5.00,
  "minimumOrderValue": 15.00,
  "averageDeliveryTimeInMinutes": 30
}

# Atualizar restaurante
PUT /api/v1/restaurants/{id}

# Alterar status
PATCH /api/v1/restaurants/{id}/status

# Remover restaurante
DELETE /api/v1/restaurants/{id}
```

### Produtos (Products)
```bash
# Listar produtos
GET /api/v1/products
GET /api/v1/products?restaurantId=1
GET /api/v1/products?category=PIZZA

# Buscar por ID
GET /api/v1/products/{id}

# Criar produto
POST /api/v1/products
{
  "name": "Pizza Margherita",
  "description": "Pizza clássica",
  "price": 25.90,
  "restaurantId": 1,
  "category": "PIZZA",
  "available": true
}

# Atualizar produto
PUT /api/v1/products/{id}

# Alterar disponibilidade
PATCH /api/v1/products/{id}/availability

# Remover produto
DELETE /api/v1/products/{id}
```

### Pedidos (Orders)
```bash
# Listar pedidos
GET /api/v1/orders
GET /api/v1/orders?customerId=1
GET /api/v1/orders?restaurantId=1
GET /api/v1/orders?status=PENDING

# Buscar por ID
GET /api/v1/orders/{id}

# Criar pedido
POST /api/v1/orders
{
  "customerId": 1,
  "restaurantId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "observations": "Sem cebola"
    }
  ],
  "paymentMethod": "CREDIT_CARD",
  "deliveryAddress": {
    "street": "Rua de Entrega",
    "number": "456",
    "neighborhood": "Bairro",
    "city": "São Paulo",
    "state": "SP",
    "postalCode": "05678-901"
  }
}

# Atualizar status
PATCH /api/v1/orders/{id}/status
{
  "status": "CONFIRMED"
}

# Cancelar pedido
DELETE /api/v1/orders/{id}
```

## Exemplos de Uso

### Criando um Cliente e Fazendo Pedido
```bash
# 1. Criar cliente
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Silva",
    "email": "maria@example.com",
    "phone": "(11) 88888-8888",
    "password": "senha123",
    "address": {
      "street": "Av. Paulista",
      "number": "1000",
      "neighborhood": "Bela Vista",
      "city": "São Paulo",
      "state": "SP",
      "postalCode": "01310-100"
    }
  }'

# 2. Listar restaurantes disponíveis
curl http://localhost:8080/api/v1/restaurants?onlyOpen=true

# 3. Ver produtos de um restaurante
curl http://localhost:8080/api/v1/products?restaurantId=1

# 4. Criar pedido
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "restaurantId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 1,
        "observations": "Bem passada"
      }
    ],
    "paymentMethod": "CREDIT_CARD",
    "deliveryAddress": {
      "street": "Av. Paulista",
      "number": "1000",
      "neighborhood": "Bela Vista",
      "city": "São Paulo",
      "state": "SP",
      "postalCode": "01310-100"
    }
  }'
```

### Gerenciando Status de Pedidos
```bash
# Confirmar pedido
curl -X PATCH http://localhost:8080/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}'

# Marcar como em preparo
curl -X PATCH http://localhost:8080/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "PREPARING"}'

# Marcar como saiu para entrega
curl -X PATCH http://localhost:8080/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "OUT_FOR_DELIVERY"}'

# Finalizar entrega
curl -X PATCH http://localhost:8080/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "DELIVERED"}'
```

## Dados de Exemplo

A aplicação carrega automaticamente 4 restaurantes de exemplo:

1. **Pizzaria do João** - Ativo e Aberto
2. **Burger House** - Ativo e Aberto  
3. **Sushi Master** - Ativo mas Fechado
4. **Café & Cia** - Inativo

## Estrutura de Resposta

### Resposta Simples (Customers)
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "phone": "(11) 99999-9999",
    "userType": "CUSTOMER",
    "active": true,
    "createdAt": "2025-08-11T21:57:25",
    "updatedAt": "2025-08-11T21:57:25"
  }
]
```

### Resposta com Metadata (Restaurants, Products, Orders)
```json
{
  "data": [
    {
      "id": 1,
      "name": "Pizzaria do João",
      "description": "Pizzas artesanais",
      "cnpj": "12.345.678/0001-90",
      "active": true,
      "open": true
    }
  ],
  "meta": {
    "total": 3,
    "timestamp": "2025-08-11T21:58:19",
    "filter": "activeOnly: true",
    "version": "v1"
  }
}
```

## Testes

### Executar Testes
```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=CustomerServiceTest
./mvnw test -Dtest=*ControllerTest
```

### Status dos Testes
- **23 testes** executados com sucesso
- **0 falhas** e **0 erros**
- Cobertura completa de Controllers e Services

## 📮 Testes com Postman

Este projeto inclui uma **coleção completa de testes do Postman** para validação de todos os endpoints da API, incluindo cenários de sucesso, falha e testes de performance.

### 📋 Recursos Disponíveis

- **📄 Coleção Principal** - Mais de 30 testes cobrindo todos os endpoints
- **⚡ Testes de Carga** - Validação de performance e stress testing
- **🌐 Ambiente Configurado** - Variáveis para desenvolvimento local
- **🚀 Script Automatizado** - Execução via Newman CLI
- **📊 Dados de Teste** - CSV com dados para testes data-driven
- **📖 Documentação Completa** - Guia detalhado de uso

### 🎯 Cobertura de Testes

| Módulo | Endpoints | Cenários Testados |
|--------|-----------|-------------------|
| **Health & System** | 2 | Status da API, informações do sistema |
| **Customers** | 6 | CRUD completo, validações, busca por email |
| **Restaurants** | 11 | CRUD, filtros avançados, gerenciamento de status |
| **Products** | 10 | CRUD, filtros por restaurante, disponibilidade |
| **Orders** | 8 | Criação, status, filtros por cliente/restaurante |
| **Cenários Negativos** | 6+ | Validações de entrada, recursos inexistentes |

### 🚀 Como Usar

#### Postman (Interface Gráfica)

```bash
# 1. Importar coleções no Postman
postman/Delivery-API-Collection.postman_collection.json
postman/Delivery-API-Local.postman_environment.json

# 2. Selecionar o ambiente "Delivery API - Local Development"
# 3. Executar a coleção sequencialmente
```

#### Newman (Linha de Comando)

```bash
# Navegar para o diretório de testes
cd postman/

# Executar script interativo
./run-tests.sh

# Ou executar diretamente
newman run Delivery-API-Collection.postman_collection.json \
  --environment Delivery-API-Local.postman_environment.json \
  --reporters html,cli \
  --reporter-html-export reports/test-results.html
```

### 📊 Tipos de Teste

- **✅ Funcionais** - Validação de funcionalidades e regras de negócio
- **🔍 Validação de Dados** - Estrutura JSON, tipos e campos obrigatórios
- **⚡ Performance** - Tempo de resposta e throughput
- **🛡️ Negativos** - Cenários de erro e edge cases
- **🔗 Integração** - Fluxos completos entre módulos

### 📚 Documentação Completa

Para instruções detalhadas, exemplos avançados e troubleshooting:

**👉 [Consulte a documentação completa dos testes](postman/README.md)**

---

## Estrutura do Projeto

```text
src/
├── main/java/com/deliverytech/deliveryapi/
│   ├── config/          # Configurações
│   ├── controller/      # Controllers REST
│   ├── service/         # Lógica de negócio
│   ├── dto/            # Data Transfer Objects
│   ├── domain/
│   │   ├── model/       # Entidades JPA
│   │   └── repository/  # Repositórios
│   └── exception/       # Tratamento de exceções
├── resources/
│   └── application.properties
└── test/               # Testes unitários
```

## Desenvolvedor

**Victor Rabay**  
TI 58A 02728 - Arquitetura de Sistemas

## Status

- ✅ **Build**: Sucesso
- ✅ **Testes**: 23/23 passando
- ✅ **APIs REST**: 4 controllers funcionais
- ✅ **Documentação**: Swagger UI disponível
- ✅ **Banco**: H2 configurado e funcionando
