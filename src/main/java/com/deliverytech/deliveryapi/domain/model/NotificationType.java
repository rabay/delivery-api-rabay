package com.deliverytech.deliveryapi.domain.model;

public enum NotificationType {
    ORDER_STATUS("Status do Pedido"),
    PAYMENT_STATUS("Status do Pagamento"),
    PROMOTION("Promoção"),
    SYSTEM("Sistema");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}