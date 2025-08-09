# Delivery Tech API

API backend para sistema de delivery moderna, escalável e de fácil manutenção, desenvolvida em Java 21 com Spring Boot 3.2.x.

## 🚀 Tecnologias e Ferramentas
- **Java 21 LTS** – última versão LTS, com melhorias de performance e segurança
- **Spring Boot 3.2.x** – criação de APIs REST com configuração mínima
- **Maven 3.2.5** – gerenciamento de dependências e ciclo de build
- **Hibernate 6.3.1.Final** – ORM para persistência de dados
- **H2 Database** – banco de dados em memória para testes e desenvolvimento rápido
- **Spring Data JPA** – acesso a dados com repository pattern
- **Spring DevTools** – para recarga automática durante o desenvolvimento

### Além disso, usamos:
- Spring MVC para camada web
- Spring Actuator para monitoramento e métricas
- Thymeleaf para templates HTML (futuro uso)

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21) – configurado para alta concorrência

## 🏗️ Arquitetura do Sistema

### Domínio Modelado
A aplicação implementa um modelo de domínio completo para delivery, incluindo:

#### Entidades Principais:
- **User** – Usuários do sistema (CUSTOMER, RESTAURANT, DELIVERY_PERSON, ADMIN)
- **Restaurant** – Restaurantes com informações de endereço, horários e categorias
- **Product** – Produtos do cardápio com categorias e tags
- **Order** – Pedidos com status completo (PENDING → DELIVERED)
- **OrderItem** – Itens do pedido com quantidades e preços
- **Payment** – Pagamentos com múltiplos métodos (CREDIT_CARD, DEBIT_CARD, PIX, CASH)
- **Review** – Avaliações de restaurantes e entregadores
- **Notification** – Sistema de notificações para usuários
- **DeliveryArea** – Áreas de entrega por restaurante

#### Value Objects:
- **Money** – Representação monetária evitando float/double
- **Address** – Endereços com validação de CEP
- **BusinessHours** – Horários de funcionamento

## 📊 Status Atual do Projeto

### ✅ Build Status: **SUCCESS**
- **Maven Build**: ✅ `mvn clean package` executando com sucesso
- **Aplicação**: ✅ `./mvnw spring-boot:run` iniciando sem erros
- **Testes**: ✅ Testes de integração passando
- **Banco de Dados**: ✅ Schema criado automaticamente pelo Hibernate
- **API REST**: ✅ Endpoint de restaurantes totalmente funcional
- **Package Structure**: ✅ Estrutura de packages corrigida e padronizada

### 🚀 Funcionalidades Implementadas Recentemente

1. **✅ Endpoint Raiz (/) Implementado**:
   - Endpoint de boas-vindas em `/` evitando erro 404
   - Resposta JSON informativa com mapa de endpoints disponíveis
   - Informações da aplicação (nome, versão, timestamp)
   - Guia de navegação para desenvolvedores

2. **✅ Refatoração de Package Structure**:
   - **Antes**: `com.deliverytech.delivery_api` (com underscore - não padrão)
   - **Depois**: `com.deliverytech.deliveryapi` (seguindo convenções Java)
   - **Impacto**: 38 arquivos Java refatorados automaticamente
   - **Status**: ✅ Compilação, testes e execução funcionando perfeitamente

3. **✅ API REST de Restaurantes Completa**:
   - Endpoint `/api/v1/restaurants` com filtros avançados
   - Endpoint `/api/v1/restaurants/{id}` para busca específica
   - DTOs com Java 21 Records
   - Service Layer com lógica de negócio
   - 4 restaurantes de exemplo carregados automaticamente

4. **✅ Endpoints de Monitoramento Corrigidos**:
   - `/` - Endpoint raiz de boas-vindas com mapa de navegação ✨ **NOVO**
   - `/health` - Health check customizado funcionando
   - `/actuator/info` - Informações completas da aplicação
   - Configuração de métricas e informações personalizadas

5. **✅ Dados de Exemplo Funcionais**:
   - DataInitializer carregando dados automaticamente
   - Restaurantes com diferentes status (ativo, inativo, aberto, fechado)
   - Endereços completos e informações realistas

### 🔧 Problemas Resolvidos Recentemente
O projeto passou por um processo de correção de compatibilidade e padronização:

1. **❌ Package Structure Inconsistente**: 
   - **Problema**: Package `com.deliverytech.delivery_api` com underscore (não seguia convenções Java)
   - **Solução**: ✅ Refatorado para `com.deliverytech.deliveryapi` (38 arquivos atualizados)
   
