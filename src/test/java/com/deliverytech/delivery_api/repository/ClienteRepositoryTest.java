package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager entityManager;

    @Test
    void testFindByEmail() {
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    Optional<Cliente> found = clienteRepository.findByEmailAndExcluidoFalse("joao@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("João");
    }

    @Test
    void testFindByAtivoTrue() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria");
        cliente.setEmail("maria@email.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    assertThat(clienteRepository.findByAtivoTrueAndExcluidoFalse()).extracting(Cliente::getNome).contains("Maria");
    }

    @Test
    void testRankingClientesPorPedidos() {
        // Clear any existing data to ensure test isolation
        entityManager.getEntityManager().createQuery("DELETE FROM ItemPedido").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM Pedido").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM Produto").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM Cliente").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM Restaurante").executeUpdate();
        entityManager.flush();

        Cliente cliente = new Cliente();
        cliente.setNome("Ranking Teste");
        cliente.setEmail("ranking@teste.com");
        cliente.setAtivo(true);
        cliente.setExcluido(false);
        clienteRepository.save(cliente);

        // Create a competing client with fewer orders
        Cliente clienteCompeticao = new Cliente();
        clienteCompeticao.setNome("Cliente Competicao");
        clienteCompeticao.setEmail("competicao@teste.com");
        clienteCompeticao.setAtivo(true);
        clienteCompeticao.setExcluido(false);
        clienteRepository.save(clienteCompeticao);

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
        // "Ranking Teste" should be first due to having 5 orders vs 2 orders from competitor
        assertThat(ranking.get(0).getNomeCliente()).isEqualTo("Ranking Teste");
        assertThat(ranking.get(0).getQuantidadePedidos()).isEqualTo(5L);
        assertThat(ranking.get(1).getNomeCliente()).isEqualTo("Cliente Competicao");
        assertThat(ranking.get(1).getQuantidadePedidos()).isEqualTo(2L);
    }
}
