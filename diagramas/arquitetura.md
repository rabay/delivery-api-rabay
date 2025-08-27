# Diagrama de Arquitetura - Delivery API

Este diagrama representa a arquitetura geral do sistema de delivery API, mostrando as camadas e componentes principais.

## Arquitetura Geral do Sistema

```mermaid
graph TB
    subgraph "Client Layer"
        Web[Web Client]
        Mobile[Mobile App]
        Postman[API Testing]
    end
    
    subgraph "API Gateway"
        Gateway[Spring Boot API Gateway]
    end
    
    subgraph "Controller Layer"
        AuthC[AuthController]
        ClienteC[ClienteController]
        RestC[RestauranteController]
        ProdC[ProdutoController]
        PedC[PedidoController]
        RelC[RelatorioController]
        HealthC[HealthController]
        DbC[DbController]
    end
    
    subgraph "Security Layer"
        JWT[JwtAuthenticationFilter]
        SecConfig[SecurityConfig]
        JwtUtil[JwtUtil]
    end
    
    subgraph "Service Layer"
        UsuarioS[UsuarioService]
        ClienteS[ClienteService]
        RestS[RestauranteService]
        ProdS[ProdutoService]
        PedS[PedidoService]
        RelS[RelatorioService]
    end
    
    subgraph "Repository Layer"
        UsuarioR[UsuarioRepository]
        ClienteR[ClienteRepository]
        RestR[RestauranteRepository]
        ProdR[ProdutoRepository]
        PedR[PedidoRepository]
    end
    
    subgraph "Data Layer"
        H2[(H2 Database)]
        DataSQL[data.sql]
    end
    
    subgraph "Configuration"
        AppProps[application.properties]
        SecurityConf[Security Configuration]
        OpenAPI[OpenAPI Configuration]
    end
    
    Web --> Gateway
    Mobile --> Gateway
    Postman --> Gateway
    
    Gateway --> JWT
    JWT --> AuthC
    JWT --> ClienteC
    JWT --> RestC
    JWT --> ProdC
    JWT --> PedC
    JWT --> RelC
    JWT --> HealthC
    JWT --> DbC
    
    AuthC --> UsuarioS
    ClienteC --> ClienteS
    RestC --> RestS
    ProdC --> ProdS
    PedC --> PedS
    RelC --> RelS
    
    UsuarioS --> UsuarioR
    ClienteS --> ClienteR
    RestS --> RestR
    ProdS --> ProdR
    PedS --> PedR
    
    UsuarioR --> H2
    ClienteR --> H2
    RestR --> H2
    ProdR --> H2
    PedR --> H2
    
    H2 --> DataSQL
    
    JWT -.-> JwtUtil
    JWT -.-> SecConfig
    
    SecConfig -.-> AppProps
    OpenAPI -.-> AppProps
```

## Arquitetura de Segurança JWT

```mermaid
graph LR
    subgraph "Authentication Flow"
        Login[Login Request] --> AuthManager[AuthenticationManager]
        AuthManager --> UserDetails[UserDetailsService]
        UserDetails --> BCrypt[BCrypt Password Encoder]
        BCrypt --> JWTGen[JWT Token Generation]
        JWTGen --> Response[Login Response]
    end
    
    subgraph "Authorization Flow"
        Request[API Request] --> JWTFilter[JwtAuthenticationFilter]
        JWTFilter --> TokenValidation[Token Validation]
        TokenValidation --> SecurityContext[Security Context]
        SecurityContext --> Controller[Protected Controller]
    end
    
    subgraph "Components"
        JWTUtil[JwtUtil]
        SecurityConfig[SecurityConfig]
        Usuario[Usuario Entity]
        Role[Role Enum]
    end
    
    JWTGen -.-> JWTUtil
    TokenValidation -.-> JWTUtil
    SecurityContext -.-> Usuario
    Usuario -.-> Role
    JWTFilter -.-> SecurityConfig
```

## Componentes Principais

### Client Layer
- **Web Client**: Interface web para usuários finais
- **Mobile App**: Aplicação móvel para clientes e entregadores
- **API Testing**: Ferramentas como Postman para testes

### Controller Layer
- **AuthController**: Gerencia autenticação e registro
- **ClienteController**: CRUD de clientes
- **RestauranteController**: CRUD de restaurantes
- **ProdutoController**: CRUD de produtos
- **PedidoController**: Gerenciamento de pedidos
- **RelatorioController**: Relatórios e analytics
- **HealthController**: Health checks
- **DbController**: Acesso direto ao banco (desenvolvimento)

### Security Layer
- **JwtAuthenticationFilter**: Filtro de autenticação JWT
- **SecurityConfig**: Configuração de segurança
- **JwtUtil**: Utilitários para manipulação de tokens JWT

### Service Layer
- Camada de lógica de negócio
- Implementação das regras de negócio
- Coordenação entre repositories

### Repository Layer
- Acesso a dados via Spring Data JPA
- Consultas customizadas
- Mapeamento objeto-relacional

### Data Layer
- **H2 Database**: Banco de dados em memória
- **data.sql**: Scripts de inicialização de dados