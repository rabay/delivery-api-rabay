package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Resposta quando realizar o login", title = "Login Response DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
}
