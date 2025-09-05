package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para atualização de status.", title = "Status Request DTO")
public class StatusRequest {
    @Schema(description = "Indica se o registro está ativo.", example = "true")
    private Boolean ativo;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}