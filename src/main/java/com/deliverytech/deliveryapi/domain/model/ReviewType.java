package com.deliverytech.deliveryapi.domain.model;

public enum ReviewType {
    RESTAURANT("Restaurante"),
    DELIVERY_PERSON("Entregador");

    private final String description;

    ReviewType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}