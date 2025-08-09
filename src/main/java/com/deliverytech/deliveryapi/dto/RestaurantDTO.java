package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.Restaurant;

import java.time.LocalDateTime;

/**
 * DTO para representação de restaurante nas respostas da API
 * Implementa as melhores práticas de API REST com Java Records
 */
public record RestaurantDTO(
        Long id,
        String name,
        String description,
        String cnpj,
        String phone,
        AddressDTO address,
        String logo,
        boolean active,
        boolean open,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Converte uma entidade Restaurant para RestaurantDTO
     */
    public static RestaurantDTO from(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCnpj(),
                restaurant.getPhone(),
                AddressDTO.from(restaurant.getAddress()),
                restaurant.getLogo(),
                restaurant.isActive(),
                restaurant.isOpen(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt()
        );
    }
}
