package com.deliverytech.delivery_api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteResumoResponse {
    private Long id;
    private String nome;
    private String categoria;
    private BigDecimal taxaEntrega;
    private Integer tempoEntregaMinutos;
    private BigDecimal avaliacao;
    private Boolean ativo;
}