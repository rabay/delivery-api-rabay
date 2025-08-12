package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;

import java.time.LocalDateTime;

/**
 * DTO para representação de categoria de restaurante nas respostas da API
 */
public record RestaurantCategoryDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Converte uma entidade RestaurantCategory para RestaurantCategoryDTO
     */
    public static RestaurantCategoryDTO from(RestaurantCategory category) {
        if (category == null) {
            return null;
        }
        
        return new RestaurantCategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
