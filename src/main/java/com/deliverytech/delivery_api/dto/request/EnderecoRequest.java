package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.validation.ValidCEP;
import com.deliverytech.delivery_api.validation.ValidEstado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequest {
    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 100, message = "Rua não pode ter mais de 100 caracteres")
    private String rua;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número não pode ter mais de 10 caracteres")
    private String numero;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 50, message = "Bairro não pode ter mais de 50 caracteres")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50, message = "Cidade não pode ter mais de 50 caracteres")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @ValidEstado(message = "Estado deve ser um código UF válido do Brasil")
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP(message = "CEP deve ter o formato 00000-000 ou 00000000")
    private String cep;

    @Size(max = 100, message = "Complemento não pode ter mais de 100 caracteres")
    private String complemento;
}