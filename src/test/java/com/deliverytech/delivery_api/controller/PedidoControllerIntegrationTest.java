package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PedidoControllerIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Long extractId(String json) {
        // More robust extraction using regex pattern
        System.out.println("Extracting ID from JSON: " + json);
        if (json == null || json.isEmpty()) {
            System.err.println("JSON is null or empty");
            return null;
        }
        
        // Look for "id": followed by a number (possibly with quotes)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"id\"\\s*:\\s*(\"?)(\\d+)\\1");
        java.util.regex.Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            try {
                Long id = Long.valueOf(matcher.group(2));
                System.out.println("Successfully extracted ID: " + id);
                System.out.println("ID class: " + id.getClass().getName());
                return id;
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse ID from string: " + matcher.group(2));
                return null;
            }
        } else {
            System.err.println("Could not find 'id' field in JSON");
            // Let's try a different approach
            System.err.println("Trying alternative extraction method...");
            int idStart = json.indexOf("\"id\":");
            if (idStart != -1) {
                idStart += 5; // length of "\"id\":"
                // Skip whitespace
                while (idStart < json.length() && Character.isWhitespace(json.charAt(idStart))) {
                    idStart++;
                }
                // Skip quote if present
                if (idStart < json.length() && json.charAt(idStart) == '"') {
                    idStart++;
                }
                // Find end of ID
                int idEnd = idStart;
                while (idEnd < json.length() && (Character.isDigit(json.charAt(idEnd)) || json.charAt(idEnd) == '"')) {
                    if (json.charAt(idEnd) == '"') {
                        break;
                    }
                    idEnd++;
                }
                String idStr = json.substring(idStart, idEnd);
                System.out.println("Alternative extraction found ID string: '" + idStr + "'");
                try {
                    Long id = Long.valueOf(idStr);
                    System.out.println("Successfully parsed ID: " + id);
                    return id;
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse ID from alternative method: " + idStr);
                }
            }
            return null;
        }
    }

    @Test
    void criarPedido_e_buscarPorCliente_retornaListaComPedido() {
        System.out.println("=== STARTING TEST ===");
        // Autenticar como admin
        System.out.println("Attempting authentication...");
        String authJson = "{\"username\": \"admin@deliveryapi.com\", \"password\": \"admin123\"}";
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> authReq = new HttpEntity<>(authJson, authHeaders);
        ResponseEntity<String> authResp = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", authReq, String.class);
        System.out.println("Auth response status: " + authResp.getStatusCode());
        System.out.println("Auth response body: " + authResp.getBody());
        assertThat(authResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Extract token from ApiResult.data.token using Jackson
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String token = null;
        try {
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(authResp.getBody());
            com.fasterxml.jackson.databind.JsonNode data = root.path("data");
            if (!data.isMissingNode()) {
                com.fasterxml.jackson.databind.JsonNode tokenNode = data.path("token");
                if (!tokenNode.isMissingNode()) {
                    token = tokenNode.asText();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse auth response: " + e.getMessage());
        }
        System.out.println("Extracted token: " + token);
        assertThat(token).isNotNull();

        // Configurar headers com token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token == null) {
            throw new IllegalStateException("Token not found in auth response");
        }
        headers.setBearerAuth(token);

        // Criar cliente
        System.out.println("Creating client...");
        String clienteJson = "{\"nome\": \"Cli Int\", \"email\": \"cli@test.com\", \"telefone\": \"11999999999\", \"endereco\": \"Rua Teste, 123\", \"senha\": \"123456\"}";
        HttpEntity<String> cliReq = new HttpEntity<>(clienteJson, headers);
        ResponseEntity<String> cliResp = restTemplate.postForEntity("http://localhost:" + port + "/api/clientes", cliReq, String.class);
        System.out.println("Client response status: " + cliResp.getStatusCode());
        System.out.println("Client response body: " + cliResp.getBody());
        assertThat(cliResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println("Cliente Response: " + cliResp.getBody());
        Long clienteId = extractId(cliResp.getBody());
        System.out.println("Extracted clienteId: " + clienteId);
        System.out.println("ClienteId class: " + (clienteId != null ? clienteId.getClass().getName() : "null"));
        assertThat(clienteId).isNotNull().isPositive();

        // Criar restaurante
        System.out.println("Creating restaurant...");
        String restauranteJson = "{\"nome\": \"Rest Int\", \"categoria\": \"Geral\", \"endereco\": \"Rua Teste, 123\", \"taxaEntrega\": 5.0, \"tempoEntregaMinutos\": 30, \"telefone\": \"11999999999\", \"email\": \"rest@test.com\", \"avaliacao\": 4.0 }";
        HttpEntity<String> restReq = new HttpEntity<>(restauranteJson, headers);
        ResponseEntity<String> restResp = restTemplate.postForEntity("http://localhost:" + port + "/api/restaurantes", restReq, String.class);
        System.out.println("Restaurant response status: " + restResp.getStatusCode());
        System.out.println("Restaurant response body: " + restResp.getBody());
        assertThat(restResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println("Restaurante Response: " + restResp.getBody());
        Long restauranteId = extractId(restResp.getBody());
        System.out.println("Extracted restauranteId: " + restauranteId);
        assertThat(restauranteId).isNotNull().isPositive();

        // Criar produto
        System.out.println("Creating product...");
        String produtoJson = "{\"nome\": \"Prod Int\", \"categoria\": \"Geral\", \"preco\": 9.9, \"disponivel\": true, \"restauranteId\": " + restauranteId + ", \"quantidadeEstoque\": 10 }";
        HttpEntity<String> prodReq = new HttpEntity<>(produtoJson, headers);
        ResponseEntity<String> prodResp = restTemplate.postForEntity("http://localhost:" + port + "/api/produtos", prodReq, String.class);
        System.out.println("Product response status: " + prodResp.getStatusCode());
        System.out.println("Product response body: " + prodResp.getBody());
        assertThat(prodResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println("Produto Response: " + prodResp.getBody());
        Long produtoId = extractId(prodResp.getBody());
        System.out.println("Extracted produtoId: " + produtoId);
        assertThat(produtoId).isNotNull().isPositive();

        // Criar pedido - Fixed by adding precoUnitario to the item
        System.out.println("Creating order...");
        String pedidoJson = "{\"clienteId\": " + clienteId + ", \"restauranteId\": " + restauranteId + ", \"enderecoEntrega\": { \"rua\": \"Rua Teste\", \"numero\": \"123\", \"bairro\": \"Bairro Teste\", \"cidade\": \"Cidade Teste\", \"estado\": \"SP\", \"cep\": \"01000-000\" }, \"itens\": [{ \"produtoId\": " + produtoId + ", \"quantidade\": 1, \"precoUnitario\": 9.9 }] }";
        HttpEntity<String> pedidoReq = new HttpEntity<>(pedidoJson, headers);
        ResponseEntity<String> pedidoResp = restTemplate.postForEntity("http://localhost:" + port + "/api/pedidos", pedidoReq, String.class);
        System.out.println("Pedido Response Status: " + pedidoResp.getStatusCode());
        System.out.println("Pedido Response Body: " + pedidoResp.getBody());
        
        // Check if order creation was successful before proceeding
        assertThat(pedidoResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        // Print debug information before making the search request
        System.out.println("=== DEBUG INFO ===");
        System.out.println("clienteId: " + clienteId);
        System.out.println("clienteId type: " + (clienteId != null ? clienteId.getClass().getName() : "null"));
        System.out.println("clienteId value: " + clienteId);
        
        // Buscar pedidos pelo cliente
        // Note: This endpoint does not require authentication according to SecurityConfig
        HttpHeaders noAuthHeaders = new HttpHeaders();
        noAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> buscarReq = new HttpEntity<>(noAuthHeaders);
        String url = "http://localhost:" + port + "/api/clientes/" + clienteId + "/pedidos";
        System.out.println("Making request to: " + url);
        System.out.println("Using clienteId: " + clienteId + " (type: " + (clienteId != null ? clienteId.getClass().getName() : "null") + ")");
        ResponseEntity<String> buscarResp = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            buscarReq, 
            String.class
        );
        System.out.println("Search Response Status: " + buscarResp.getStatusCode());
        System.out.println("Search Response Body: " + buscarResp.getBody());
        System.out.println("Search Response Headers: " + buscarResp.getHeaders());
        
        // Assert that we get a successful response
        assertThat(buscarResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(buscarResp.getBody()).isNotNull();
        assertThat(buscarResp.getBody()).contains("id");
    }
}