2. **❌ Endpoint Raiz Indefinido**:
   - **Problema**: Acesso à URL base retornava erro 404
   - **Solução**: ✅ Criado endpoint `/` com informações de boas-vindas e mapa de navegação

3. **❌ Erros de Injeção de Dependência**: Bean `virtualThreadExecutor` resolvido

4. **❌ Controllers de Demonstração**: Removidos `HomeController` e `JavaFeaturesController` que não faziam parte do domínio

5. **❌ Incompatibilidade Hibernate 6.3.1**: 
   - **Problema**: `BasicValue cannot be cast to ToOne` – bug do Hibernate 6.3.x com mapeamentos JPA complexos
   - **Solução**: Simplificação de relacionamentos e remoção de `@ElementCollection` problemáticos

### 🔄 Ajustes de Compatibilidade Hibernate 6.3.x
Para garantir compatibilidade com Hibernate 6.3.1.Final, foram realizados ajustes:

#### Entidades Simplificadas:
- **Restaurant**: `@ElementCollection` para `businessHours` e `categories` temporariamente comentados
- **RestaurantCategory**: Relacionamento bidirecional com `Restaurant` removido
- **Order & Payment**: Convertidos para referências por ID em vez de entidades completas

#### Repositórios Ajustados:
- **RestaurantRepository**: Consultas com `r.categories` comentadas
- **RestaurantCategoryRepository**: Métodos dependentes de relacionamentos bidirecionais comentados
- **OrderItemRepository**: Método `findMostOrderedProductsByRestaurant` corrigido

### 📋 TODO para Próximas Versões
- [x] **Implementar endpoint REST para restaurantes** ✅
- [x] **Criar DTOs e Service Layer para Restaurantes** ✅
- [x] **Configurar dados de exemplo no banco** ✅
- [x] **Corrigir estrutura de packages para seguir convenções Java** ✅
- [x] **Implementar endpoint raiz (/) para melhor UX** ✅
- [ ] Reimplementar relacionamentos JPA usando estratégias compatíveis com Hibernate 6.3.x
- [ ] Restaurar consultas de categoria usando join tables ou consultas nativas
- [ ] Implementar `businessHours` usando tabela separada em vez de `@ElementCollection`
- [ ] Adicionar endpoints REST para outras entidades (Products, Orders, Users)
- [ ] Implementar segurança com Spring Security e RBAC
- [ ] Adicionar integração com gateways de pagamento
- [ ] Implementar cache para catálogos de restaurantes

## � Endpoint de Boas-Vindas (Implementado!)

### 🚀 **Novo Endpoint Raiz: GET /**
Um endpoint de boas-vindas foi criado para melhorar a experiência do desenvolvedor e evitar erros 404 ao acessar a URL base.

#### **Funcionalidades do Endpoint:**
- **Mensagem de boas-vindas** em português brasileiro
- **Mapa de navegação** com todos os endpoints disponíveis
- **Informações da aplicação** (nome, versão, status)
- **Timestamp** para debugging
- **Estrutura JSON padronizada**

#### **Exemplo de Resposta:**
```json
{
  "message": "Bem-vindo à Delivery Tech API",
  "status": "online",
  "application": "delivery-api",
  "version": "0.0.1-SNAPSHOT",
  "timestamp": "2025-08-08T23:37:25.037443758",
  "endpoints": {
    "health": "/health",
    "actuator": "/actuator",
    "restaurants": "/api/v1/restaurants"
  }
}
```

#### **Como Testar:**
```bash
# Acesse via browser
http://localhost:8080/

# Ou via curl
curl "http://localhost:8080/" | jq .

# Ou simpemente
curl http://localhost:8080/
```

## �🆕 API REST - Restaurantes (Implementada!)

A API de Restaurantes foi **totalmente implementada** seguindo as melhores práticas de desenvolvimento moderno:

### 🏗️ **Arquitetura Implementada**
- **Controllers**: REST endpoints com ResponseEntity e tratamento de erros
- **Services**: Camada de serviço com lógica de negócio
- **DTOs**: Java 21 Records para responses imutáveis e limpas
- **Data Initializer**: População automática de dados de exemplo

### 📊 **Dados de Exemplo Disponíveis**
A aplicação carrega automaticamente 4 restaurantes de exemplo:

1. **Pizzaria do João** - São Paulo/SP (Ativo e Aberto)
   - CNPJ: 12.345.678/0001-90 | Tel: (11) 98765-4321
   - Rua das Flores, 123 - Centro

