package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.exception.ProdutoIndisponivelException;
import com.deliverytech.delivery_api.mapper.ProdutoMapper;
import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
// Remove the Optional import to avoid conflicts
// import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache(anyString())).thenReturn(cache);
    }

    @Test
    public void testValidarEstoque_WithSufficientStock_ShouldNotThrowException() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(10);
        produto.setDisponivel(true);

        assertDoesNotThrow(() -> produtoService.validarEstoque(produto, 5));
    }

    @Test
    public void testValidarEstoque_WithInfiniteStock_ShouldNotThrowException() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(-1);
        produto.setDisponivel(true);

        assertDoesNotThrow(() -> produtoService.validarEstoque(produto, 100));
    }

    @Test
    public void testValidarEstoque_WithZeroStock_ShouldThrowProdutoIndisponivelException() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(0);
        produto.setDisponivel(true);

        assertThrows(ProdutoIndisponivelException.class, () -> produtoService.validarEstoque(produto, 1));
    }

    @Test
    public void testValidarEstoque_WithInsufficientStock_ShouldThrowEstoqueInsuficienteException() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(5);
        produto.setDisponivel(true);

        EstoqueInsuficienteException exception = assertThrows(
            EstoqueInsuficienteException.class, 
            () -> produtoService.validarEstoque(produto, 10)
        );
        
        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    public void testValidarEstoque_WithNotAvailableProduct_ShouldThrowProdutoIndisponivelException() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(10);
        produto.setDisponivel(false);

        assertThrows(ProdutoIndisponivelException.class, () -> produtoService.validarEstoque(produto, 5));
    }

    @Test
    public void testAtualizarEstoque_ShouldUpdateProductQuantity() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setQuantidadeEstoque(10);

        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));
        when(produtoRepository.save(produto)).thenReturn(produto);

        produtoService.atualizarEstoque(1L, 20);

        assertEquals(20, produto.getQuantidadeEstoque());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testAjustarEstoque_WithRegularStock_ShouldAdjustQuantity() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setQuantidadeEstoque(10);

        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));
        when(produtoRepository.save(produto)).thenReturn(produto);

        produtoService.ajustarEstoque(1L, 5);

        assertEquals(15, produto.getQuantidadeEstoque());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testAjustarEstoque_WithInfiniteStock_ShouldNotAdjustQuantity() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setQuantidadeEstoque(-1);

        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));

        produtoService.ajustarEstoque(1L, 5);

        assertEquals(-1, produto.getQuantidadeEstoque());
        verify(produtoRepository, never()).save(produto);
    }

    @Test
    public void testReservarEstoque_WithRegularStock_ShouldReduceQuantity() {
        // Create test data
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(10);
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        
        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(3);
        item.setPrecoUnitario(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("30.00"));
        
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setItens(java.util.Arrays.asList(item));
        
        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));
        when(produtoRepository.save(produto)).thenReturn(produto);

        // Test the method
        produtoService.reservarEstoque(pedido);

        // Verify the result
        assertEquals(7, produto.getQuantidadeEstoque());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void testReservarEstoque_WithInfiniteStock_ShouldNotChangeQuantity() {
        // Create test data
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(-1);
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        
        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(3);
        item.setPrecoUnitario(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("30.00"));
        
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setItens(java.util.Arrays.asList(item));
        
        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));

        // Test the method
        produtoService.reservarEstoque(pedido);

        // Verify the result
        assertEquals(-1, produto.getQuantidadeEstoque());
        verify(produtoRepository, never()).save(produto);
    }

    @Test
    public void testCancelarReservaEstoque_WithRegularStock_ShouldRestoreQuantity() {
        // Create test data
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setQuantidadeEstoque(7); // After reservation
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        
        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(3);
        item.setPrecoUnitario(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("30.00"));
        
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setItens(java.util.Arrays.asList(item));
        
        when(produtoRepository.findByIdWithLock(1L)).thenReturn(java.util.Optional.of(produto));
        when(produtoRepository.save(produto)).thenReturn(produto);

        // Test the method
        produtoService.cancelarReservaEstoque(pedido);

        // Verify the result
        assertEquals(10, produto.getQuantidadeEstoque()); // Restored to original
        verify(produtoRepository, times(1)).save(produto);
    }
}