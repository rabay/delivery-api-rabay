package com.deliverytech.delivery_api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para ValidationException")
class ValidationExceptionTest {

  @Test
  @DisplayName("Deve criar ValidationException com mensagem")
  void deveCriarValidationExceptionComMensagem() {
    // Given
    String mensagem = "Dados inválidos fornecidos";

    // When
    ValidationException exception = new ValidationException(mensagem);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isNull();
  }

  @Test
  @DisplayName("Deve criar ValidationException com mensagem e causa")
  void deveCriarValidationExceptionComMensagemECausa() {
    // Given
    String mensagem = "Erro de validação";
    Throwable causa = new IllegalArgumentException("Argumento inválido");

    // When
    ValidationException exception = new ValidationException(mensagem, causa);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isEqualTo(causa);
  }

  @Test
  @DisplayName("Deve ser uma RuntimeException")
  void deveSerRuntimeException() {
    // When
    ValidationException exception = new ValidationException("Teste");

    // Then
    assertThat(exception).isInstanceOf(RuntimeException.class);
  }
}
