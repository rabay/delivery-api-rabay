package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Test
    void shouldListProdutosByRestaurante() throws Exception {
        // First create a restaurante
        com.deliverytech.delivery_api.dto.request.RestauranteRequest restauranteRequest = 
            new com.deliverytech.delivery_api.dto.request.RestauranteRequest();
        restauranteRequest.setNome("Restaurante Teste");
        restauranteRequest.setCategoria("Teste");
        restauranteRequest.setEndereco("Rua Teste, 123");
        restauranteRequest.setTaxaEntrega(BigDecimal.valueOf(5.0));
        restauranteRequest.setTempoEntregaMinutos(30);
        restauranteRequest.setAvaliacao(BigDecimal.valueOf(4.5));
        
        Restaurante restaurante = restauranteService.cadastrar(restauranteRequest);
        
        // Create some produtos for this restaurante
        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        produto1.setCategoria("Categoria 1");
        produto1.setDescricao("Descrição 1");
        produto1.setPreco(BigDecimal.valueOf(10.0));
        produto1.setRestaurante(restaurante);
        produto1.setQuantidadeEstoque(10);
        produtoService.cadastrar(produto1);
        
        Produto produto2 = new Produto();
        produto2.setNome("Produto 2");
        produto2.setCategoria("Categoria 2");
        produto2.setDescricao("Descrição 2");
        produto2.setPreco(BigDecimal.valueOf(15.0));
        produto2.setRestaurante(restaurante);
        produto2.setQuantidadeEstoque(5);
        produtoService.cadastrar(produto2);
        
        // Test the endpoint
        mockMvc.perform(get("/api/restaurantes/{restauranteId}/produtos", restaurante.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // now the response is an ApiResult with data.items (paged response)
                .andExpect(jsonPath("$.data.items.length()").value(2));
    }

    @Test
    void shouldReturnNotFoundWhenRestauranteDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/restaurantes/{restauranteId}/produtos", 999999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAllProdutos() throws Exception {
        mockMvc.perform(get("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldFindProdutoById() throws Exception {
        // First create a restaurante
        com.deliverytech.delivery_api.dto.request.RestauranteRequest restauranteRequest = 
            new com.deliverytech.delivery_api.dto.request.RestauranteRequest();
        restauranteRequest.setNome("Restaurante Teste 2");
        restauranteRequest.setCategoria("Teste");
        restauranteRequest.setEndereco("Rua Teste, 456");
        restauranteRequest.setTaxaEntrega(BigDecimal.valueOf(7.0));
        restauranteRequest.setTempoEntregaMinutos(25);
        restauranteRequest.setAvaliacao(BigDecimal.valueOf(4.0));
        
        Restaurante restaurante = restauranteService.cadastrar(restauranteRequest);
        
        // Create a produto
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setCategoria("Categoria Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(BigDecimal.valueOf(20.0));
        produto.setRestaurante(restaurante);
        produto.setQuantidadeEstoque(15);
        Produto savedProduto = produtoService.cadastrar(produto);
        
        // Test finding the produto by ID
        mockMvc.perform(get("/api/produtos/{id}", savedProduto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(savedProduto.getId()))
                .andExpect(jsonPath("$.data.nome").value("Produto Teste"));
    }

    @Test
    void shouldReturnNotFoundWhenProdutoDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", 999999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}