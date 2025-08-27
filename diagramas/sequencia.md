# Diagrama de Sequência - Delivery API

Este diagrama mostra os principais fluxos de interação no sistema de delivery API.

## Fluxo de Autenticação e Criação de Pedido

```mermaid
sequenceDiagram
    participant Client as Cliente
    participant AuthC as AuthController
    participant UserS as UsuarioService
    participant JWT as JwtUtil
    participant PedC as PedidoController
    participant PedS as PedidoService
    participant ClienteS as ClienteService
    participant RestS as RestauranteService
    participant ProdS as ProdutoService
    participant PedR as PedidoRepository
    participant DB as H2 Database
    
    %% Authentication Flow
    Note over Client, DB: Fluxo de Autenticação
    Client->>AuthC: POST /api/auth/login
    AuthC->>UserS: authenticate(credentials)
    UserS->>DB: findByEmail(email)
    DB-->>UserS: Usuario entity
    UserS->>UserS: validatePassword()
    UserS-->>AuthC: Authentication success
    AuthC->>JWT: gerarToken(email)
    JWT-->>AuthC: JWT token
    AuthC-->>Client: LoginResponse with token
    
    %% Order Creation Flow
    Note over Client, DB: Fluxo de Criação de Pedido
    Client->>PedC: POST /api/pedidos (with JWT)
    PedC->>PedC: validateJWT()
    PedC->>PedS: criarPedido(pedidoRequest)
    
    %% Validations
    PedS->>ClienteS: validarCliente(clienteId)
    ClienteS-->>PedS: Cliente válido
    PedS->>RestS: validarRestaurante(restauranteId)
    RestS-->>PedS: Restaurante válido
    
    %% Product validation loop
    loop Para cada item do pedido
        PedS->>ProdS: validarProduto(produtoId)
        ProdS-->>PedS: Produto válido e disponível
    end
    
    %% Order processing
    PedS->>PedS: calcularValorTotal()
    PedS->>PedS: gerarNumeroPedido()
    PedS->>PedR: save(pedido)
    PedR->>DB: INSERT pedido
    DB-->>PedR: Pedido salvo
    PedR-->>PedS: Pedido entity
    PedS-->>PedC: PedidoResponse
    PedC-->>Client: 201 Created with PedidoResponse
```

## Fluxo de Consulta de Relatórios

```mermaid
sequenceDiagram
    participant Client as Cliente Admin
    participant RelC as RelatorioController
    participant RelS as RelatorioService
    participant PedR as PedidoRepository
    participant DB as H2 Database
    
    Client->>RelC: GET /api/relatorios/vendas (with JWT)
    RelC->>RelC: validateJWT() & checkAdminRole()
    RelC->>RelS: getRelatorioVendas(filters)
    
    par Consultas paralelas
        RelS->>PedR: findVendasPorPeriodo(dataInicio, dataFim)
        PedR->>DB: Query vendas por período
        DB-->>PedR: ResultSet vendas
        PedR-->>RelS: Lista de vendas
    and
        RelS->>PedR: findFaturamentoPorCategoria()
        PedR->>DB: Query faturamento por categoria
        DB-->>PedR: ResultSet categorias
        PedR-->>RelS: Faturamento por categoria
    and
        RelS->>PedR: findClientesMaisAtivos()
        PedR->>DB: Query clientes ativos
        DB-->>PedR: ResultSet clientes
        PedR-->>RelS: Clientes mais ativos
    end
    
    RelS->>RelS: consolidarDados()
    RelS-->>RelC: RelatorioVendas
    RelC-->>Client: 200 OK with report data
```

## Fluxo de Registro de Usuário

```mermaid
sequenceDiagram
    participant Client as Novo Usuario
    participant AuthC as AuthController
    participant UserS as UsuarioService
    participant UserR as UsuarioRepository
    participant BCrypt as BCryptEncoder
    participant JWT as JwtUtil
    participant DB as H2 Database
    
    Client->>AuthC: POST /api/auth/register
    AuthC->>AuthC: validateRequestData()
    AuthC->>UserS: registrarUsuario(registerRequest)
    
    UserS->>UserR: existsByEmail(email)
    UserR->>DB: SELECT count(*) WHERE email = ?
    DB-->>UserR: count result
    UserR-->>UserS: boolean exists
    
    alt Email já existe
        UserS-->>AuthC: EmailDuplicadoException
        AuthC-->>Client: 409 Conflict
    else Email disponível
        UserS->>BCrypt: encode(password)
        BCrypt-->>UserS: encoded password
        UserS->>UserS: createUsuario(data, encodedPassword)
        UserS->>UserR: save(usuario)
        UserR->>DB: INSERT usuario
        DB-->>UserR: Usuario saved
        UserR-->>UserS: Usuario entity
        UserS->>JWT: gerarToken(email)
        JWT-->>UserS: JWT token
        UserS-->>AuthC: LoginResponse with token
        AuthC-->>Client: 201 Created with token
    end
```

## Fluxo de Atualização de Status do Pedido

```mermaid
sequenceDiagram
    participant Entregador as Entregador/Admin
    participant PedC as PedidoController
    participant PedS as PedidoService
    participant PedR as PedidoRepository
    participant DB as H2 Database
    
    Entregador->>PedC: PUT /api/pedidos/{id}/status (with JWT)
    PedC->>PedC: validateJWT() & checkRole()
    PedC->>PedS: atualizarStatus(pedidoId, novoStatus)
    
    PedS->>PedR: findById(pedidoId)
    PedR->>DB: SELECT * FROM pedidos WHERE id = ?
    DB-->>PedR: Pedido entity
    PedR-->>PedS: Optional<Pedido>
    
    alt Pedido não encontrado
        PedS-->>PedC: EntityNotFoundException
        PedC-->>Entregador: 404 Not Found
    else Pedido encontrado
        PedS->>PedS: validarTransicaoStatus(statusAtual, novoStatus)
        
        alt Transição inválida
            PedS-->>PedC: IllegalStateException
            PedC-->>Entregador: 400 Bad Request
        else Transição válida
            PedS->>PedS: pedido.setStatus(novoStatus)
            PedS->>PedS: pedido.setDataAtualizacao(now())
            PedS->>PedR: save(pedido)
            PedR->>DB: UPDATE pedidos SET status = ?, data_atualizacao = ? WHERE id = ?
            DB-->>PedR: Update success
            PedR-->>PedS: Updated Pedido
            PedS-->>PedC: PedidoResponse
            PedC-->>Entregador: 200 OK with updated pedido
        end
    end
```

## Descrição dos Fluxos

### 1. Fluxo de Autenticação e Criação de Pedido
- Demonstra o processo completo desde login até criação de pedido
- Inclui validação JWT e regras de negócio
- Mostra as validações em cascata (cliente, restaurante, produtos)

### 2. Fluxo de Consulta de Relatórios
- Mostra como funciona a autorização por role (ADMIN)
- Demonstra consultas paralelas para performance
- Inclui agregação de dados de múltiplas fontes

### 3. Fluxo de Registro de Usuário
- Processo de criação de nova conta
- Validação de email duplicado
- Criptografia de senha e geração de token

### 4. Fluxo de Atualização de Status do Pedido
- Demonstra autorização por role específica
- Validação de transições de status
- Tratamento de erros e exceções