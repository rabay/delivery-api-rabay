package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EntityNotFoundException")
class EntityNotFoundExceptionTest {

    @Test
    @DisplayName("Deve criar EntityNotFoundException com mensagem simples")
    void deveCriarEntityNotFoundExceptionComMensagemSimples() {
        // Given
        String mensagem = "Entidade não encontrada";

        // When
        EntityNotFoundException exception = new EntityNotFoundException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar EntityNotFoundException com entidade e ID")
    void deveCriarEntityNotFoundExceptionComEntidadeEId() {
        // Given
        String entidade = "Cliente";
        Long id = 123L;

        // When
        EntityNotFoundException exception = new EntityNotFoundException(entidade, id);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Cliente não encontrado com ID: 123");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar EntityNotFoundException com entidade, campo e valor")
    void deveCriarEntityNotFoundExceptionComEntidadeCampoEValor() {
        // Given
        String entidade = "Usuario";
        String campo = "email";
        String valor = "teste@email.com";

        // When
        EntityNotFoundException exception = new EntityNotFoundException(entidade, campo, valor);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Usuario não encontrado com email: teste@email.com");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        EntityNotFoundException exception = new EntityNotFoundException("Teste");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}