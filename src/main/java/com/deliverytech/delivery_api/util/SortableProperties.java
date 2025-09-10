package com.deliverytech.delivery_api.util;

import java.util.Set;

/**
 * Constantes com propriedades permitidas para ordenação (sort) por entidade. Centraliza as
 * allow-lists usadas pelo PageableUtil e controllers.
 */
public final class SortableProperties {
  private SortableProperties() {}

  public static final Set<String> PRODUTO = Set.of("id", "nome", "preco", "disponivel");
  public static final Set<String> RESTAURANTE = Set.of("id", "nome", "categoria", "avaliacao");
  public static final Set<String> PEDIDO = Set.of("id", "dataPedido", "valorTotal", "status");
  public static final Set<String> CLIENTE = Set.of("id", "nome", "email");
}
