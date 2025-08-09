package com.deliverytech.deliveryapi.domain.model;

public enum UserType {
    CUSTOMER,       // Cliente que realiza pedidos
    RESTAURANT,     // Proprietário ou gerente de restaurante
    DELIVERY_PERSON, // Entregador
    ADMIN           // Administrador do sistema
}