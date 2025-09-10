package com.deliverytech.delivery_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ProdutoRequest Builder")
class ProdutoRequestTest {

    @Test
    @DisplayName("Deve criar ProdutoRequest com builder padrão")
    void deveCriarProdutoRequestComBuilderPadrao() {
        // Given & When
        ProdutoRequest produto = ProdutoRequest.builder().build();

        // Then
        assertNotNull(produto);
        assertNull(produto.getNome());
        assertNull(produto.getCategoria());
        assertNull(produto.getDescricao());
        assertNull(produto.getPreco());
        assertNull(produto.getRestauranteId());
        assertTrue(produto.getDisponivel()); // valor padrão definido no @Builder.Default
        assertNull(produto.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve criar ProdutoRequest com todos os campos usando builder")
    void deveCriarProdutoRequestComTodosCampos() {
        // Given
        String nome = "Pizza Margherita";
        String categoria = "Pizzas";
        String descricao = "Pizza tradicional com molho de tomate, mussarela e manjericão";
        BigDecimal preco = new BigDecimal("29.90");
        Long restauranteId = 1L;
        Boolean disponivel = true;
        Integer quantidadeEstoque = 10;

        // When
        ProdutoRequest produto = ProdutoRequest.builder()
                .nome(nome)
                .categoria(categoria)
                .descricao(descricao)
                .preco(preco)
                .restauranteId(restauranteId)
                .disponivel(disponivel)
                .quantidadeEstoque(quantidadeEstoque)
                .build();

        // Then
        assertNotNull(produto);
        assertEquals(nome, produto.getNome());
        assertEquals(categoria, produto.getCategoria());
        assertEquals(descricao, produto.getDescricao());
        assertEquals(preco, produto.getPreco());
        assertEquals(restauranteId, produto.getRestauranteId());
        assertEquals(disponivel, produto.getDisponivel());
        assertEquals(quantidadeEstoque, produto.getQuantidadeEstoque());
    }

    @Test
    @DisplayName("Deve criar ProdutoRequest com campos obrigatórios apenas")
    void deveCriarProdutoRequestComCamposObrigatorios() {
        // Given
        String nome = "Coca-Cola";
        String categoria = "Bebidas";
        BigDecimal preco = new BigDecimal("5.00");
        Long restauranteId = 2L;
        Integer quantidadeEstoque = 50;

        // When
        ProdutoRequest produto = ProdutoRequest.builder()
                .nome(nome)
                .categoria(categoria)
                .preco(preco)
                .restauranteId(restauranteId)
                .quantidadeEstoque(quantidadeEstoque)
                .build();

        // Then
        assertNotNull(produto);
        assertEquals(nome, produto.getNome());
        assertEquals(categoria, produto.getCategoria());
        assertEquals(preco, produto.getPreco());
        assertEquals(restauranteId, produto.getRestauranteId());
        assertEquals(quantidadeEstoque, produto.getQuantidadeEstoque());
        assertTrue(produto.getDisponivel()); // valor padrão
        assertNull(produto.getDescricao()); // campo opcional
    }

    @Test
    @DisplayName("Deve permitir modificar disponibilidade no builder")
    void devePermitirModificarDisponibilidade() {
        // Given
        Boolean disponivel = false;

        // When
        ProdutoRequest produto = ProdutoRequest.builder()
                .nome("Produto Teste")
                .categoria("Teste")
                .preco(BigDecimal.ONE)
                .restauranteId(1L)
                .quantidadeEstoque(1)
                .disponivel(disponivel)
                .build();

        // Then
        assertNotNull(produto);
        assertFalse(produto.getDisponivel());
    }

    @Test
    @DisplayName("Deve criar ProdutoRequest com valores BigDecimal")
    void deveCriarProdutoRequestComValoresBigDecimal() {
        // Given
        BigDecimal preco = new BigDecimal("99.99");

        // When
        ProdutoRequest produto = ProdutoRequest.builder()
                .nome("Produto Premium")
                .categoria("Premium")
                .preco(preco)
                .restauranteId(1L)
                .quantidadeEstoque(5)
                .build();

        // Then
        assertNotNull(produto);
        assertEquals(new BigDecimal("99.99"), produto.getPreco());
    }

    @Test
    @DisplayName("Deve criar ProdutoRequest com builder fluente")
    void deveCriarProdutoRequestComBuilderFluente() {
        // When
        ProdutoRequest produto = ProdutoRequest.builder()
                .nome("Hambúrguer")
                .categoria("Lanches")
                .preco(new BigDecimal("15.00"))
                .restauranteId(3L)
                .quantidadeEstoque(20)
                .build();

        // Then
        assertNotNull(produto);
        assertEquals("Hambúrguer", produto.getNome());
        assertEquals("Lanches", produto.getCategoria());
        assertEquals(new BigDecimal("15.00"), produto.getPreco());
        assertEquals(3L, produto.getRestauranteId());
        assertEquals(20, produto.getQuantidadeEstoque());
    }
}