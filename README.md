# 🚀 Delivery API Rabay

> **ATENÇÃO:** Este projeto utiliza CI/CD completo com GitHub Actions, build/teste automatizado, cobertura Jacoco e build de imagem Docker. Apenas o relatório de cobertura é salvo como artefato. Nenhum binário (JAR/WAR) ou imagem Docker é publicado pelo pipeline.

API backend desenvolvida em **Java 21** com **Spring Boot 3.2.x**, como base para um sistema de delivery moderno, escalável e de fácil manutenção.

---


## 📌 Visão Geral

Este projeto fornece uma estrutura robusta para aplicações de delivery, incluindo:

- Cadastro e gerenciamento de clientes e restaurantes
- Catálogo de produtos
- Gestão de pedidos
- Integração com banco H2 para testes
- Monitoramento via endpoints health/info

---

## 🛠️ Tecnologias e Ferramentas

- **Java 21 LTS**
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database (memória)
- Maven Wrapper
- Docker
- Docker Compose
- GitHub Actions (CI/CD)
- Jacoco (cobertura)
- [act](https://github.com/nektos/act) (validação local do workflow)
- Spring DevTools

---

## 📈 Status do Projeto

✅ Aplicação funcional, endpoints REST testados via Postman/Newman, DataLoader populando H2 e testes automatizados presentes. Estrutura de código alinhada a padrões modernos de arquitetura Java/Spring Boot, facilitando manutenção, testes e evolução.

---



## ✅ Funcionalidades e Refatorações Recentes
---



## 🆕 Implementações e Correções Recentes (Agosto/2025)

- [x] **Validação de E-mail Único e Erro 409:**
	- Cadastro de cliente agora retorna HTTP 409 (Conflict) e mensagem clara ao tentar cadastrar e-mail já existente.
	- Exceção customizada (`EmailDuplicadoException`) e tratamento global via `GlobalExceptionHandler`.
	- Teste automatizado específico na collection Postman para garantir o comportamento correto.

- [x] **Refatoração completa do módulo Cliente:**
	- Implementação do padrão DTO para requisições e respostas (`ClienteRequest`, `ClienteResponse`).
	- Criação da camada de mapeamento (`ClienteMapper`) para conversão entre entidade e DTO.
	- Refatoração do serviço (`ClienteService`, `ClienteServiceImpl`) e controller para uso exclusivo de DTOs, eliminando exposição direta da entidade.
	- Métodos legados marcados como `@Deprecated` para facilitar transição e manter compatibilidade temporária.
	- Testes unitários e collection Postman atualizados para refletir o novo contrato de API (payloads e respostas).
	- Validação e tratamento de erros padronizados para cadastro, atualização, busca e inativação de clientes.
	- Documentação e exemplos de payloads revisados neste README.

- [x] **Testes Postman idempotentes e robustos:**
	- Os testes automatizados agora utilizam e-mails dinâmicos e únicos a cada execução, evitando falhas por dados duplicados.
	- Adicionado teste específico para tentativa de cadastro com e-mail duplicado, validando o retorno do erro 409.
	- Scripts de teste revisados para aceitar múltiplos status onde aplicável (ex: 200 ou 405), tornando a suite mais resiliente.

- [x] **Soft Delete (Exclusão Lógica) implementado para Cliente, Restaurante e Produto:**
	- Todas as entidades principais agora possuem o campo `excluido` (Boolean).
	- Endpoints DELETE marcam o registro como `excluido=true` ao invés de remover fisicamente.
	- Todas as consultas, buscas e listagens foram refatoradas para considerar apenas registros com `excluido=false`.
	- O campo `excluido` é retornado nas respostas detalhadas (por ID).
- [x] **Refatoração dos repositórios:**
	- Métodos derivados atualizados para incluir o filtro `AndExcluidoFalse` (ex: `findByAtivoTrueAndExcluidoFalse`, `findByCategoriaAndExcluidoFalse`).
	- Métodos com ordem corrigidos para o padrão correto do Spring Data JPA (ex: `findAllByExcluidoFalseOrderByAvaliacaoDesc`).
- [x] **Atualização do DataLoader e serviços:**
	- Todos os usos de métodos antigos dos repositórios foram atualizados para as novas assinaturas.
	- DataLoader validando queries e exemplos de uso com soft delete.
- [x] **Testes automatizados revisados:**
	- Testes de repositório e service atualizados para cobrir cenários de soft delete.
	- Todos os testes passam e a build está verde.
- [x] **Correção de build:**
	- Corrigidos erros de contexto do Spring causados por métodos derivados inválidos.
	- Projeto compila e executa normalmente após as correções.

---

- [x] Refatoração completa dos serviços seguindo padrão interface/implementação, alinhado a projeto de referência
- [x] **Padronização de transações:** Todos os serviços (Cliente, Produto, Restaurante, Pedido) agora utilizam `@Transactional` no nível de classe e `@Transactional(readOnly = true)` nos métodos de leitura, conforme boas práticas do projeto de referência. Isso garante integridade transacional, melhor performance em consultas e alinhamento com padrões Spring modernos.
- [x] Criação e uso de DTOs para requisições e respostas (ex: ClienteRequest, RestauranteRequest, ItemPedidoRequest, **PedidoRequest, PedidoResponse, StatusUpdateRequest, ItemPedidoResponse**)
- [x] **PedidoController refatorado:** Agora utiliza DTOs, validação com `@Valid`, mapeamento explícito entre entidades e DTOs, e respostas REST padronizadas. Endpoints de pedido aceitam e retornam apenas DTOs, alinhando a API ao padrão moderno e desacoplando o domínio da camada web.
	- **Novo contrato de resposta:** O endpoint de criação de pedido (`POST /pedidos`) agora retorna um objeto `cliente` (com `id` e `nome`) dentro do `PedidoResponse`, e não apenas o campo `clienteId`. Isso garante maior clareza e aderência ao padrão REST.
- [x] **Campo enderecoEntrega** adicionado ao modelo Pedido, com mapeamento JPA e integração total com DTOs e controller.
- [x] Métodos utilitários de mapeamento implementados no controller para conversão entre entidades e DTOs.
- [x] Enum StatusPedido implementado para status de pedidos, eliminando uso de String.
- [x] Modelos de domínio revisados e enriquecidos (Pedido, Produto, ItemPedido, Cliente, Restaurante, Endereco).
- [x] Repositórios atualizados com métodos customizados e queries otimizadas.
- [x] Controladores e DataLoader adaptados para novas assinaturas e tipos.
- [x] Testes automatizados revisados e compatíveis com as novas estruturas.
- [x] Build Maven com empacotamento Spring Boot (repackage) para geração de fat jar executável.
- [x] Collection Postman para testes de API.

---


## 🏗️ Estrutura do Projeto

```text
delivery-api-rabay/
├── src/
│   ├── main/
│   │   ├── java/com/deliverytech/delivery_api/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── DeliveryApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/deliverytech/delivery_api/
│           ├── repository/
│           ├── service/
│           └── DeliveryApiApplicationTests.java
├── entregaveis/
│   ├── delivery-api-rabay.postman_collection.json
│   └── README-atividade4.md
├── pom.xml
├── README.md
└── ...
```

---

## 📊 Estratégia de Inicialização de Dados

A aplicação utiliza uma estratégia dupla para inicialização de dados, separando responsabilidades entre autenticação JWT e dados de exemplo:

### 1. UserDataLoader.java - Usuários JWT

**Localização:** `src/main/java/com/deliverytech/delivery_api/config/UserDataLoader.java`

**Responsabilidade:** Criação automática de usuários para sistema de autenticação JWT.

**Dados criados:**
- **Admin:** `admin@deliveryapi.com` / `admin123` (Role: ADMIN)
- **Cliente:** `cliente@test.com` / `cliente123` (Role: CLIENTE) 
- **Restaurante:** `restaurante@test.com` / `restaurante123` (Role: RESTAURANTE)
- **Entregador:** `entregador@test.com` / `entregador123` (Role: ENTREGADOR)

**Características:**
- Executa apenas se não existirem usuários no banco (`usuarioRepository.count() == 0`)
- Utiliza `@Order(100)` para executar após outros data loaders
- Configurado com `@Profile({"!test", "dev", "default"})` - não executa em testes
- Senhas são automaticamente criptografadas com `PasswordEncoder`
- Execução transacional para garantir integridade

### 2. data.sql - Dados de Exemplo

**Localização:** `src/main/resources/data.sql`

**Responsabilidade:** Criação de dados de exemplo para clientes, restaurantes, produtos e pedidos.

**Dados criados:**
- **5 Clientes:** João Silva, Maria Santos, Pedro Oliveira, Ana Costa, Carlos Ferreira
- **5 Restaurantes:** Pizza Express, Burger King, Sushi House, Gyros Athenas, Chiparia do Porto
- **10 Produtos:** Variados por categoria e restaurante
- **3 Pedidos:** Com diferentes status (CRIADO, ENTREGUE, CANCELADO)
- **Itens de pedido:** Relacionamentos entre pedidos e produtos

**Características:**
- Utiliza `MERGE INTO` para operações idempotentes
- Dados preservam integridade referencial
- Configurado para executar após criação do schema (`spring.jpa.defer-datasource-initialization=true`)
- Atualmente desabilitado via `spring.sql.init.mode=never` para evitar conflitos

### 3. Configuração Atual

```properties
# application.properties
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=never  # data.sql desabilitado
```

**Status:** UserDataLoader ativo, data.sql desabilitado para evitar duplicação.

### 4. Como Adicionar Novos Dados

#### Para Usuários JWT:
1. **Modifique UserDataLoader.java** para adicionar novos usuários padrão
2. Adicione no método `loadDefaultUsers()` utilizando o builder pattern:
```java
Usuario.builder()
    .nome("Novo Usuario")
    .email("novo@example.com")
    .senha(passwordEncoder.encode("senha123"))
    .role(Role.CLIENTE)
    .ativo(true)
    .dataCriacao(LocalDateTime.now())
    .build()
```

#### Para Dados de Exemplo:
1. **Opção A - Via data.sql:**
   - Modifique `src/main/resources/data.sql`
   - Habilite execução: `spring.sql.init.mode=always`
   - Use sempre `MERGE INTO` para idempotência

2. **Opção B - Via DataLoader dedicado:**
   - Crie novo `@Component` implementando `CommandLineRunner`
   - Configure `@Order(200)` para executar após UserDataLoader
   - Use `@Profile("dev")` para controlar ambiente

#### Para Dados de Produção:
1. **Utilize migrations:** Flyway ou Liquibase para versionamento
2. **Scripts de deploy:** Separados por ambiente
3. **APIs administrativas:** Para criação controlada via endpoints

### 5. Evolução Histórica

- **Versão anterior:** DataLoader.java único com múltiplas responsabilidades
- **Problema:** Misturava criação de usuários JWT com dados de exemplo
- **Refatoração:** Separação de responsabilidades em componentes específicos
- **Benefício:** Maior controle, testabilidade e flexibilidade por ambiente

### 6. Recomendações

✅ **Mantenha separação:** Usuários JWT vs. dados de negócio  
✅ **Use profiles:** Controle execução por ambiente  
✅ **Operações idempotentes:** MERGE INTO ou verificações de existência  
✅ **Versionamento:** Para mudanças em dados críticos  
✅ **Transações:** Para operações que requerem integridade  

---


## 🏃‍♂️ Como Executar, Testar e Usar CI/CD


### 1. Pré-requisitos

- JDK 21 instalado
- Docker instalado (para build de imagem e uso do compose)
- [act](https://github.com/nektos/act) instalado (opcional, para simular o workflow localmente)

### 2. Clone o repositório

```bash
git clone https://github.com/rabay/delivery-api-rabay.git
cd delivery-api-rabay
```

### 3. Build, Testes e Cobertura Local

```bash
./mvnw clean verify
# Relatório Jacoco: target/site/jacoco/index.html
```

### 4. Executar aplicação localmente

```bash
./mvnw spring-boot:run
```

### 5. Build da imagem Docker

```bash
docker build -t delivery-api-rabay:latest .
```

### 6. Executar com Docker Compose

```bash
docker compose up --build
# ou
docker-compose up --build
```

### 7. Executar workflow CI/CD localmente (opcional)

```bash
act push
```

> O workflow executa build, testes, cobertura Jacoco, Dependency-Check e build da imagem Docker, mas **não publica binários nem imagens**.

### 8. Verificação de Dependências (OWASP Dependency-Check)

#### a) Execução Local (CLI)

```bash
# Defina sua NVD API Key (obrigatório):
export NVD_API_KEY=seu_token_nvd

# Execute o script Python (recomendado: com Central Analyzer desabilitado para evitar erros de rede):
python scripts/run_dependency_check.py
# Por padrão, o script já executa com --disableCentral, evitando timeouts ao acessar o Maven Central.
# Relatórios HTML e XML gerados em: dependency-check-report/
```

#### b) Execução no CI/CD (GitHub Actions)


O workflow já executa o Dependency-Check automaticamente, publica o relatório como artefato (`dependency-check-report`) **e gera um summary em Markdown** (exibido no painel do GitHub Actions, igual ao Jacoco).

O summary traz:
- Total de dependências analisadas
- Quantidade de dependências vulneráveis
- Tabela de vulnerabilidades por severidade
- Link para o relatório HTML completo

### 9. Acesse endpoints básicos

- Health: [http://localhost:8080/health](http://localhost:8080/health)
- Info: [http://localhost:8080/info](http://localhost:8080/info)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
	- JDBC URL: `jdbc:h2:mem:testdb` | User: `sa` | Senha: (em branco)

#### Endpoints de inspeção do banco (desenvolvimento)

Adicionalmente, a aplicação expõe endpoints de inspeção do banco para uso em ambiente de desenvolvimento. Eles consultam o H2 em memória a partir da própria JVM da aplicação e são úteis quando o console externo não consegue acessar a mesma instância "in-memory".

- GET `/db/schema` — lista tabelas, constraints, contagens e metadados opcionais
- GET `/db/schema?table=NAME` — retorna colunas/metadados da tabela informada
- GET `/db/integrity` — executa um teste de inserção inválida para confirmar enforcement de FKs
- POST `/db/query` — executa apenas queries `SELECT` passadas em JSON (ex.: `{ "sql": "SELECT * FROM CLIENTE LIMIT 10" }`)

Segurança: esses endpoints são destinados ao ambiente de desenvolvimento; proteja-os com `@Profile("dev")` ou autenticação antes de expor em ambientes públicos.

### 10. Relatórios

- **Cobertura Jacoco:** `target/site/jacoco/index.html`
- **Dependency-Check:** `dependency-check-report/index.html` (local ou artefato do CI)



### Teste Manual e Automatizado (Postman/Newman)

Importe a collection Postman:

- `entregaveis/delivery-api-rabay.postman_collection.json`

#### Execução manual:
- Execute todos os endpoints para validar regras de negócio e dados de exemplo.

#### Execução automatizada (Newman):

Com a aplicação rodando, execute:

```bash
newman run entregaveis/delivery-api-rabay.postman_collection.json --reporters cli --insecure
```

**Validação automatizada:**
- Todos os requests principais possuem scripts de teste (assertions) para status code, campos obrigatórios e estrutura do corpo.
- Os fluxos de pedidos validam que o campo `cliente` está presente no retorno, além de `id`, `status` e demais campos.
- O resultado esperado é: todos os requests com status 2xx/201/204, sem falhas de assertions.

**Cobertura dos testes automatizados:**
- Criação, atualização, exclusão lógica e consulta de clientes, restaurantes, produtos e pedidos.
- Validação de soft delete e isolamento de dados de teste.
- Testes de fluxo completo de pedidos, incluindo assertions detalhados no retorno.

---

## 📦 Exemplos de Uso (Payloads)



### Criar Cliente

```json
POST /clientes
{
	"nome": "Novo Cliente",
	"email": "novo@email.com",
	"telefone": "11999999999",
	"endereco": "Rua Exemplo, 123, Centro, São Paulo, SP"
}
```

#### Exemplo de resposta (ClienteResponse)

```json
{
	"id": 1,
	"nome": "Novo Cliente",
	"email": "novo@email.com",
	"telefone": "11999999999",
	"endereco": "Rua Exemplo, 123, Centro, São Paulo, SP",
	"ativo": true,
	"excluido": false
}
```

### Criar Restaurante

```json
POST /restaurantes
{
	"nome": "Novo Restaurante",
	"categoria": "Italiana",
	"ativo": true,
	"avaliacao": 4.5
}
```

### Criar Produto

```json
POST /produtos
{
	"nome": "Produto Teste",
	"categoria": "Pizza",
	"disponivel": true,
	"restaurante": {"id": 1}
}
```



### Criar Pedido

```json
POST /pedidos
{
		"clienteId": 1,
		"restauranteId": 1,
		"enderecoEntrega": {
				"rua": "Rua Exemplo",
				"numero": "123",
				"bairro": "Centro",
				"cidade": "São Paulo",
				"estado": "SP",
				"cep": "01000-000",
				"complemento": "Apto 101"
		},
		"itens": [
				{ "produtoId": 1, "quantidade": 2 },
				{ "produtoId": 2, "quantidade": 1 }
		]
}
```

#### Exemplo de resposta do endpoint de criação de pedido

```json
{
	"id": 10,
	"cliente": {
		"id": 1,
		"nome": "João Silva"
	},
	"restauranteId": 1,
	"enderecoEntrega": {
		"rua": "Rua Exemplo",
		"numero": "123",
		"bairro": "Centro",
		"cidade": "São Paulo",
		"estado": "SP",
		"cep": "01000-000",
		"complemento": "Apto 101"
	},
	"valorTotal": 99.90,
	"status": "CRIADO",
	"dataPedido": "2025-08-22T09:00:00",
	"itens": [
		{ "produtoId": 1, "nomeProduto": "Pizza Margherita", "quantidade": 2, "precoUnitario": 30.00 },
		{ "produtoId": 2, "nomeProduto": "Pizza Pepperoni", "quantidade": 1, "precoUnitario": 39.90 }
	]
}
```


### Atualizar Cliente

```json
PUT /clientes/{id}
{
	"nome": "Cliente Atualizado",
	"email": "atualizado@email.com",
	"telefone": "11988888888",
	"endereco": "Rua Nova, 456, Centro, São Paulo, SP"
}
```

#### Exemplo de resposta (ClienteResponse)

{
	"id": 1,
	"nome": "Cliente Atualizado",
	"email": "atualizado@email.com",
	"telefone": "11988888888",
	"endereco": "Rua Nova, 456, Centro, São Paulo, SP",
	"ativo": true,
	"excluido": false
}

---

### Buscar Cliente por Email

`GET /clientes/email/{email}`

Resposta:
```json
{
	"id": 1,
	"nome": "Novo Cliente",
	"email": "novo@email.com",
	"telefone": "11999999999",
	"endereco": "Rua Exemplo, 123, Centro, São Paulo, SP",
	"ativo": true,
	"excluido": false
}
```

---

### Inativar Cliente (Soft Delete)

`DELETE /clientes/{id}`

Resposta:
```json
{
	"id": 1,
	"nome": "Novo Cliente",
	"excluido": true,
	...
}
```

---

```json
PUT /pedidos/{id}/status
{
	"status": "CONFIRMADO"
}
```

---


## 🗑️ Exclusão Lógica (Soft Delete)

Desde agosto/2025, a exclusão de clientes, restaurantes e produtos é feita por soft delete:

- As entidades possuem o campo `excluido` (Boolean, default false).
- Ao excluir (DELETE), o registro não é removido do banco, apenas marcado como `excluido=true`.
- Consultas e buscas retornam apenas registros com `excluido=false`.
- O campo `excluido` pode ser visualizado ao buscar por ID.

### Exemplo de resposta após exclusão lógica

```json
{
	"id": 1,
	"nome": "Restaurante Exemplo",
	"excluido": true,
	...
}
```

### Endpoints afetados

- `DELETE /clientes/{id}`: marca cliente como excluído
- `DELETE /restaurantes/{id}`: marca restaurante como excluído
- `DELETE /produtos/{id}`: marca produto como excluído
- Listagens e buscas ignoram registros excluídos

---



## 📋 Endpoints Principais (Cliente)

- `GET /clientes` — Lista todos os clientes ativos (retorna lista de `ClienteResponse`)
- `GET /clientes/email/{email}` — Busca cliente por email (retorna `ClienteResponse`)
- `POST /clientes` — Cria cliente (recebe `ClienteRequest`, retorna `ClienteResponse`)
- `PUT /clientes/{id}` — Atualiza cliente (recebe `ClienteRequest`, retorna `ClienteResponse`)
- `DELETE /clientes/{id}` — Inativa cliente (soft delete, retorna `ClienteResponse`)

Todos os endpoints agora utilizam DTOs para entrada e saída, garantindo desacoplamento entre domínio e API, maior segurança e facilidade de evolução do contrato.

---

- `GET /clientes`, `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}`
- `GET /restaurantes`, `POST /restaurantes`, ...
- `GET /produtos`, `POST /produtos`, ...
- `GET /pedidos/cliente/{clienteId}`: Lista pedidos de um cliente (retorna lista de PedidoResponse)
- `POST /pedidos`: Cria pedido (recebe PedidoRequest, retorna PedidoResponse)
- `PUT /pedidos/{id}/status`: Atualiza status do pedido (recebe StatusUpdateRequest, retorna PedidoResponse)
- `GET /health`, `GET /info`, `GET /h2-console`
 - `GET /clientes`, `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}`
 - `GET /restaurantes`, `POST /restaurantes`, ...
 - `GET /produtos`, `POST /produtos`, ...
 - `GET /pedidos/cliente/{clienteId}`: Lista pedidos de um cliente (retorna lista de PedidoResponse)
 - `POST /pedidos`: Cria pedido (recebe PedidoRequest, retorna PedidoResponse)
 - `PUT /pedidos/{id}/status`: Atualiza status do pedido (recebe StatusUpdateRequest, retorna PedidoResponse)
 - `GET /health`, `GET /info`, `GET /h2-console`
 - `GET /db/schema`, `GET /db/schema?table={name}`, `GET /db/integrity`, `POST /db/query` (endpoints de inspeção - ambiente dev)

---

## 📊 Endpoints de Relatórios (Analytics)

A API de relatórios expõe endpoints REST para consultas analíticas sobre vendas, produtos, clientes e faturamento, utilizando projeções e consultas otimizadas.

Base URL: `/api/relatorios`

### 1. Vendas por Restaurante
- **GET** `/api/relatorios/vendas-por-restaurante`
- **Parâmetros:**
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
- **Resposta:**
	- Lista de objetos:
		- `nomeRestaurante` (String)
		- `totalVendas` (BigDecimal)
		- `quantidadePedidos` (Long)

### 2. Produtos Mais Vendidos
- **GET** `/api/relatorios/produtos-mais-vendidos`
- **Parâmetros:**
	- `limite` (int, opcional, padrão 5)
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
- **Resposta:**
	- Lista de objetos:
		- `idProduto` (Long)
		- `nomeProduto` (String)
		- `totalVendas` (BigDecimal)
		- `quantidadeItemPedido` (Long)

### 3. Clientes Ativos (Ranking)
- **GET** `/api/relatorios/clientes-ativos`
- **Parâmetros:**
	- `limite` (int, opcional, padrão 10)
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
- **Resposta:**
	- Lista de objetos:
		- `idCliente` (Long)
		- `nomeCliente` (String)
		- `totalCompras` (BigDecimal)
		- `quantidadePedidos` (Long)

### 4. Pedidos por Período e Status
- **GET** `/api/relatorios/pedidos-por-periodo`
- **Parâmetros:**
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
	- `status` (String, obrigatório)
- **Resposta:**
	- Lista de objetos:
		- `periodo` (String)
		- `status` (String)
		- `totalPedidos` (int)

### 5. Faturamento por Categoria
- **GET** `/api/relatorios/faturamento-por-categoria`
- **Parâmetros:**
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
- **Resposta:**
	- Lista de objetos:
		- `categoria` (String)
		- `totalFaturado` (BigDecimal)

### 6. Resumo Geral de Vendas
- **GET** `/api/relatorios/resumo-vendas`
- **Parâmetros:**
	- `inicio` (yyyy-MM-dd, obrigatório)
	- `fim` (yyyy-MM-dd, obrigatório)
- **Resposta:**
	- Objeto:
		- `totalPedidos` (int)
		- `valorTotalVendas` (double)

#### Observações
- Todos os endpoints retornam dados em formato JSON.
- Datas devem ser informadas no padrão ISO (yyyy-MM-dd).
- Os relatórios utilizam projeções para otimizar a consulta e trafegar apenas os campos necessários.
- Para mais detalhes sobre as projeções, consulte o pacote `com.deliverytech.delivery_api.projection`.

*Documentação gerada automaticamente conforme implementação do módulo de relatórios.*

---

## 🔧 Configuração

- Porta: 8080
- Banco: H2 em memória
- Profile: development

---

## 👨‍💻 CI/CD e Política de Artefatos

- O pipeline GitHub Actions executa build, testes, cobertura Jacoco e build Docker.
- **Apenas o relatório de cobertura é salvo como artefato.**
- Nenhum JAR/WAR ou imagem Docker é publicado pelo workflow.
- O build Docker é feito apenas para validação.
- Para publicar imagens, configure um job/passo extra conforme sua necessidade.

---

## 🚦 Padrão para consultas com relacionamentos LAZY (fetch join)

- Para evitar erros de LazyInitializationException ao acessar coleções LAZY (ex: Pedido.itens) fora do contexto de sessão do Hibernate, foi implementado o método customizado no PedidoRepository usando @Query com fetch join:

```java
@Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto")
List<Pedido> findAllWithItens();
```

- O DataLoader utiliza esse método para validar os relacionamentos e garantir que os itens dos pedidos estejam carregados corretamente, mesmo em contexto de inicialização ou testes.
- Sempre que for necessário acessar coleções LAZY fora do controller/service, recomenda-se criar métodos com fetch join no repositório correspondente.

---

## �️ Troubleshooting

- **Erro de espaço em disco:** Limpe caches Maven (`rm -rf ~/.m2/repository`), imagens/containers Docker não utilizados (`docker system prune -af`), arquivos temporários e extensões antigas do VSCode.
- **Docker não encontrado:** Certifique-se de que o Docker está instalado e o serviço está ativo.
- **Problemas com dependências Maven:** Rode `./mvnw dependency:purge-local-repository` e depois `./mvnw clean verify`.
- **Relatório Jacoco não gerado:** Verifique se os testes estão passando e se a pasta `target/site/jacoco` existe após o build.
- **act não encontrado:** Instale o act conforme a [documentação oficial](https://github.com/nektos/act).

- README.md atualizado
- Collection Postman (`entregaveis/delivery-api-rabay.postman_collection.json`)
- Aplicação pronta para demonstração
- Documentação completa

---

## 📝 Organização dos Commits

Commits organizados por feature, correção e entregáveis. Veja histórico no repositório GitHub.

---

## 🚀 Preparação para Demonstração

1. Faça build e teste local (`./mvnw clean verify`)
2. Gere e visualize o relatório de cobertura (`target/site/jacoco/index.html`)
3. Execute o workflow localmente com act, se desejar simular o CI
4. Use Docker Compose para rodar a stack completa
5. Consulte este README para exemplos e instruções

1. Certifique-se que a aplicação está rodando (`./mvnw spring-boot:run`)
2. Execute a collection Postman ou o comando Newman
3. Valide os dados no H2 Console
4. Consulte este README para exemplos e instruções

## Desenvolvido por:

Victor Alexandre Rabay - TI 58A 02728 - Arquitetura de Sistemas
Desenvolvido com JDK 21 e Spring Boot 3.2.x