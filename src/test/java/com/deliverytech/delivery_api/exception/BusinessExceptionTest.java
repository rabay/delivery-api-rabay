package com.deliverytech.delivery_api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para BusinessException")
class BusinessExceptionTest {

  @Test
  @DisplayName("Deve criar BusinessException com mensagem")
  void deveCriarBusinessExceptionComMensagem() {
    // Given
    String mensagem = "Erro de negócio";

    // When
    BusinessException exception = new BusinessException(mensagem);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isNull();
  }

  @Test
  @DisplayName("Deve criar BusinessException com mensagem e causa")
  void deveCriarBusinessExceptionComMensagemECausa() {
    // Given
    String mensagem = "Erro de negócio com causa";
    Throwable causa = new RuntimeException("Erro interno");

    // When
    BusinessException exception = new BusinessException(mensagem, causa);

    // Then
    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo(mensagem);
    assertThat(exception.getCause()).isEqualTo(causa);
  }

  @Test
  @DisplayName("Deve ser uma RuntimeException")
  void deveSerRuntimeException() {
    // When
    BusinessException exception = new BusinessException("Teste");

    // Then
    assertThat(exception).isInstanceOf(RuntimeException.class);
  }
}
