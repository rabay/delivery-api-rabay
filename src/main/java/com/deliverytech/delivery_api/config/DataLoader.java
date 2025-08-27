package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        // Check if users already exist
        if (usuarioRepository.count() > 0) {
            log.info("Usuários já existem no banco de dados, pulando inicialização");
            return;
        }

        log.info("Criando usuários padrão para testes...");

        // Create Admin user
        Usuario admin = Usuario.builder()
            .nome("Administrator")
            .email("admin@deliveryapi.com")
            .senha(passwordEncoder.encode("admin123"))
            .role(Role.ADMIN)
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .build();
        usuarioRepository.save(admin);

        // Create Cliente user
        Usuario cliente = Usuario.builder()
            .nome("Cliente Teste")
            .email("cliente@test.com")
            .senha(passwordEncoder.encode("cliente123"))
            .role(Role.CLIENTE)
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .build();
        usuarioRepository.save(cliente);

        // Create Restaurante user
        Usuario restaurante = Usuario.builder()
            .nome("Restaurante Teste")
            .email("restaurante@test.com")
            .senha(passwordEncoder.encode("restaurante123"))
            .role(Role.RESTAURANTE)
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .restauranteId(1L) // Associate with first restaurant
            .build();
        usuarioRepository.save(restaurante);

        // Create Entregador user
        Usuario entregador = Usuario.builder()
            .nome("Entregador Teste")
            .email("entregador@test.com")
            .senha(passwordEncoder.encode("entregador123"))
            .role(Role.ENTREGADOR)
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .build();
        usuarioRepository.save(entregador);

        log.info("✅ Usuários padrão criados com sucesso!");
        log.info("👤 Admin: admin@deliveryapi.com / admin123");
        log.info("👤 Cliente: cliente@test.com / cliente123");
        log.info("👤 Restaurante: restaurante@test.com / restaurante123");
        log.info("👤 Entregador: entregador@test.com / entregador123");
    }

    private void validarConsultas() {
        System.out.println("\n=== Validação das Consultas Derivadas ===");

        // ClienteRepository
        System.out.println("\nClientes ativos:");
    clienteRepository.findByAtivoTrueAndExcluidoFalse().forEach(c -> System.out.println("- " + c.getNome() + " (" + c.getEmail() + ")"));

        System.out.println("\nCliente por email (joao@email.com):");
    clienteRepository.findByEmailAndExcluidoFalse("joao@email.com").ifPresentOrElse(
            c -> System.out.println("Encontrado: " + c.getNome()),
            () -> System.out.println("Não encontrado")
        );

    // System.out.println("\nExiste cliente com email maria@email.com? " + clienteRepository.existsByEmail("maria@email.com")); // método removido
    // Atualizado para considerar excluido=false
    System.out.println("Existe cliente com email maria@email.com e não excluído? " + clienteRepository.existsByEmailAndExcluidoFalse("maria@email.com"));

        System.out.println("\nClientes com nome contendo 'Silva':");
    clienteRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse("Silva").forEach(c -> System.out.println("- " + c.getNome()));

        // RestauranteRepository
        System.out.println("\nRestaurantes por categoria 'Italiana':");
    restauranteRepository.findByCategoriaAndExcluidoFalse("Italiana").forEach(r -> System.out.println("- " + r.getNome()));

        System.out.println("\nRestaurantes ativos:");
    restauranteRepository.findByAtivoTrueAndExcluidoFalse().forEach(r -> System.out.println("- " + r.getNome()));

        System.out.println("\nRestaurantes com taxa de entrega <= 5.00:");
    restauranteRepository.findByTaxaEntregaLessThanEqualAndExcluidoFalse(new java.math.BigDecimal("5.00")).forEach(r -> System.out.println("- " + r.getNome()));

        System.out.println("\nTop 5 restaurantes por nome:");
    restauranteRepository.findTop5ByExcluidoFalseOrderByNomeAsc().forEach(r -> System.out.println("- " + r.getNome()));

        // ProdutoRepository
        var restaurantes = restauranteRepository.findAll();
        if (!restaurantes.isEmpty()) {
            var primeiroRestaurante = restaurantes.get(0);
            System.out.println("\nProdutos do restaurante '" + primeiroRestaurante.getNome() + "':");
            produtoRepository.findByRestauranteIdAndExcluidoFalse(primeiroRestaurante.getId()).forEach(p -> System.out.println("- " + p.getNome()));
        }

        System.out.println("\nProdutos disponíveis:");
    produtoRepository.findByDisponivelTrueAndExcluidoFalse().forEach(p -> System.out.println("- " + p.getNome()));

        System.out.println("\nProdutos da categoria 'Pizza':");
    produtoRepository.findByCategoriaAndExcluidoFalse("Pizza").forEach(p -> System.out.println("- " + p.getNome()));

        System.out.println("\nProdutos com preço <= 30.00:");
    produtoRepository.findByPrecoLessThanEqualAndExcluidoFalse(new java.math.BigDecimal("30.00")).forEach(p -> System.out.println("- " + p.getNome()));

        // PedidoRepository
        var clientes = clienteRepository.findAll();
        if (!clientes.isEmpty()) {
            var primeiroCliente = clientes.get(0);
            System.out.println("\nPedidos do cliente '" + primeiroCliente.getNome() + "':");
            pedidoRepository.findByClienteId(primeiroCliente.getId()).forEach(p -> System.out.println("- Pedido ID: " + p.getId() + ", Status: " + p.getStatus()));
        }

        System.out.println("\nPedidos com status CRIADO:");
        pedidoRepository.findByStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO).forEach(p -> System.out.println("- Pedido ID: " + p.getId()));

        System.out.println("\nTop 10 pedidos mais recentes:");
        pedidoRepository.findTop10ByOrderByDataPedidoDesc().forEach(p -> System.out.println("- Pedido ID: " + p.getId() + ", Data: " + p.getDataPedido()));

        var agora = java.time.LocalDateTime.now();
        var ontem = agora.minusDays(1);
        System.out.println("\nPedidos entre ontem e agora:");
        pedidoRepository.findByDataPedidoBetween(ontem, agora).forEach(p -> System.out.println("- Pedido ID: " + p.getId()));

        // Relacionamentos
        System.out.println("\nItens dos pedidos:");
        pedidoRepository.findAllWithItens().forEach(p -> {
            System.out.println("Pedido ID: " + p.getId() + ", Cliente: " + (p.getCliente() != null ? p.getCliente().getNome() : "-") + ", Itens: " + (p.getItens() != null ? p.getItens().size() : 0));
        });
    }

    private void inserirPedidos() {
        System.out.println("--- Inserindo Pedidos ---");

        var clientes = clienteRepository.findAll();
        if (clientes.size() < 2) {
            System.out.println("Poucos clientes para criar pedidos de teste.");
            return;
        }

        Pedido pedido1 = new Pedido();
    pedido1.setCliente(clientes.get(0));
    pedido1.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
    pedido1.setDataPedido(java.time.LocalDateTime.now().minusDays(1));

        Pedido pedido2 = new Pedido();
    pedido2.setCliente(clientes.get(1));
    pedido2.setStatus(com.deliverytech.delivery_api.model.StatusPedido.ENTREGUE);
    pedido2.setDataPedido(java.time.LocalDateTime.now().minusHours(5));

        Pedido pedido3 = new Pedido();
    pedido3.setCliente(clientes.get(2));
    pedido3.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CANCELADO);
    pedido3.setDataPedido(java.time.LocalDateTime.now().minusDays(2));

        pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2, pedido3));
        System.out.println("✓ 3 pedidos inseridos");
    }

    private void inserirClientes() {
        System.out.println("--- Inserindo clientes ---");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@email.com");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@email.com");
        cliente2.setAtivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Oliveira");
        cliente3.setEmail("pedro@email.com");
        cliente3.setAtivo(false);

        Cliente cliente4 = new Cliente();
        cliente4.setNome("Ana Costa");
        cliente4.setEmail("ana@email.com");
        cliente4.setAtivo(true);

        Cliente cliente5 = new Cliente();
        cliente5.setNome("Carlos Ferreira");
        cliente5.setEmail("carlos@email.com");
        cliente5.setAtivo(true);

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3, cliente4, cliente5));
        System.out.println("✓ 5 clientes inseridos");
    }

    private void inserirRestaurantes() {
        System.out.println("--- Inserindo Restaurantes ---");


    Restaurante restaurante1 = new Restaurante();
    restaurante1.setNome("Pizza Express");
    restaurante1.setCategoria("Italiana");
    restaurante1.setAtivo(true);
    restaurante1.setAvaliacao(new java.math.BigDecimal("4.5"));

    Restaurante restaurante2 = new Restaurante();
    restaurante2.setNome("Burger King");
    restaurante2.setCategoria("Fast Food");
    restaurante2.setAtivo(true);
    restaurante2.setAvaliacao(new java.math.BigDecimal("4.2"));

    Restaurante restaurante3 = new Restaurante();
    restaurante3.setNome("Sushi House");
    restaurante3.setCategoria("Japonesa");
    restaurante3.setAtivo(true);
    restaurante3.setAvaliacao(new java.math.BigDecimal("4.8"));

    Restaurante restaurante4 = new Restaurante();
    restaurante4.setNome("Gyros Athenas");
    restaurante4.setCategoria("Grega");
    restaurante4.setAtivo(true);
    restaurante4.setAvaliacao(new java.math.BigDecimal("4.0"));

    Restaurante restaurante5 = new Restaurante();
    restaurante5.setNome("Chiparia do Porto");
    restaurante5.setCategoria("Frutos do Mar");
    restaurante5.setAtivo(true);
    restaurante5.setAvaliacao(new java.math.BigDecimal("4.3"));

        restauranteRepository
                .saveAll(Arrays.asList(restaurante1, restaurante2, restaurante3, restaurante4, restaurante5));
        System.out.println("✓ 5 restaurantes inseridos");

        // Inserir produtos após restaurantes para poder associá-los
        inserirProdutos();
    }

    private void inserirProdutos() {
        System.out.println("--- Inserindo Produtos ---");

        // Buscar restaurantes para associar aos produtos
        var restaurantes = restauranteRepository.findAll();
        var pizzaExpress = restaurantes.stream().filter(r -> r.getNome().equals("Pizza Express")).findFirst()
                .orElse(null);
        var burgerKing = restaurantes.stream().filter(r -> r.getNome().equals("Burger King")).findFirst().orElse(null);
        var sushiHouse = restaurantes.stream().filter(r -> r.getNome().equals("Sushi House")).findFirst().orElse(null);
        var gyrosAthenas = restaurantes.stream().filter(r -> r.getNome().equals("Gyros Athenas")).findFirst()
                .orElse(null);
        var chipariaPorto = restaurantes.stream().filter(r -> r.getNome().equals("Chiparia do Porto")).findFirst()
                .orElse(null);

        Produto produto1 = new Produto();
        produto1.setNome("Pizza Margherita");
        produto1.setCategoria("Pizza");
        produto1.setRestaurante(pizzaExpress);
        produto1.setDisponivel(true);

        Produto produto2 = new Produto();
        produto2.setNome("Pizza Pepperoni");
        produto2.setCategoria("Pizza");
        produto2.setRestaurante(pizzaExpress);
        produto2.setDisponivel(true);

        Produto produto3 = new Produto();
        produto3.setNome("Big Burger");
        produto3.setCategoria("Hambúrguer");
        produto3.setRestaurante(burgerKing);
        produto3.setDisponivel(true);

        Produto produto4 = new Produto();
        produto4.setNome("Batata Frita Grande");
        produto4.setCategoria("Acompanhamento");
        produto4.setRestaurante(burgerKing);
        produto4.setDisponivel(true);

        Produto produto5 = new Produto();
        produto5.setNome("Sushi Salmão");
        produto5.setCategoria("Sushi");
        produto5.setRestaurante(sushiHouse);
        produto5.setDisponivel(true);

        Produto produto6 = new Produto();
        produto6.setNome("Hot Roll");
        produto6.setCategoria("Sushi");
        produto6.setRestaurante(sushiHouse);
        produto6.setDisponivel(true);

        Produto produto7 = new Produto();
        produto7.setNome("Gyros de Cordeiro");
        produto7.setCategoria("Espeto");
        produto7.setRestaurante(gyrosAthenas);
        produto7.setDisponivel(true);

        Produto produto8 = new Produto();
        produto8.setNome("Souvlaki de Frango");
        produto8.setCategoria("Espeto");
        produto8.setRestaurante(gyrosAthenas);
        produto8.setDisponivel(true);

        Produto produto9 = new Produto();
        produto9.setNome("Fish & Chips Tradicional");
        produto9.setCategoria("Peixe");
        produto9.setRestaurante(chipariaPorto);
        produto9.setDisponivel(true);

        Produto produto10 = new Produto();
        produto10.setNome("Porção de Camarão Empanado");
        produto10.setCategoria("Frutos do Mar");
        produto10.setRestaurante(chipariaPorto);
        produto10.setDisponivel(true);

        produtoRepository.saveAll(Arrays.asList(
                produto1, produto2, produto3, produto4, produto5,
                produto6, produto7, produto8, produto9, produto10));
        System.out.println("✓ 10 produtos inseridos");
    }
}