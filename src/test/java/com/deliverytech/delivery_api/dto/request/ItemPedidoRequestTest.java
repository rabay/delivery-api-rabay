package com.deliverytech.delivery_api.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ItemPedidoRequest")
class ItemPedidoRequestTest {

  @Test
  @DisplayName("Deve criar ItemPedidoRequest com construtor padrão")
  void deveCriarItemPedidoRequestComConstrutorPadrao() {
    // Given & When
    ItemPedidoRequest item = new ItemPedidoRequest();

    // Then
    assertNotNull(item);
    assertNull(item.getProdutoId());
    assertNull(item.getQuantidade());
    assertNull(item.getPrecoUnitario());
  }

  @Test
  @DisplayName("Deve criar ItemPedidoRequest com construtor parametrizado")
  void deveCriarItemPedidoRequestComConstrutorParametrizado() {
    // Given
    Long produtoId = 1L;
    Integer quantidade = 2;

    // When
    ItemPedidoRequest item = new ItemPedidoRequest(produtoId, quantidade);

    // Then
    assertNotNull(item);
    assertEquals(produtoId, item.getProdutoId());
    assertEquals(quantidade, item.getQuantidade());
    assertNull(item.getPrecoUnitario()); // Não é definido no construtor
  }

  @Test
  @DisplayName("Deve definir e obter produtoId")
  void deveDefinirEObterProdutoId() {
    // Given
    ItemPedidoRequest item = new ItemPedidoRequest();
    Long produtoId = 5L;

    // When
    item.setProdutoId(produtoId);

    // Then
    assertEquals(produtoId, item.getProdutoId());
  }

  @Test
  @DisplayName("Deve definir e obter quantidade")
  void deveDefinirEObterQuantidade() {
    // Given
    ItemPedidoRequest item = new ItemPedidoRequest();
    Integer quantidade = 3;

    // When
    item.setQuantidade(quantidade);

    // Then
    assertEquals(quantidade, item.getQuantidade());
  }

  @Test
  @DisplayName("Deve definir e obter precoUnitario")
  void deveDefinirEObterPrecoUnitario() {
    // Given
    ItemPedidoRequest item = new ItemPedidoRequest();
    BigDecimal precoUnitario = new BigDecimal("15.50");

    // When
    item.setPrecoUnitario(precoUnitario);

    // Then
    assertEquals(precoUnitario, item.getPrecoUnitario());
  }

  @Test
  @DisplayName("Deve criar ItemPedidoRequest completo")
  void deveCriarItemPedidoRequestCompleto() {
    // Given
    Long produtoId = 10L;
    Integer quantidade = 5;
    BigDecimal precoUnitario = new BigDecimal("25.00");

    // When
    ItemPedidoRequest item = new ItemPedidoRequest();
    item.setProdutoId(produtoId);
    item.setQuantidade(quantidade);
    item.setPrecoUnitario(precoUnitario);

    // Then
    assertNotNull(item);
    assertEquals(produtoId, item.getProdutoId());
    assertEquals(quantidade, item.getQuantidade());
    assertEquals(precoUnitario, item.getPrecoUnitario());
  }

  @Test
  @DisplayName("Deve permitir valores null")
  void devePermitirValoresNull() {
    // Given
    ItemPedidoRequest item = new ItemPedidoRequest(1L, 1);

    // When
    item.setProdutoId(null);
    item.setQuantidade(null);
    item.setPrecoUnitario(null);

    // Then
    assertNull(item.getProdutoId());
    assertNull(item.getQuantidade());
    assertNull(item.getPrecoUnitario());
  }

  @Test
  @DisplayName("Deve manter valores após múltiplas definições")
  void deveManterValoresAposMultiplasDefinicoes() {
    // Given
    ItemPedidoRequest item = new ItemPedidoRequest();

    // When
    item.setProdutoId(1L);
    item.setProdutoId(2L);
    item.setQuantidade(5);
    item.setQuantidade(10);
    item.setPrecoUnitario(new BigDecimal("10.00"));
    item.setPrecoUnitario(new BigDecimal("20.00"));

    // Then
    assertEquals(2L, item.getProdutoId());
    assertEquals(10, item.getQuantidade());
    assertEquals(new BigDecimal("20.00"), item.getPrecoUnitario());
  }

  @Test
  @DisplayName("Deve criar ItemPedidoRequest com quantidade mínima")
  void deveCriarItemPedidoRequestComQuantidadeMinima() {
    // Given
    Long produtoId = 100L;
    Integer quantidade = 1;

    // When
    ItemPedidoRequest item = new ItemPedidoRequest(produtoId, quantidade);

    // Then
    assertNotNull(item);
    assertEquals(produtoId, item.getProdutoId());
    assertEquals(quantidade, item.getQuantidade());
  }

  @Test
  @DisplayName("Deve criar ItemPedidoRequest com quantidade alta")
  void deveCriarItemPedidoRequestComQuantidadeAlta() {
    // Given
    Long produtoId = 50L;
    Integer quantidade = 100;

    // When
    ItemPedidoRequest item = new ItemPedidoRequest(produtoId, quantidade);

    // Then
    assertNotNull(item);
    assertEquals(produtoId, item.getProdutoId());
    assertEquals(quantidade, item.getQuantidade());
  }
}
