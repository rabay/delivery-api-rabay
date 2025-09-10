package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PedidoServiceIT extends AbstractIntegrationTest {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private RestauranteService restauranteService;
    
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void criarPedidoComSucesso() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Pedido");
        req.setCategoria("Cat");
        req.setTaxaEntrega(BigDecimal.ONE);
        req.setEmail("mail@p.com");
        req.setTempoEntregaMinutos(10);
        req.setAvaliacao(BigDecimal.ONE);
        Restaurante restaurante = restauranteService.cadastrar(req);
        ProdutoRequest produtoReq = new ProdutoRequest();
        produtoReq.setNome("Produto Pedido");
        produtoReq.setCategoria("Cat");
        produtoReq.setPreco(BigDecimal.TEN);
        produtoReq.setDisponivel(true);
        Produto produto = produtoService.cadastrar(produtoReq, restaurante.getId());
        PedidoRequest pedidoRequest = new PedidoRequest();
        pedidoRequest.setRestauranteId(restaurante.getId());
        pedidoRequest.setClienteId(1L);
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(produto.getId());
        item.setQuantidade(1);
        pedidoRequest.setItens(List.of(item));
        Pedido pedido = pedidoService.criar(pedidoRequest);
        assertThat(pedido).isNotNull();
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedido.getItens()).hasSize(1);
    }
    
    @Test
    void criarPedidoComProdutoInexistenteDeveLancarExcecao() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Pedido Inexistente");
        req.setCategoria("Cat");
        req.setTaxaEntrega(BigDecimal.ONE);
        req.setEmail("mail2@p.com");
        req.setTempoEntregaMinutos(10);
        req.setAvaliacao(BigDecimal.ONE);
        Restaurante restaurante = restauranteService.cadastrar(req);
        PedidoRequest pedidoRequest = new PedidoRequest();
        pedidoRequest.setRestauranteId(restaurante.getId());
        pedidoRequest.setClienteId(1L);
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProdutoId(999L); // ID inexistente
        item.setQuantidade(1);
        pedidoRequest.setItens(List.of(item));
        assertThrows(EntityNotFoundException.class, () -> pedidoService.criar(pedidoRequest));
    }

    @Test
    void criarPedidoSemItensDeveLancarExcecao() {
        RestauranteRequest req = new RestauranteRequest();
        req.setNome("Restaurante Sem Itens");
        req.setCategoria("Cat");
        req.setTaxaEntrega(BigDecimal.ONE);
        req.setEmail("mail3@p.com");
        req.setTempoEntregaMinutos(10);
        req.setAvaliacao(BigDecimal.ONE);
        Restaurante restaurante = restauranteService.cadastrar(req);
        PedidoRequest pedidoRequest = new PedidoRequest();
        pedidoRequest.setRestauranteId(restaurante.getId());
        pedidoRequest.setClienteId(1L);
        pedidoRequest.setItens(Collections.emptyList());
        assertThrows(BusinessException.class, () -> pedidoService.criar(pedidoRequest));
    }
}
