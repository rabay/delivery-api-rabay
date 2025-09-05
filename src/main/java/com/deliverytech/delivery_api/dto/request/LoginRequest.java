package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Schema(description = "DTO para autenticação de usuário.", title = "Login Request DTO")
@Data
public class LoginRequest {

    @NotBlank(message = "Username é obrigatório")
    @Schema(description = "Email do usuário para autenticação.", example = "usuario@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password é obrigatório")
    @Schema(description = "Senha do usuário para autenticação.", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}