package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

// import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class ProdutoServiceTest {
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private RestauranteService restauranteService;

    @Test
    void contextLoads() {
        assertThat(produtoService).isNotNull();
    }

    @Test
    void softDeleteDeveMarcarComoExcluidoENaoRetornarEmBuscarDisponiveis() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Produto");
        req.setCategoria("Cat");
        req.setEndereco("End");
        req.setTaxaEntrega(BigDecimal.ONE);
        req.setTelefone("123");
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
    produto = produtoService.cadastrar(produto);
    final Long produtoId = produto.getId();
    produtoService.deletar(produtoId);
    List<Produto> disponiveis = produtoService.buscarDisponiveis();
    assertThat(disponiveis.stream().anyMatch(p -> p.getId().equals(produtoId))).isFalse();
    var opt = produtoService.buscarPorId(produtoId);
    assertThat(opt).isPresent();
    assertThat(opt.get().getExcluido()).isTrue();
    }
}
