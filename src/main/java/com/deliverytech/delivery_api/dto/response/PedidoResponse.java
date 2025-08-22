
package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.model.Endereco;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de resposta para pedidos.")
public class PedidoResponse {
    @Schema(description = "ID do pedido.", example = "10")
    private Long id;
    @Schema(description = "ID do cliente.", example = "1")
    private Long clienteId;
    @Schema(description = "ID do restaurante.", example = "2")
    private Long restauranteId;
    @Schema(description = "Endereço de entrega do pedido.")
    private Endereco enderecoEntrega;
    @Schema(description = "Valor total do pedido.", example = "99.90")
    private BigDecimal valorTotal;
    @Schema(description = "Status atual do pedido.", example = "CRIADO")
    private StatusPedido status;
    @Schema(description = "Data/hora do pedido.", example = "2025-08-21T12:34:56")
    private LocalDateTime dataPedido;
    @Schema(description = "Lista de itens do pedido.")
    private List<ItemPedidoResponse> itens;

    public PedidoResponse() {}

    public PedidoResponse(Long id, Long clienteId, Long restauranteId, Endereco enderecoEntrega, BigDecimal valorTotal, StatusPedido status, LocalDateTime dataPedido, List<ItemPedidoResponse> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataPedido = dataPedido;
        this.itens = itens;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getRestauranteId() { return restauranteId; }
    public void setRestauranteId(Long restauranteId) { this.restauranteId = restauranteId; }
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(Endereco enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public List<ItemPedidoResponse> getItens() { return itens; }
    public void setItens(List<ItemPedidoResponse> itens) { this.itens = itens; }
}
