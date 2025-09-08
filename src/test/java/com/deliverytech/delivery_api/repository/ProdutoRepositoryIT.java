package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProdutoRepositoryIT extends BaseIntegrationTest {

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
        p.setPreco(new BigDecimal("15.00"));
        p.setQuantidadeEstoque(10); // Add required field
        produtoRepository.save(p);
        List<Produto> results = produtoRepository.findByRestauranteAndExcluidoFalse(r);
        assertThat(results).extracting(Produto::getNome).contains("X-Burguer");
        // adicional: verificar busca por id do restaurante (findByRestauranteIdAndExcluidoFalse)
        List<Produto> resultsById = produtoRepository.findByRestauranteIdAndExcluidoFalse(r.getId());
        assertThat(resultsById).extracting(Produto::getNome).contains("X-Burguer");
    }
}
