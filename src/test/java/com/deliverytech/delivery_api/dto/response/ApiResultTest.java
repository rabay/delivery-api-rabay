package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResult")
class ApiResultTest {

    @Test
    @DisplayName("Deve criar ApiResult com dados de sucesso")
    void deveCriarApiResultComDadosSucesso() {
        // Given
        String data = "Dados de teste";
        String message = "Operação realizada com sucesso";
        boolean success = true;

        // When
        ApiResult<String> result = new ApiResult<>(data, message, success);

        // Then
        assertNotNull(result);
        assertEquals(data, result.data());
        assertEquals(message, result.message());
        assertTrue(result.success());
    }

    @Test
    @DisplayName("Deve criar ApiResult com dados de erro")
    void deveCriarApiResultComDadosErro() {
        // Given
        String message = "Erro interno do servidor";
        boolean success = false;

        // When
        ApiResult<Void> result = new ApiResult<>(null, message, success);

        // Then
        assertNotNull(result);
        assertNull(result.data());
        assertEquals(message, result.message());
        assertFalse(result.success());
    }

    @Test
    @DisplayName("Deve criar ApiResult com dados complexos")
    void deveCriarApiResultComDadosComplexos() {
        // Given
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setAtivo(true);

        String message = "Cliente encontrado";
        boolean success = true;

        // When
        ApiResult<ClienteResponse> result = new ApiResult<>(cliente, message, success);

        // Then
        assertNotNull(result);
        assertNotNull(result.data());
        assertEquals(1L, result.data().getId());
        assertEquals("João Silva", result.data().getNome());
        assertEquals("joao@example.com", result.data().getEmail());
        assertTrue(result.data().isAtivo());
        assertEquals(message, result.message());
        assertTrue(result.success());
    }

    @Test
    @DisplayName("Deve criar ApiResult com dados nulos")
    void deveCriarApiResultComDadosNulos() {
        // When
        ApiResult<String> result = new ApiResult<>(null, "Mensagem sem dados", true);

        // Then
        assertNotNull(result);
        assertNull(result.data());
        assertEquals("Mensagem sem dados", result.message());
        assertTrue(result.success());
    }

    @Test
    @DisplayName("Deve criar ApiResult com mensagem vazia")
    void deveCriarApiResultComMensagemVazia() {
        // When
        ApiResult<Integer> result = new ApiResult<>(42, "", false);

        // Then
        assertNotNull(result);
        assertEquals(42, result.data());
        assertEquals("", result.message());
        assertFalse(result.success());
    }

    @Test
    @DisplayName("Deve verificar igualdade entre ApiResults")
    void deveVerificarIgualdadeEntreApiResults() {
        // Given
        ApiResult<String> result1 = new ApiResult<>("teste", "ok", true);
        ApiResult<String> result2 = new ApiResult<>("teste", "ok", true);
        ApiResult<String> result3 = new ApiResult<>("outro", "ok", true);

        // Then
        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
    }

    @Test
    @DisplayName("Deve gerar hashCode consistente")
    void deveGerarHashCodeConsistente() {
        // Given
        ApiResult<String> result1 = new ApiResult<>("teste", "ok", true);
        ApiResult<String> result2 = new ApiResult<>("teste", "ok", true);

        // Then
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString legível")
    void deveGerarToStringLegivel() {
        // Given
        ApiResult<String> result = new ApiResult<>("dados", "sucesso", true);

        // When
        String toString = result.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("dados"));
        assertTrue(toString.contains("sucesso"));
        assertTrue(toString.contains("true"));
    }
}