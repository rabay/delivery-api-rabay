package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nome;
    private String email;
    private Role role;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private Long restauranteId;

    public static UserResponse fromEntity(Usuario usuario) {
        return UserResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .restauranteId(usuario.getRestauranteId())
                .build();
    }
}
