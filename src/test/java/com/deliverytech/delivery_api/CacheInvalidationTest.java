package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class CacheInvalidationTest extends AbstractIntegrationTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoService pedidoService;

    @SpyBean
    private ProdutoRepository produtoRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // Clear all caches before each test
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Test
    void testProdutoCache() {
        // First call, should hit the database
        produtoService.buscarPorId(1L);
        verify(produtoRepository, times(1)).findById(1L);

        // Second call, should hit the cache
        produtoService.buscarPorId(1L);
        verify(produtoRepository, times(1)).findById(1L); // Should still be 1

        assertNotNull(cacheManager.getCache("produtos").get(1L));
    }

    @Test
    void testProdutoCacheEvictionOnUpdate() {
        // Put a product in the cache
        produtoService.buscarPorId(1L);
        assertNotNull(cacheManager.getCache("produtos").get(1L));

        // Update the product
        var produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("New Name");
        produtoRequest.setDescricao("New Description");
        produtoRequest.setPreco(BigDecimal.TEN);
        produtoRequest.setDisponivel(true);
        produtoService.updateProduto(1L, produtoRequest); // Using the renamed method

        // Check that the cache is evicted
        assertNull(cacheManager.getCache("produtos").get(1L));
    }

    @Test
    void testProdutoCacheEvictionOnDelete() {
        // Put a product in the cache
        produtoService.buscarPorId(1L);
        assertNotNull(cacheManager.getCache("produtos").get(1L));

        // Delete the product
        produtoService.deletar(1L);

        // Check that the cache is evicted
        assertNull(cacheManager.getCache("produtos").get(1L));
    }

    @Test
    void testPedidoCacheEviction() {
        // 1. Cache a single product
        produtoService.buscarPorId(1L);
        assertNotNull(cacheManager.getCache("produtos").get(1L));

        // 2. Cache the list of products
        produtoService.listarTodos(Pageable.unpaged());
        reset(produtoRepository);
        produtoService.listarTodos(Pageable.unpaged());
        verify(produtoRepository, never()).findAll(any(Pageable.class));

        // 3. Create an order with a product
        var pedidoRequest = new PedidoRequest();
        pedidoRequest.setClienteId(1L);
        pedidoRequest.setRestauranteId(1L);
        var item = new ItemPedidoRequest();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        pedidoRequest.setItens(List.of(item));

        pedidoService.criar(pedidoRequest);

        // 4. Verify caches are evicted
        assertNull(cacheManager.getCache("produtos").get(1L));

        reset(produtoRepository);
        produtoService.listarTodos(Pageable.unpaged());
        verify(produtoRepository, times(1)).findAll(any(Pageable.class));
    }
}
