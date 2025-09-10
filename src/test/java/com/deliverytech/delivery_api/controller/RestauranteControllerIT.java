package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class RestauranteControllerIT extends AbstractIntegrationTest {

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
        request.setCategoria("Brasileira");
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
    void shouldCreateRestaurante_WithInvalidData_ReturnBadRequest() throws Exception {
        RestauranteRequest request = new RestauranteRequest();
        request.setNome(""); // Nome vazio
        request.setCategoria("CategoriaInvalida"); // Categoria inválida
        request.setEndereco("Rua B, 456");
        request.setTaxaEntrega(new BigDecimal("-1.0")); // Taxa negativa
        request.setTempoEntregaMinutos(-5); // Tempo negativo

        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetRestauranteById_whenExists() throws Exception {
        // criar via service para simplificar
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Get");
        req.setCategoria("Italiana");
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
    void shouldGetRestauranteById_WithInvalidId_ReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/restaurantes/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Restaurante não encontrado")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateRestaurante_andReturnUpdated() throws Exception {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Up");
        req.setCategoria("Mexicana");
        req.setEndereco("Rua C, 2");
        req.setTaxaEntrega(BigDecimal.valueOf(4));
        req.setTempoEntregaMinutos(25);
        Restaurante saved = restauranteService.cadastrar(req);

        RestauranteRequest update = new RestauranteRequest();
        update.setNome("Restaurante Updated");
        update.setCategoria("Japonesa");
        update.setEndereco("Rua C, 2");
        update.setTaxaEntrega(BigDecimal.valueOf(6));
        update.setTempoEntregaMinutos(40);

        mockMvc.perform(put("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Restaurante Updated"))
                .andExpect(jsonPath("$.data.categoria").value("Japonesa"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldInactivateRestaurante_andReturnOk() throws Exception {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante To Inactivate");
        req.setCategoria("Chinesa");
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
            req.setCategoria("Americana");
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

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListRestaurantes_FilterByCategoria() throws Exception {
        // criar restaurantes de diferentes categorias (usando categoria única para teste)
        RestauranteRequest req1 = new RestauranteRequest();
        req1.setNome("Restaurante Mexicano Unico");
        req1.setCategoria("Mexicana");
        req1.setEndereco("Rua A, 1");
        req1.setTaxaEntrega(BigDecimal.valueOf(5));
        req1.setTempoEntregaMinutos(25);
        restauranteService.cadastrar(req1);

        // criar outro restaurante de categoria diferente
        RestauranteRequest req2 = new RestauranteRequest();
        req2.setNome("Restaurante Americana Unico");
        req2.setCategoria("Americana");
        req2.setEndereco("Rua B, 2");
        req2.setTaxaEntrega(BigDecimal.valueOf(4));
        req2.setTempoEntregaMinutos(20);
        restauranteService.cadastrar(req2);

        mockMvc.perform(get("/api/restaurantes")
                .param("categoria", "Mexicana")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.totalItems").isNumber())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateRestaurante_WithValidData() throws Exception {
        // criar restaurante
        RestauranteRequest createReq = new RestauranteRequest();
        createReq.setNome("Restaurante Original");
        createReq.setCategoria("Brasileira");
        createReq.setEndereco("Rua Original, 1");
        createReq.setTaxaEntrega(BigDecimal.valueOf(3));
        createReq.setTempoEntregaMinutos(20);
        Restaurante saved = restauranteService.cadastrar(createReq);

        // atualizar
        RestauranteRequest updateReq = new RestauranteRequest();
        updateReq.setNome("Restaurante Atualizado");
        updateReq.setCategoria("Italiana");
        updateReq.setEndereco("Rua Atualizada, 2");
        updateReq.setTaxaEntrega(BigDecimal.valueOf(5));
        updateReq.setTempoEntregaMinutos(25);

        mockMvc.perform(put("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome", is("Restaurante Atualizado")))
                .andExpect(jsonPath("$.data.categoria", is("Italiana")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeactivateRestaurante() throws Exception {
        // criar restaurante
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Para Desativar");
        req.setCategoria("Chinesa");
        req.setEndereco("Rua Desativar, 1");
        req.setTaxaEntrega(BigDecimal.valueOf(4));
        req.setTempoEntregaMinutos(30);
        Restaurante saved = restauranteService.cadastrar(req);

        mockMvc.perform(delete("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("inativado")));

        // verificar se foi desativado (deve retornar o restaurante mas com ativo=false)
        mockMvc.perform(get("/api/restaurantes/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ativo", is(false)))
                .andExpect(jsonPath("$.data.excluido", is(true)));
    }
}
