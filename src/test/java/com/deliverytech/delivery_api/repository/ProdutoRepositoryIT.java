package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.entities.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProdutoRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    public void whenFindByRestauranteId_thenReturnProdutos() {
        // given
        Produto produto = new Produto("Produto Teste", "Descrição Teste", BigDecimal.valueOf(10.00), true, 1L);
        entityManager.persist(produto);
        entityManager.flush();

        // when
        List<Produto> found = produtoRepository.findByRestauranteId(1L);

        // then
        assertThat(found).hasSize(1).extracting(Produto::getNome).contains(produto.getNome());
    }

    @Test
    public void whenFindByNomeContainingIgnoreCaseAndRestauranteId_thenReturnProdutos() {
        // given
        Produto produto = new Produto("Produto Teste", "Descrição Teste", BigDecimal.valueOf(10.00), true, 1L);
        entityManager.persist(produto);
        entityManager.flush();

        // when
        List<Produto> found = produtoRepository.findByNomeContainingIgnoreCaseAndRestauranteId("produto", 1L);

        // then
        assertThat(found).hasSize(1).extracting(Produto::getNome).contains(produto.getNome());
    }

    @Test
    public void whenFindAvailableProductsByRestaurante_thenReturnAvailableProdutos() {
        // given
        Produto produto1 = new Produto("Produto 1", "Descrição 1", BigDecimal.valueOf(10.00), true, 1L);
        Produto produto2 = new Produto("Produto 2", "Descrição 2", BigDecimal.valueOf(20.00), false, 1L);
        entityManager.persist(produto1);
        entityManager.persist(produto2);
        entityManager.flush();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Produto> found = produtoRepository.findAvailableProductsByRestaurante(1L, pageable);

        // then
        assertThat(found).hasSize(1).extracting(Produto::getNome).contains(produto1.getNome());
    }
}
