package com.deliverytech.delivery_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.mapper.PedidoMapper;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PedidoServiceImplTest {

  @Mock private PedidoRepository pedidoRepository;

  @Mock private ProdutoRepository produtoRepository;

  @Mock private ClienteRepository clienteRepository;

  @Mock private RestauranteRepository restauranteRepository;

  @Mock private PedidoMapper pedidoMapper;

  @Mock private ProdutoService produtoService;

  @InjectMocks private PedidoServiceImpl pedidoService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidarEstoqueItens_WithSufficientStock_ShouldNotThrowException() {
    // Create test data
    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);

    Produto produto = new Produto();
    produto.setId(1L);
    produto.setNome("Produto Teste");
    produto.setQuantidadeEstoque(10);
    produto.setDisponivel(true);
    produto.setRestaurante(restaurante);

    ItemPedido item = new ItemPedido();
    item.setId(1L);
    item.setProduto(produto);
    item.setQuantidade(3);
    item.setPrecoUnitario(new BigDecimal("10.00"));
    item.setSubtotal(new BigDecimal("30.00"));

    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setItens(java.util.Arrays.asList(item));

    // Configure mocks
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
    doNothing().when(produtoService).validarEstoque(produto, 3);

    // Test the method
    assertDoesNotThrow(() -> pedidoService.validarEstoqueItens(pedido));

    // Verify that produtoService.validarEstoque was called
    verify(produtoService, times(1)).validarEstoque(produto, 3);
  }

  @Test
  public void testValidarEstoqueItens_WithInsufficientStock_ShouldThrowException() {
    // Create test data
    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);

    Produto produto = new Produto();
    produto.setId(1L);
    produto.setNome("Produto Teste");
    produto.setQuantidadeEstoque(2);
    produto.setDisponivel(true);
    produto.setRestaurante(restaurante);

    ItemPedido item = new ItemPedido();
    item.setId(1L);
    item.setProduto(produto);
    item.setQuantidade(5); // More than available
    item.setPrecoUnitario(new BigDecimal("10.00"));
    item.setSubtotal(new BigDecimal("50.00"));

    Pedido pedido = new Pedido();
    pedido.setId(1L);
    pedido.setItens(java.util.Arrays.asList(item));

    // Configure mocks
    when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
    doThrow(new EstoqueInsuficienteException("Estoque insuficiente"))
        .when(produtoService)
        .validarEstoque(produto, 5);

    // Test the method
    assertThrows(
        EstoqueInsuficienteException.class, () -> pedidoService.validarEstoqueItens(pedido));
  }
}
