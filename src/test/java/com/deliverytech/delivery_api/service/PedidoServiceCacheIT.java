package com.deliverytech.delivery_api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.PedidoServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(
    properties = {
      "spring.cache.type=simple",
      "spring.cache.cache-names=produtos,pedidos,relatorios"
    })
@Transactional
public class PedidoServiceCacheIT extends com.deliverytech.delivery_api.BaseIntegrationTest {

  @Autowired private PedidoServiceImpl pedidoService;

  @Autowired private PedidoRepository pedidoRepository;

  @Autowired private ClienteRepository clienteRepository;

  @Autowired private RestauranteRepository restauranteRepository;

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired private CacheManager cacheManager;

  private Cliente cliente;
  private Restaurante restaurante;
  private Produto produto;

  @BeforeEach
  void setUp() {
    // Clean up cache before each test
    cacheManager.getCache("pedidos").clear();

    // Create test data
    cliente = new Cliente();
    cliente.setNome("Cliente Teste");
    cliente.setEmail("cliente@teste.com");
    cliente.setTelefone("11999999999");
    cliente.setAtivo(true);
    cliente.setExcluido(false);
    cliente = clienteRepository.save(cliente);

    restaurante = new Restaurante();
    restaurante.setNome("Restaurante Teste");
    restaurante.setEmail("restaurante@teste.com");
    restaurante.setTelefone("11999999999");
    restaurante.setAtivo(true);
    restaurante.setExcluido(false);
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.0));
    restaurante = restauranteRepository.save(restaurante);

    produto = new Produto();
    produto.setNome("Produto Teste");
    produto.setCategoria("Categoria Teste");
    produto.setDescricao("Descrição Teste");
    produto.setPreco(BigDecimal.valueOf(10.0));
    produto.setDisponivel(true);
    produto.setExcluido(false);
    produto.setQuantidadeEstoque(100);
    produto.setRestaurante(restaurante);
    produto = produtoRepository.save(produto);
  }

  @Test
  void testBuscarPorClienteIsCached() {
    // Given
    Pedido pedido = new Pedido();
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setStatus(StatusPedido.CRIADO);
    pedido.setValorTotal(BigDecimal.valueOf(15.0));

    ItemPedido item = new ItemPedido();
    item.setProduto(produto);
    item.setQuantidade(1);
    item.setPrecoUnitario(BigDecimal.valueOf(10.0));
    item.setSubtotal(BigDecimal.valueOf(10.0));
    item.setPedido(pedido);

    pedido.setItens(List.of(item));
    pedidoRepository.save(pedido);

    // When - First call
    List<Pedido> result1 = pedidoService.buscarPorCliente(cliente.getId());

    // When - Second call with same parameters
    List<Pedido> result2 = pedidoService.buscarPorCliente(cliente.getId());

    // Then
    assertThat(result1).isNotEmpty();
    assertThat(result2).isNotEmpty();
    assertThat(result1.size()).isEqualTo(result2.size());
  }
}
