package com.deliverytech.delivery_api.service;

// import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private com.deliverytech.delivery_api.repository.ProdutoRepository produtoRepository;
    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve atualizar status do pedido para CONFIRMADO")
    void deveAtualizarStatusParaConfirmado() {
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setId(10L);
        pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var atualizado = pedidoService.atualizarStatus(10L, com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO);
        assertEquals(com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO, atualizado.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar status de pedido já cancelado")
    void deveLancarExcecaoAoAtualizarStatusPedidoCancelado() {
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setId(11L);
        pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CANCELADO);
        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        var ex = assertThrows(RuntimeException.class, () -> pedidoService.atualizarStatus(11L, com.deliverytech.delivery_api.model.StatusPedido.CONFIRMADO));
        assertTrue(ex.getMessage().toLowerCase().contains("cancelado"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pedido já entregue")
    void deveLancarExcecaoAoCancelarPedidoEntregue() {
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setId(12L);
        pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.ENTREGUE);
        when(pedidoRepository.findById(12L)).thenReturn(Optional.of(pedido));
        var ex = assertThrows(RuntimeException.class, () -> pedidoService.cancelar(12L));
        assertTrue(ex.getMessage().toLowerCase().contains("entregue"));
    }

    @Test
    @DisplayName("Deve buscar pedidos por cliente")
    void deveBuscarPedidosPorCliente() {
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setId(13L);
        pedido.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
        when(pedidoRepository.findByClienteId(100L)).thenReturn(java.util.List.of(pedido));
        var pedidos = pedidoService.buscarPorCliente(100L);
        assertEquals(1, pedidos.size());
        assertEquals(13L, pedidos.get(0).getId());
    }

    @Test
    @DisplayName("Deve calcular total do pedido corretamente")
    void deveCalcularTotalPedido() {
        var produto = new com.deliverytech.delivery_api.model.Produto();
        produto.setId(1L);
        produto.setPreco(java.math.BigDecimal.valueOf(15));
        produto.setAtivo(true);
        produto.setExcluido(false);
        var item = new com.deliverytech.delivery_api.model.ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(3);
        item.setPrecoUnitario(produto.getPreco());
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setItens(java.util.List.of(item));
        var total = pedidoService.calcularTotal(pedido);
        assertEquals(java.math.BigDecimal.valueOf(45), total);
    }

    @Test
    @DisplayName("Deve criar pedido com itens válidos")
    void deveCriarPedidoComItensValidos() {
        var produto = new com.deliverytech.delivery_api.model.Produto();
        produto.setId(1L);
        produto.setPreco(java.math.BigDecimal.TEN);
        produto.setAtivo(true);
        produto.setExcluido(false);

        var item = new com.deliverytech.delivery_api.model.ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(2);

        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setItens(java.util.List.of(item));

        when(produtoRepository.findById(1L)).thenReturn(java.util.Optional.of(produto));
        when(pedidoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = pedidoService.criar(pedido);
        assertNotNull(result);
        assertEquals(java.math.BigDecimal.valueOf(20), result.getValorTotal());
        assertEquals(com.deliverytech.delivery_api.model.StatusPedido.CRIADO, result.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido sem itens")
    void deveLancarExcecaoAoCriarPedidoSemItens() {
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setItens(java.util.Collections.emptyList());
        var ex = assertThrows(RuntimeException.class, () -> pedidoService.criar(pedido));
        assertTrue(ex.getMessage().contains("Pedido deve conter ao menos um item"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com produto inexistente")
    void deveLancarExcecaoProdutoInexistente() {
        var item = new com.deliverytech.delivery_api.model.ItemPedido();
        var produto = new com.deliverytech.delivery_api.model.Produto();
        produto.setId(99L);
        item.setProduto(produto);
        item.setQuantidade(1);
        var pedido = new com.deliverytech.delivery_api.model.Pedido();
        pedido.setItens(java.util.List.of(item));
        when(produtoRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        var ex = assertThrows(RuntimeException.class, () -> pedidoService.criar(pedido));
        assertTrue(ex.getMessage().contains("Produto não encontrado"));
    }
}
