package com.deliverytech.delivery_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.projection.RelatorioVendas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.testcontainers.junit.jupiter.Testcontainers;

// Remove @DataJpaTest since we're using @SpringBootTest in BaseIntegrationTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class PedidoRepositoryTest extends BaseRepositoryTest {
  @Autowired private PedidoRepository pedidoRepository;
  @Autowired private ClienteRepository clienteRepository;

  @Autowired private RestauranteRepository restauranteRepository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void testCalcularTotalVendasPorRestaurante() {
    Restaurante r = new Restaurante();
    r.setNome("Restaurante Teste");
    r.setCategoria("Pizza");
    r.setAtivo(true);
    r.setAvaliacao(new BigDecimal("4.5"));
    restauranteRepository.save(r);

    Cliente c = new Cliente();
    c.setNome("Cliente Teste");
    c.setEmail("cliente@teste.com");
    c.setAtivo(true);
    clienteRepository.save(c);

    Pedido p = new Pedido();
    p.setCliente(c);
    p.setRestaurante(r);
    p.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
    p.setDataPedido(LocalDateTime.now());
    p.setValorTotal(new BigDecimal("100.00"));
    p.setItens(new ArrayList<>());
    pedidoRepository.save(p);

    List<RelatorioVendas> relatorio = pedidoRepository.calcularTotalVendasPorRestaurante();
    assertThat(relatorio).isNotEmpty();
    assertThat(relatorio.get(0).getNomeRestaurante()).isEqualTo("Restaurante Teste");
    assertThat(relatorio.get(0).getTotalVendas()).isEqualByComparingTo("100.00");
    assertThat(relatorio.get(0).getQuantidadePedidos()).isEqualTo(1L);
  }

  @Test
  void testFindByClienteId() {
    Cliente c = new Cliente();
    c.setNome("Carlos");
    c.setEmail("carlos@teste-unico.com");
    c.setAtivo(true);
    clienteRepository.save(c);

    // Create restaurante
    Restaurante r = new Restaurante();
    r.setNome("Restaurante Carlos");
    r.setCategoria("Teste");
    r.setAtivo(true);
    restauranteRepository.save(r);

    Pedido p = new Pedido();
    p.setCliente(c);
    p.setRestaurante(r); // Add missing restaurante
    p.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
    p.setDataPedido(LocalDateTime.now());
    p.setValorTotal(BigDecimal.valueOf(100.0)); // Add required field
    pedidoRepository.save(p);
    List<Pedido> results = pedidoRepository.findByClienteId(c.getId());
    assertThat(results).isNotEmpty();
  }

  @Test
  void testFindTop10ByOrderByDataPedidoDesc() {
    Cliente c = new Cliente();
    c.setNome("Cliente Recentes");
    c.setEmail("recentes@teste.com");
    c.setAtivo(true);
    clienteRepository.save(c);

    Restaurante r = new Restaurante();
    r.setNome("Restaurante Recentes");
    r.setCategoria("Teste");
    r.setAtivo(true);
    restauranteRepository.save(r);

    // criar 12 pedidos com datas decrescentes
    for (int i = 0; i < 12; i++) {
      Pedido p = new Pedido();
      p.setCliente(c);
      p.setRestaurante(r);
      p.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
      p.setDataPedido(LocalDateTime.now().minusDays(i));
      p.setValorTotal(BigDecimal.valueOf(50.0 + i)); // Add required field with unique values
      pedidoRepository.save(p);
    }

    List<Pedido> recentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
    assertThat(recentes).hasSize(10);
    // verificar que a primeira posição é a mais recente
    assertThat(recentes.get(0).getDataPedido()).isAfterOrEqualTo(recentes.get(9).getDataPedido());
  }
}
