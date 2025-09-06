package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de resposta para restaurante.", title = "Restaurante Response DTO")
public class RestauranteResponse {
  @Schema(description = "ID único do restaurante.", example = "1")
  private Long id;

  @Schema(description = "Nome do restaurante.", example = "Restaurante do Zé")
  private String nome;

  @Schema(description = "Categoria do restaurante.", example = "Comida Brasileira")
  private String categoria;

  @Schema(
      description = "Endereço completo do restaurante.",
      example = "Rua das Flores, 123 - São Paulo/SP")
  private String endereco;

  @Schema(description = "Taxa de entrega do restaurante.", example = "5.00")
  private BigDecimal taxaEntrega;

  @Schema(description = "Telefone de contato do restaurante.", example = "11987654321")
  private String telefone;

  @Schema(description = "Email de contato do restaurante.", example = "contato@restaurantedoze.com")
  private String email;

  @Schema(description = "Tempo estimado de entrega em minutos.", example = "30")
  private Integer tempoEntregaMinutos;

  @Schema(description = "Avaliação média do restaurante (0.0 a 5.0).", example = "4.5")
  private BigDecimal avaliacao;

  @Schema(description = "Indica se o restaurante está ativo.", example = "true")
  private Boolean ativo;
}
