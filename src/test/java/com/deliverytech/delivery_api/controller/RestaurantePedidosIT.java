package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RestaurantePedidosIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnPagedPedidosForRestaurante_whenExists() throws Exception {
        // criar restaurante
        RestauranteRequest rr = new RestauranteRequest();
        rr.setNome("Restaurante Pedidos");
        rr.setCategoria("Cat");
        rr.setEndereco("Rua Test, 1");
        rr.setTaxaEntrega(BigDecimal.valueOf(2));
        rr.setTempoEntregaMinutos(20);
        Restaurante saved = restauranteService.cadastrar(rr);

        // criar produto via serviço com restauranteId
        com.deliverytech.delivery_api.dto.request.ProdutoRequest pr = new com.deliverytech.delivery_api.dto.request.ProdutoRequest();
        pr.setNome("Produto Test");
        pr.setDescricao("Desc");
        pr.setPreco(BigDecimal.valueOf(10));
        pr.setDisponivel(true);
        pr.setQuantidadeEstoque(10);
        pr.setCategoria("Geral");
        pr.setRestauranteId(saved.getId());
        var prodResp = produtoService.cadastrar(pr);

        // criar pedido via endpoint POST /api/pedidos
        com.deliverytech.delivery_api.dto.request.PedidoRequest pedido = new com.deliverytech.delivery_api.dto.request.PedidoRequest();
        pedido.setRestauranteId(saved.getId());
        pedido.setClienteId(1L);
        com.deliverytech.delivery_api.dto.request.EnderecoRequest endereco = new com.deliverytech.delivery_api.dto.request.EnderecoRequest();
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro");
        endereco.setCidade("Cidade");
        endereco.setEstado("SP");
        endereco.setCep("01234-567");
        pedido.setEnderecoEntrega(endereco);

        com.deliverytech.delivery_api.dto.request.ItemPedidoRequest item = new com.deliverytech.delivery_api.dto.request.ItemPedidoRequest();
        item.setProdutoId(prodResp.getId());
        item.setQuantidade(1);
        pedido.setItens(List.of(item));

        String pedidoJson = objectMapper.writeValueAsString(pedido);

        // create pedidos twice to have more than one
        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pedidoJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pedidoJson))
                .andExpect(status().isCreated());

        // now call GET /api/restaurantes/{id}/pedidos
        mockMvc.perform(get("/api/restaurantes/{id}/pedidos", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.links.first").exists());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturn404_whenRestauranteNotFound() throws Exception {
        mockMvc.perform(get("/api/restaurantes/{id}/pedidos", 9999999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    // no helpers
}
