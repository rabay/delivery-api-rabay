package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    @NotNull private Long clienteId;
    @NotNull private Long restauranteId;
    @NotNull private EnderecoRequest enderecoEntrega;
    @NotNull private List<ItemPedidoRequest> itens;
}
