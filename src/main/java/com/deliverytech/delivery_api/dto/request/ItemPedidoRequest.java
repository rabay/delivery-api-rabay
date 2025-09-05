package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Schema(description = "DTO para item de pedido.", title = "Item Pedido Request DTO")
public class ItemPedidoRequest {
    @NotNull(message = "ID do produto é obrigatório")
    @Schema(description = "ID do produto.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    @Schema(description = "Quantidade do produto no pedido. Deve ser maior que zero.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantidade;
    
    @JsonIgnore
    private java.math.BigDecimal precoUnitario;

    public ItemPedidoRequest() {}

    public ItemPedidoRequest(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public java.math.BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(java.math.BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}