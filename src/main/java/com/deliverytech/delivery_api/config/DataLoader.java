package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
// import java.math.BigDecimal;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");

        // Inserir dados de teste (sem limpar dados existentes)
        inserirClientes();
        inserirRestaurantes();
        inserirPedidos();

        System.out.println("=== CARGA DE DADOS CONCLUÍDA ===");
        System.out.println("\n✅ Spring Boot Application iniciada com sucesso!");
        System.out.println("\n🎯 SISTEMA DE CAPTURA AUTOMÁTICA ATIVO!");
        System.out.println("📁 Respostas serão salvas em: ./entregaveis/");
        System.out.println("🔄 Faça requisições para /api/* e veja os arquivos sendo gerados!\n");
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
        pedido1.setStatus("CRIADO");
        pedido1.setDataPedido(java.time.LocalDateTime.now().minusDays(1));

        Pedido pedido2 = new Pedido();
        pedido2.setCliente(clientes.get(1));
        pedido2.setStatus("FINALIZADO");
        pedido2.setDataPedido(java.time.LocalDateTime.now().minusHours(5));

        Pedido pedido3 = new Pedido();
        pedido3.setCliente(clientes.get(2));
        pedido3.setStatus("CANCELADO");
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
        restaurante1.setAvaliacao(4.5);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Burger King");
        restaurante2.setCategoria("Fast Food");
        restaurante2.setAtivo(true);
        restaurante2.setAvaliacao(4.2);

        Restaurante restaurante3 = new Restaurante();
        restaurante3.setNome("Sushi House");
        restaurante3.setCategoria("Japonesa");
        restaurante3.setAtivo(true);
        restaurante3.setAvaliacao(4.8);

        Restaurante restaurante4 = new Restaurante();
        restaurante4.setNome("Gyros Athenas");
        restaurante4.setCategoria("Grega");
        restaurante4.setAtivo(true);
        restaurante4.setAvaliacao(4.0);

        Restaurante restaurante5 = new Restaurante();
        restaurante5.setNome("Chiparia do Porto");
        restaurante5.setCategoria("Frutos do Mar");
        restaurante5.setAtivo(true);
        restaurante5.setAvaliacao(4.3);

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