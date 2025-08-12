package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para representação de produtos
 */
public record ProductDTO(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String image,
    boolean active,
    boolean available,
    Integer preparationTimeInMinutes,
    Long restaurantId,
    String restaurantName,
    Long categoryId,
    String categoryName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ProductDTO from(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().getAmount(),
            product.getImage(),
            product.isActive(),
            product.isAvailable(),
            product.getPreparationTimeInMinutes(),
            product.getRestaurant().getId(),
            product.getRestaurant().getName(),
            product.getCategory() != null ? product.getCategory().getId() : null,
            product.getCategory() != null ? product.getCategory().getName() : null,
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
