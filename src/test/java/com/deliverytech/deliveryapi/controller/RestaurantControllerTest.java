package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.AddressDTO;
import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import com.deliverytech.deliveryapi.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void testUpdateRestaurant() throws Exception {
        // Given
        Long restaurantId = 1L;
        
        AddressDTO addressDTO = new AddressDTO(
                "Rua Exemplo", "123", "Sala 101", "Bairro Exemplo",
                "São Paulo", "SP", "01234-567", "Próximo ao metrô"
        );

        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Pizza Palace",
                "O melhor restaurante de pizza da cidade",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://exemplo.com/logo.png",
                BigDecimal.valueOf(15.0),
                BigDecimal.valueOf(25.0),
                30,
                List.of(1L)
        );

        RestaurantDTO expectedResponse = new RestaurantDTO(
                restaurantId,
                "Pizza Palace",
                "O melhor restaurante de pizza da cidade",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://exemplo.com/logo.png",
                true,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        when(restaurantService.updateRestaurant(restaurantId, request)).thenReturn(expectedResponse);
        
        mockMvc.perform(put("/api/v1/restaurants/" + restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.data.description").value(expectedResponse.description()))
                .andExpect(jsonPath("$.data.active").value(expectedResponse.active()))
                .andExpect(jsonPath("$.meta.message").value("Restaurante atualizado com sucesso"));
        
        verify(restaurantService, times(1)).updateRestaurant(restaurantId, request);
    }
    
    @Test
    @DisplayName("Deve deletar restaurante com sucesso")
    void testDeleteRestaurant() throws Exception {
        Long restaurantId = 1L;
        
        doNothing().when(restaurantService).deleteRestaurant(restaurantId);
        
        mockMvc.perform(delete("/api/v1/restaurants/" + restaurantId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.message").value("Restaurante removido com sucesso"));
        
        verify(restaurantService, times(1)).deleteRestaurant(restaurantId);
    }
    
    @Test
    @DisplayName("Deve ativar restaurante com sucesso")
    void testToggleActiveStatus() throws Exception {
        // Given
        Long restaurantId = 1L;
        boolean active = true;
        
        AddressDTO addressDTO = new AddressDTO(
                "Rua Exemplo", "123", "Sala 101", "Bairro Exemplo",
                "São Paulo", "SP", "01234-567", "Próximo ao metrô"
        );

        RestaurantDTO expectedResponse = new RestaurantDTO(
                restaurantId,
                "Pizza Palace",
                "O melhor restaurante de pizza da cidade",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://exemplo.com/logo.png",
                true,  // ativo
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        when(restaurantService.updateActiveStatus(restaurantId, active)).thenReturn(expectedResponse);
        
        mockMvc.perform(patch("/api/v1/restaurants/" + restaurantId + "/status")
                .param("active", String.valueOf(active))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.active").value(true))
                .andExpect(jsonPath("$.meta.message").value("Restaurante ativado com sucesso"));
        
        verify(restaurantService, times(1)).updateActiveStatus(restaurantId, active);
    }
    
    @Test
    @DisplayName("Deve fechar restaurante com sucesso")
    void testToggleOpenStatusFalse() throws Exception {
        // Given
        Long restaurantId = 1L;
        boolean open = false;
        
        AddressDTO addressDTO = new AddressDTO(
                "Rua Exemplo", "123", "Sala 101", "Bairro Exemplo",
                "São Paulo", "SP", "01234-567", "Próximo ao metrô"
        );

        RestaurantDTO expectedResponse = new RestaurantDTO(
                restaurantId,
                "Pizza Palace",
                "O melhor restaurante de pizza da cidade",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://exemplo.com/logo.png",
                true,
                false,  // fechado
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        when(restaurantService.updateOpenStatus(restaurantId, open)).thenReturn(expectedResponse);
        
        mockMvc.perform(patch("/api/v1/restaurants/" + restaurantId + "/open-status")
                .param("open", String.valueOf(open))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.open").value(false))
                .andExpect(jsonPath("$.meta.message").value("Restaurante fechado com sucesso"));
        
        verify(restaurantService, times(1)).updateOpenStatus(restaurantId, open);
    }
}
