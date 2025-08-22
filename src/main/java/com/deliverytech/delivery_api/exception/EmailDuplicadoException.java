package com.deliverytech.delivery_api.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String email) {
        super("E-mail já cadastrado: " + email);
    }
}
