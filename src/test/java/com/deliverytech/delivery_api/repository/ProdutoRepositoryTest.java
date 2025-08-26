package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager entityManager;

    @Test
    void testProdutosMaisVendidos() {
        Restaurante r = new Restaurante();
        r.setNome("Restaurante Lanches");
        r.setCategoria("Lanches");
        r.setAtivo(true);
        r.setAvaliacao(new BigDecimal("4.0"));
        restauranteRepository.save(r);

        Produto p = new Produto();
        p.setNome("Coxinha");
        p.setCategoria("Salgado");
        p.setDisponivel(true);
        p.setRestaurante(r);
        p.setPreco(new BigDecimal("10.00"));
        produtoRepository.save(p);

    // Criar um pedido e item_pedido para gerar vendas e persistir via TestEntityManager
    com.deliverytech.delivery_api.model.Cliente cliente = new com.deliverytech.delivery_api.model.Cliente();
    cliente.setNome("Cliente Teste");
    cliente.setEmail("cliente@teste.com");
    entityManager.persist(cliente);

    com.deliverytech.delivery_api.model.Pedido pedido = new com.deliverytech.delivery_api.model.Pedido();
    pedido.setCliente(cliente);
    pedido.setRestaurante(r);
    pedido.setValorTotal(new java.math.BigDecimal("10.00"));
    pedido.setSubtotal(new java.math.BigDecimal("10.00"));
    pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
    pedido.setDataPedido(java.time.LocalDateTime.now());
    entityManager.persist(pedido);

    com.deliverytech.delivery_api.model.ItemPedido item = new com.deliverytech.delivery_api.model.ItemPedido();
    item.setProduto(p);
    item.setQuantidade(1);
    item.setPrecoUnitario(p.getPreco());
    item.setPedido(pedido);
    item.setSubtotal(p.getPreco());
    entityManager.persist(item);

    // flush to ensure data visible to native queries
    entityManager.flush();

    var inicio = java.time.LocalDateTime.now().minusYears(1);
    var fim = java.time.LocalDateTime.now();
    var pageable = org.springframework.data.domain.PageRequest.of(0, 5);
    List<RelatorioVendasProdutos> ranking = produtoRepository.produtosMaisVendidos(inicio, fim, pageable);
    assertThat(ranking).isNotEmpty();
    assertThat(ranking.get(0).getNomeProduto()).isEqualTo("Coxinha");
    }

    @Test
    void testFindByRestaurante() {
    Restaurante r = new Restaurante();
    r.setNome("Burguer House");
    r.setCategoria("Lanches");
    r.setAtivo(true);
    r.setAvaliacao(new java.math.BigDecimal("4.2"));
        restauranteRepository.save(r);
        Produto p = new Produto();
        p.setNome("X-Burguer");
        p.setCategoria("Lanche");
        p.setDisponivel(true);
        p.setRestaurante(r);
        produtoRepository.save(p);
    List<Produto> results = produtoRepository.findByRestauranteAndExcluidoFalse(r);
        assertThat(results).extracting(Produto::getNome).contains("X-Burguer");
    // adicional: verificar busca por id do restaurante (findByRestauranteIdAndExcluidoFalse)
    List<Produto> resultsById = produtoRepository.findByRestauranteIdAndExcluidoFalse(r.getId());
        assertThat(resultsById).extracting(Produto::getNome).contains("X-Burguer");
    }
}
