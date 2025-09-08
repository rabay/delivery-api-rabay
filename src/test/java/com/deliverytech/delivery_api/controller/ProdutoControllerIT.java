package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Restaurante createRestaurante(String nome) {
        RestauranteRequest restauranteRequest = new RestauranteRequest();
        restauranteRequest.setNome(nome);
        restauranteRequest.setCategoria("Teste");
        restauranteRequest.setEndereco("Rua Teste, 123");
        restauranteRequest.setTaxaEntrega(BigDecimal.valueOf(5.0));
        restauranteRequest.setTempoEntregaMinutos(30);
        restauranteRequest.setAvaliacao(BigDecimal.valueOf(4.5));
        return restauranteService.cadastrar(restauranteRequest);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProduto_andReturnCreated() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Create");

        ProdutoRequest req = new ProdutoRequest();
        req.setNome("Produto Create");
        req.setCategoria("Cat");
        req.setDescricao("Desc");
        req.setPreco(BigDecimal.valueOf(9.99));
        req.setRestauranteId(r.getId());
        req.setQuantidadeEstoque(10);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.nome").value("Produto Create"));
    }

    @Test
    void shouldListProdutosByRestaurante_andReturnPagedItems() throws Exception {
        Restaurante r = createRestaurante("Rest Prod List");

        Produto p1 = new Produto();
        p1.setNome("P1"); p1.setCategoria("C"); p1.setDescricao("D"); p1.setPreco(BigDecimal.valueOf(1)); p1.setRestaurante(r); p1.setQuantidadeEstoque(5);
        produtoService.cadastrar(p1);

        Produto p2 = new Produto();
        p2.setNome("P2"); p2.setCategoria("C"); p2.setDescricao("D"); p2.setPreco(BigDecimal.valueOf(2)); p2.setRestaurante(r); p2.setQuantidadeEstoque(3);
        produtoService.cadastrar(p2);

        mockMvc.perform(get("/api/restaurantes/{restauranteId}/produtos", r.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.links.first").exists())
                .andExpect(jsonPath("$.data.links.last").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduto_andReturnUpdated() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Update");

        Produto p = new Produto();
        p.setNome("P Update"); p.setCategoria("C"); p.setDescricao("D"); p.setPreco(BigDecimal.valueOf(5)); p.setRestaurante(r); p.setQuantidadeEstoque(8);
        Produto saved = produtoService.cadastrar(p);

        ProdutoRequest update = new ProdutoRequest();
        update.setNome("P Updated");
        update.setCategoria("C2");
        update.setDescricao("D2");
        update.setPreco(BigDecimal.valueOf(7));
        update.setRestauranteId(r.getId());
        update.setQuantidadeEstoque(4);

        mockMvc.perform(put("/api/produtos/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("P Updated"))
                .andExpect(jsonPath("$.data.categoria").value("C2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldPatchDisponibilidade_andReturnOk() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Patch");

        Produto p = new Produto();
        p.setNome("P Patch"); p.setCategoria("C"); p.setDescricao("D"); p.setPreco(BigDecimal.valueOf(3)); p.setRestaurante(r); p.setQuantidadeEstoque(6);
        Produto saved = produtoService.cadastrar(p);

        Map<String, Boolean> body = new HashMap<>();
        body.put("disponivel", Boolean.FALSE);

        mockMvc.perform(patch("/api/produtos/{id}/disponibilidade", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.disponivel").value(false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteProduto_andReturnOk() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Delete");

        Produto p = new Produto();
        p.setNome("P Del"); p.setCategoria("C"); p.setDescricao("D"); p.setPreco(BigDecimal.valueOf(4)); p.setRestaurante(r); p.setQuantidadeEstoque(2);
        Produto saved = produtoService.cadastrar(p);

        mockMvc.perform(delete("/api/produtos/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produto removido com sucesso"));
    }

    @Test
    void shouldListProdutosPaged_andReturnCorrectPage() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Page");
        for (int i = 0; i < 6; i++) {
            Produto p = new Produto();
            p.setNome("PageP" + i); p.setCategoria("C"); p.setDescricao("D"); p.setPreco(BigDecimal.valueOf(1 + i)); p.setRestaurante(r); p.setQuantidadeEstoque(5);
            produtoService.cadastrar(p);
        }

        mockMvc.perform(get("/api/produtos?page=1&size=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(3))
                .andExpect(jsonPath("$.data.links.first").exists())
                .andExpect(jsonPath("$.data.links.last").exists());
    }
}
