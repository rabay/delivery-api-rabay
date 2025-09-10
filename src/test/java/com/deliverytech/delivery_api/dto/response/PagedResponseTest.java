package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para PagedResponse")
class PagedResponseTest {

  @Test
  @DisplayName("Deve criar PagedResponse com dados completos")
  void deveCriarPagedResponseComDadosCompletos() {
    // Given
    List<String> items = List.of("Item 1", "Item 2", "Item 3");
    long totalItems = 100;
    int page = 0;
    int size = 10;
    int totalPages = 10;
    Map<String, String> links =
        Map.of(
            "first", "/api/items?page=0", "last", "/api/items?page=9", "next", "/api/items?page=1");
    String message = "Página retornada com sucesso";
    boolean success = true;

    // When
    PagedResponse<String> response =
        new PagedResponse<>(items, totalItems, page, size, totalPages, links, message, success);

    // Then
    assertNotNull(response);
    assertEquals(items, response.items());
    assertEquals(totalItems, response.totalItems());
    assertEquals(page, response.page());
    assertEquals(size, response.size());
    assertEquals(totalPages, response.totalPages());
    assertEquals(links, response.links());
    assertEquals(message, response.message());
    assertTrue(response.success());
  }

  @Test
  @DisplayName("Deve criar PagedResponse com lista vazia")
  void deveCriarPagedResponseComListaVazia() {
    // Given
    List<String> items = List.of();
    long totalItems = 0;
    int page = 0;
    int size = 10;
    int totalPages = 0;
    Map<String, String> links = Map.of("first", "/api/items?page=0");
    String message = "Nenhum item encontrado";
    boolean success = true;

    // When
    PagedResponse<String> response =
        new PagedResponse<>(items, totalItems, page, size, totalPages, links, message, success);

    // Then
    assertNotNull(response);
    assertTrue(response.items().isEmpty());
    assertEquals(0, response.totalItems());
    assertEquals(0, response.totalPages());
    assertEquals(message, response.message());
    assertTrue(response.success());
  }

  @Test
  @DisplayName("Deve criar PagedResponse com dados de cliente")
  void deveCriarPagedResponseComDadosCliente() {
    // Given
    ClienteResponse cliente1 = new ClienteResponse();
    cliente1.setId(1L);
    cliente1.setNome("João Silva");
    cliente1.setEmail("joao@example.com");
    cliente1.setAtivo(true);

    ClienteResponse cliente2 = new ClienteResponse();
    cliente2.setId(2L);
    cliente2.setNome("Maria Santos");
    cliente2.setEmail("maria@example.com");
    cliente2.setAtivo(true);

    List<ClienteResponse> items = List.of(cliente1, cliente2);
    long totalItems = 25;
    int page = 1;
    int size = 10;
    int totalPages = 3;
    Map<String, String> links =
        Map.of(
            "first",
            "/api/clientes?page=0",
            "prev",
            "/api/clientes?page=0",
            "next",
            "/api/clientes?page=2",
            "last",
            "/api/clientes?page=2");
    String message = "Página 2 de 3 retornada";
    boolean success = true;

    // When
    PagedResponse<ClienteResponse> response =
        new PagedResponse<>(items, totalItems, page, size, totalPages, links, message, success);

    // Then
    assertNotNull(response);
    assertEquals(2, response.items().size());
    assertEquals(25, response.totalItems());
    assertEquals(1, response.page());
    assertEquals(10, response.size());
    assertEquals(3, response.totalPages());
    assertEquals(4, response.links().size()); // first, prev, next, last
    assertEquals(message, response.message());
    assertTrue(response.success());

    // Verificar dados dos clientes
    assertEquals("João Silva", response.items().get(0).getNome());
    assertEquals("Maria Santos", response.items().get(1).getNome());
  }

  @Test
  @DisplayName("Deve criar PagedResponse com links mínimos")
  void deveCriarPagedResponseComLinksMinimos() {
    // Given
    List<Integer> items = List.of(1, 2, 3);
    Map<String, String> links = Map.of("self", "/api/items?page=0");

    // When
    PagedResponse<Integer> response = new PagedResponse<>(items, 3, 0, 10, 1, links, "OK", true);

    // Then
    assertNotNull(response);
    assertEquals(1, response.links().size());
    assertEquals("/api/items?page=0", response.links().get("self"));
  }

  @Test
  @DisplayName("Deve criar PagedResponse com página intermediária")
  void deveCriarPagedResponseComPaginaIntermediaria() {
    // Given
    List<String> items = List.of("Item 11", "Item 12", "Item 13");
    long totalItems = 50;
    int page = 1;
    int size = 10;
    int totalPages = 5;

    // When
    PagedResponse<String> response =
        new PagedResponse<>(items, totalItems, page, size, totalPages, Map.of(), "Página 2", true);

    // Then
    assertNotNull(response);
    assertEquals(3, response.items().size());
    assertEquals(50, response.totalItems());
    assertEquals(1, response.page());
    assertEquals(10, response.size());
    assertEquals(5, response.totalPages());
  }

  @Test
  @DisplayName("Deve criar PagedResponse com status de erro")
  void deveCriarPagedResponseComStatusErro() {
    // Given
    List<String> items = List.of();
    String message = "Erro ao processar página";
    boolean success = false;

    // When
    PagedResponse<String> response =
        new PagedResponse<>(items, 0, 0, 10, 0, Map.of(), message, success);

    // Then
    assertNotNull(response);
    assertTrue(response.items().isEmpty());
    assertEquals(message, response.message());
    assertFalse(response.success());
  }

  @Test
  @DisplayName("Deve verificar igualdade entre PagedResponses")
  void deveVerificarIgualdadeEntrePagedResponses() {
    // Given
    List<String> items = List.of("A", "B");
    Map<String, String> links = Map.of("first", "/test");

    PagedResponse<String> response1 = new PagedResponse<>(items, 10, 0, 5, 2, links, "OK", true);
    PagedResponse<String> response2 = new PagedResponse<>(items, 10, 0, 5, 2, links, "OK", true);
    PagedResponse<String> response3 =
        new PagedResponse<>(List.of("C", "D"), 10, 0, 5, 2, links, "OK", true);

    // Then
    assertEquals(response1, response2);
    assertNotEquals(response1, response3);
  }

  @Test
  @DisplayName("Deve gerar hashCode consistente")
  void deveGerarHashCodeConsistente() {
    // Given
    List<String> items = List.of("Test");
    PagedResponse<String> response1 = new PagedResponse<>(items, 5, 0, 5, 1, Map.of(), "OK", true);
    PagedResponse<String> response2 = new PagedResponse<>(items, 5, 0, 5, 1, Map.of(), "OK", true);

    // Then
    assertEquals(response1.hashCode(), response2.hashCode());
  }

  @Test
  @DisplayName("Deve gerar toString legível")
  void deveGerarToStringLegivel() {
    // Given
    List<String> items = List.of("Item 1");
    PagedResponse<String> response =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of("first", "/test"), "Sucesso", true);

    // When
    String toString = response.toString();

    // Then
    assertNotNull(toString);
    assertTrue(toString.contains("Item 1"));
    assertTrue(toString.contains("Sucesso"));
    assertTrue(toString.contains("true"));
  }
}
