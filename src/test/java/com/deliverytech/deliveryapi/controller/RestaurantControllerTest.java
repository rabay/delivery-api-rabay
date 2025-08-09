package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.AddressDTO;
import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import com.deliverytech.deliveryapi.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateRestaurantSuccessfully() throws Exception {
        // Given
        String jsonRequest = """
            {
                "name": "Restaurante Teste",
                "description": "Descrição do restaurante",
                "cnpj": "12.345.678/0001-90",
                "phone": "(11) 99999-9999",
                "address": {
                    "street": "Rua Exemplo",
                    "number": "123",
                    "complement": "Sala 101",
                    "neighborhood": "Bairro Exemplo",
                    "city": "Cidade Exemplo",
                    "state": "SP",
                    "postalCode": "01234-567",
                    "reference": "Próximo ao ponto de ônibus"
                },
                "logo": "https://exemplo.com/logo.png",
                "deliveryFee": 5.00,
                "minimumOrderValue": 15.00,
                "averageDeliveryTimeInMinutes": 30
            }
            """;

        AddressDTO addressDTO = new AddressDTO(
                "Rua Exemplo",
                "123",
                "Sala 101",
                "Bairro Exemplo",
                "Cidade Exemplo",
                "SP",
                "01234-567",
                "Próximo ao ponto de ônibus"
        );

        RestaurantDTO restaurantDTO = new RestaurantDTO(
                1L,
                "Restaurante Teste",
                "Descrição do restaurante",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://exemplo.com/logo.png",
                true,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(restaurantService.createRestaurant(any(CreateRestaurantRequest.class))).thenReturn(restaurantDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meta.message").value("Restaurante criado com sucesso"))
                .andExpect(jsonPath("$.data.name").value("Restaurante Teste"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        // Given
        String jsonRequest = """
            {
                "name": "",
                "description": "Descrição do restaurante",
                "cnpj": "12.345.678/0001-90",
                "phone": "(11) 99999-9999",
                "address": {
                    "street": "Rua Exemplo",
                    "number": "123",
                    "neighborhood": "Bairro Exemplo",
                    "city": "Cidade Exemplo",
                    "state": "SP",
                    "postalCode": "01234-567"
                },
                "deliveryFee": -5.00
            }
            """;

        when(restaurantService.createRestaurant(any(CreateRestaurantRequest.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        // When & Then
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Dados inválidos"));
    }
}
