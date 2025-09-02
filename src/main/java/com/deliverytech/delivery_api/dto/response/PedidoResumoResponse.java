package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Resumo do pedido para listagens.")
public class PedidoResumoResponse {
    @Schema(description = "ID do pedido.", example = "1")
    private Long id;
    
    @Schema(description = "Nome do cliente.", example = "João Silva")
    private String clienteNome;
    
    @Schema(description = "Nome do restaurante.", example = "Restaurante do Zé")
    private String restauranteNome;
    
    @Schema(description = "Valor total do pedido.", example = "49.90")
    private BigDecimal valorTotal;
    
    @Schema(description = "Status do pedido.", example = "ENTREGUE")
    private String status;
    
    @Schema(description = "Data/hora do pedido.", example = "2025-08-21T12:34:56")
    private LocalDateTime dataPedido;
    
    public PedidoResumoResponse() {}
    
    public PedidoResumoResponse(Long id, String clienteNome, String restauranteNome, 
                               BigDecimal valorTotal, String status, LocalDateTime dataPedido) {
        this.id = id;
        this.clienteNome = clienteNome;
        this.restauranteNome = restauranteNome;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataPedido = dataPedido;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
    
    public String getRestauranteNome() {
        return restauranteNome;
    }
    
    public void setRestauranteNome(String restauranteNome) {
        this.restauranteNome = restauranteNome;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
    
    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
}