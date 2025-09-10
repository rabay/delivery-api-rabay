package com.deliverytech.delivery_api.mapper.typemaps;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.dto.response.PedidoResumoResponse;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class PedidoTypeMapTest {

  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    new PedidoTypeMap(modelMapper).configure();
  }

  @Test
  void shouldMapPedidoToPedidoResumoResponse() {
    Cliente cliente = new Cliente();
    cliente.setId(1L);
    cliente.setNome("João da Silva");

    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);
    restaurante.setNome("Restaurante Teste");

    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setValorTotal(new BigDecimal("49.90"));
    pedido.setDesconto(new BigDecimal("10.00"));
    pedido.setStatus(StatusPedido.ENTREGUE);
    pedido.setDataPedido(LocalDateTime.of(2025, 8, 21, 12, 34, 56));

    PedidoResumoResponse response = modelMapper.map(pedido, PedidoResumoResponse.class);

    assertThat(response.getId()).isEqualTo(pedido.getId());
    assertThat(response.getClienteNome()).isEqualTo("João da Silva");
    assertThat(response.getRestauranteNome()).isEqualTo("Restaurante Teste");
    assertThat(response.getValorTotal()).isEqualTo(new BigDecimal("49.90"));
    assertThat(response.getDesconto()).isEqualTo(new BigDecimal("10.00"));
    assertThat(response.getStatus()).isEqualTo("ENTREGUE");
    assertThat(response.getDataPedido()).isEqualTo(LocalDateTime.of(2025, 8, 21, 12, 34, 56));
  }
}
