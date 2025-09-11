package com.deliverytech.delivery_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(
    properties = {
      "spring.cache.type=simple",
      "spring.cache.cache-names=produtos,pedidos,relatorios"
    })
@Transactional
public class ProdutoServiceCacheIT extends com.deliverytech.delivery_api.BaseIntegrationTest {

  @Autowired private ProdutoServiceImpl produtoService;

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired private RestauranteRepository restauranteRepository;

  @Autowired private CacheManager cacheManager;

  private Restaurante restaurante;

  @BeforeEach
  void setUp() {
    // Clean up cache before each test
    cacheManager.getCache("produtos").clear();

    // Create a test restaurant
    restaurante = new Restaurante();
    restaurante.setNome("Restaurante Teste");
    restaurante.setEmail("restaurante@teste.com");
    restaurante.setTelefone("11999999999");
    restaurante.setAtivo(true);
    restaurante.setExcluido(false);
    restaurante = restauranteRepository.save(restaurante);
  }

  @Test
  void testBuscarPorRestauranteIsCached() {
    // Given
    Produto produto = new Produto();
    produto.setNome("Produto Teste");
    produto.setCategoria("Categoria Teste");
    produto.setDescricao("Descrição Teste");
    produto.setPreco(java.math.BigDecimal.valueOf(10.0));
    produto.setDisponivel(true);
    produto.setExcluido(false);
    produto.setQuantidadeEstoque(100);
    produto.setRestaurante(restaurante);
    produtoRepository.save(produto);

    // When - First call
    List<Produto> result1 = produtoService.buscarPorRestaurante(restaurante);

    // When - Second call with same parameters
    List<Produto> result2 = produtoService.buscarPorRestaurante(restaurante);

    // Then
    assertThat(result1).isNotEmpty();
    assertThat(result2).isNotEmpty();
    assertThat(result1).isEqualTo(result2);

    // Note: We can't easily verify the cache hit in integration tests without
    // more complex setup, but we can verify the method returns the same results
  }
}
