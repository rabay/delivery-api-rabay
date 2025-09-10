package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ClienteControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateClienteWithValidData() throws Exception {
        // Given
        ClienteRequest request = new ClienteRequest();
        request.setNome("João Silva");
        request.setEmail("joao@email.com");
        request.setTelefone("11999999999");
        request.setEndereco("Rua A, 123");

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.data.telefone").value("11999999999"))
                .andExpect(jsonPath("$.data.endereco").value("Rua A, 123"))
                .andExpect(jsonPath("$.data.ativo").value(true));
    }

    @Test
    void shouldReturnConflictWhenCreatingClienteWithDuplicateEmail() throws Exception {
        // Given
        ClienteRequest request1 = new ClienteRequest();
        request1.setNome("Maria Silva");
        request1.setEmail("maria@email.com");
        request1.setTelefone("11988888888");
        request1.setEndereco("Rua B, 456");

        // Create first client
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        // Try to create another client with the same email
        ClienteRequest request2 = new ClienteRequest();
        request2.setNome("Maria Silva 2");
        request2.setEmail("maria@email.com"); // Same email
        request2.setTelefone("11977777777");
        request2.setEndereco("Rua C, 789");

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingClienteWithInvalidData() throws Exception {
        // Given
        ClienteRequest request = new ClienteRequest();
        request.setNome(""); // Invalid: empty name
        request.setEmail("invalid-email"); // Invalid: malformed email
        request.setTelefone("123"); // Invalid: too short
        request.setEndereco(""); // Invalid: empty address

        // When & Then
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListAtivosClientes() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldFindClienteById() throws Exception {
        // First create a client
        ClienteRequest request = new ClienteRequest();
        request.setNome("Cliente Teste");
        request.setEmail("cliente@teste.com");
        request.setTelefone("11955555555");
        request.setEndereco("Rua Teste, 123");

        String response = mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // The API now returns an envelope ApiResult with a `data` field
        // Parse the response and extract the data node first
        com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(response);
        com.fasterxml.jackson.databind.JsonNode dataNode = root.path("data");
        ClienteResponse createdClient = objectMapper.treeToValue(dataNode, ClienteResponse.class);
        Long clientId = createdClient.getId();

        // Now test finding by ID
        mockMvc.perform(get("/api/clientes/{id}", clientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(clientId))
                .andExpect(jsonPath("$.data.nome").value("Cliente Teste"))
                .andExpect(jsonPath("$.data.email").value("cliente@teste.com"));
    }

    @Test
    void shouldReturnNotFoundWhenFindingNonExistentCliente() throws Exception {
        // Try to find a client with non-existent ID
        mockMvc.perform(get("/api/clientes/{id}", 999999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}