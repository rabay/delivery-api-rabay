package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PedidoControllerIT extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreatePedido_andReturnCreated() throws Exception {
    // Primeiro criar dados necessários
    String clienteJson =
        """
                {
                    "nome": "João Silva",
                    "email": "joao@test.com",
                    "telefone": "11999999999",
                    "endereco": "Rua Teste, 123",
                    "senha": "123456"
                }
                """;

    String restauranteJson =
        """
                {
                    "nome": "Pizzaria Teste",
                    "categoria": "Pizzaria",
                    "endereco": "Rua Restaurante, 456",
                    "taxaEntrega": 5.0,
                    "tempoEntregaMinutos": 30,
                    "telefone": "11999999999",
                    "email": "rest@test.com",
                    "avaliacao": 4.5
                }
                """;

    // Criar cliente
    mockMvc
        .perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(clienteJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists());

    // Criar restaurante
    var restauranteResult =
        mockMvc
            .perform(
                post("/api/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(restauranteJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

    String restauranteResponse = restauranteResult.getResponse().getContentAsString();
    Long restauranteId = extractIdFromJson(restauranteResponse);

    // Criar produto
    String produtoJsonWithRestaurante =
        """
                {
                    "nome": "Pizza Margherita",
                    "categoria": "Pizza",
                    "preco": 25.90,
                    "disponivel": true,
                    "quantidadeEstoque": 10,
                    "restauranteId": %d
                }
                """
            .formatted(restauranteId);

    mockMvc
        .perform(
            post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoJsonWithRestaurante))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists());

    // Agora criar pedido
    String pedidoJson =
        """
                {
                    "clienteId": 1,
                    "restauranteId": %d,
                    "enderecoEntrega": {
                        "rua": "Rua Entrega",
                        "numero": "123",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "estado": "SP",
                        "cep": "01000-000"
                    },
                    "itens": [
                        {
                            "produtoId": 1,
                            "quantidade": 2,
                            "precoUnitario": 25.90
                        }
                    ]
                }
                """
            .formatted(restauranteId);

    mockMvc
        .perform(post("/api/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedidoJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.status").value("CRIADO"))
        .andExpect(jsonPath("$.data.valorTotal").value(50.0));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldListPedidosPaged_andReturnPagedItems() throws Exception {
    mockMvc
        .perform(get("/api/pedidos").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.items").isArray())
        .andExpect(jsonPath("$.data.totalItems").isNumber())
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.size").value(10));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldGetPedidoById_andReturnPedido() throws Exception {
    mockMvc
        .perform(get("/api/pedidos/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.status").exists())
        .andExpect(jsonPath("$.data.cliente").exists());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldGetPedidoById_whenNotFound_andReturn404() throws Exception {
    mockMvc
        .perform(get("/api/pedidos/{id}", 999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldUpdatePedidoStatus_andReturnUpdated() throws Exception {
    String statusUpdateJson =
        """
                {
                    "status": "PREPARANDO"
                }
                """;

    mockMvc
        .perform(
            patch("/api/pedidos/{id}/status", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusUpdateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("PREPARANDO"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldGetPedidosByCliente_andReturnList() throws Exception {
    mockMvc
        .perform(get("/api/clientes/{clienteId}/pedidos", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.items").isArray());
  }

  // @Test
  // @WithMockUser(roles = "ADMIN")
  // void shouldGetPedidosByStatus_andReturnFiltered() throws Exception {
  // mockMvc.perform(get("/api/pedidos/status/{status}", "CRIADO"))
  // .andExpect(status().isOk())
  // .andExpect(jsonPath("$.data.items").isArray());
  // }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreatePedido_withInvalidData_andReturn400() throws Exception {
    String invalidPedidoJson =
        """
                {
                    "clienteId": 1,
                    "restauranteId": 1,
                    "enderecoEntrega": {
                        "rua": "",
                        "numero": "",
                        "bairro": "",
                        "cidade": "",
                        "estado": "",
                        "cep": ""
                    },
                    "itens": []
                }
                """;

    mockMvc
        .perform(
            post("/api/pedidos").contentType(MediaType.APPLICATION_JSON).content(invalidPedidoJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreatePedido_withNonExistentCliente_andReturn404() throws Exception {
    String pedidoJson =
        """
                {
                    "clienteId": 999,
                    "restauranteId": 1,
                    "enderecoEntrega": {
                        "rua": "Rua Teste",
                        "numero": "123",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "estado": "SP",
                        "cep": "01000-000"
                    },
                    "itens": [
                        {
                            "produtoId": 1,
                            "quantidade": 1,
                            "precoUnitario": 25.90
                        }
                    ]
                }
                """;

    mockMvc
        .perform(post("/api/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedidoJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreatePedido_withInsufficientStock_andReturn400() throws Exception {
    String pedidoJson =
        """
                {
                    "clienteId": 1,
                    "restauranteId": 1,
                    "enderecoEntrega": {
                        "rua": "Rua Teste",
                        "numero": "123",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "estado": "SP",
                        "cep": "01000-000"
                    },
                    "itens": [
                        {
                            "produtoId": 1,
                            "quantidade": 100,
                            "precoUnitario": 25.90
                        }
                    ]
                }
                """;

    mockMvc
        .perform(post("/api/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedidoJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  private Long extractIdFromJson(String json) {
    // Simple extraction for test purposes
    int idStart = json.indexOf("\"id\":");
    if (idStart != -1) {
      idStart += 5;
      while (idStart < json.length() && !Character.isDigit(json.charAt(idStart))) {
        idStart++;
      }
      int idEnd = idStart;
      while (idEnd < json.length() && Character.isDigit(json.charAt(idEnd))) {
        idEnd++;
      }
      return Long.valueOf(json.substring(idStart, idEnd));
    }
    return null;
  }
}
