package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class StatusUpdateRequest {
    @NotBlank(message = "Status é obrigatório")
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
