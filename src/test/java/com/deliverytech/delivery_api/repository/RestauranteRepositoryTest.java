package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RestauranteRepositoryTest {
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testFindByNomeContainingIgnoreCase() {
    Restaurante r = new Restaurante();
    r.setNome("Pizza Place");
    r.setCategoria("Italiana");
    r.setAtivo(true);
    r.setAvaliacao(new java.math.BigDecimal("4.5"));
        restauranteRepository.save(r);
        List<Restaurante> results = restauranteRepository.findByNomeContainingIgnoreCase("pizza");
        assertThat(results).isNotEmpty();
    }

    @Test
    void testFindByCategoria() {
    Restaurante r = new Restaurante();
    r.setNome("Sushi Bar");
    r.setCategoria("Japonesa");
    r.setAtivo(true);
    r.setAvaliacao(new java.math.BigDecimal("4.8"));
        restauranteRepository.save(r);
        assertThat(restauranteRepository.findByCategoria("Japonesa")).extracting(Restaurante::getNome).contains("Sushi Bar");
    }
}
