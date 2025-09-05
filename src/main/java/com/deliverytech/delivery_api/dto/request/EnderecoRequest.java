package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.validation.ValidCEP;
import com.deliverytech.delivery_api.validation.ValidEstado;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para informações de endereço.", title = "Endereco Request DTO")
public class EnderecoRequest {
    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 100, message = "Rua não pode ter mais de 100 caracteres")
    @Schema(description = "Nome da rua/avenida do endereço. Máximo de 100 caracteres.", example = "Rua das Flores", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rua;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número não pode ter mais de 10 caracteres")
    @Schema(description = "Número do endereço. Máximo de 10 caracteres.", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 50, message = "Bairro não pode ter mais de 50 caracteres")
    @Schema(description = "Bairro do endereço. Máximo de 50 caracteres.", example = "Centro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50, message = "Cidade não pode ter mais de 50 caracteres")
    @Schema(description = "Cidade do endereço. Máximo de 50 caracteres.", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @ValidEstado(message = "Estado deve ser um código UF válido do Brasil")
    @Schema(description = "Estado do endereço (sigla UF).", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP(message = "CEP deve ter o formato 00000-000 ou 00000000")
    @Schema(description = "CEP do endereço (formato 00000-000 ou 00000000).", example = "01234-567", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cep;

    @Size(max = 100, message = "Complemento não pode ter mais de 100 caracteres")
    @Schema(description = "Complemento do endereço. Máximo de 100 caracteres.", example = "Apto 101")
    private String complemento;
}