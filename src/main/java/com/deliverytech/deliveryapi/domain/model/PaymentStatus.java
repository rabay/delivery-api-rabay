package com.deliverytech.deliveryapi.domain.model;

public enum PaymentStatus {
    PENDING("Pendente"),
    PROCESSING("Em processamento"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado"),
    REFUNDED("Reembolsado"),
    CANCELLED("Cancelado");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}