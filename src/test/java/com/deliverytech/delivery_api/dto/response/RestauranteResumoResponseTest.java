package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para RestauranteResumoResponse Builder")
class RestauranteResumoResponseTest {

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com builder padrão")
  void deveCriarRestauranteResumoResponseComBuilderPadrao() {
    // Given & When
    RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder().build();

    // Then
    assertNotNull(restaurante);
    assertNull(restaurante.getId());
    assertNull(restaurante.getNome());
    assertNull(restaurante.getCategoria());
    assertNull(restaurante.getTaxaEntrega());
    assertNull(restaurante.getTempoEntregaMinutos());
    assertNull(restaurante.getAvaliacao());
    assertNull(restaurante.getAtivo());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com todos os campos usando builder")
  void deveCriarRestauranteResumoResponseComTodosCampos() {
    // Given
    Long id = 1L;
    String nome = "Restaurante do Zé";
    String categoria = "Comida Brasileira";
    BigDecimal taxaEntrega = new BigDecimal("5.00");
    Integer tempoEntregaMinutos = 30;
    BigDecimal avaliacao = new BigDecimal("4.5");
    Boolean ativo = true;

    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(id)
            .nome(nome)
            .categoria(categoria)
            .taxaEntrega(taxaEntrega)
            .tempoEntregaMinutos(tempoEntregaMinutos)
            .avaliacao(avaliacao)
            .ativo(ativo)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(id, restaurante.getId());
    assertEquals(nome, restaurante.getNome());
    assertEquals(categoria, restaurante.getCategoria());
    assertEquals(taxaEntrega, restaurante.getTaxaEntrega());
    assertEquals(tempoEntregaMinutos, restaurante.getTempoEntregaMinutos());
    assertEquals(avaliacao, restaurante.getAvaliacao());
    assertEquals(ativo, restaurante.getAtivo());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com campos essenciais")
  void deveCriarRestauranteResumoResponseComCamposEssenciais() {
    // Given
    Long id = 2L;
    String nome = "Pizzaria Italiana";
    String categoria = "Pizzas";
    BigDecimal taxaEntrega = new BigDecimal("8.00");
    Boolean ativo = true;

    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(id)
            .nome(nome)
            .categoria(categoria)
            .taxaEntrega(taxaEntrega)
            .ativo(ativo)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(id, restaurante.getId());
    assertEquals(nome, restaurante.getNome());
    assertEquals(categoria, restaurante.getCategoria());
    assertEquals(taxaEntrega, restaurante.getTaxaEntrega());
    assertEquals(ativo, restaurante.getAtivo());
    // Campos opcionais devem ser null
    assertNull(restaurante.getTempoEntregaMinutos());
    assertNull(restaurante.getAvaliacao());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com valores BigDecimal")
  void deveCriarRestauranteResumoResponseComValoresBigDecimal() {
    // Given
    BigDecimal taxaEntrega = new BigDecimal("12.50");
    BigDecimal avaliacao = new BigDecimal("4.8");

    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(3L)
            .nome("Restaurante Premium")
            .categoria("Gastronomia")
            .taxaEntrega(taxaEntrega)
            .tempoEntregaMinutos(45)
            .avaliacao(avaliacao)
            .ativo(true)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(new BigDecimal("12.50"), restaurante.getTaxaEntrega());
    assertEquals(new BigDecimal("4.8"), restaurante.getAvaliacao());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com dados de entrega")
  void deveCriarRestauranteResumoResponseComDadosEntrega() {
    // Given
    BigDecimal taxaEntrega = BigDecimal.ZERO;
    Integer tempoEntrega = 15;

    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(4L)
            .nome("Fast Food Express")
            .categoria("Fast Food")
            .taxaEntrega(taxaEntrega)
            .tempoEntregaMinutos(tempoEntrega)
            .avaliacao(new BigDecimal("4.0"))
            .ativo(true)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(BigDecimal.ZERO, restaurante.getTaxaEntrega());
    assertEquals(15, restaurante.getTempoEntregaMinutos());
  }

  @Test
  @DisplayName("Deve permitir restaurante inativo")
  void devePermitirRestauranteInativo() {
    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(5L)
            .nome("Restaurante Temporariamente Fechado")
            .categoria("Temporário")
            .taxaEntrega(new BigDecimal("5.00"))
            .ativo(false)
            .build();

    // Then
    assertNotNull(restaurante);
    assertFalse(restaurante.getAtivo());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com alta avaliação")
  void deveCriarRestauranteResumoResponseComAltaAvaliacao() {
    // Given
    BigDecimal avaliacaoMaxima = new BigDecimal("5.0");

    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(6L)
            .nome("Restaurante Cinco Estrelas")
            .categoria("Alta Gastronomia")
            .taxaEntrega(new BigDecimal("15.00"))
            .tempoEntregaMinutos(60)
            .avaliacao(avaliacaoMaxima)
            .ativo(true)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(new BigDecimal("5.0"), restaurante.getAvaliacao());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse com builder fluente")
  void deveCriarRestauranteResumoResponseComBuilderFluente() {
    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(7L)
            .nome("Cafeteria Central")
            .categoria("Cafeteria")
            .taxaEntrega(new BigDecimal("3.00"))
            .tempoEntregaMinutos(10)
            .avaliacao(new BigDecimal("4.2"))
            .ativo(true)
            .build();

    // Then
    assertNotNull(restaurante);
    assertEquals(7L, restaurante.getId());
    assertEquals("Cafeteria Central", restaurante.getNome());
    assertEquals("Cafeteria", restaurante.getCategoria());
    assertEquals(new BigDecimal("3.00"), restaurante.getTaxaEntrega());
    assertEquals(10, restaurante.getTempoEntregaMinutos());
    assertEquals(new BigDecimal("4.2"), restaurante.getAvaliacao());
    assertTrue(restaurante.getAtivo());
  }

  @Test
  @DisplayName("Deve criar RestauranteResumoResponse sem avaliação")
  void deveCriarRestauranteResumoResponseSemAvaliacao() {
    // When
    RestauranteResumoResponse restaurante =
        RestauranteResumoResponse.builder()
            .id(8L)
            .nome("Restaurante Novo")
            .categoria("Novo")
            .taxaEntrega(new BigDecimal("7.00"))
            .tempoEntregaMinutos(25)
            .ativo(true)
            .build();

    // Then
    assertNotNull(restaurante);
    assertNull(restaurante.getAvaliacao());
  }
}
