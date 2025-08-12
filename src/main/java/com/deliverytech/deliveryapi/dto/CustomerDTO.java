package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.User;

import java.time.LocalDateTime;

/**
 * DTO para representação de cliente nas respostas da API
 */
public record CustomerDTO(
    Long id,
    String name,
    String email,
    String phone,
    String profileImage,
    AddressDTO address,
    boolean emailVerified,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    /**
     * Converte uma entidade User para CustomerDTO
     */
    public static CustomerDTO from(User user) {
        if (user == null) {
            return null;
        }
        
        return new CustomerDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getProfileImage(),
            AddressDTO.from(user.getAddress()),
            user.isEmailVerified(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
