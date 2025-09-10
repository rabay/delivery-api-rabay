package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import com.deliverytech.delivery_api.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ApiResultPedidoPaged")
class ApiResultPedidoPagedTest {

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com construtor padrão")
  void deveCriarApiResultPedidoPagedComConstrutorPadrao() {
    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();

    // Then
    assertNotNull(result);
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados de pedidos paginados")
  void deveDefinirEObterDadosPedidosPaginados() {
    // Given
    PedidoResponse pedido1 = new PedidoResponse();
    pedido1.setId(1L);
    pedido1.setStatus(StatusPedido.CONFIRMADO);
    pedido1.setValorTotal(new BigDecimal("45.90"));
    pedido1.setDataPedido(LocalDateTime.now().minusHours(2));

    PedidoResponse pedido2 = new PedidoResponse();
    pedido2.setId(2L);
    pedido2.setStatus(StatusPedido.PREPARANDO);
    pedido2.setValorTotal(new BigDecimal("32.50"));
    pedido2.setDataPedido(LocalDateTime.now().minusHours(1));

    List<PedidoResponse> items = List.of(pedido1, pedido2);
    Map<String, String> links =
        Map.of("first", "/api/pedidos?page=0", "last", "/api/pedidos?page=5");

    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 67, 0, 10, 7, links, "Pedidos retornados", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = pagedData;
    result.message = "Consulta de pedidos realizada com sucesso";
    result.success = true;

    // Then
    assertNotNull(result.data);
    assertEquals(2, result.data.items().size());
    assertEquals(67, result.data.totalItems());
    assertEquals(0, result.data.page());
    assertEquals(10, result.data.size());
    assertEquals(7, result.data.totalPages());
    assertEquals("Consulta de pedidos realizada com sucesso", result.message);
    assertTrue(result.success);

    // Verificar dados dos pedidos
    assertEquals("CONFIRMADO", result.data.items().get(0).getStatus().name());
    assertEquals(new BigDecimal("45.90"), result.data.items().get(0).getValorTotal());
    assertEquals("PREPARANDO", result.data.items().get(1).getStatus().name());
    assertEquals(new BigDecimal("32.50"), result.data.items().get(1).getValorTotal());
  }

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com dados vazios")
  void deveCriarApiResultPedidoPagedComDadosVazios() {
    // Given
    List<PedidoResponse> items = List.of();
    Map<String, String> links = Map.of("first", "/api/pedidos?page=0");
    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 0, 0, 10, 0, links, "Nenhum pedido encontrado", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
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
  @DisplayName("Deve criar ApiResultPedidoPaged com status de erro")
  void deveCriarApiResultPedidoPagedComStatusErro() {
    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = null;
    result.message = "Erro ao consultar pedidos";
    result.success = false;

    // Then
    assertNull(result.data);
    assertEquals("Erro ao consultar pedidos", result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve definir e obter dados nulos")
  void deveDefinirEObterDadosNulos() {
    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = null;
    result.message = null;
    result.success = false;

    // Then
    assertNull(result.data);
    assertNull(result.message);
    assertFalse(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com pedidos cancelados")
  void deveCriarApiResultPedidoPagedComPedidosCancelados() {
    // Given
    PedidoResponse pedido = new PedidoResponse();
    pedido.setId(100L);
    pedido.setStatus(StatusPedido.CANCELADO);
    pedido.setValorTotal(new BigDecimal("25.00"));
    pedido.setDataPedido(LocalDateTime.now().minusDays(1));

    List<PedidoResponse> items = List.of(pedido);
    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Pedido cancelado encontrado", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = pagedData;
    result.message = "Pedido cancelado encontrado";
    result.success = true;

    // Then
    assertEquals(1, result.data.items().size());
    assertEquals("CANCELADO", result.data.items().get(0).getStatus().name());
    assertEquals(new BigDecimal("25.00"), result.data.items().get(0).getValorTotal());
    assertEquals("Pedido cancelado encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com pedidos finalizados")
  void deveCriarApiResultPedidoPagedComPedidosFinalizados() {
    // Given
    PedidoResponse pedido = new PedidoResponse();
    pedido.setId(200L);
    pedido.setStatus(StatusPedido.ENTREGUE);
    pedido.setValorTotal(new BigDecimal("78.90"));
    pedido.setDataPedido(LocalDateTime.now().minusDays(2));

    List<PedidoResponse> items = List.of(pedido);
    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Pedido finalizado encontrado", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = pagedData;
    result.message = "Pedido finalizado encontrado";
    result.success = true;

    // Then
    assertEquals(1, result.data.items().size());
    assertEquals("ENTREGUE", result.data.items().get(0).getStatus().name());
    assertEquals(new BigDecimal("78.90"), result.data.items().get(0).getValorTotal());
    assertEquals("Pedido finalizado encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com pedidos de alto valor")
  void deveCriarApiResultPedidoPagedComPedidosAltoValor() {
    // Given
    PedidoResponse pedido = new PedidoResponse();
    pedido.setId(300L);
    pedido.setStatus(StatusPedido.CONFIRMADO);
    pedido.setValorTotal(new BigDecimal("999.99"));
    pedido.setDataPedido(LocalDateTime.now().minusMinutes(30));

    List<PedidoResponse> items = List.of(pedido);
    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Pedido de alto valor encontrado", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = pagedData;
    result.message = "Pedido de alto valor encontrado";
    result.success = true;

    // Then
    assertEquals(1, result.data.items().size());
    assertEquals("CONFIRMADO", result.data.items().get(0).getStatus().name());
    assertEquals(new BigDecimal("999.99"), result.data.items().get(0).getValorTotal());
    assertEquals("Pedido de alto valor encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve criar ApiResultPedidoPaged com pedidos recentes")
  void deveCriarApiResultPedidoPagedComPedidosRecentes() {
    // Given
    LocalDateTime agora = LocalDateTime.now();
    PedidoResponse pedido = new PedidoResponse();
    pedido.setId(400L);
    pedido.setStatus(StatusPedido.CRIADO);
    pedido.setValorTotal(new BigDecimal("15.50"));
    pedido.setDataPedido(agora.minusMinutes(5));

    List<PedidoResponse> items = List.of(pedido);
    PagedResponse<PedidoResponse> pagedData =
        new PagedResponse<>(items, 1, 0, 10, 1, Map.of(), "Pedido recente encontrado", true);

    // When
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();
    result.data = pagedData;
    result.message = "Pedido recente encontrado";
    result.success = true;

    // Then
    assertEquals(1, result.data.items().size());
    assertEquals("CRIADO", result.data.items().get(0).getStatus().name());
    assertEquals(new BigDecimal("15.50"), result.data.items().get(0).getValorTotal());
    assertTrue(result.data.items().get(0).getDataPedido().isAfter(agora.minusMinutes(10)));
    assertEquals("Pedido recente encontrado", result.message);
    assertTrue(result.success);
  }

  @Test
  @DisplayName("Deve manter valores após múltiplas definições")
  void deveManterValoresAposMultiplasDefinicoes() {
    // Given
    ApiResultPedidoPaged result = new ApiResultPedidoPaged();

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
