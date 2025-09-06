package com.deliverytech.delivery_api.exception;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String entity, Long id) {
    super(entity + " não encontrado com ID: " + id);
  }

  public EntityNotFoundException(String entity, String field, String value) {
    super(entity + " não encontrado com " + field + ": " + value);
  }
}
