package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ApiResultMapPaged")
class ApiResultMapPagedTest {

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com construtor padrão")
  void deveCriarApiResultMapPagedComConstrutorPadrao() {
    // When
    ApiResultMapPaged result = new ApiResultMapPaged();

    // Then
    assertNotNull(result);
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados de mapas paginados")
  void deveDefinirEObterDadosMapasPaginados() {
    // Given
    Map<String, Object> map1 = new HashMap<>();
    map1.put("id", 1L);
    map1.put("nome", "Item 1");
    map1.put("valor", 100.50);
    map1.put("ativo", true);

    Map<String, Object> map2 = new HashMap<>();
    map2.put("id", 2L);
    map2.put("nome", "Item 2");
    map2.put("valor", 200.75);
    map2.put("ativo", false);

    List<Map<String, Object>> items = List.of(map1, map2);
    Map<String, String> links =
        Map.of("first", "/api/generic?page=0", "last", "/api/generic?page=3");

    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 67, 0, 10, 7, links, "Dados genéricos", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Consulta genérica realizada com sucesso";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(2, result.data.items().size());
    assertEquals(67, result.data.totalItems());
    assertEquals(0, result.data.page());
    assertEquals(10, result.data.size());
    assertEquals(7, result.data.totalPages());
    assertEquals("Consulta genérica realizada com sucesso", result.message);
    assertTrue(result.success);

    // Verificar dados dos mapas
    assertEquals(1L, result.data.items().get(0).get("id"));
    assertEquals("Item 1", result.data.items().get(0).get("nome"));
    assertEquals(100.50, result.data.items().get(0).get("valor"));
    assertEquals(true, result.data.items().get(0).get("ativo"));
    assertEquals(2L, result.data.items().get(1).get("id"));
    assertEquals("Item 2", result.data.items().get(1).get("nome"));
    assertEquals(200.75, result.data.items().get(1).get("valor"));
    assertEquals(false, result.data.items().get(1).get("ativo"));
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com dados vazios")
  void deveCriarApiResultMapPagedComDadosVazios() {
    // Given
    List<Map<String, Object>> items = List.of();
    Map<String, String> links = Map.of("first", "/api/generic?page=0");
    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 0, 0, 10, 0, links, "Nenhum dado encontrado", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Resultado vazio";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertTrue(result.data.items().isEmpty());
    assertEquals(0, result.data.totalItems());
    assertEquals(0, result.data.totalPages());
    assertEquals("Resultado vazio", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com status de erro")
  void deveCriarApiResultMapPagedComStatusErro() {
    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = null;
    result.message = "Erro ao consultar dados genéricos";
    result.success = false;

    // Then
    assertNull(result.data);
    assertEquals("Erro ao consultar dados genéricos", result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados nulos")
  void deveDefinirEObterDadosNulos() {
    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = null;
    result.message = null;
    result.success = false;

    // Then
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com mapa simples")
  void deveCriarApiResultMapPagedComMapaSimples() {
    // Given
    Map<String, Object> simpleMap = new HashMap<>();
    simpleMap.put("key", "value");
    simpleMap.put("number", 42);
    simpleMap.put("flag", true);

    List<Map<String, Object>> items = List.of(simpleMap);
    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Mapa simples", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Mapa simples";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals("value", result.data.items().get(0).get("key"));
    assertEquals(42, result.data.items().get(0).get("number"));
    assertEquals(true, result.data.items().get(0).get("flag"));
    assertEquals("Mapa simples", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com mapa complexo")
  void deveCriarApiResultMapPagedComMapaComplexo() {
    // Given
    Map<String, Object> complexMap = new HashMap<>();
    complexMap.put("id", 100L);
    complexMap.put("nome", "Objeto Complexo");
    complexMap.put("dados", Map.of("subkey", "subvalue", "count", 5));
    complexMap.put("lista", List.of("item1", "item2", "item3"));
    complexMap.put("timestamp", System.currentTimeMillis());

    List<Map<String, Object>> items = List.of(complexMap);
    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Mapa complexo", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Mapa complexo";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(100L, result.data.items().get(0).get("id"));
    assertEquals("Objeto Complexo", result.data.items().get(0).get("nome"));
    assertNotNull(result.data.items().get(0).get("dados"));
    assertNotNull(result.data.items().get(0).get("lista"));
    assertNotNull(result.data.items().get(0).get("timestamp"));
    assertEquals("Mapa complexo", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com mapa vazio")
  void deveCriarApiResultMapPagedComMapaVazio() {
    // Given
    Map<String, Object> emptyMap = new HashMap<>();
    List<Map<String, Object>> items = List.of(emptyMap);
    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Mapa vazio", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Mapa vazio";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertTrue(result.data.items().get(0).isEmpty());
    assertEquals("Mapa vazio", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultMapPaged com múltiplos tipos de dados")
  void deveCriarApiResultMapPagedComMultiplosTiposDados() {
    // Given
    Map<String, Object> mixedMap = new HashMap<>();
    mixedMap.put("string", "texto");
    mixedMap.put("integer", 123);
    mixedMap.put("long", 123456789L);
    mixedMap.put("double", 123.45);
    mixedMap.put("boolean", true);
    mixedMap.put("null", null);

    List<Map<String, Object>> items = List.of(mixedMap);
    Map<String, String> links =
        Map.of(
            "first",
            "/api/mixed?page=0",
            "prev",
            "/api/mixed?page=1",
            "self",
            "/api/mixed?page=2",
            "next",
            "/api/mixed?page=3",
            "last",
            "/api/mixed?page=4");

    PagedResponse<Map<String, Object>> pagedData =
        new PagedResponse<>(items, 100, 2, 10, 10, links, "Dados mistos na página 3", true);

    // When
    ApiResultMapPaged result = new ApiResultMapPaged();
    result.data = pagedData;
    result.message = "Dados mistos na página 3";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(1, result.data.items().size());
    assertEquals(100, result.data.totalItems());
    assertEquals(2, result.data.page());
    assertEquals(10, result.data.totalPages());
    assertEquals(5, result.data.links().size());
    assertEquals("texto", result.data.items().get(0).get("string"));
    assertEquals(123, result.data.items().get(0).get("integer"));
    assertEquals(123456789L, result.data.items().get(0).get("long"));
    assertEquals(123.45, result.data.items().get(0).get("double"));
    assertEquals(true, result.data.items().get(0).get("boolean"));
    assertNull(result.data.items().get(0).get("null"));
    assertEquals("Dados mistos na página 3", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve manter valores após múltiplas definições")
  void deveManterValoresAposMultiplasDefinicoes() {
    // Given
    ApiResultMapPaged result = new ApiResultMapPaged();

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
