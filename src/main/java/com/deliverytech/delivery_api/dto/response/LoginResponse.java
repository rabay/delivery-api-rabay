package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de resposta para autenticação de usuário.", title = "Login Response DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @Schema(description = "Token JWT para autenticação nas requisições subsequentes.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}