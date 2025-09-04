# 🚀 Delivery API Rabay

API backend desenvolvida em **Java 21** com **Spring Boot 3.x**, servindo como base para sistemas de delivery modernos, escaláveis e de fácil manutenção.

---

## 📌 Visão Geral

Este projeto oferece uma estrutura robusta para aplicações de delivery, incluindo:

- Cadastro e gestão de clientes, restaurantes, produtos e pedidos
- Contratos claros via DTOs
- Exclusão lógica (soft delete) nas entidades principais
- Inicialização de dados para desenvolvimento
- Suíte de testes automatizados e collections Postman
- Monitoramento e métricas via Actuator

---

## 🛠️ Tecnologias e Ferramentas

- **Java 21** – última versão LTS
- **Spring Boot 3.x** – APIs REST com configuração mínima
- **Spring Data JPA** – persistência de dados
- **Spring Security** – autenticação e autorização
- **MySQL** – banco de dados para produção/testes
- **Maven** – build e dependências (wrapper incluído)
- **Docker & Docker Compose** – ambiente containerizado
- **GitHub Actions** – CI/CD
- **Jacoco** – cobertura de testes

---

## 📈 Status do Projeto

✅ **Funcional**
> Build, testes e cobertura automatizados. Dados de exemplo carregados e endpoints principais disponíveis.

---

## ✅ Funcionalidades Implementadas

- [x] Estrutura inicial com Spring Boot
- [x] Modelos de domínio (Cliente, Restaurante, Produto, Pedido)
- [x] Repositórios com consultas customizadas
- [x] DataLoader e scripts SQL para carga de dados
- [x] Exclusão lógica (soft delete)
- [x] Testes unitários, integração e E2E (Postman/Newman)
- [x] Dockerfile e docker-compose para ambiente local
- [x] Monitoramento com Actuator

---

## 🚀 Como Executar

⚠️ **IMPORTANTE:** A aplicação depende de uma instância MySQL provisionada. Recomenda-se executar via Docker Compose para garantir o ambiente completo.

1. **Clone o repositório**
2. **Execute a aplicação e o banco de dados com Docker Compose:**

   ```bash
   docker compose up --build
   ```

3. **(Opcional) Execute a collection Postman (Newman):**

    ```bash
    newman run entregaveis/delivery-api-rabay.postman_collection.json \
       --environment entregaveis/delivery-api-rabay.postman_environment.json \
       --reporters cli --insecure
    ```

---

## 🗂️ Estrutura do Projeto

```text
src/
├── main
│   ├── java
│   │   └── com
│   │       └── deliverytech
│   │           └── delivery_api
│   │               ├── config
│   │               ├── controller
│   │               ├── dto
│   │               │   ├── request
│   │               │   └── response
│   │               ├── exception
│   │               ├── filter
│   │               ├── mapper
│   │               │   ├── converters
│   │               │   └── typemaps
│   │               ├── model
│   │               ├── projection
│   │               ├── repository
│   │               ├── security
│   │               ├── service
│   │               │   └── impl
│   │               └── validation
│   └── resources
│       ├── static
│       └── templates
└── test
   ├── java
   │   └── com
   │       └── deliverytech
   │           └── delivery_api
   │               ├── config
   │               ├── controller
   │               ├── dto
   │               │   └── response
   │               ├── filter
   │               ├── mapper
   │               │   ├── converters
   │               │   └── typemaps
   │               ├── model
   │               ├── repository
   │               ├── security
   │               ├── service
   │               │   └── impl
   │               ├── test
   │               ├── util
   │               └── validation
   └── resources
      └── mockito-extensions
```

---

## 🔗 Endpoints Principais


- **Healthcheck & Info:**
   - `GET /health` — status da aplicação
   - `GET /info` — informações do build

- **Autenticação e Usuários:**
   - `POST /auth/login` — autenticação de usuário
   - `POST /auth/register` — cadastro de novo usuário
   - `GET /usuarios` — listar usuários (admin)
   - `GET /usuarios/{id}` — detalhes do usuário

