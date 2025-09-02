# Diagrama de Arquitetura - Delivery API

Este diagrama representa a arquitetura moderna do sistema de delivery API, incluindo padrões cloud-native e componentes de resiliência.

## Arquitetura Geral do Sistema (Cloud-Native)

```mermaid
graph TB
    subgraph "Client Layer"
        Web[Web Client]
        Mobile[Mobile App]
        Postman[API Testing]
        ThirdParty[Third Party APIs]
    end
    
    subgraph "Edge Services"
        Gateway[API Gateway<br/>Spring Cloud Gateway]
        LoadBalancer[Load Balancer<br/>NGINX/HAProxy]
        WAF[WAF<br/>Web Application Firewall]
    end
    
    subgraph "Service Mesh"
        ServiceRegistry[Eureka Service Registry]
        ConfigServer[Spring Cloud Config]
        CircuitBreaker[Circuit Breaker<br/>Resilience4j]
    end
    
    subgraph "Application Layer"
        AuthService[Auth Service<br/>JWT/OAuth2]
        OrderService[Order Service<br/>Pedido Management]
        RestaurantService[Restaurant Service<br/>Restaurante Management]
        ProductService[Product Service<br/>Produto Management]
        CustomerService[Customer Service<br/>Cliente Management]
        NotificationService[Notification Service<br/>Push Notifications]
    end
    
    subgraph "Integration Layer"
        PaymentGateway[Payment Gateway<br/>Stripe/PagSeguro]
        DeliveryProvider[Delivery Provider<br/>Logistics API]
        EmailService[Email Service<br/>SMTP/SendGrid]
        SMSService[SMS Service<br/>Twilio]
    end
    
    subgraph "Data Layer"
        PrimaryDB[(Primary DB<br/>MySQL/PostgreSQL)]
        CacheDB[(Redis Cache<br/>Session & Data Cache)]
        SearchDB[(Elasticsearch<br/>Search & Analytics)]
        MessageQueue[(RabbitMQ/Kafka<br/>Event Streaming)]
    end
    
    subgraph "Infrastructure"
        Monitoring[Monitoring Stack<br/>Prometheus + Grafana]
        Logging[Centralized Logging<br/>ELK Stack]
        Backup[Backup & Recovery<br/>Automated Backups]
        CDN[CDN<br/>CloudFront/CloudFlare]
    end
    
    Web --> LoadBalancer
    Mobile --> LoadBalancer
    Postman --> LoadBalancer
    ThirdParty --> LoadBalancer
    
    LoadBalancer --> WAF
    WAF --> Gateway
    
    Gateway --> ServiceRegistry
    ServiceRegistry --> AuthService
    ServiceRegistry --> OrderService
    ServiceRegistry --> RestaurantService
    ServiceRegistry --> ProductService
    ServiceRegistry --> CustomerService
    ServiceRegistry --> NotificationService
    
    AuthService --> ConfigServer
    OrderService --> ConfigServer
    RestaurantService --> ConfigServer
    ProductService --> ConfigServer
    CustomerService --> ConfigServer
    NotificationService --> ConfigServer
    
    OrderService --> CircuitBreaker
    CircuitBreaker --> PaymentGateway
    CircuitBreaker --> DeliveryProvider
    
    NotificationService --> EmailService
    NotificationService --> SMSService
    
    AuthService --> PrimaryDB
    OrderService --> PrimaryDB
    RestaurantService --> PrimaryDB
    ProductService --> PrimaryDB
    CustomerService --> PrimaryDB
    
    AuthService --> CacheDB
    OrderService --> CacheDB
    RestaurantService --> CacheDB
    ProductService --> CacheDB
    CustomerService --> CacheDB
    
    OrderService --> SearchDB
    RestaurantService --> SearchDB
    ProductService --> SearchDB
    
    OrderService --> MessageQueue
    NotificationService --> MessageQueue
    
    Monitoring -.-> AuthService
    Monitoring -.-> OrderService
    Monitoring -.-> RestaurantService
    Monitoring -.-> ProductService
    Monitoring -.-> CustomerService
    Monitoring -.-> NotificationService
    
    Logging -.-> AuthService
    Logging -.-> OrderService
    Logging -.-> RestaurantService
    Logging -.-> ProductService
    Logging -.-> CustomerService
    Logging -.-> NotificationService
    
    PrimaryDB --> Backup
    CacheDB --> Backup
    SearchDB --> Backup
    
    CDN -.-> Web
    CDN -.-> Mobile
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
- **Third Party APIs**: Integrações com sistemas externos

### Edge Services

- **API Gateway**: Ponto único de entrada, roteamento e segurança
- **Load Balancer**: Distribuição de carga entre instâncias
- **WAF**: Proteção contra ataques web comuns

### Service Mesh

- **Service Registry**: Descoberta dinâmica de serviços
- **Config Server**: Gerenciamento centralizado de configurações
- **Circuit Breaker**: Proteção contra falhas em cascata

### Application Layer

- **Auth Service**: Autenticação e autorização JWT/OAuth2
- **Order Service**: Gerenciamento completo do ciclo de pedidos
- **Restaurant Service**: CRUD e gestão de restaurantes
- **Product Service**: Catálogo de produtos e disponibilidade
- **Customer Service**: Perfil e histórico de clientes
- **Notification Service**: Notificações push, email e SMS

### Integration Layer

- **Payment Gateway**: Processamento de pagamentos (Stripe, PagSeguro)
- **Delivery Provider**: Integração com provedores de entrega
- **Email/SMS Services**: Comunicação com usuários

### Data Layer

- **Primary DB**: Banco principal (MySQL/PostgreSQL)
- **Redis Cache**: Cache de sessão e dados frequentes
- **Elasticsearch**: Busca e analytics avançados
- **Message Queue**: Streaming de eventos assíncrono

### Infrastructure

- **Monitoring**: Observabilidade com métricas e alertas
- **Logging**: Logs centralizados para troubleshooting
- **Backup**: Estratégia de backup e recuperação
- **CDN**: Distribuição de conteúdo estático
