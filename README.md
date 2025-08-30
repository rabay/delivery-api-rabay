# Delivery API Rabay

[![Build, Testes e Coverage Java Maven](https://github.com/rabay/delivery-api-rabay/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/rabay/delivery-api-rabay/actions/workflows/build.yml)

> Projeto base em Java 21 com Spring Boot para sistemas de delivery, pensado para desenvolvimento rápido, testes e evolução.

## Visão geral

- Gestão de clientes, restaurantes, produtos e pedidos
- Contratos claros via DTOs
- Exclusão lógica (soft delete) nas entidades principais
- Inicialização de dados para desenvolvimento (UserDataLoader / data.sql)
- Suíte de testes automatizados e collection Postman

## Tecnologias

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- MySQL (produção/testes)
- Maven (wrapper), Docker, Docker Compose
- GitHub Actions, Jacoco

## Quick start

Pré-requisitos: JDK 21 e Maven; Docker é opcional.

Build, executar testes e gerar relatório de cobertura:

```bash
./mvnw clean verify
# relatório Jacoco: target/site/jacoco/index.html
```

Executar localmente:

```bash
./mvnw spring-boot:run
```

Build de imagem Docker:

```bash
docker build -t delivery-api-rabay:latest .
```

Executar com Docker Compose:

```bash
docker compose up --build
```

Executar a collection Postman (Newman):

```bash
newman run entregaveis/delivery-api-rabay.postman_collection.json --reporters cli --insecure
```

## Inicialização de dados

- `UserDataLoader` (src/main/java/.../config/UserDataLoader.java): cria contas padrão (admin, cliente, restaurante, entregador) em perfis de desenvolvimento quando o repositório de usuários estiver vazio.
- `src/main/resources/data.sql`: script idempotente (MERGE INTO) com dados de exemplo; pode estar desabilitado via `spring.sql.init.mode`.

Recomendação: use `UserDataLoader` para desenvolvimento e migrações (Flyway/Liquibase) em produção.

## Endpoints principais

- Health: `GET /health`
- Info: `GET /info`

Recursos principais:

- Clientes: `GET/POST/PUT/DELETE /clientes` (DTOs)
- Restaurantes: `GET/POST/PUT/DELETE /restaurantes`
- Produtos: `GET/POST/PUT/DELETE /produtos`
- Pedidos: `POST /pedidos`, `GET /pedidos/cliente/{clienteId}`, `PUT /pedidos/{id}/status`

Endpoints de inspeção (ambiente dev — proteja com perfil/autenticação):

- `GET /db/schema` — metadados do schema
- `GET /db/integrity` — teste rápido de integridade
- `POST /db/query` — executa apenas `SELECT` em JSON (use com cuidado)

## Testes

- Unit e Integration: Maven Surefire/Failsafe
- E2E: Postman collection + Newman

## Padrões de código

- Use features de Java 21 quando fizer sentido
- Separe interfaces e implementações de serviços
- Use DTOs para contratos de API
- Aplique `@Transactional` nos serviços e `readOnly` para métodos de leitura
- Documente APIs públicas com JavaDoc

## Troubleshooting rápido

- Erro de conexão MySQL: verifique containers, credenciais e `application.yml`
- Porta ocupada: ajuste `server.port`
- Testes falhando: execute `./mvnw -DskipTests=false test` para ver detalhes

## Recursos

- Spring Boot: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- Spring Data JPA: [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)
- GitHub Actions: [https://docs.github.com/en/actions](https://docs.github.com/en/actions)

---

Desenvolvido por Victor Alexandre Rabay
