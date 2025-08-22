---
post_title: "Delivery API Rabay - Backend Spring Boot"
author1: "Victor Alexandre Rabay"
post_slug: "delivery-api-rabay"
microsoft_alias: "victorrabay"
featured_image: ""
categories: ["backend", "spring-boot", "java", "delivery"]
tags: ["spring-boot", "java", "api", "delivery", "docker", "maven"]
ai_note: "Este README foi gerado com auxílio de IA."
summary: "API backend moderna para sistemas de delivery, construída com Java 21 e Spring Boot 3.2.x, seguindo padrões de arquitetura, segurança e testes profissionais."
post_date: "2025-08-22"
---


## Projeto: Delivery API Rabay

API backend desenvolvida em Java 21 com Spring Boot 3.2.x, servindo como base robusta e escalável para sistemas de delivery. O projeto oferece estrutura para cadastro de usuários, restaurantes, catálogo de produtos, gestão de pedidos, integração com pagamentos e monitoramento via Actuator.

## Stack Tecnológica

- **Java 21** (LTS)
- **Spring Boot 3.2.x**
- **Maven**
- **Spring MVC, Spring Data JPA, Spring Security**
- **H2 Database** (dev/test)
- **Thymeleaf** (templates)
- **Docker** (multi-stage)



## Arquitetura do Projeto

- Arquitetura baseada em camadas (Controller, Service, Repository)
- Separação de domínios por agregados (Order, Restaurant, Customer)
- Uso de Value Objects (Money, Address)
- Eventos de domínio (@DomainEvents)
- RBAC com Spring Security (ROLE_CUSTOMER, ROLE_RESTAURANT_OWNER, ROLE_ADMIN)
- Observabilidade com Micrometer e logs estruturados (MDC)



## Como Começar

1. **Pré-requisitos:**
   - Java 21+
   - Maven 3.9+
   - Docker (opcional para containerização)

2. **Build local:**

   ```bash
   mvn clean package
   java -jar target/delivery-api-0.0.1-SNAPSHOT.jar
   ```

3. **Rodando com Docker:**

   ```bash
   docker build -t delivery-api:latest .
   docker run --rm -p 8080:8080 delivery-api:latest
   ```

4. **Acesso:**
   - API: [http://localhost:8080/](http://localhost:8080/)
   - Health: [http://localhost:8080/health](http://localhost:8080/health)
   - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Estrutura de Pastas

- `src/main/java` - Código fonte principal
- `src/main/resources` - Configurações e templates
- `src/test/java` - Testes unitários e de integração
- `Dockerfile` - Build multi-stage otimizado
- `pom.xml` - Gerenciamento de dependências

## Principais Funcionalidades

- Cadastro e autenticação de usuários
- Gerenciamento de restaurantes e produtos
- Gestão de pedidos (criação, atualização, consulta)
- Integração com gateways de pagamento (idempotência)
- Métricas customizadas e monitoramento
- Segurança RBAC e proteção de endpoints sensíveis

## Fluxo de Desenvolvimento

- Branch principal: `main`
- Commits seguindo Conventional Commits
- PRs revisados com foco em segurança, testes e padrões
- Integração contínua recomendada com análise estática e testes automatizados

## Padrões de Código

- Java: uso de records para DTOs, imutabilidade, streams, Optional
- Spring Boot: injeção por construtor, services stateless, controllers finos
- Nomenclatura seguindo Google Java Style
- Evitar magic numbers, duplicidade e métodos longos

## Testes

- Testes unitários com JUnit 5 e Mockito
- Estrutura de testes espelhando pacotes de produção
- Cobertura mínima recomendada: 80%
- Testes de integração com banco real (opcional)
- Execução: `mvn test`

## Contribuindo

- Siga os padrões de código e exemplos do projeto
- Adicione testes para novas funcionalidades
- Documente endpoints e regras de negócio relevantes
- Use PRs para propor melhorias

## Licença

Este projeto é open-source e segue a licença MIT.

## Sobre

Victor Alexandre Rabay - TI 58A 02728 - Arquitetura de Sistemas  
Desenvolvido com JDK 21 e Spring Boot 3.2.x
