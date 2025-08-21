package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

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
        List<Produto> results = produtoRepository.findByRestaurante(r);
        assertThat(results).extracting(Produto::getNome).contains("X-Burguer");
    }
}
