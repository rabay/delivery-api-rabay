package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para criação/atualização de produto.", title = "Produto Request DTO")
public class ProdutoRequest {
  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  @Schema(
      description = "Nome do produto. Deve ter entre 2 e 100 caracteres.",
      example = "Pizza Margherita",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String nome;

  @NotBlank(message = "Categoria é obrigatória")
  @Size(max = 50, message = "Categoria não pode ter mais de 50 caracteres")
  @ValidCategoria(type = ValidCategoria.Type.PRODUTO)
  @Schema(
      description = "Categoria do produto. Máximo de 50 caracteres.",
      example = "Pizzas",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String categoria;

  @Size(min = 10, max = 500, message = "Descrição deve ter pelo menos 10 caracteres")
  @Schema(
      description = "Descrição detalhada do produto. Deve ter pelo menos 10 caracteres.",
      example = "Pizza tradicional com molho de tomate, mussarela e manjericão")
  private String descricao;

  @NotNull(message = "Preço é obrigatório")
  @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
  @DecimalMax(value = "500.00", message = "Preço máximo é R$500,00")
  @Schema(
      description = "Preço do produto. Deve ser maior que zero e máximo R$500,00.",
      example = "29.90",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal preco;

  @NotNull(message = "ID do restaurante é obrigatório")
  @Positive(message = "ID do restaurante deve ser positivo")
  @Schema(
      description = "ID do restaurante proprietário do produto.",
      example = "1",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long restauranteId;

  @Schema(description = "Indica se o produto está disponível para venda.", example = "true")
  @Builder.Default
  private Boolean disponivel = true;

  @NotNull(message = "Quantidade em estoque é obrigatória")
  @Schema(
      description = "Quantidade disponível em estoque.",
      example = "10",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer quantidadeEstoque;
}
