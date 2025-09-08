package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import java.util.List;

@Import({com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl.class, com.deliverytech.delivery_api.mapper.RestauranteMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RestauranteServiceIT extends BaseIntegrationTest {

    @Autowired
    private RestauranteService restauranteService;

    @Test
    void contextLoads() {
        assertThat(restauranteService).isNotNull();
    }

    @Test
    void deveCadastrarRestauranteQuandoDadosValidos() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Teste");
        req.setCategoria("Italiana");
        req.setEndereco("Rua Y");
        req.setTaxaEntrega(java.math.BigDecimal.ONE);
        req.setTelefone("999");
        req.setEmail("mail@r.com");
        req.setTempoEntregaMinutos(25);
        req.setAvaliacao(java.math.BigDecimal.valueOf(4.5)); // Valid rating 0-5
        Restaurante restaurante = restauranteService.cadastrar(req);
        assertThat(restaurante).isNotNull();
        assertThat(restaurante.getNome()).isEqualTo("Restaurante Teste");
        assertThat(restaurante.getCategoria()).isEqualTo("Italiana");
        assertThat(restaurante.getId()).isNotNull();
    }

    @Test
    void deveAtualizarRestauranteQuandoExistente() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Atualiza");
        req.setCategoria("Japonesa");
        req.setEndereco("Rua Z");
        req.setTaxaEntrega(java.math.BigDecimal.valueOf(2));
        req.setTelefone("888");
        req.setEmail("atualiza@r.com");
        req.setTempoEntregaMinutos(40);
        req.setAvaliacao(java.math.BigDecimal.valueOf(4.0)); // Valid rating 0-5
        Restaurante restaurante = restauranteService.cadastrar(req);
        RestauranteRequest update = new RestauranteRequest();
        update.setNome("Restaurante Atualizado");
        update.setCategoria("Fusion");
        update.setEndereco("Rua Nova");
        update.setTaxaEntrega(java.math.BigDecimal.valueOf(3));
        update.setTelefone("777");
        update.setEmail("novo@r.com");
        update.setTempoEntregaMinutos(50);
        update.setAvaliacao(java.math.BigDecimal.valueOf(4.5)); // Valid rating 0-5
        Restaurante atualizado = restauranteService.atualizar(restaurante.getId(), update);
        assertThat(atualizado.getNome()).isEqualTo("Restaurante Atualizado");
        assertThat(atualizado.getCategoria()).isEqualTo("Fusion");
        assertThat(atualizado.getEndereco()).isEqualTo("Rua Nova");
    }

    @Test
    void deveLancarExcecaoAoBuscarRestauranteInexistente() {
        var opt = restauranteService.buscarPorId(-999L);
        assertThat(opt).isNotPresent();
    }

    @Test
    void deveLancarExcecaoAoCadastrarComNomeVazio() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("");
        req.setCategoria("Teste");
        req.setEndereco("Rua Q");
        req.setTaxaEntrega(java.math.BigDecimal.ONE);
        req.setTelefone("000");
        req.setEmail("inv@r.com");
        req.setTempoEntregaMinutos(10);
        req.setAvaliacao(java.math.BigDecimal.ONE);
        try {
            restauranteService.cadastrar(req);
            assertThat(false).as("Deveria lançar exceção para nome vazio").isTrue();
        } catch (Exception e) {
            assertThat(e.getMessage()).containsIgnoringCase("nome");
        }
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
