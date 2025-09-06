package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para registro de novo usuário.", title = "Register Request DTO")
public class RegisterRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Schema(
      description = "Nome completo do usuário. Deve ter entre 2 e 100 caracteres.",
      example = "Maria Silva",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String nome;

  @Email(message = "Email deve ter formato válido")
  @NotBlank(message = "Email é obrigatório")
  @Schema(
      description = "Email do usuário. Deve ser único e válido.",
      example = "maria.silva@example.com",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  @Schema(
      description = "Senha do usuário. Deve ter pelo menos 6 caracteres.",
      example = "senhaSegura123",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String senha;

  @NotNull(message = "Role é obrigatória")
  @Schema(
      description = "Função/perfil do usuário no sistema.",
      example = "CLIENTE",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Role role;

  @Schema(
      description = "ID do restaurante (obrigatório apenas para usuários do tipo RESTAURANTE).",
      example = "1")
  private Long restauranteId; // Optional, for restaurant users
}
