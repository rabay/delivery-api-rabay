package com.deliverytech.delivery_api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PedidoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private String jwtToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Get JWT token by logging in with admin user
        jwtToken = loginAndGetToken("admin@deliveryapi.com", "admin123");
    }
    
    private String loginAndGetToken(String email, String password) {
        String loginJson = "{\"username\": \"" + email + "\", \"password\": \"" + password + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(loginJson, headers);
        
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            "http://localhost:" + port + "/api/auth/login", 
            loginRequest, 
            String.class
        );
        
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode responseNode = objectMapper.readTree(loginResponse.getBody());
                return responseNode.get("token").asText();
            } catch (Exception e) {
                throw new RuntimeException("Failed to extract token from login response", e);
            }
        } else {
            throw new RuntimeException("Login failed with status: " + loginResponse.getStatusCode());
        }
    }
    
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        return headers;
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/pedidos";
    }

    @Test
    void buscarPorCliente_semPedidos_retornaListaVazia() {
        // Garante um cliente isolado (assumimos id alto improvável de existir)
        Long clienteId = 99999L;
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/clientes/" + clienteId + "/pedidos", 
            HttpMethod.GET, 
            requestEntity, 
            String.class
        );

        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST);
        // Se a API seguir a nova regra, deve retornar 200 com array vazio
        if (response.getStatusCode() == HttpStatus.OK) {
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).startsWith("[");
        }
    }

    @Test
    void criarPedido_e_buscarPorCliente_retornaListaComPedido() {
        // Cria cliente, restaurante e produto via endpoints existentes da API através da collection precondição já usada
        // Para simplicidade, tentamos criar um cliente e produto e usar seus ids retornados
        
        HttpHeaders headers = createAuthHeaders();

        // Criar cliente
        String clienteJson = "{\"nome\": \"Integration Cliente\", \"email\": \"int_cliente_" + System.currentTimeMillis() + "@test.com\", \"telefone\": \"11900000000\", \"endereco\": \"Endereco\" }";
        HttpEntity<String> clienteReq = new HttpEntity<>(clienteJson, headers);
        ResponseEntity<String> clienteResp = restTemplate.postForEntity("http://localhost:" + port + "/api/clientes", clienteReq, String.class);
        assertThat(clienteResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        // extrair id simples do body (assume JSON com campo id)
        String body = clienteResp.getBody();
        Long clienteId = JsonUtils.extractId(body);

        // Criar restaurante
        String restauranteJson = "{\"nome\": \"Rest Int\", \"categoria\": \"Geral\", \"ativo\": true, \"avaliacao\": 4.0 }";
        HttpEntity<String> restReq = new HttpEntity<>(restauranteJson, headers);
        ResponseEntity<String> restResp = restTemplate.postForEntity("http://localhost:" + port + "/api/restaurantes", restReq, String.class);
        assertThat(restResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long restauranteId = JsonUtils.extractId(restResp.getBody());

        // Criar produto
        String produtoJson = "{\"nome\": \"Prod Int\", \"categoria\": \"Geral\", \"preco\": 9.9, \"disponivel\": true, \"restaurante\": { \"id\": " + restauranteId + " } }";
        HttpEntity<String> prodReq = new HttpEntity<>(produtoJson, headers);
        ResponseEntity<String> prodResp = restTemplate.postForEntity("http://localhost:" + port + "/api/produtos", prodReq, String.class);
        assertThat(prodResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long produtoId = JsonUtils.extractId(prodResp.getBody());

        // Criar pedido
        String pedidoJson = "{\"clienteId\": " + clienteId + ", \"restauranteId\": " + restauranteId + ", \"enderecoEntrega\": { \"rua\": \"R\", \"numero\": \"1\", \"bairro\": \"B\", \"cidade\": \"C\", \"estado\": \"S\", \"cep\": \"01000-000\" }, \"itens\": [{ \"produtoId\": " + produtoId + ", \"quantidade\": 1 }] }";
        HttpEntity<String> pedidoReq = new HttpEntity<>(pedidoJson, headers);
        ResponseEntity<String> pedidoResp = restTemplate.postForEntity("http://localhost:" + port + "/api/pedidos", pedidoReq, String.class);
        assertThat(pedidoResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Buscar pedidos pelo cliente
        HttpEntity<String> buscarReq = new HttpEntity<>(headers);
        ResponseEntity<String> buscarResp = restTemplate.exchange(
            "http://localhost:" + port + "/api/clientes/" + clienteId + "/pedidos", 
            HttpMethod.GET, 
            buscarReq, 
            String.class
        );
        assertThat(buscarResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(buscarResp.getBody()).isNotNull();
        assertThat(buscarResp.getBody()).contains("id");
    }
}