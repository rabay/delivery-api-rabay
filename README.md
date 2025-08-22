
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

- [x] Refatoração completa dos serviços seguindo padrão interface/implementação, alinhado a projeto de referência
- [x] **Padronização de transações:** Todos os serviços (Cliente, Produto, Restaurante, Pedido) agora utilizam `@Transactional` no nível de classe e `@Transactional(readOnly = true)` nos métodos de leitura, conforme boas práticas do projeto de referência. Isso garante integridade transacional, melhor performance em consultas e alinhamento com padrões Spring modernos.
- [x] Criação e uso de DTOs para requisições e respostas (ex: ClienteRequest, RestauranteRequest, ItemPedidoRequest)
- [x] Enum StatusPedido implementado para status de pedidos, eliminando uso de String
- [x] Modelos de domínio revisados e enriquecidos (Pedido, Produto, ItemPedido, Cliente, Restaurante)
- [x] Repositórios atualizados com métodos customizados e queries otimizadas
- [x] Controladores e DataLoader adaptados para novas assinaturas e tipos
- [x] Testes automatizados revisados e compatíveis com as novas estruturas
- [x] Build Maven com empacotamento Spring Boot (repackage) para geração de fat jar executável
- [x] Collection Postman para testes de API

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

### 10. Relatórios

- **Cobertura Jacoco:** `target/site/jacoco/index.html`
- **Dependency-Check:** `dependency-check-report/index.html` (local ou artefato do CI)


### Teste Manual

Importe a collection Postman:

- `entregaveis/delivery-api-rabay.postman_collection.json`
- Execute todos os endpoints para validar regras de negócio e dados de exemplo.


### Teste Automatizado (Newman)

Com a aplicação rodando, execute:

```bash
newman run entregaveis/delivery-api-rabay.postman_collection.json --reporters cli --insecure
```

Saída esperada: todos os requests com status 2xx/201/204, sem falhas.

---

## 📦 Exemplos de Uso (Payloads)


### Criar Cliente

```json
POST /clientes
{
	"nome": "Novo Cliente",
	"email": "novo@email.com",
	"ativo": true
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
	"cliente": {"id": 1}
}
```

---

## 📋 Endpoints Principais

- `GET /clientes`, `POST /clientes`, `PUT /clientes/{id}`, `DELETE /clientes/{id}`
- `GET /restaurantes`, `POST /restaurantes`, ...
- `GET /produtos`, `POST /produtos`, ...
- `GET /pedidos/cliente/{id}`, `POST /pedidos`, ...
- `GET /health`, `GET /info`, `GET /h2-console`

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


Victor Alexandre Rabay - TI 58A 02728 - Arquitetura de Sistemas
Desenvolvido com JDK 21 e Spring Boot 3.2.x