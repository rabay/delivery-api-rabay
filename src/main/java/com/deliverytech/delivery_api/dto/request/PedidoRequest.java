package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO para criação de pedido.", title = "Pedido Request DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    @NotNull
    @Schema(description = "ID do cliente que está fazendo o pedido.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;
    
    @NotNull
    @Schema(description = "ID do restaurante onde o pedido será feito.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long restauranteId;
    
    @NotNull
    @Schema(description = "Endereço de entrega do pedido.", requiredMode = Schema.RequiredMode.REQUIRED)
    private EnderecoRequest enderecoEntrega;
    
    @NotNull
    @Schema(description = "Lista de itens do pedido.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ItemPedidoRequest> itens;
    
    @Schema(description = "Valor do desconto aplicado ao pedido.", example = "10.00")
    private BigDecimal desconto;
}