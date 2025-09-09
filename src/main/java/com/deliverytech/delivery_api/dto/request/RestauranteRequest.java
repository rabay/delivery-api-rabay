package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import com.deliverytech.delivery_api.validation.ValidPhone;
import com.deliverytech.delivery_api.validation.ValidTempoEntrega;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "DTO para criação/atualização de restaurante.",
    title = "Restaurante Request DTO")
public class RestauranteRequest {

  @NotBlank(message = "Nome é obrigatório")
  @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
  @Schema(
      description = "Nome do restaurante. Máximo de 100 caracteres.",
      example = "Restaurante do Zé",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String nome;

  @NotBlank(message = "Categoria é obrigatória")
  @Size(max = 50, message = "Categoria não pode ter mais de 50 caracteres")
  @ValidCategoria(type = ValidCategoria.Type.RESTAURANTE)
  @Schema(
      description = "Categoria do restaurante. Máximo de 50 caracteres.",
      example = "Comida Brasileira",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String categoria;

  @NotBlank(message = "Endereço é obrigatório")
  @Size(max = 255, message = "Endereço não pode ter mais de 255 caracteres")
  @Schema(
      description = "Endereço completo do restaurante. Máximo de 255 caracteres.",
      example = "Rua das Flores, 123 - São Paulo/SP",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String endereco;

  @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser positiva")
  @NotNull(message = "Taxa de entrega é obrigatória")
  @Schema(
      description = "Taxa de entrega do restaurante. Deve ser positiva.",
      example = "5.00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal taxaEntrega;

  @NotNull(message = "Tempo de entrega é obrigatório")
  @Min(value = 10, message = "Tempo de entrega deve ser pelo menos 10 minutos")
    @Max(value = 120, message = "Tempo de entrega não pode exceder 120 minutos")
  @ValidTempoEntrega
  @Schema(
      description = "Tempo estimado de entrega em minutos. Deve ser pelo menos 10 minutos.",
      example = "30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer tempoEntregaMinutos;

  @ValidPhone
  @Schema(
      description = "Telefone de contato do restaurante. Deve ter 10 ou 11 dígitos.",
      example = "11987654321")
  private String telefone;

  @Email(message = "Email deve ter formato válido")
  @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
  @Schema(
      description = "Email de contato do restaurante. Máximo de 100 caracteres.",
      example = "contato@restaurantedoze.com")
  private String email;

  @DecimalMin(value = "0.0", message = "Avaliação deve ser positiva")
  @DecimalMax(value = "5.0", message = "Avaliação máxima é 5.0")
  @Schema(description = "Avaliação média do restaurante (0.0 a 5.0).", example = "4.5")
  private BigDecimal avaliacao;
}
