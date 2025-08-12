package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.OrderItem;

import java.math.BigDecimal;

/**
 * DTO para itens do pedido
 */
public record OrderItemDTO(
    Long id,
    Long productId,
    String productName,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    String observations
) {
    public static OrderItemDTO from(OrderItem item) {
        return new OrderItemDTO(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getUnitPrice().getAmount(),
            item.getSubtotal().getAmount(),
            item.getNotes()
        );
    }
}
