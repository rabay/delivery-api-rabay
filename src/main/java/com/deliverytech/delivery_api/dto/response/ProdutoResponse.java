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
public class ProdutoResponse {
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal preco;
    private RestauranteResumoResponse restaurante;
    private Boolean disponivel;
}
