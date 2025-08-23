package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import java.util.List;

@DataJpaTest
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
        Cliente cliente = new Cliente();
        cliente.setNome("Ranking Teste");
        cliente.setEmail("ranking@teste.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);

        // criar pedido vinculado ao cliente para que ranking tenha dados
        com.deliverytech.delivery_api.model.Restaurante restaurante = new com.deliverytech.delivery_api.model.Restaurante();
        restaurante.setNome("Restaurante Ranking");
        restaurante.setCategoria("Teste");
        restaurante.setAtivo(true);
        entityManager.persist(restaurante);

        com.deliverytech.delivery_api.model.Pedido pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setValorTotal(new java.math.BigDecimal("20.00"));
        pedido.setSubtotal(new java.math.BigDecimal("20.00"));
        pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
        pedido.setDataPedido(java.time.LocalDateTime.now());
        entityManager.persist(pedido);

        com.deliverytech.delivery_api.model.Produto produto = new com.deliverytech.delivery_api.model.Produto();
        produto.setNome("Produto Ranking");
        produto.setCategoria("Categoria");
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produto.setPreco(new java.math.BigDecimal("20.00"));
        entityManager.persist(produto);

        com.deliverytech.delivery_api.model.ItemPedido item = new com.deliverytech.delivery_api.model.ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(1);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco());
        entityManager.persist(item);

        entityManager.flush();

    var inicio = java.time.LocalDateTime.now().minusYears(1);
    var fim = java.time.LocalDateTime.now();
    var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
    List<RelatorioVendasClientes> ranking = clienteRepository.rankingClientesPorPedidos(inicio, fim, pageable);
        assertThat(ranking).isNotEmpty();
        assertThat(ranking.get(0).getNomeCliente()).isEqualTo("Ranking Teste");
    }
}