2. **Burger House** - São Paulo/SP (Ativo e Aberto) 
   - CNPJ: 98.765.432/0001-10 | Tel: (11) 87654-3210
   - Avenida Paulista, 1000 - Bela Vista

3. **Sushi Master** - São Paulo/SP (Ativo mas Fechado)
   - CNPJ: 11.222.333/0001-44 | Tel: (11) 99988-7766
   - Rua da Liberdade, 500 - Liberdade

4. **Café & Cia** - São Paulo/SP (Inativo)
   - CNPJ: 55.666.777/0001-88 | Tel: (11) 77766-5544
   - Rua Augusta, 200 - Consolação

### 🔗 **Endpoints Disponíveis**

#### `GET /api/v1/restaurants`
**Lista restaurantes com filtros avançados**

**Parâmetros de Query (opcionais):**
- `includeInactive=true` - Inclui restaurantes inativos
- `onlyOpen=true` - Apenas restaurantes abertos no momento
- `search=termo` - Busca por nome do restaurante (busca parcial)

**Exemplos de Uso:**
```bash
# Lista restaurantes ativos (padrão) - retorna 3
curl "http://localhost:8080/api/v1/restaurants"

# Inclui restaurantes inativos - retorna 4
curl "http://localhost:8080/api/v1/restaurants?includeInactive=true"

# Apenas restaurantes abertos - retorna 2
curl "http://localhost:8080/api/v1/restaurants?onlyOpen=true"

# Busca por nome - retorna 1
curl "http://localhost:8080/api/v1/restaurants?search=pizza"
```

#### `GET /api/v1/restaurants/{id}`
**Busca restaurante específico por ID**

```bash
# Buscar restaurante por ID
curl "http://localhost:8080/api/v1/restaurants/1"

# ID inexistente retorna 404
curl "http://localhost:8080/api/v1/restaurants/999"
```

### 📄 **Estrutura da Resposta**

**Response com lista de restaurantes:**
```json
{
  "data": [
    {
      "id": 1,
      "name": "Pizzaria do João",
      "description": "Pizzas artesanais com ingredientes frescos...",
      "cnpj": "12.345.678/0001-90",
      "phone": "(11) 98765-4321",
      "address": {
        "street": "Rua das Flores",
        "number": "123",
        "complement": "Apto 201",
        "neighborhood": "Centro",
        "city": "São Paulo",
        "state": "SP",
        "postalCode": "01234-567",
        "reference": "Próximo ao metrô"
      },
      "logo": "https://example.com/logo-pizzaria.png",
      "active": true,
      "open": true,
      "createdAt": "2025-08-08T23:20:31.457554",
      "updatedAt": "2025-08-08T23:20:31.457621"
    }
  ],
  "meta": {
    "total": 3,
    "timestamp": "2025-08-08T23:20:37.380889066",
    "filter": "activeOnly: true (default)",
    "version": "v1"
  }
}
```

### ⚡ **Recursos Modernos Utilizados**
- **Java 21 Records** para DTOs imutáveis
- **Pattern Matching** para lógica condicional
- **Spring Boot 3.2.x** com anotações modernas
- **CORS configurado** para desenvolvimento
- **Versionamento explícito** (/api/v1/)
- **Estrutura de resposta padronizada** com seção `data` e `meta`
- **Query parameters flexíveis** para filtragem
- **Tratamento de erros** com códigos HTTP apropriados
- [ ] Implementar `businessHours` usando tabela separada em vez de `@ElementCollection`
- [ ] Adicionar endpoints REST para outras entidades (Products, Orders, Users)
- [ ] Implementar segurança com Spring Security e RBAC
- [ ] Adicionar integração com gateways de pagamento
- [ ] Implementar cache para catálogos de restaurantes

## 🏃‍♂️ Como Executar

### Pré-requisitos
- JDK 21 instalado
- Maven 3.6+

### Passos
1. **Clone o repositório**
   ```bash
   git clone https://github.com/rabay/delivery-api-rabay.git
   cd delivery-api-rabay
   ```

2. **Execute o build e testes**
   ```bash
   mvn clean package
   ```

3. **Inicie a aplicação**
   ```bash
   ./mvnw spring-boot:run
   # ou
   java -jar target/delivery-api-0.0.1-SNAPSHOT.jar
   ```

