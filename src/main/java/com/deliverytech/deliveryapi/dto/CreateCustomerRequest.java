package com.deliverytech.deliveryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de cliente
 */
@Schema(description = "Dados para criação de um novo cliente")
public record CreateCustomerRequest(
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Schema(description = "Nome completo do cliente", example = "João Silva")
    String name,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    @Schema(description = "Email do cliente", example = "joao@exemplo.com")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Size(max = 255, message = "Senha deve ter no máximo 255 caracteres")
    @Schema(description = "Senha para acesso", example = "minhasenha123")
    String password,
    
    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    @Schema(description = "Telefone do cliente", example = "(11) 99999-9999")
    String phone,
    
    @Schema(description = "Endereço do cliente (opcional)")
    AddressDTO address
) {
}
