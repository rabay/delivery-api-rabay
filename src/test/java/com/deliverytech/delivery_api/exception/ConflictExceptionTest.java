package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para ConflictException")
class ConflictExceptionTest {

    @Test
    @DisplayName("Deve criar ConflictException com mensagem")
    void deveCriarConflictExceptionComMensagem() {
        // Given
        String mensagem = "Recurso já existe";

        // When
        ConflictException exception = new ConflictException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar ConflictException com mensagem e causa")
    void deveCriarConflictExceptionComMensagemECausa() {
        // Given
        String mensagem = "Conflito de dados";
        Throwable causa = new RuntimeException("Erro interno");

        // When
        ConflictException exception = new ConflictException(mensagem, causa);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getCause()).isEqualTo(causa);
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        ConflictException exception = new ConflictException("Teste");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
