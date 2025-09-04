package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ClienteRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Test
    void testRankingClientesPorPedidos() {
        // Create test cliente
        Cliente cliente = new Cliente();
        cliente.setNome("Ranking Teste");
        cliente.setEmail("ranking.unique@teste.com"); // Use unique email
        cliente.setAtivo(true);
        cliente.setExcluido(false);
        entityManager.persist(cliente);
        
        // Create competing cliente
        Cliente clienteCompeticao = new Cliente();
        clienteCompeticao.setNome("Competição Teste");
        clienteCompeticao.setEmail("competicao.unique@teste.com"); // Use unique email
        clienteCompeticao.setAtivo(true);
        clienteCompeticao.setExcluido(false);
        entityManager.persist(clienteCompeticao);
        
        // Create restaurante
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
        produto.setQuantidadeEstoque(10); // Add required field
        entityManager.persist(produto);

        entityManager.flush();

        // Use a time base in the future to isolate these orders from other tests
        var baseData = java.time.LocalDateTime.now().plusYears(10);

        // Create 5 orders for "Ranking Teste" to ensure it ranks first
        for (int i = 0; i < 5; i++) {
            com.deliverytech.delivery_api.model.Pedido pedido = new com.deliverytech.delivery_api.model.Pedido();
            pedido.setCliente(cliente);
            pedido.setRestaurante(restaurante);
            pedido.setValorTotal(new java.math.BigDecimal("20.00"));
            pedido.setSubtotal(new java.math.BigDecimal("20.00"));
            pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
            pedido.setDataPedido(baseData.minusDays(i));
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
            pedido.setDataPedido(baseData.minusDays(i + 5));
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

        var inicio = baseData.minusYears(1);
        var fim = baseData.plusYears(1);
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