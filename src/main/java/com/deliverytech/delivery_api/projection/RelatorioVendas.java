package com.deliverytech.delivery_api.projection;

import java.math.BigDecimal;

public interface RelatorioVendas {
  String getNomeRestaurante();

  BigDecimal getTotalVendas();

  Long getQuantidadePedidos();
}
