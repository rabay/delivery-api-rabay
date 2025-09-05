package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.model.Endereco;
import com.deliverytech.delivery_api.model.StatusPedido;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de resposta para pedidos.", title = "Pedido Response DTO")
public class PedidoResponse {
    @Schema(description = "ID único do pedido.", example = "10")
    private Long id;

    @Schema(description = "Informações resumidas do cliente.")
    private ClienteResumoResponse cliente;

    @Schema(description = "ID do restaurante.", example = "2")
    private Long restauranteId;

    @Schema(description = "Endereço de entrega do pedido.")
    private Endereco enderecoEntrega;

    @Schema(description = "Valor total do pedido.", example = "99.90")
    private BigDecimal valorTotal;
    
    @Schema(description = "Desconto aplicado ao pedido.", example = "10.00")
    private BigDecimal desconto;

    @Schema(description = "Status atual do pedido.", example = "CRIADO")
    private StatusPedido status;

    @Schema(description = "Data/hora do pedido.", example = "2025-08-21T12:34:56")
    private LocalDateTime dataPedido;

    @Schema(description = "Lista de itens do pedido.")
    private List<ItemPedidoResponse> itens;

    public PedidoResponse() {}

    // Constructor without discount (for backward compatibility)
    public PedidoResponse(
            Long id,
            ClienteResumoResponse cliente,
            Long restauranteId,
            Endereco enderecoEntrega,
            BigDecimal valorTotal,
            StatusPedido status,
            LocalDateTime dataPedido,
            List<ItemPedidoResponse> itens) {
        this.id = id;
        this.cliente = cliente;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataPedido = dataPedido;
        this.itens = itens;
    }

    // Constructor with discount
    public PedidoResponse(
            Long id,
            ClienteResumoResponse cliente,
            Long restauranteId,
            Endereco enderecoEntrega,
            BigDecimal valorTotal,
            BigDecimal desconto,
            StatusPedido status,
            LocalDateTime dataPedido,
            List<ItemPedidoResponse> itens) {
        this.id = id;
        this.cliente = cliente;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.status = status;
        this.dataPedido = dataPedido;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteResumoResponse getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResumoResponse cliente) {
        this.cliente = cliente;
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getDesconto() {
        return desconto;
    }
    
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<ItemPedidoResponse> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponse> itens) {
        this.itens = itens;
    }
}