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
@Schema(description = "DTO de resposta para produto.", title = "Produto Response DTO")
public class ProdutoResponse {
    @Schema(description = "ID único do produto.", example = "1")
    private Long id;
    
    @Schema(description = "Nome do produto.", example = "Pizza Margherita")
    private String nome;
    
    @Schema(description = "Categoria do produto.", example = "Pizzas")
    private String categoria;
    
    @Schema(description = "Descrição detalhada do produto.", example = "Pizza tradicional com molho de tomate, mussarela e manjericão")
    private String descricao;
    
    @Schema(description = "Preço do produto.", example = "29.90")
    private BigDecimal preco;
    
    @Schema(description = "Informações resumidas do restaurante proprietário.")
    private RestauranteResumoResponse restaurante;
    
    @Schema(description = "Indica se o produto está disponível para venda.", example = "true")
    private Boolean disponivel;
    
    @Schema(description = "Quantidade disponível em estoque.", example = "10")
    private Integer quantidadeEstoque;
}