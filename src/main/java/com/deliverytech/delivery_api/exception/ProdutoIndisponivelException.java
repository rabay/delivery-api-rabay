package com.deliverytech.delivery_api.exception;

public class ProdutoIndisponivelException extends RuntimeException {
  public ProdutoIndisponivelException(String message) {
    super(message);
  }
}
