# Delivery API

Uma API REST para sistema de delivery desenvolvida com Spring Boot.

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (para desenvolvimento)
- **Maven**

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6 ou superior

## Como executar

1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd delivery-api
```

2. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

Ou no Windows:
```cmd
mvnw.cmd spring-boot:run
```

3. A aplicação estará disponível em: `http://localhost:8080`

## Compilar o projeto

Para compilar o projeto:
```bash
./mvnw clean compile
```

## Executar testes

Para executar os testes:
```bash
./mvnw test
```

## Gerar o JAR

Para gerar o arquivo JAR:
```bash
./mvnw clean package
```

O arquivo JAR será gerado na pasta `target/`.

## Banco de dados

O projeto utiliza o banco H2 em memória para desenvolvimento. O console do H2 pode ser acessado em:
`http://localhost:8080/h2-console`

## Estrutura do projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/deliverytech/delivery_api/
│   │       └── DeliveryApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/deliverytech/delivery_api/
            └── DeliveryApiApplicationTests.java
```

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request
