# Documentação Swagger / OpenAPI — Delivery API Rabay

Este arquivo documenta como acessar e usar a documentação interativa (Swagger UI) desta API, e reúne o que foi implementado conforme a entrega ENTREGAS/ENTREGA-07.

## ✅ Acesso ao Swagger UI (ambiente de desenvolvimento)
- **URL do Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Endpoint dos docs OpenAPI (JSON)**: http://localhost:8080/api-docs
- **Status**: ✅ Funcionando e acessível

> **Observação**: As propriedades que controlam os caminhos estão em `src/main/resources/application.properties`:
```
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

## ✅ Dependência usada
O projeto utiliza o starter do Springdoc para Spring MVC:
- `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0` (ver `pom.xml`).

## ✅ Localização da configuração OpenAPI
A configuração principal do OpenAPI está em:
- `src/main/java/com/deliverytech/delivery_api/config/OpenApiConfig.java`

Esta classe define as informações completas da API:
- **Title**: "Delivery API Rabay"
- **Version**: "1.0"
- **Description**: "API pública para operações de delivery: cadastro de clientes, restaurantes, produtos e pedidos. Documentação detalhada para integração e testes."
- **Contact**: Equipe Rabay Delivery (contato@rabay.com.br)
- **License**: MIT License
- **Servers**: http://localhost:8080

## ✅ Autenticação / JWT no Swagger
- ✅ **SecurityScheme HTTP Bearer JWT** implementado em `OpenApiConfig.java`
- ✅ **SecurityRequirement global** aplicado a todos os endpoints
- ✅ **Botão "Authorize"** disponível no Swagger UI para fornecer token Bearer
- ✅ **Testado**: Login funcionando com credenciais `admin@deliveryapi.com` / `admin123`

## ✅ Controllers e documentação existente
Controllers com anotações completas:
- ✅ `AuthController` - Autenticação e cadastro de usuários
- ✅ `ClienteController` - Operações de clientes
- ✅ `RestauranteController` - Gerenciamento de restaurantes
- ✅ `ProdutoController` - Cadastro e gerenciamento de produtos
- ✅ `PedidoController` - Criação e atualização de pedidos
- ✅ `RelatorioController` - Relatórios e estatísticas
- ✅ `HealthController` - Verificações de saúde
- ✅ `DbController` - Operações de administração do banco

## ✅ Exemplos e respostas padronizadas
- ✅ `@ApiResponses` com códigos HTTP e exemplos implementados nos controllers críticos
- ✅ `@ExampleObject` com exemplos JSON detalhados em DTOs de pedido
- ✅ `@Schema` com exemplos em DTOs principais
- ✅ Exemplos de sucesso e erro documentados

## ✅ Validações
- ✅ Validações Bean Validation (JSR-380) implementadas
- ✅ Validators personalizados para email, CEP, etc.
- ✅ Validações aparecem automaticamente no OpenAPI

## ✅ O que está implementado (resumo completo)
- ✅ Dependência `springdoc-openapi-starter-webmvc-ui` v2.7.0
- ✅ Propriedades Springdoc configuradas em `application.properties`
- ✅ `OpenApiConfig.java` com metadata completa e SecurityScheme JWT
- ✅ Controllers principais anotados com `@Tag`, `@Operation`, `@ApiResponses`
- ✅ DTOs com `@Schema` e exemplos estruturados
- ✅ Infra de JWT integrada ao OpenAPI
- ✅ Aplicação testada e funcionando com Docker Compose
- ✅ Endpoints protegidos funcionando com autenticação Bearer

## 📋 Status da implementação
**Phase 1** ✅ SecurityScheme JWT - COMPLETA
**Phase 2** ✅ ApiResponses em endpoints críticos - COMPLETA  
**Phase 3** ✅ Exemplos em DTOs - COMPLETA
**Phase 4** ✅ Testes e validação - COMPLETA

## 🧪 Testes realizados
- ✅ Build Docker bem-sucedido
- ✅ Aplicação iniciada com Docker Compose
- ✅ Health check: `{"status":"UP"}`
- ✅ Login JWT: Credenciais válidas retornam token
- ✅ Endpoints protegidos: Funcionam com Authorization Bearer
- ✅ Listagem de restaurantes: Retorna dados paginados
- ✅ Listagem de produtos: Retorna dados com relacionamentos

## � Implementações realizadas

### 1. Configuração OpenAPI (OpenApiConfig.java)
```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Delivery API Rabay")
            .version("1.0")
            .description("API pública para operações de delivery...")
            .contact(new Contact().name("Equipe Rabay Delivery").email("contato@rabay.com.br"))
            .license(new License().name("MIT License")))
        .servers(List.of(new Server().url("http://localhost:8080")))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", 
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .security(List.of(new SecurityRequirement().addList("bearerAuth")));
}
```

### 2. Controllers com @ApiResponses
**AuthController.java:**
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
        content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\"data\":{\"token\":\"eyJ...\"},\"message\":\"Login realizado\",\"success\":true}"))),
    @ApiResponse(responseCode = "400", description = "Request inválido"),
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
})
```

**PedidoController.java:**
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
    @ApiResponse(responseCode = "400", description = "Request inválido ou erro de negócio"),
    @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
```

### 3. DTOs com exemplos (PedidoRequest.java)
```java
@Schema(description = "DTO para criação de pedido", 
        example = "{\"clienteId\":1,\"restauranteId\":2,\"enderecoEntrega\":{\"rua\":\"Rua das Flores\",\"numero\":\"123\",\"bairro\":\"Centro\",\"cidade\":\"São Paulo\",\"estado\":\"SP\",\"cep\":\"01234-567\"},\"itens\":[{\"produtoId\":5,\"quantidade\":2},{\"produtoId\":10,\"quantidade\":1}],\"desconto\":10.0}")
public class PedidoRequest {
    // ... campos com @Schema
}
```

### 4. Propriedades Springdoc (application.properties)
```properties
# Springdoc OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

## 🔐 Credenciais de teste
Para testar a autenticação no Swagger UI:
- **Email**: `admin@deliveryapi.com`
- **Senha**: `admin123`
- **Role**: ADMIN

## 📖 Como usar
1. Acesse http://localhost:8080/swagger-ui/index.html
2. Clique no botão **"Authorize"** (cadeado)
3. Digite: `Bearer <token>` (obtido do endpoint de login)
4. Teste os endpoints protegidos

## 📦 Artefatos de entrega
- ✅ Postman Collection: `entregaveis/delivery-api-rabay.postman_collection.json`
- ✅ Postman Environment: `entregaveis/delivery-api-rabay.postman_environment.json`
- ✅ Documentação completa neste README

---
**Status**: ✅ IMPLEMENTAÇÃO CONCLUÍDA E TESTADA
**Data**: 09/09/2025
**Branch**: feature/swagger-documentation (pronta para merge)
