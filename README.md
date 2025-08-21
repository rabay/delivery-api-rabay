
# 🚀 Delivery API Rabay

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

## �️ Tecnologias e Ferramentas

- **Java 21 LTS**
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database (memória)
- Maven
- Spring DevTools

---

## 📈 Status do Projeto

✅ Aplicação funcional, endpoints REST testados via Postman/Newman, DataLoader populando H2 e testes automatizados presentes.

---

## ✅ Funcionalidades Implementadas

- [x] CRUD completo para Cliente, Restaurante, Produto, Pedido
- [x] Consultas customizadas nos repositórios
- [x] DataLoader para carga de dados de teste
- [x] Testes automatizados de repositório e serviço
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

## 🏃‍♂️ Como Executar e Testar


1. **Pré-requisitos:** JDK 21 instalado

2. Clone o repositório:

```bash
git clone https://github.com/rabay/delivery-api-rabay.git
cd delivery-api-rabay
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

4. Acesse endpoints básicos:

- Health: [http://localhost:8080/health](http://localhost:8080/health)
- Info: [http://localhost:8080/info](http://localhost:8080/info)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
	- JDBC URL: `jdbc:h2:mem:testdb` | User: `sa` | Senha: (em branco)

---

## 🧪 Instruções de Teste (API)


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

## 👨‍💻 Desenvolvedor

Victor Alexandre Rabay - TI 58A 02728 - Arquitetura de Sistemas
Desenvolvido com JDK 21 e Spring Boot 3.2.x

---

## 📦 Entregáveis

- README.md atualizado
- Collection Postman (`entregaveis/delivery-api-rabay.postman_collection.json`)
- Aplicação pronta para demonstração
- Documentação completa

---

## 📝 Organização dos Commits

Commits organizados por feature, correção e entregáveis. Veja histórico no repositório GitHub.

---

## 🚀 Preparação para Demonstração

1. Certifique-se que a aplicação está rodando (`./mvnw spring-boot:run`)
2. Execute a collection Postman ou o comando Newman
3. Valide os dados no H2 Console
4. Consulte este README para exemplos e instruções
