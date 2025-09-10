package com.deliverytech.delivery_api.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PedidoResumoResponseTest {

  @Test
  void testPedidoResumoResponseConstructorAndGetters() {
    // Arrange
    Long id = 1L;
    String clienteNome = "João Silva";
    String restauranteNome = "Restaurante do Zé";
    BigDecimal valorTotal = new BigDecimal("49.90");
    String status = "ENTREGUE";
    LocalDateTime dataPedido = LocalDateTime.now();

    // Act
    PedidoResumoResponse response =
        new PedidoResumoResponse(id, clienteNome, restauranteNome, valorTotal, status, dataPedido);

    // Assert
    assertEquals(id, response.getId());
    assertEquals(clienteNome, response.getClienteNome());
    assertEquals(restauranteNome, response.getRestauranteNome());
    assertEquals(valorTotal, response.getValorTotal());
    assertEquals(status, response.getStatus());
    assertEquals(dataPedido, response.getDataPedido());
  }

  @Test
  void testPedidoResumoResponseSetters() {
    // Arrange
    PedidoResumoResponse response = new PedidoResumoResponse();
    Long id = 2L;
    String clienteNome = "Maria Santos";
    String restauranteNome = "Pizzaria Bella";
    BigDecimal valorTotal = new BigDecimal("89.50");
    String status = "EM_PREPARACAO";
    LocalDateTime dataPedido = LocalDateTime.now();

    // Act
    response.setId(id);
    response.setClienteNome(clienteNome);
    response.setRestauranteNome(restauranteNome);
    response.setValorTotal(valorTotal);
    response.setStatus(status);
    response.setDataPedido(dataPedido);

    // Assert
    assertEquals(id, response.getId());
    assertEquals(clienteNome, response.getClienteNome());
    assertEquals(restauranteNome, response.getRestauranteNome());
    assertEquals(valorTotal, response.getValorTotal());
    assertEquals(status, response.getStatus());
    assertEquals(dataPedido, response.getDataPedido());
  }

  @Test
  void testPedidoResumoResponseDefaultConstructor() {
    // Act
    PedidoResumoResponse response = new PedidoResumoResponse();

    // Assert
    assertNull(response.getId());
    assertNull(response.getClienteNome());
    assertNull(response.getRestauranteNome());
    assertNull(response.getValorTotal());
    assertNull(response.getStatus());
    assertNull(response.getDataPedido());
  }
}
