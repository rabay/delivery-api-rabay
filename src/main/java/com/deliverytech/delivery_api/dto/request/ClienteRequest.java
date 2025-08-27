package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min=2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço não pode ter mais de 255 caracteres")
    private String endereco;
}
