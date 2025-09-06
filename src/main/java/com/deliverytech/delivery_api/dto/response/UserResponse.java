package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para informações do usuário.", title = "User Response DTO")
public class UserResponse {
  @Schema(description = "ID único do usuário.", example = "1")
  private Long id;

  @Schema(description = "Nome completo do usuário.", example = "Maria Silva")
  private String nome;

  @Schema(description = "Email do usuário.", example = "maria.silva@example.com")
  private String email;

  @Schema(description = "Função/perfil do usuário no sistema.", example = "CLIENTE")
  private Role role;

  @Schema(description = "Indica se o usuário está ativo.", example = "true")
  private Boolean ativo;

  @Schema(description = "Data/hora de criação do usuário.", example = "2025-08-21T12:34:56")
  private LocalDateTime dataCriacao;

  @Schema(
      description = "ID do restaurante (apenas para usuários do tipo RESTAURANTE).",
      example = "1")
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
