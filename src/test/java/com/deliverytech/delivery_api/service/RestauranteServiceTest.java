package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import java.util.List;

@SpringBootTest
class RestauranteServiceTest {
    @Autowired
    private RestauranteService restauranteService;

    @Test
    void contextLoads() {
        assertThat(restauranteService).isNotNull();
    }

    @Test
    void softDeleteDeveMarcarComoExcluidoENaoRetornarEmListarAtivos() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante SoftDelete");
        req.setCategoria("Teste");
        req.setEndereco("Rua X");
        req.setTaxaEntrega(java.math.BigDecimal.TEN);
        req.setTelefone("123");
        req.setEmail("soft@delete.com");
        req.setTempoEntregaMinutos(30);
        req.setAvaliacao(java.math.BigDecimal.ONE);
        Restaurante restaurante = restauranteService.cadastrar(req);
        restauranteService.inativar(restaurante.getId());
        List<Restaurante> ativos = restauranteService.listarAtivos();
        assertThat(ativos.stream().anyMatch(r -> r.getId().equals(restaurante.getId()))).isFalse();
        // Opcional: buscar diretamente e verificar campo excluido
        var opt = restauranteService.buscarPorId(restaurante.getId());
        assertThat(opt).isPresent();
        assertThat(opt.get().getExcluido()).isTrue();
    }
}
