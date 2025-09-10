package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ApiResultRestaurantePaged")
class ApiResultRestaurantePagedTest {

  @Test
  @DisplayName("Deve criar ApiResultRestaurantePaged com construtor padrão")
  void deveCriarApiResultRestaurantePagedComConstrutorPadrao() {
    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();

    // Then
    assertNotNull(result);
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados de restaurantes paginados")
  void deveDefinirEObterDadosRestaurantesPaginados() {
    // Given
    RestauranteResponse restaurante1 = new RestauranteResponse();
    restaurante1.setId(1L);
    restaurante1.setNome("Pizzaria do João");
    restaurante1.setCategoria("Pizzas");
    restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
    restaurante1.setTempoEntregaMinutos(30);
    restaurante1.setAtivo(true);

    RestauranteResponse restaurante2 = new RestauranteResponse();
    restaurante2.setId(2L);
    restaurante2.setNome("Hamburgueria Express");
    restaurante2.setCategoria("Hambúrgueres");
    restaurante2.setTaxaEntrega(new BigDecimal("3.50"));
    restaurante2.setTempoEntregaMinutos(20);
    restaurante2.setAtivo(true);

    List<RestauranteResponse> items = List.of(restaurante1, restaurante2);
    Map<String, String> links =
        Map.of("first", "/api/restaurantes?page=0", "last", "/api/restaurantes?page=3");

    PagedResponse<RestauranteResponse> pagedData =
        new PagedResponse<>(items, 35, 0, 10, 4, links, "Restaurantes retornados", true);

    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = pagedData;
    result.message = "Consulta de restaurantes realizada com sucesso";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(2, result.data.items().size());
    assertEquals(35, result.data.totalItems());
    assertEquals(0, result.data.page());
    assertEquals(10, result.data.size());
    assertEquals(4, result.data.totalPages());
    assertEquals("Consulta de restaurantes realizada com sucesso", result.message);
    assertTrue(result.success);

    // Verificar dados dos restaurantes
    assertEquals("Pizzaria do João", result.data.items().get(0).getNome());
    assertEquals(new BigDecimal("5.00"), result.data.items().get(0).getTaxaEntrega());
    assertEquals(30, result.data.items().get(0).getTempoEntregaMinutos());
    assertEquals("Hamburgueria Express", result.data.items().get(1).getNome());
    assertEquals(new BigDecimal("3.50"), result.data.items().get(1).getTaxaEntrega());
    assertEquals(20, result.data.items().get(1).getTempoEntregaMinutos());
  }

  @Test
  @DisplayName("Deve criar ApiResultRestaurantePaged com dados vazios")
  void deveCriarApiResultRestaurantePagedComDadosVazios() {
    // Given
    List<RestauranteResponse> items = List.of();
    Map<String, String> links = Map.of("first", "/api/restaurantes?page=0");
    PagedResponse<RestauranteResponse> pagedData =
        new PagedResponse<>(items, 0, 0, 10, 0, links, "Nenhum restaurante encontrado", true);

    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
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
  @DisplayName("Deve criar ApiResultRestaurantePaged com status de erro")
  void deveCriarApiResultRestaurantePagedComStatusErro() {
    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = null;
    result.message = "Erro ao consultar restaurantes";
    result.success = false;

    // Then
    assertNull(result.data);
    assertEquals("Erro ao consultar restaurantes", result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados nulos")
  void deveDefinirEObterDadosNulos() {
    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = null;
    result.message = null;
    result.success = false;

    // Then
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRestaurantePaged com restaurantes inativos")
  void deveCriarApiResultRestaurantePagedComRestaurantesInativos() {
    // Given
    RestauranteResponse restaurante = new RestauranteResponse();
    restaurante.setId(50L);
    restaurante.setNome("Restaurante Fechado");
    restaurante.setCategoria("Fechado Temporariamente");
    restaurante.setTaxaEntrega(BigDecimal.ZERO);
    restaurante.setTempoEntregaMinutos(0);
    restaurante.setAtivo(false);

    List<RestauranteResponse> items = List.of(restaurante);
    PagedResponse<RestauranteResponse> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Restaurante encontrado", true);

    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = pagedData;
    result.message = "Restaurante inativo encontrado";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertFalse(result.data.items().get(0).getAtivo());
    assertEquals(BigDecimal.ZERO, result.data.items().get(0).getTaxaEntrega());
    assertEquals(0, result.data.items().get(0).getTempoEntregaMinutos());
    assertEquals("Restaurante inativo encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRestaurantePaged com restaurantes sem taxa de entrega")
  void deveCriarApiResultRestaurantePagedComRestaurantesSemTaxaEntrega() {
    // Given
    RestauranteResponse restaurante = new RestauranteResponse();
    restaurante.setId(60L);
    restaurante.setNome("Restaurante Delivery Grátis");
    restaurante.setCategoria("Entrega Gratuita");
    restaurante.setTaxaEntrega(BigDecimal.ZERO);
    restaurante.setTempoEntregaMinutos(45);
    restaurante.setAtivo(true);

    List<RestauranteResponse> items = List.of(restaurante);
    PagedResponse<RestauranteResponse> pagedData =
        new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Restaurante com entrega gratuita encontrado", true);

    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = pagedData;
    result.message = "Restaurante com entrega gratuita encontrado";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(BigDecimal.ZERO, result.data.items().get(0).getTaxaEntrega());
    assertEquals(45, result.data.items().get(0).getTempoEntregaMinutos());
    assertTrue(result.data.items().get(0).getAtivo());
    assertEquals("Restaurante com entrega gratuita encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultRestaurantePaged com restaurantes de entrega rápida")
  void deveCriarApiResultRestaurantePagedComRestaurantesEntregaRapida() {
    // Given
    RestauranteResponse restaurante = new RestauranteResponse();
    restaurante.setId(70L);
    restaurante.setNome("Fast Food Express");
    restaurante.setCategoria("Fast Food");
    restaurante.setTaxaEntrega(new BigDecimal("2.00"));
    restaurante.setTempoEntregaMinutos(15);
    restaurante.setAtivo(true);

    List<RestauranteResponse> items = List.of(restaurante);
    PagedResponse<RestauranteResponse> pagedData =
        new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Restaurante de entrega rápida encontrado", true);

    // When
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();
    result.data = pagedData;
    result.message = "Restaurante de entrega rápida encontrado";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(new BigDecimal("2.00"), result.data.items().get(0).getTaxaEntrega());
    assertEquals(15, result.data.items().get(0).getTempoEntregaMinutos());
    assertTrue(result.data.items().get(0).getAtivo());
    assertEquals("Restaurante de entrega rápida encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve manter valores após múltiplas definições")
  void deveManterValoresAposMultiplasDefinicoes() {
    // Given
    ApiResultRestaurantePaged result = new ApiResultRestaurantePaged();

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
