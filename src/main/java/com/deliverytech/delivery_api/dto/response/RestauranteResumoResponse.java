package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de resposta resumida para restaurante.", title = "Restaurante Resumo Response DTO")
public class RestauranteResumoResponse {
    @Schema(description = "ID único do restaurante.", example = "1")
    private Long id;
    
    @Schema(description = "Nome do restaurante.", example = "Restaurante do Zé")
    private String nome;
    
    @Schema(description = "Categoria do restaurante.", example = "Comida Brasileira")
    private String categoria;
    
    @Schema(description = "Taxa de entrega do restaurante.", example = "5.00")
    private BigDecimal taxaEntrega;
    
    @Schema(description = "Tempo estimado de entrega em minutos.", example = "30")
    private Integer tempoEntregaMinutos;
    
    @Schema(description = "Avaliação média do restaurante (0.0 a 5.0).", example = "4.5")
    private BigDecimal avaliacao;
    
    @Schema(description = "Indica se o restaurante está ativo.", example = "true")
    private Boolean ativo;
}