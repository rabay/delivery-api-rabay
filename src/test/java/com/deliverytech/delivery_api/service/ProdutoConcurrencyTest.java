package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProdutoConcurrencyTest {

  @Container
  public static final MySQLContainer<?> mysql =
      new MySQLContainer<>("mysql:8.0.34")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void datasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
  }

  @Autowired private ProdutoService produtoService;
  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private RestauranteRepository restauranteRepository;

  @BeforeAll
  public static void beforeAll() {
    // Testcontainers iniciará o container automaticamente
  }

  @AfterAll
  public static void afterAll() {
    if (mysql != null) {
      mysql.stop();
    }
  }

  @Test
  public void testConcurrentReservationsDoNotOversell() throws InterruptedException {
    // Cria restaurante e produto com estoque inicial 5
    Restaurante r = Restaurante.builder().nome("R1").build();
    restauranteRepository.save(r);

    Produto p =
        Produto.builder()
            .nome("Produto Concorrencia")
            .preco(BigDecimal.TEN)
            .restaurante(r)
            .quantidadeEstoque(5)
            .disponivel(true)
            .build();

    produtoRepository.save(p);

    int threads = 10;
    ExecutorService executor = Executors.newFixedThreadPool(threads);

    List<Callable<Boolean>> tasks = new ArrayList<>();

    for (int i = 0; i < threads; i++) {
      tasks.add(
          () -> {
            try {
              // Cada thread constrói um pedido com 1 unidade do produto
              Pedido pedido = Pedido.builder().build();
              ItemPedido item = ItemPedido.builder().produto(p).quantidade(1).build();
              pedido.addItem(item);

              // Chama o serviço que é @Transactional
              produtoService.reservarEstoque(pedido);
              return true;
            } catch (Exception ex) {
              // Esperado: algumas threads podem lançar EstoqueInsuficienteException
              return false;
            }
          });
    }

    List<Future<Boolean>> results = executor.invokeAll(tasks);

    int success = 0;
    for (Future<Boolean> f : results) {
      try {
        if (f.get()) success++;
      } catch (ExecutionException e) {
        // contar como falha
      }
    }

    executor.shutdownNow();

    Produto produtoFinal = produtoRepository.findById(p.getId()).orElseThrow();

    // Verificações: número de sucessos não deve exceder estoque inicial
    assertTrue(success <= 5, "Número de reservas com sucesso excede estoque inicial");

    // Estoque final não deve ser negativo
    assertTrue(
        produtoFinal.getQuantidadeEstoque() >= 0,
        "Estoque final não deve ser negativo. Atual: " + produtoFinal.getQuantidadeEstoque());
  }
}
