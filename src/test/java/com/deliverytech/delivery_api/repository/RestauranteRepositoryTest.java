package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

// Remove @DataJpaTest since we're using @SpringBootTest in BaseIntegrationTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RestauranteRepositoryTest extends BaseIntegrationTest {
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
    List<Restaurante> results = restauranteRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse("pizza");
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
    assertThat(restauranteRepository.findByCategoriaAndExcluidoFalse("Japonesa")).extracting(Restaurante::getNome).contains("Sushi Bar");
    }

    @Test
    void testFindByTaxaEntregaLessThanEqual() {
        Restaurante r1 = new Restaurante();
        r1.setNome("Barra Rápida");
        r1.setCategoria("Lanches");
        r1.setAtivo(true);
        r1.setTaxaEntrega(new java.math.BigDecimal("4.50"));
        restauranteRepository.save(r1);

        Restaurante r2 = new Restaurante();
        r2.setNome("Entrega Cara");
        r2.setCategoria("Lanches");
        r2.setAtivo(true);
        r2.setTaxaEntrega(new java.math.BigDecimal("6.00"));
        restauranteRepository.save(r2);

        List<Restaurante> resultados = restauranteRepository.findByTaxaEntregaLessThanEqualAndExcluidoFalse(new java.math.BigDecimal("5.00"));
        assertThat(resultados).extracting(Restaurante::getNome).contains("Barra Rápida");
        assertThat(resultados).extracting(Restaurante::getNome).doesNotContain("Entrega Cara");
    }
}