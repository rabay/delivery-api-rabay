package com.deliverytech.deliveryapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request para criação e atualização de produtos
 */
public record CreateProductRequest(
    @NotBlank(message = "Nome é obrigatório")
    String name,
    
    @NotBlank(message = "Descrição é obrigatória")
    String description,
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    BigDecimal price,
    
    String image,
    
    @Positive(message = "Tempo de preparo deve ser positivo")
    Integer preparationTimeInMinutes,
    
    @NotNull(message = "ID do restaurante é obrigatório")
    Long restaurantId,
    
    Long categoryId
) {
}
