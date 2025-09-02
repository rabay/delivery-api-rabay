package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class ProdutoTest {

    private Produto produto;

    @BeforeEach
    public void setUp() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .categoria("Categoria Teste")
                .descricao("Descrição Teste")
                .preco(new BigDecimal("10.00"))
                .disponivel(true)
                .excluido(false)
                .quantidadeEstoque(10)
                .build();
    }

    @Test
    public void testIsInfiniteStock_WithNegativeValue_ShouldReturnTrue() {
        produto.setQuantidadeEstoque(-1);
        assertTrue(produto.isInfiniteStock());
        
        produto.setQuantidadeEstoque(-5);
        assertTrue(produto.isInfiniteStock());
    }

    @Test
    public void testIsInfiniteStock_WithNonNegativeValue_ShouldReturnFalse() {
        produto.setQuantidadeEstoque(0);
        assertFalse(produto.isInfiniteStock());
        
        produto.setQuantidadeEstoque(10);
        assertFalse(produto.isInfiniteStock());
    }

    @Test
    public void testIsDisponivel_WithInfiniteStock_ShouldReturnTrue() {
        produto.setQuantidadeEstoque(-1);
        produto.setDisponivel(true);
        assertTrue(produto.isDisponivel());
    }

    @Test
    public void testIsDisponivel_WithPositiveStockAndAvailable_ShouldReturnTrue() {
        produto.setQuantidadeEstoque(5);
        produto.setDisponivel(true);
        assertTrue(produto.isDisponivel());
    }

    @Test
    public void testIsDisponivel_WithZeroStockAndAvailable_ShouldReturnFalse() {
        produto.setQuantidadeEstoque(0);
        produto.setDisponivel(true);
        assertFalse(produto.isDisponivel());
    }

    @Test
    public void testIsDisponivel_WithPositiveStockAndNotAvailable_ShouldReturnFalse() {
        produto.setQuantidadeEstoque(5);
        produto.setDisponivel(false);
        assertFalse(produto.isDisponivel());
    }

    @Test
    public void testReduzirEstoque_WithRegularStock_ShouldReduceQuantity() {
        produto.setQuantidadeEstoque(10);
        produto.reduzirEstoque(3);
        assertEquals(7, produto.getQuantidadeEstoque());
    }

    @Test
    public void testReduzirEstoque_WithInfiniteStock_ShouldNotChangeQuantity() {
        produto.setQuantidadeEstoque(-1);
        produto.reduzirEstoque(5);
        assertEquals(-1, produto.getQuantidadeEstoque());
    }

    @Test
    public void testReduzirEstoque_WithNullQuantity_ShouldNotChangeQuantity() {
        produto.setQuantidadeEstoque(10);
        produto.reduzirEstoque(null);
        assertEquals(10, produto.getQuantidadeEstoque());
    }

    @Test
    public void testAumentarEstoque_WithRegularStock_ShouldIncreaseQuantity() {
        produto.setQuantidadeEstoque(10);
        produto.aumentarEstoque(5);
        assertEquals(15, produto.getQuantidadeEstoque());
    }

    @Test
    public void testAumentarEstoque_WithInfiniteStock_ShouldNotChangeQuantity() {
        produto.setQuantidadeEstoque(-1);
        produto.aumentarEstoque(5);
        assertEquals(-1, produto.getQuantidadeEstoque());
    }

    @Test
    public void testAumentarEstoque_WithNullQuantity_ShouldNotChangeQuantity() {
        produto.setQuantidadeEstoque(10);
        produto.aumentarEstoque(null);
        assertEquals(10, produto.getQuantidadeEstoque());
    }
}