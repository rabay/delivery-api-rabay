package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testProdutosMaisVendidos() {
        Restaurante r = new Restaurante();
        r.setNome("Restaurante Lanches");
        r.setCategoria("Lanches");
        r.setAtivo(true);
        r.setAvaliacao(new BigDecimal("4.0"));
        restauranteRepository.save(r);

        Produto p = new Produto();
        p.setNome("Coxinha");
        p.setCategoria("Salgado");
        p.setDisponivel(true);
        p.setRestaurante(r);
        p.setPreco(new BigDecimal("10.00"));
        produtoRepository.save(p);

        // Não há vendas reais, mas consulta deve retornar produto com quantidade 0
        List<RelatorioVendasProdutos> ranking = produtoRepository.produtosMaisVendidos();
        assertThat(ranking).isNotEmpty();
        assertThat(ranking.get(0).getNomeProduto()).isEqualTo("Coxinha");
    }

    @Test
    void testFindByRestaurante() {
    Restaurante r = new Restaurante();
    r.setNome("Burguer House");
    r.setCategoria("Lanches");
    r.setAtivo(true);
    r.setAvaliacao(new java.math.BigDecimal("4.2"));
        restauranteRepository.save(r);
        Produto p = new Produto();
        p.setNome("X-Burguer");
        p.setCategoria("Lanche");
        p.setDisponivel(true);
        p.setRestaurante(r);
        produtoRepository.save(p);
    List<Produto> results = produtoRepository.findByRestauranteAndExcluidoFalse(r);
        assertThat(results).extracting(Produto::getNome).contains("X-Burguer");
    }
}
