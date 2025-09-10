package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EmailJaCadastradoException")
class EmailJaCadastradoExceptionTest {

    @Test
    @DisplayName("Deve criar EmailJaCadastradoException com email")
    void deveCriarEmailJaCadastradoExceptionComEmail() {
        // Given
        String email = "teste@email.com";

        // When
        EmailJaCadastradoException exception = new EmailJaCadastradoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Email já cadastrado: " + email);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar EmailJaCadastradoException com email diferente")
    void deveCriarEmailJaCadastradoExceptionComEmailDiferente() {
        // Given
        String email = "outro@email.com";

        // When
        EmailJaCadastradoException exception = new EmailJaCadastradoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Email já cadastrado: " + email);
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        EmailJaCadastradoException exception = new EmailJaCadastradoException("teste@email.com");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}