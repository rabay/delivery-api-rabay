package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para item de pedido.")
public class ItemPedidoRequest {
    @NotNull(message = "ID do produto é obrigatório")
    @Schema(description = "ID do produto.", example = "5")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    @Schema(description = "Quantidade do produto no pedido.", example = "2")
    private Integer quantidade;

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
}