4. **Acesse os endpoints**
   - **Página Inicial**: [http://localhost:8080/](http://localhost:8080/) ✨ **NOVO**
   - Health Check: [http://localhost:8080/health](http://localhost:8080/health)
   - Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)
   - **API Restaurantes**: [http://localhost:8080/api/v1/restaurants](http://localhost:8080/api/v1/restaurants)
   - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

5. **Teste a API completa**
   ```bash
   # Página inicial com mapa de navegação ✨ NOVO
   curl "http://localhost:8080/" | jq .
   
   # Health check customizado
   curl "http://localhost:8080/health" | jq .
   
   # Lista todos os restaurantes ativos
   curl "http://localhost:8080/api/v1/restaurants" | jq .
   
   # Busca por nome (pizza)
   curl "http://localhost:8080/api/v1/restaurants?search=pizza" | jq .
   
   # Apenas restaurantes abertos
   curl "http://localhost:8080/api/v1/restaurants?onlyOpen=true" | jq .
   
   # Inclui inativos (mostra todos os 4 restaurantes)
   curl "http://localhost:8080/api/v1/restaurants?includeInactive=true" | jq .
   ```

## 🔧 Configuração

### Ambiente de Desenvolvimento
- **Porta**: 8080
- **Banco**: H2 em memória
- **Profile**: development
- **Virtual Threads**: Ativados para alta concorrência

### Variáveis de Ambiente
```properties
server.port=8080
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

## 📋 Endpoints Disponíveis

### Navigation & Welcome
- `GET /` – **Página inicial da API** com mapa de navegação e informações de boas-vindas ✨ **NOVO**

### Health & Monitoring
- `GET /health` – Status da aplicação (inclui versão Java)
- `GET /actuator/health` – Health check detalhado
- `GET /actuator/metrics` – Métricas da aplicação
- `GET /actuator/info` – Informações da aplicação

### API REST - Restaurantes (v1)
- `GET /api/v1/restaurants` – Lista restaurantes com filtros opcionais
  - `?includeInactive=true` – Inclui restaurantes inativos
  - `?onlyOpen=true` – Apenas restaurantes abertos
  - `?search=nome` – Busca por nome do restaurante
- `GET /api/v1/restaurants/{id}` – Busca restaurante específico por ID

### Banco de Dados
- `GET /h2-console` – Console do banco H2 (para desenvolvimento)

## 🗂️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/deliverytech/deliveryapi/           # 🔄 Package refatorado (era delivery_api)
│   │   ├── DeliveryApiApplication.java
│   │   ├── config/             # Configurações e inicializadores
│   │   ├── controller/         # Controllers REST
│   │   ├── dto/               # Data Transfer Objects (Java Records)
│   │   ├── service/           # Camada de serviços
│   │   ├── domain/
│   │   │   ├── model/          # Entidades e Value Objects
│   │   │   └── repository/     # Repositórios Spring Data JPA
│   │   └── infrastructure/     # Configurações e utilitários
│   └── resources/
│       ├── application.properties
│       └── static/             # Recursos estáticos
└── test/
    └── java/com/deliverytech/deliveryapi/          # 🔄 Package de testes também refatorado
        └── DeliveryApiApplicationTests.java
```

### 🔄 Alterações na Estrutura de Packages
A estrutura foi **refatorada para seguir as melhores práticas Java**:
- **Antes**: `com.deliverytech.delivery_api` (com underscore)
- **Depois**: `com.deliverytech.deliveryapi` (sem underscore, seguindo convenções)
- **Impacto**: 38 arquivos Java atualizados automaticamente
- **Status**: ✅ Compilação e execução funcionando perfeitamente

## 🏆 Desenvolvedor

**Victor Rabay**  
TI 58A 02728 - Arquitetura de Sistemas  
Desenvolvido com JDK 21 e Spring Boot 3.2.x

---

**Última Atualização**: Agosto 2025  
**Versão**: 0.0.1-SNAPSHOT  
**Status**: ✅ API REST de Restaurantes + Endpoint Raiz implementados e funcionando perfeitamente  
**Próximo**: Implementar endpoints para Products, Orders e Users

### 🎯 Principais Conquistas Recentes
- ✅ **Package Structure Refatorada**: Seguindo convenções Java (`com.deliverytech.deliveryapi`)
- ✅ **Endpoint Raiz Implementado**: Página inicial com mapa de navegação em `/`
- ✅ **API REST Completa**: Endpoints de restaurantes totalmente funcionais
- ✅ **Build & Testes**: 100% de sucesso após refatoração
