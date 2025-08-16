package com.deliverytech.deliveryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request para criação de pedidos
 */
@Schema(description = "Dados para criação de um novo pedido")
public record CreateOrderRequest(
    @NotNull(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do cliente que está fazendo o pedido", example = "1")
    Long customerId,
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante onde o pedido será feito", example = "1")
    Long restaurantId,
    
    @NotNull(message = "Endereço de entrega é obrigatório")
    @Valid
    @Schema(description = "Endereço onde o pedido será entregue")
    AddressDTO deliveryAddress,
    
    @NotNull(message = "Itens do pedido são obrigatórios")
    @Valid
    @Schema(description = "Lista de itens do pedido")
    List<OrderItemRequest> items,
    
    @Schema(description = "Observações gerais do pedido", example = "Entregar no portão")
    String observations
) {
}
