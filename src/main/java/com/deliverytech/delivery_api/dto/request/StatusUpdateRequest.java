package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para atualização de status de pedido.", title = "Status Update Request DTO")
public class StatusUpdateRequest {
    @NotBlank(message = "Status é obrigatório")
    @Schema(
            description = "Novo status do pedido (valores possíveis: CRIADO, CONFIRMADO, EM_PREPARACAO, PRONTO, EM_ENTREGA, ENTREGUE, CANCELADO).",
            example = "ENTREGUE",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    public StatusUpdateRequest() {}

    public StatusUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}