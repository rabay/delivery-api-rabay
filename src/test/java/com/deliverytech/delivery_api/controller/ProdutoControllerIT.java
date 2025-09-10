package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class ProdutoControllerIT extends AbstractIntegrationTest {

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
        req.setCategoria("Pizza");
        req.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres");
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
    void shouldListProdutosPaged_andReturnPagedItems() throws Exception {
        // Criar produtos específicos para este teste
        Restaurante r = createRestaurante("Rest Prod List Specific");

        Produto p1 = new Produto();
        p1.setNome("Produto List 1"); p1.setCategoria("Pizza"); p1.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p1.setPreco(BigDecimal.valueOf(10.00)); p1.setRestaurante(r); p1.setQuantidadeEstoque(5);
        produtoService.cadastrar(p1);

        Produto p2 = new Produto();
        p2.setNome("Produto List 2"); p2.setCategoria("Bebida"); p2.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p2.setPreco(BigDecimal.valueOf(5.00)); p2.setRestaurante(r); p2.setQuantidadeEstoque(3);
        produtoService.cadastrar(p2);

        mockMvc.perform(get("/api/produtos?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.links.first").exists())
                .andExpect(jsonPath("$.data.links.last").exists())
                .andExpect(jsonPath("$.data.totalItems").isNumber());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduto_andReturnUpdated() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Update");

        Produto p = new Produto();
        p.setNome("P Update"); p.setCategoria("Pizza"); p.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p.setPreco(BigDecimal.valueOf(5)); p.setRestaurante(r); p.setQuantidadeEstoque(8);
        Produto saved = produtoService.cadastrar(p);

        ProdutoRequest update = new ProdutoRequest();
        update.setNome("P Updated");
        update.setCategoria("Bebida");
        update.setDescricao("Esta é uma descrição atualizada válida com pelo menos 10 caracteres");
        update.setPreco(BigDecimal.valueOf(7));
        update.setRestauranteId(r.getId());
        update.setQuantidadeEstoque(4);

        mockMvc.perform(put("/api/produtos/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("P Updated"))
                .andExpect(jsonPath("$.data.categoria").value("Bebida"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldPatchDisponibilidade_andReturnOk() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Patch");

        Produto p = new Produto();
        p.setNome("P Patch"); p.setCategoria("Sobremesa"); p.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p.setPreco(BigDecimal.valueOf(3)); p.setRestaurante(r); p.setQuantidadeEstoque(6);
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
        p.setNome("P Del"); p.setCategoria("Lanche"); p.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p.setPreco(BigDecimal.valueOf(4)); p.setRestaurante(r); p.setQuantidadeEstoque(2);
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
            p.setNome("PageP" + i); p.setCategoria("Pizza"); p.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p.setPreco(BigDecimal.valueOf(1 + i)); p.setRestaurante(r); p.setQuantidadeEstoque(5);
            produtoService.cadastrar(p);
        }

        mockMvc.perform(get("/api/produtos?page=1&size=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(3))
                .andExpect(jsonPath("$.data.links.first").exists())
                .andExpect(jsonPath("$.data.links.last").exists());
    }

    @Test
    void shouldGetProdutoById_andReturnProduto() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Get");

        Produto p = new Produto();
        p.setNome("Produto Get"); p.setCategoria("Bebida"); p.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p.setPreco(BigDecimal.valueOf(5.50)); p.setRestaurante(r); p.setQuantidadeEstoque(10);
        Produto saved = produtoService.cadastrar(p);

        mockMvc.perform(get("/api/produtos/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(saved.getId().intValue()))
                .andExpect(jsonPath("$.data.nome").value("Produto Get"))
                .andExpect(jsonPath("$.data.categoria").value("Bebida"));
    }

    @Test
    void shouldGetProdutoById_whenNotFound_andReturn404() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", 99999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"));
    }

    @Test
    void shouldSearchProdutosByNome_andReturnFilteredResults() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Search Specific");

        Produto p1 = new Produto();
        p1.setNome("Pizza Margherita Special"); p1.setCategoria("Pizza"); p1.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p1.setPreco(BigDecimal.valueOf(25.00)); p1.setRestaurante(r); p1.setQuantidadeEstoque(5);
        produtoService.cadastrar(p1);

        Produto p2 = new Produto();
        p2.setNome("Pizza Calabresa Special"); p2.setCategoria("Pizza"); p2.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p2.setPreco(BigDecimal.valueOf(28.00)); p2.setRestaurante(r); p2.setQuantidadeEstoque(3);
        produtoService.cadastrar(p2);

        Produto p3 = new Produto();
        p3.setNome("Refrigerante Cola Special"); p3.setCategoria("Bebida"); p3.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p3.setPreco(BigDecimal.valueOf(5.00)); p3.setRestaurante(r); p3.setQuantidadeEstoque(20);
        produtoService.cadastrar(p3);

        mockMvc.perform(get("/api/produtos/buscar")
                .param("nome", "Special")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(3))
                .andExpect(jsonPath("$.data.items[0].nome").value("Pizza Margherita Special"))
                .andExpect(jsonPath("$.data.items[1].nome").value("Pizza Calabresa Special"))
                .andExpect(jsonPath("$.data.items[2].nome").value("Refrigerante Cola Special"));
    }

    @Test
    void shouldSearchProdutosByCategoria_andReturnFilteredResults() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Cat");

        Produto p1 = new Produto();
        p1.setNome("Coca-Cola"); p1.setCategoria("Bebida"); p1.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p1.setPreco(BigDecimal.valueOf(5.00)); p1.setRestaurante(r); p1.setQuantidadeEstoque(10);
        produtoService.cadastrar(p1);

        Produto p2 = new Produto();
        p2.setNome("Fanta Laranja"); p2.setCategoria("Bebida"); p2.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p2.setPreco(BigDecimal.valueOf(4.50)); p2.setRestaurante(r); p2.setQuantidadeEstoque(8);
        produtoService.cadastrar(p2);

        Produto p3 = new Produto();
        p3.setNome("Pizza Margherita"); p3.setCategoria("Pizza"); p3.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres"); p3.setPreco(BigDecimal.valueOf(25.00)); p3.setRestaurante(r); p3.setQuantidadeEstoque(5);
        produtoService.cadastrar(p3);

        mockMvc.perform(get("/api/produtos/categoria/{categoria}", "Bebida")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.items[0].categoria").value("Bebida"))
                .andExpect(jsonPath("$.data.items[1].categoria").value("Bebida"));
    }

    @Test
    void shouldSearchProdutosByCategoria_whenNoResults_andReturnEmpty() throws Exception {
        mockMvc.perform(get("/api/produtos/categoria/{categoria}", "CategoriaInexistente")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(0))
                .andExpect(jsonPath("$.data.totalItems").value(0));
    }

    @Test
    void shouldCreateProduto_withInvalidCategoria_andReturn400() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Invalid");

        ProdutoRequest req = new ProdutoRequest();
        req.setNome("Produto Invalid");
        req.setCategoria("CategoriaInvalida");
        req.setDescricao("Esta é uma descrição válida com pelo menos 10 caracteres");
        req.setPreco(BigDecimal.valueOf(9.99));
        req.setRestauranteId(r.getId());
        req.setQuantidadeEstoque(10);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateProduto_withShortDescricao_andReturn400() throws Exception {
        Restaurante r = createRestaurante("Rest Prod Short Desc");

        ProdutoRequest req = new ProdutoRequest();
        req.setNome("Produto Short");
        req.setCategoria("Pizza");
        req.setDescricao("Short");
        req.setPreco(BigDecimal.valueOf(9.99));
        req.setRestauranteId(r.getId());
        req.setQuantidadeEstoque(10);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
