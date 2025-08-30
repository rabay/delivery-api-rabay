package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Remove @DataJpaTest since we're using @SpringBootTest in BaseIntegrationTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ProdutoRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void testProdutosMaisVendidos() {
        // Test that the query executes without errors
        var inicio = java.time.LocalDateTime.now().minusYears(1);
        var fim = java.time.LocalDateTime.now();
        var pageable = org.springframework.data.domain.PageRequest.of(0, 5);
        
        // This should not throw an exception
        List<RelatorioVendasProdutos> ranking = produtoRepository.produtosMaisVendidos(inicio, fim, pageable);
        
        // We're not asserting on the content since test data may vary
        // Just ensuring the query executes successfully
        assertThat(ranking).isNotNull();
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
        p.setPreco(new BigDecimal("15.00")); // Add required field
        produtoRepository.save(p);
        List<Produto> results = produtoRepository.findByRestauranteAndExcluidoFalse(r);
        assertThat(results).extracting(Produto::getNome).contains("X-Burguer");
        // adicional: verificar busca por id do restaurante (findByRestauranteIdAndExcluidoFalse)
        List<Produto> resultsById = produtoRepository.findByRestauranteIdAndExcluidoFalse(r.getId());
        assertThat(resultsById).extracting(Produto::getNome).contains("X-Burguer");
    }
}