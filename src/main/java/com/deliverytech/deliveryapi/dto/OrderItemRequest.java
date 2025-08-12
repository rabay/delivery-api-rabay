package com.deliverytech.deliveryapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request para item do pedido
 */
public record OrderItemRequest(
    @NotNull(message = "ID do produto é obrigatório")
    Long productId,
    
    @Positive(message = "Quantidade deve ser maior que zero")
    int quantity,
    
    String observations
) {
}
