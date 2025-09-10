package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
class ValidationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void criarProduto_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given: Invalid ProdutoRequest (missing required fields)
        ProdutoRequest invalidRequest = new ProdutoRequest();
        invalidRequest.setNome(""); // Should fail @NotBlank validation
        invalidRequest.setCategoria(""); // Should fail @NotBlank validation
        invalidRequest.setPreco(new BigDecimal("-10.00")); // Should fail @DecimalMin validation
        invalidRequest.setRestauranteId(-1L); // Should fail @Positive validation

        // When & Then: Should return 400 Bad Request
        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizarProduto_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given: Invalid ProdutoRequest (missing required fields)
        ProdutoRequest invalidRequest = new ProdutoRequest();
        invalidRequest.setNome(""); // Should fail @NotBlank validation
        invalidRequest.setCategoria(""); // Should fail @NotBlank validation
        invalidRequest.setPreco(new BigDecimal("-10.00")); // Should fail @DecimalMin validation
        invalidRequest.setRestauranteId(-1L); // Should fail @Positive validation

        // When & Then: Should return 400 Bad Request
        mockMvc.perform(put("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void criarRestaurante_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given: Invalid RestauranteRequest (missing required fields)
        RestauranteRequest invalidRequest = new RestauranteRequest();
        invalidRequest.setNome(""); // Should fail @NotBlank validation
        invalidRequest.setCategoria(""); // Should fail @NotBlank validation
        invalidRequest.setEndereco(""); // Should fail @NotBlank validation
        invalidRequest.setTaxaEntrega(new BigDecimal("-10.00")); // Should fail @DecimalMin validation
        invalidRequest.setTempoEntregaMinutos(-5); // Should fail @Min validation
        invalidRequest.setEmail("invalid-email"); // Should fail @Email validation

        // When & Then: Should return 400 Bad Request
        mockMvc.perform(post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizarRestaurante_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given: Invalid RestauranteRequest (missing required fields)
        RestauranteRequest invalidRequest = new RestauranteRequest();
        invalidRequest.setNome(""); // Should fail @NotBlank validation
        invalidRequest.setCategoria(""); // Should fail @NotBlank validation
        invalidRequest.setEndereco(""); // Should fail @NotBlank validation
        invalidRequest.setTaxaEntrega(new BigDecimal("-10.00")); // Should fail @DecimalMin validation
        invalidRequest.setTempoEntregaMinutos(-5); // Should fail @Min validation
        invalidRequest.setEmail("invalid-email"); // Should fail @Email validation

        // When & Then: Should return 400 Bad Request
        mockMvc.perform(put("/api/restaurantes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}