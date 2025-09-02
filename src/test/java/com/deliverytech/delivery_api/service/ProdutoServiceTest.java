package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProdutoServiceTest extends BaseIntegrationTest {

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private RestauranteService restauranteService;

    @Test
    void softDeleteDeveMarcarComoExcluidoENaoRetornarEmBuscarDisponiveis() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante SoftDelete");
        req.setCategoria("Cat");
        req.setTaxaEntrega(BigDecimal.ONE);
        req.setEmail("mail@p.com");
        req.setTempoEntregaMinutos(10);
        req.setAvaliacao(BigDecimal.ONE);
        Restaurante restaurante = restauranteService.cadastrar(req);
        Produto produto = new Produto();
        produto.setNome("Produto SoftDelete");
        produto.setCategoria("Cat");
        produto.setDescricao("Desc");
        produto.setPreco(BigDecimal.TEN);
        produto.setRestaurante(restaurante);
        produto.setQuantidadeEstoque(10); // Add required field
        produto = produtoService.cadastrar(produto);
        final Long produtoId = produto.getId();
        produtoService.deletar(produtoId);
        List<Produto> disponiveis = produtoService.buscarDisponiveisEntities();
        assertThat(disponiveis.stream().anyMatch(p -> p.getId().equals(produtoId))).isFalse();
    }
}