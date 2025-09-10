package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para EstoqueInsuficienteException")
class EstoqueInsuficienteExceptionTest {

    @Test
    @DisplayName("Deve criar EstoqueInsuficienteException com mensagem")
    void deveCriarEstoqueInsuficienteExceptionComMensagem() {
        // Given
        String mensagem = "Estoque insuficiente para o produto";

        // When
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar EstoqueInsuficienteException com mensagem específica")
    void deveCriarEstoqueInsuficienteExceptionComMensagemEspecifica() {
        // Given
        String mensagem = "Quantidade solicitada maior que o estoque disponível";

        // When
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        EstoqueInsuficienteException exception = new EstoqueInsuficienteException("Teste");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}