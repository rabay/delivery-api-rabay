package com.deliverytech.delivery_api.exception;

/**
 * Exceção lançada quando há conflitos de dados, como tentativas de criar recursos duplicados.
 * Retorna HTTP 409 Conflict.
 */
public class ConflictException extends RuntimeException {
  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
