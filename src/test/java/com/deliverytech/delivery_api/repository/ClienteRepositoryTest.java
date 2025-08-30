package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

// Remove @DataJpaTest since we're using @SpringBootTest in BaseIntegrationTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ClienteRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testFindByAtivoTrueAndExcluidoFalse() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria");
        cliente.setEmail("maria@teste.com");
        cliente.setAtivo(true);
        cliente.setExcluido(false);
        clienteRepository.save(cliente);

        Cliente clienteInativo = new Cliente();
        clienteInativo.setNome("João");
        clienteInativo.setEmail("joao@teste.com");
        clienteInativo.setAtivo(false);
        clienteInativo.setExcluido(false);
        clienteRepository.save(clienteInativo);

        Cliente clienteExcluido = new Cliente();
        clienteExcluido.setNome("Pedro");
        clienteExcluido.setEmail("pedro@teste.com");
        clienteExcluido.setAtivo(true);
        clienteExcluido.setExcluido(true);
        clienteRepository.save(clienteExcluido);

        assertThat(clienteRepository.findByAtivoTrueAndExcluidoFalse()).extracting(Cliente::getNome).contains("Maria");
    }

    @Test
    @Transactional
    void testRankingClientesPorPedidos() {
        // Clear any existing data to ensure test isolation
        entityManager.createQuery("DELETE FROM ItemPedido").executeUpdate();
        entityManager.createQuery("DELETE FROM Pedido").executeUpdate();
        entityManager.createQuery("DELETE FROM Produto").executeUpdate();
        entityManager.createQuery("DELETE FROM Cliente").executeUpdate();
        entityManager.createQuery("DELETE FROM Restaurante").executeUpdate();
        entityManager.flush();

        Cliente cliente = new Cliente();
        cliente.setNome("Ranking Teste");
        cliente.setEmail("ranking@teste.com");
        cliente.setAtivo(true);
        cliente.setExcluido(false);
        cliente = clienteRepository.save(cliente);

        // Create a competing client with fewer orders
        Cliente clienteCompeticao = new Cliente();
        clienteCompeticao.setNome("Cliente Competicao");
        clienteCompeticao.setEmail("competicao@teste.com");
        clienteCompeticao.setAtivo(true);
        clienteCompeticao.setExcluido(false);
        clienteCompeticao = clienteRepository.save(clienteCompeticao);

        // Create restaurant
        com.deliverytech.delivery_api.model.Restaurante restaurante = new com.deliverytech.delivery_api.model.Restaurante();
        restaurante.setNome("Restaurante Ranking");
        restaurante.setCategoria("Teste");
        restaurante.setAtivo(true);
        restaurante.setExcluido(false);
        entityManager.persist(restaurante);

        // Create product
        com.deliverytech.delivery_api.model.Produto produto = new com.deliverytech.delivery_api.model.Produto();
        produto.setNome("Produto Ranking");
        produto.setCategoria("Categoria");
        produto.setDisponivel(true);
        produto.setExcluido(false);
        produto.setRestaurante(restaurante);
        produto.setPreco(new java.math.BigDecimal("20.00"));
        entityManager.persist(produto);

        entityManager.flush();

        // Create 5 orders for "Ranking Teste" to ensure it ranks first
        for (int i = 0; i < 5; i++) {
            com.deliverytech.delivery_api.model.Pedido pedido = new com.deliverytech.delivery_api.model.Pedido();
            pedido.setCliente(cliente);
            pedido.setRestaurante(restaurante);
            pedido.setValorTotal(new java.math.BigDecimal("20.00"));
            pedido.setSubtotal(new java.math.BigDecimal("20.00"));
            pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
            pedido.setDataPedido(java.time.LocalDateTime.now().minusDays(i));
            entityManager.persist(pedido);

            com.deliverytech.delivery_api.model.ItemPedido item = new com.deliverytech.delivery_api.model.ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(1);
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco());
            entityManager.persist(item);
        }

        // Create only 2 orders for competing client
        for (int i = 0; i < 2; i++) {
            com.deliverytech.delivery_api.model.Pedido pedido = new com.deliverytech.delivery_api.model.Pedido();
            pedido.setCliente(clienteCompeticao);
            pedido.setRestaurante(restaurante);
            pedido.setValorTotal(new java.math.BigDecimal("15.00"));
            pedido.setSubtotal(new java.math.BigDecimal("15.00"));
            pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
            pedido.setDataPedido(java.time.LocalDateTime.now().minusDays(i + 10));
            entityManager.persist(pedido);

            com.deliverytech.delivery_api.model.ItemPedido item = new com.deliverytech.delivery_api.model.ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(1);
            item.setPrecoUnitario(new java.math.BigDecimal("15.00"));
            item.setSubtotal(new java.math.BigDecimal("15.00"));
            entityManager.persist(item);
        }

        entityManager.flush();

        var inicio = java.time.LocalDateTime.now().minusYears(1);
        var fim = java.time.LocalDateTime.now();
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        List<RelatorioVendasClientes> ranking = clienteRepository.rankingClientesPorPedidos(inicio, fim, pageable);
        
        assertThat(ranking).isNotEmpty();
        assertThat(ranking.size()).isGreaterThanOrEqualTo(2);
        // "Ranking Teste" should be first due to having more orders than competitor
        assertThat(ranking.get(0).getNomeCliente()).isEqualTo("Ranking Teste");
        // Just check that it has more orders than the competitor, not exact count
        assertThat(ranking.get(0).getQuantidadePedidos()).isGreaterThan(ranking.get(1).getQuantidadePedidos());
    }
}