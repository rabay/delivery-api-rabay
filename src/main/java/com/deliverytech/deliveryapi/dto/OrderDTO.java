package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para representação de pedidos
 */
public record OrderDTO(
    Long id,
    String code,
    OrderStatus status,
    Long customerId,
    String customerName,
    Long restaurantId,
    String restaurantName,
    AddressDTO deliveryAddress,
    BigDecimal subtotal,
    BigDecimal deliveryFee,
    BigDecimal totalValue,
    List<OrderItemDTO> items,
    LocalDateTime estimatedDeliveryTime,
    LocalDateTime actualDeliveryTime,
    LocalDateTime createdAt,
    LocalDateTime confirmedAt,
    LocalDateTime preparingAt,
    LocalDateTime readyAt,
    LocalDateTime outForDeliveryAt,
    LocalDateTime deliveredAt,
    LocalDateTime cancelledAt,
    String cancellationReason
) {
    public static OrderDTO from(Order order) {
        return new OrderDTO(
            order.getId(),
            order.getCode(),
            order.getStatus(),
            order.getCustomer().getId(),
            order.getCustomer().getName(),
            order.getRestaurant().getId(),
            order.getRestaurant().getName(),
            AddressDTO.from(order.getDeliveryAddress()),
            order.getSubtotal().getAmount(),
            order.getDeliveryFee().getAmount(),
            order.getTotal().getAmount(),
            List.of(), // Items serão carregados via repository se necessário
            null, // TODO: Implementar estimatedDeliveryTime na entidade
            order.getDeliveryEndDate(),
            order.getCreatedAt(),
            order.getConfirmationDate(),
            order.getPreparationStartDate(),
            order.getPreparationEndDate(),
            order.getDeliveryStartDate(),
            order.getDeliveryEndDate(),
            order.getCancellationDate(),
            null // TODO: Implementar cancellationReason na entidade
        );
    }
}
