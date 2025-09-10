package com.deliverytech.delivery_api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para TransactionException")
class TransactionExceptionTest {

  @Test
  @DisplayName("Deve criar TransactionException com mensagem")
  void deveCriarTransactionExceptionComMensagem() {
    // Given
    String mensagem = "Erro na transação";

    // When
    TransactionException exception = new TransactionException(mensagem);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isNull();
  }

  @Test
  @DisplayName("Deve criar TransactionException com mensagem e causa")
  void deveCriarTransactionExceptionComMensagemECausa() {
    // Given
    String mensagem = "Falha na transação";
    Throwable causa = new RuntimeException("Erro de banco de dados");

    // When
    TransactionException exception = new TransactionException(mensagem, causa);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isEqualTo(causa);
  }

  @Test
  @DisplayName("Deve ser uma RuntimeException")
  void deveSerRuntimeException() {
    // When
    TransactionException exception = new TransactionException("Teste");

    // Then
    assertThat(exception).isInstanceOf(RuntimeException.class);
  }
}
