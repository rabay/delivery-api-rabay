package com.deliverytech.deliveryapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request para criação de pedidos
 */
public record CreateOrderRequest(
    @NotNull(message = "ID do cliente é obrigatório")
    Long customerId,
    
    @NotNull(message = "ID do restaurante é obrigatório")
    Long restaurantId,
    
    @NotNull(message = "Endereço de entrega é obrigatório")
    @Valid
    AddressDTO deliveryAddress,
    
    @NotNull(message = "Itens do pedido são obrigatórios")
    @Valid
    List<OrderItemRequest> items,
    
    String observations
) {
}
