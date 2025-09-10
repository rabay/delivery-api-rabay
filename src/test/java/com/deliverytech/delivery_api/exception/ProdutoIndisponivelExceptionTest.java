package com.deliverytech.delivery_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para ProdutoIndisponivelException")
class ProdutoIndisponivelExceptionTest {

    @Test
    @DisplayName("Deve criar ProdutoIndisponivelException com mensagem")
    void deveCriarProdutoIndisponivelExceptionComMensagem() {
        // Given
        String mensagem = "Produto indisponível no momento";

        // When
        ProdutoIndisponivelException exception = new ProdutoIndisponivelException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Deve criar ProdutoIndisponivelException com mensagem específica")
    void deveCriarProdutoIndisponivelExceptionComMensagemEspecifica() {
        // Given
        String mensagem = "Produto fora do catálogo";

        // When
        ProdutoIndisponivelException exception = new ProdutoIndisponivelException(mensagem);

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(mensagem);
    }

    @Test
    @DisplayName("Deve ser uma RuntimeException")
    void deveSerRuntimeException() {
        // When
        ProdutoIndisponivelException exception = new ProdutoIndisponivelException("Teste");

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}