package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestauranteControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteService restauranteService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateRestauranteWithValidData_andReturnLocationHeader() throws Exception {
        RestauranteRequest request = new RestauranteRequest();
        request.setNome("Restaurante Teste");
        request.setCategoria("Geral");
        request.setEndereco("Rua A, 123");
        request.setTaxaEntrega(new BigDecimal("5.0"));
        request.setTempoEntregaMinutos(30);
        request.setTelefone("11999999999");
        request.setEmail("rest@test.com");

        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetRestauranteById_whenExists() throws Exception {
        // criar via service para simplificar
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Get");
        req.setCategoria("Teste");
        req.setEndereco("Rua B, 1");
        req.setTaxaEntrega(BigDecimal.valueOf(3));
        req.setTempoEntregaMinutos(20);
        Restaurante saved = restauranteService.cadastrar(req);

        mockMvc.perform(get("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.nome").value("Restaurante Get"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateRestaurante_andReturnUpdated() throws Exception {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Up");
        req.setCategoria("Teste");
        req.setEndereco("Rua C, 2");
        req.setTaxaEntrega(BigDecimal.valueOf(4));
        req.setTempoEntregaMinutos(25);
        Restaurante saved = restauranteService.cadastrar(req);

        RestauranteRequest update = new RestauranteRequest();
        update.setNome("Restaurante Updated");
        update.setCategoria("Atualizado");
        update.setEndereco("Rua C, 2");
        update.setTaxaEntrega(BigDecimal.valueOf(6));
        update.setTempoEntregaMinutos(40);

        mockMvc.perform(put("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Restaurante Updated"))
                .andExpect(jsonPath("$.data.categoria").value("Atualizado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldInactivateRestaurante_andReturnOk() throws Exception {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante To Inactivate");
        req.setCategoria("Teste");
        req.setEndereco("Rua D, 3");
        req.setTaxaEntrega(BigDecimal.valueOf(2));
        req.setTempoEntregaMinutos(15);
        Restaurante saved = restauranteService.cadastrar(req);

        mockMvc.perform(delete("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Restaurante inativado com sucesso"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnPagedRestaurantes_andIncludeLinks() throws Exception {
        // criar alguns restaurantes
        for (int i = 0; i < 7; i++) {
            RestauranteRequest req = new RestauranteRequest();
            req.setNome("R Pag " + i);
            req.setCategoria("Cat");
            req.setEndereco("Rua X, " + i);
            req.setTaxaEntrega(BigDecimal.valueOf(1));
            req.setTempoEntregaMinutos(10 + i);
            restauranteService.cadastrar(req);
        }

        mockMvc.perform(get("/api/restaurantes?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(5))
                .andExpect(jsonPath("$.data.links.first").exists())
                .andExpect(jsonPath("$.data.links.last").exists());
    }
}
