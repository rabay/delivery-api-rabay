package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResultProdutoPaged")
class ApiResultProdutoPagedTest {

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com construtor padrão")
    void deveCriarApiResultProdutoPagedComConstrutorPadrao() {
        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();

        // Then
        assertNotNull(result);
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados de produtos paginados")
    void deveDefinirEObterDadosProdutosPaginados() {
        // Given
        ProdutoResponse produto1 = new ProdutoResponse();
        produto1.setId(1L);
        produto1.setNome("Pizza Margherita");
        produto1.setDescricao("Pizza tradicional com molho de tomate e queijo");
        produto1.setPreco(new BigDecimal("35.90"));
        produto1.setDisponivel(true);

        ProdutoResponse produto2 = new ProdutoResponse();
        produto2.setId(2L);
        produto2.setNome("Refrigerante Cola");
        produto2.setDescricao("Refrigerante de cola 350ml");
        produto2.setPreco(new BigDecimal("5.50"));
        produto2.setDisponivel(true);

        List<ProdutoResponse> items = List.of(produto1, produto2);
        Map<String, String> links = Map.of(
            "first", "/api/produtos?page=0",
            "last", "/api/produtos?page=1"
        );

        PagedResponse<ProdutoResponse> pagedData = new PagedResponse<>(
            items, 12, 0, 10, 2, links, "Produtos retornados", true);

        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = pagedData;
        result.message = "Consulta de produtos realizada com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(2, result.data.items().size());
        assertEquals(12, result.data.totalItems());
        assertEquals(0, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(2, result.data.totalPages());
        assertEquals("Consulta de produtos realizada com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados dos produtos
        assertEquals("Pizza Margherita", result.data.items().get(0).getNome());
        assertEquals(new BigDecimal("35.90"), result.data.items().get(0).getPreco());
        assertEquals("Refrigerante Cola", result.data.items().get(1).getNome());
        assertEquals(new BigDecimal("5.50"), result.data.items().get(1).getPreco());
    }

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com dados vazios")
    void deveCriarApiResultProdutoPagedComDadosVazios() {
        // Given
        List<ProdutoResponse> items = List.of();
        Map<String, String> links = Map.of("first", "/api/produtos?page=0");
        PagedResponse<ProdutoResponse> pagedData = new PagedResponse<>(
            items, 0, 0, 10, 0, links, "Nenhum produto encontrado", true);

        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = pagedData;
        result.message = "Consulta sem resultados";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertTrue(result.data.items().isEmpty());
        assertEquals(0, result.data.totalItems());
        assertEquals(0, result.data.totalPages());
        assertEquals("Consulta sem resultados", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com status de erro")
    void deveCriarApiResultProdutoPagedComStatusErro() {
        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = null;
        result.message = "Erro ao consultar produtos";
        result.success = false;

        // Then
        assertNull(result.data);
        assertEquals("Erro ao consultar produtos", result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados nulos")
    void deveDefinirEObterDadosNulos() {
        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = null;
        result.message = null;
        result.success = false;

        // Then
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com produtos inativos")
    void deveCriarApiResultProdutoPagedComProdutosInativos() {
        // Given
        ProdutoResponse produto = new ProdutoResponse();
        produto.setId(100L);
        produto.setNome("Produto Descontinuado");
        produto.setDescricao("Este produto não está mais disponível");
        produto.setPreco(new BigDecimal("0.00"));
        produto.setDisponivel(false);

        List<ProdutoResponse> items = List.of(produto);
        PagedResponse<ProdutoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Produto encontrado", true);

        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = pagedData;
        result.message = "Produto inativo encontrado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertFalse(result.data.items().get(0).getDisponivel());
        assertEquals("Produto inativo encontrado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com produtos de preço zero")
    void deveCriarApiResultProdutoPagedComProdutosPrecoZero() {
        // Given
        ProdutoResponse produto = new ProdutoResponse();
        produto.setId(200L);
        produto.setNome("Produto Gratuito");
        produto.setDescricao("Produto promocional gratuito");
        produto.setPreco(BigDecimal.ZERO);
        produto.setDisponivel(true);

        List<ProdutoResponse> items = List.of(produto);
        PagedResponse<ProdutoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Produto gratuito encontrado", true);

        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = pagedData;
        result.message = "Produto gratuito encontrado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(BigDecimal.ZERO, result.data.items().get(0).getPreco());
        assertTrue(result.data.items().get(0).getDisponivel());
        assertEquals("Produto gratuito encontrado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultProdutoPaged com produtos de alto valor")
    void deveCriarApiResultProdutoPagedComProdutosAltoValor() {
        // Given
        ProdutoResponse produto = new ProdutoResponse();
        produto.setId(300L);
        produto.setNome("Produto Premium");
        produto.setDescricao("Produto de luxo com preço elevado");
        produto.setPreco(new BigDecimal("9999.99"));
        produto.setDisponivel(true);

        List<ProdutoResponse> items = List.of(produto);
        PagedResponse<ProdutoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Produto premium encontrado", true);

        // When
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();
        result.data = pagedData;
        result.message = "Produto premium encontrado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(new BigDecimal("9999.99"), result.data.items().get(0).getPreco());
        assertTrue(result.data.items().get(0).getDisponivel());
        assertEquals("Produto premium encontrado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        ApiResultProdutoPaged result = new ApiResultProdutoPaged();

        // When
        result.message = "Primeira mensagem";
        result.message = "Segunda mensagem";
        result.success = true;
        result.success = false;
        result.success = true;

        // Then
        assertEquals("Segunda mensagem", result.message);
        assertTrue(result.success);
    }
}