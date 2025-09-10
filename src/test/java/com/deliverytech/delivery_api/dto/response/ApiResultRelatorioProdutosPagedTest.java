package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ApiResultRelatorioProdutosPaged")
class ApiResultRelatorioProdutosPagedTest {

  // Classe mock para implementar RelatorioVendasProdutos
  static class MockRelatorioVendasProdutos
      implements com.deliverytech.delivery_api.projection.RelatorioVendasProdutos {
    private final Long idProduto;
    private final String nomeProduto;
    private final BigDecimal totalVendas;
    private final Long quantidadeItemPedido;

    public MockRelatorioVendasProdutos(
        Long idProduto, String nomeProduto, BigDecimal totalVendas, Long quantidadeItemPedido) {
      this.idProduto = idProduto;
      this.nomeProduto = nomeProduto;
      this.totalVendas = totalVendas;
      this.quantidadeItemPedido = quantidadeItemPedido;
    }

    @Override
    public Long getIdProduto() {
      return idProduto;
    }

    @Override
    public String getNomeProduto() {
      return nomeProduto;
    }

    @Override
    public BigDecimal getTotalVendas() {
      return totalVendas;
    }

    @Override
    public Long getQuantidadeItemPedido() {
      return quantidadeItemPedido;
    }
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com construtor padrão")
  void deveCriarApiResultRelatorioProdutosPagedComConstrutorPadrao() {
    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();

    // Then
    assertNotNull(result);
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados de relatório de produtos paginado")
  void deveDefinirEObterDadosRelatorioProdutosPaginado() {
    // Given
    MockRelatorioVendasProdutos produto1 =
        new MockRelatorioVendasProdutos(1L, "Pizza Margherita", new BigDecimal("299.00"), 10L);
    MockRelatorioVendasProdutos produto2 =
        new MockRelatorioVendasProdutos(2L, "Refrigerante Cola", new BigDecimal("45.00"), 15L);

    List<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> items =
        List.of(produto1, produto2);
    Map<String, String> links =
        Map.of(
            "first", "/api/relatorios/produtos?page=0", "last", "/api/relatorios/produtos?page=4");

    PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> pagedData =
        new PagedResponse<>(items, 89, 0, 10, 9, links, "Relatório de produtos", true);

    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = pagedData;
    result.message = "Relatório de produtos gerado com sucesso";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(2, result.data.items().size());
    assertEquals(89, result.data.totalItems());
    assertEquals(0, result.data.page());
    assertEquals(10, result.data.size());
    assertEquals(9, result.data.totalPages());
    assertEquals("Relatório de produtos gerado com sucesso", result.message);
    assertTrue(result.success);

    // Verificar dados dos produtos
    assertEquals(1L, result.data.items().get(0).getIdProduto());
    assertEquals("Pizza Margherita", result.data.items().get(0).getNomeProduto());
    assertEquals(new BigDecimal("299.00"), result.data.items().get(0).getTotalVendas());
    assertEquals(10L, result.data.items().get(0).getQuantidadeItemPedido());
    assertEquals(2L, result.data.items().get(1).getIdProduto());
    assertEquals("Refrigerante Cola", result.data.items().get(1).getNomeProduto());
    assertEquals(new BigDecimal("45.00"), result.data.items().get(1).getTotalVendas());
    assertEquals(15L, result.data.items().get(1).getQuantidadeItemPedido());
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com dados vazios")
  void deveCriarApiResultRelatorioProdutosPagedComDadosVazios() {
    // Given
    List<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> items = List.of();
    Map<String, String> links = Map.of("first", "/api/relatorios/produtos?page=0");
    PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> pagedData =
        new PagedResponse<>(items, 0, 0, 10, 0, links, "Nenhum produto encontrado", true);

    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = pagedData;
    result.message = "Relatório vazio";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertTrue(result.data.items().isEmpty());
    assertEquals(0, result.data.totalItems());
    assertEquals(0, result.data.totalPages());
    assertEquals("Relatório vazio", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com status de erro")
  void deveCriarApiResultRelatorioProdutosPagedComStatusErro() {
    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = null;
    result.message = "Erro ao gerar relatório de produtos";
    result.success = false;

    // Then
    assertNull(result.data);
    assertEquals("Erro ao gerar relatório de produtos", result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados nulos")
  void deveDefinirEObterDadosNulos() {
    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = null;
    result.message = null;
    result.success = false;

    // Then
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com produto mais vendido")
  void deveCriarApiResultRelatorioProdutosPagedComProdutoMaisVendido() {
    // Given
    MockRelatorioVendasProdutos produto =
        new MockRelatorioVendasProdutos(
            100L, "Produto Mais Vendido", new BigDecimal("5000.00"), 200L);

    List<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> items = List.of(produto);
    PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Produto mais vendido", true);

    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = pagedData;
    result.message = "Produto mais vendido";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(100L, result.data.items().get(0).getIdProduto());
    assertEquals("Produto Mais Vendido", result.data.items().get(0).getNomeProduto());
    assertEquals(new BigDecimal("5000.00"), result.data.items().get(0).getTotalVendas());
    assertEquals(200L, result.data.items().get(0).getQuantidadeItemPedido());
    assertEquals("Produto mais vendido", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com produto sem vendas")
  void deveCriarApiResultRelatorioProdutosPagedComProdutoSemVendas() {
    // Given
    MockRelatorioVendasProdutos produto =
        new MockRelatorioVendasProdutos(200L, "Produto Novo", BigDecimal.ZERO, 0L);

    List<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> items = List.of(produto);
    PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Produto sem vendas", true);

    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = pagedData;
    result.message = "Produto sem vendas";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(BigDecimal.ZERO, result.data.items().get(0).getTotalVendas());
    assertEquals(0L, result.data.items().get(0).getQuantidadeItemPedido());
    assertEquals("Produto sem vendas", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRelatorioProdutosPaged com produto de alto volume")
  void deveCriarApiResultRelatorioProdutosPagedComProdutoAltoVolume() {
    // Given
    MockRelatorioVendasProdutos produto =
        new MockRelatorioVendasProdutos(300L, "Produto Popular", new BigDecimal("2500.00"), 500L);

    List<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> items = List.of(produto);
    Map<String, String> links =
        Map.of(
            "first",
            "/api/relatorios/produtos?page=0",
            "prev",
            "/api/relatorios/produtos?page=4",
            "self",
            "/api/relatorios/produtos?page=5",
            "next",
            "/api/relatorios/produtos?page=6",
            "last",
            "/api/relatorios/produtos?page=9");

    PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasProdutos> pagedData =
        new PagedResponse<>(items, 1000, 5, 10, 100, links, "Produto popular na página 6", true);

    // When
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();
    result.data = pagedData;
    result.message = "Produto popular na página 6";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(1000, result.data.totalItems());
    assertEquals(5, result.data.page());
    assertEquals(100, result.data.totalPages());
    assertEquals(5, result.data.links().size());
    assertEquals(new BigDecimal("2500.00"), result.data.items().get(0).getTotalVendas());
    assertEquals(500L, result.data.items().get(0).getQuantidadeItemPedido());
    assertEquals("Produto popular na página 6", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve manter valores após múltiplas definições")
  void deveManterValoresAposMultiplasDefinicoes() {
    // Given
    ApiResultRelatorioProdutosPaged result = new ApiResultRelatorioProdutosPaged();

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
