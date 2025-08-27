package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria não pode ter mais de 50 caracteres")
    private String categoria;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço não pode ter mais de 255 caracteres")
    private String endereco;

    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser positiva")
    @NotNull(message = "Taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de entrega é obrigatório")
    @Min(value = 1, message = "Tempo de entrega deve ser pelo menos 1 minuto")
    private Integer tempoEntregaMinutos;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;
    
    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
    private String email;

    @DecimalMin(value = "0.0", message = "Avaliação deve ser positiva")
    @DecimalMax(value = "5.0", message = "Avaliação máxima é 5.0")
    private BigDecimal avaliacao;
}
