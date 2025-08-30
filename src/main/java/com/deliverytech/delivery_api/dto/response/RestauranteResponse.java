package com.deliverytech.delivery_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteResponse {
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private BigDecimal taxaEntrega;
    private String telefone;
    private String email;
    private Integer tempoEntregaMinutos;
    private BigDecimal avaliacao;
    private Boolean ativo;
}
