package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para UsuarioNaoEncontradoException")
class UsuarioNaoEncontradoExceptionTest {

    @Test
    @DisplayName("Deve criar UsuarioNaoEncontradoException com ID")
    void deveCriarUsuarioNaoEncontradoExceptionComId() {
        // Given
        Long id = 123L;

        // When
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(id);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado: 123");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar UsuarioNaoEncontradoException com email")
    void deveCriarUsuarioNaoEncontradoExceptionComEmail() {
        // Given
        String email = "teste@email.com";

        // When
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado: teste@email.com");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar UsuarioNaoEncontradoException com ID diferente")
    void deveCriarUsuarioNaoEncontradoExceptionComIdDiferente() {
        // Given
        Long id = 456L;

        // When
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(id);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado: 456");
    }

    @Test
    @DisplayName("Deve criar UsuarioNaoEncontradoException com email diferente")
    void deveCriarUsuarioNaoEncontradoExceptionComEmailDiferente() {
        // Given
        String email = "outro@email.com";

        // When
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(email);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Usuário não encontrado: outro@email.com");
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        UsuarioNaoEncontradoException exception = new UsuarioNaoEncontradoException(123L);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}