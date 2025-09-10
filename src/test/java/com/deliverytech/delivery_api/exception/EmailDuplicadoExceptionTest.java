package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EmailDuplicadoException")
class EmailDuplicadoExceptionTest {

    @Test
    @DisplayName("Deve criar EmailDuplicadoException com email")
    void deveCriarEmailDuplicadoExceptionComEmail() {
        // Given
        String email = "teste@email.com";

        // When
        EmailDuplicadoException exception = new EmailDuplicadoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("E-mail já cadastrado: " + email);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar EmailDuplicadoException com email diferente")
    void deveCriarEmailDuplicadoExceptionComEmailDiferente() {
        // Given
        String email = "outro@email.com";

        // When
        EmailDuplicadoException exception = new EmailDuplicadoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("E-mail já cadastrado: " + email);
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        EmailDuplicadoException exception = new EmailDuplicadoException("teste@email.com");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}