package com.deliverytech.delivery_api.dto.request;

import com.deliverytech.delivery_api.model.Endereco;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoRequest {
    @NotNull private Long clienteId;
    @NotNull private Long restauranteId;
    @NotNull private Endereco enderecoEntrega;
    @NotNull private List<ItemPedidoRequest> itens;

    public PedidoRequest() {}

    public PedidoRequest(
            Long clienteId,
            Long restauranteId,
            Endereco enderecoEntrega,
            List<ItemPedidoRequest> itens) {
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}
