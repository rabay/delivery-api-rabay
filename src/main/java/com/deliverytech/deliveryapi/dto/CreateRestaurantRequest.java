package com.deliverytech.deliveryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO para criação de restaurante via API REST
 * Define a estrutura JSON esperada no endpoint POST /api/v1/restaurants
 */
public record CreateRestaurantRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        
        @NotBlank(message = "Descrição é obrigatória")
        String description,
        
        @NotBlank(message = "CNPJ é obrigatório")
        @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve estar no formato 00.000.000/0000-00")
        String cnpj,
        
        @NotBlank(message = "Telefone é obrigatório")
        String phone,
        
        @NotNull(message = "Endereço é obrigatório")
        AddressDTO address,
        
        String logo,
        
        @NotNull(message = "Taxa de entrega é obrigatória")
        @PositiveOrZero(message = "Taxa de entrega deve ser maior ou igual a zero")
        BigDecimal deliveryFee,
        
        @PositiveOrZero(message = "Valor mínimo do pedido deve ser maior ou igual a zero")
        BigDecimal minimumOrderValue,
        
        Integer averageDeliveryTimeInMinutes
) {
}
