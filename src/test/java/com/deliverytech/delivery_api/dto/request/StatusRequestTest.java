package com.deliverytech.delivery_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para StatusRequest")
class StatusRequestTest {

    @Test
    @DisplayName("Deve criar StatusRequest com construtor padrão")
    void deveCriarStatusRequestComConstrutorPadrao() {
        // Given & When
        StatusRequest statusRequest = new StatusRequest();

        // Then
        assertNotNull(statusRequest);
        assertNull(statusRequest.getAtivo());
    }

    @Test
    @DisplayName("Deve definir e obter status ativo")
    void deveDefinirEObterStatusAtivo() {
        // Given
        StatusRequest statusRequest = new StatusRequest();
        Boolean ativo = true;

        // When
        statusRequest.setAtivo(ativo);

        // Then
        assertEquals(ativo, statusRequest.getAtivo());
    }

    @Test
    @DisplayName("Deve definir e obter status inativo")
    void deveDefinirEObterStatusInativo() {
        // Given
        StatusRequest statusRequest = new StatusRequest();
        Boolean ativo = false;

        // When
        statusRequest.setAtivo(ativo);

        // Then
        assertEquals(ativo, statusRequest.getAtivo());
        assertFalse(statusRequest.getAtivo());
    }

    @Test
    @DisplayName("Deve permitir definir status como null")
    void devePermitirDefinirStatusComoNull() {
        // Given
        StatusRequest statusRequest = new StatusRequest();
        statusRequest.setAtivo(true);

        // When
        statusRequest.setAtivo(null);

        // Then
        assertNull(statusRequest.getAtivo());
    }

    @Test
    @DisplayName("Deve manter valor após múltiplas definições")
    void deveManterValorAposMultiplasDefinicoes() {
        // Given
        StatusRequest statusRequest = new StatusRequest();

        // When
        statusRequest.setAtivo(true);
        statusRequest.setAtivo(false);
        statusRequest.setAtivo(true);

        // Then
        assertTrue(statusRequest.getAtivo());
    }
}