- **Clientes:**
   - `GET /clientes` — listar clientes
   - `POST /clientes` — criar cliente
   - `PUT /clientes/{id}` — atualizar cliente
   - `DELETE /clientes/{id}` — exclusão lógica

- **Restaurantes:**
   - `GET /restaurantes` — listar restaurantes
   - `POST /restaurantes` — criar restaurante
   - `PUT /restaurantes/{id}` — atualizar restaurante
   - `DELETE /restaurantes/{id}` — exclusão lógica

- **Produtos:**
   - `GET /produtos` — listar produtos
   - `POST /produtos` — criar produto
   - `PUT /produtos/{id}` — atualizar produto
   - `DELETE /produtos/{id}` — exclusão lógica

- **Pedidos:**
   - `POST /pedidos` — criar pedido
   - `GET /pedidos/cliente/{clienteId}` — pedidos de um cliente
   - `GET /pedidos/{id}` — detalhes do pedido
   - `PUT /pedidos/{id}/status` — atualizar status do pedido

- **Consultas Customizadas e Relatórios:**
   - `GET /produtos/mais-vendidos` — top 5 produtos mais vendidos
   - `GET /clientes/ranking` — ranking de clientes por pedidos
   - `GET /restaurantes/relatorio-vendas` — relatório de vendas por restaurante
   - `GET /pedidos/valor-acima/{valor}` — pedidos acima de determinado valor
   - `GET /pedidos/periodo?inicio=...&fim=...&status=...` — pedidos por período e status

- **Inspeção e Integração:**
   - `GET /db/schema` — metadados do schema (dev)
   - `GET /db/integrity` — teste de integridade (dev)
   - `POST /db/query` — executar SELECT customizado (dev)

- **Métricas e Monitoramento:**
   - `GET /actuator/metrics` — métricas gerais
   - `GET /actuator/prometheus` — endpoint Prometheus

⚠️ **IMPORTANTE:** Endpoints de inspeção, métricas e queries customizadas estão disponíveis apenas em ambiente de desenvolvimento e/ou exigem autenticação/perfil adequado.

---

## 🛡️ Filters

O projeto implementa filtros (filters) para registrar todas as requisições e respostas HTTP, promovendo rastreabilidade e auditoria das operações da API.

- **Como funciona:**
   - Cada request e response é interceptada por um filter customizado.
   - Informações como método, endpoint, status, payload e timestamp são registradas.
   - Os logs são organizados por domínio (clientes, pedidos, produtos, restaurantes, autenticação etc).

- **Locais dos logs:**
   - Em ambiente de produção/desenvolvimento: `logs/`
   - Em testes automatizados: `entregaveis/logs_test/`
   - Exemplos de arquivos gerados:
      - `logs/clientes.log`
      - `logs/pedidos.log`
      - `logs/produtos.log`
      - `logs/restaurantes.log`
      - `logs/auth.log`

- **Benefícios:**
   - Facilita troubleshooting e auditoria.
   - Permite análise de uso e detecção de padrões anômalos.
   - Segue boas práticas de observabilidade para APIs REST.

> ⚠️ **IMPORTANTE:** Certifique-se de não registrar dados sensíveis nos logs. Em produção, utilize rotação e proteção adequada dos arquivos de log.

---

## 🧪 Testes

- Unitários e integração: Maven Surefire/Failsafe
- E2E: Postman + Newman

---

## 📝 Padrões de Código

- Utilize recursos do Java 21
- Separe interfaces e implementações de serviços
- Use DTOs para contratos de API
- Aplique `@Transactional` nos serviços
- Documente APIs públicas com JavaDoc

---

## 🛠️ Troubleshooting Rápido

- Erro de conexão MySQL: verifique containers, credenciais e `application.yml`
- Porta ocupada: ajuste `server.port`
- Testes falhando: execute `./mvnw -DskipTests=false test` para detalhes

---

## 📚 Recursos Úteis

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [GitHub Actions](https://docs.github.com/en/actions)

---

Desenvolvido por Victor Alexandre Rabay para o curso de Arquitetura de Sistemas (turma T158 02728 de 2025).
