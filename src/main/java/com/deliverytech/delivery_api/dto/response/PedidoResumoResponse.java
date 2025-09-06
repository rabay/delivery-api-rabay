package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
    description = "DTO de resposta resumida para pedido (usado em listagens).",
    title = "Pedido Resumo Response DTO")
public class PedidoResumoResponse {
  @Schema(description = "ID único do pedido.", example = "10")
  private Long id;

  @Schema(description = "Nome completo do cliente.", example = "João Silva")
  private String clienteNome;

  @Schema(description = "Nome do restaurante.", example = "Restaurante do Zé")
  private String restauranteNome;

  @Schema(description = "Valor total do pedido.", example = "99.90")
  private BigDecimal valorTotal;

  @Schema(description = "Desconto aplicado ao pedido.", example = "10.00")
  private BigDecimal desconto;

  @Schema(description = "Status atual do pedido.", example = "ENTREGUE")
  private String status;

  @Schema(description = "Data/hora do pedido.", example = "2025-08-21T12:34:56")
  private LocalDateTime dataPedido;

  public PedidoResumoResponse() {}

  // Constructor without discount (for backward compatibility)
  public PedidoResumoResponse(
      Long id,
      String clienteNome,
      String restauranteNome,
      BigDecimal valorTotal,
      String status,
      LocalDateTime dataPedido) {
    this.id = id;
    this.clienteNome = clienteNome;
    this.restauranteNome = restauranteNome;
    this.valorTotal = valorTotal;
    this.status = status;
    this.dataPedido = dataPedido;
  }

  // Constructor with discount
  public PedidoResumoResponse(
      Long id,
      String clienteNome,
      String restauranteNome,
      BigDecimal valorTotal,
      BigDecimal desconto,
      String status,
      LocalDateTime dataPedido) {
    this.id = id;
    this.clienteNome = clienteNome;
    this.restauranteNome = restauranteNome;
    this.valorTotal = valorTotal;
    this.desconto = desconto;
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

  public BigDecimal getDesconto() {
    return desconto;
  }

  public void setDesconto(BigDecimal desconto) {
    this.desconto = desconto;
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
