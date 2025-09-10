package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResultPedidoResumoPaged")
class ApiResultPedidoResumoPagedTest {

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com construtor padrão")
    void deveCriarApiResultPedidoResumoPagedComConstrutorPadrao() {
        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();

        // Then
        assertNotNull(result);
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados de pedidos resumo paginados")
    void deveDefinirEObterDadosPedidosResumoPaginados() {
        // Given
        PedidoResumoResponse pedido1 = new PedidoResumoResponse();
        pedido1.setId(1L);
        pedido1.setClienteNome("João Silva");
        pedido1.setRestauranteNome("Pizzaria do João");
        pedido1.setValorTotal(new BigDecimal("45.90"));
        pedido1.setStatus("CONFIRMADO");
        pedido1.setDataPedido(LocalDateTime.now().minusHours(2));

        PedidoResumoResponse pedido2 = new PedidoResumoResponse();
        pedido2.setId(2L);
        pedido2.setClienteNome("Maria Santos");
        pedido2.setRestauranteNome("Hamburgueria Express");
        pedido2.setValorTotal(new BigDecimal("32.50"));
        pedido2.setStatus("EM_PREPARACAO");
        pedido2.setDataPedido(LocalDateTime.now().minusHours(1));

        List<PedidoResumoResponse> items = List.of(pedido1, pedido2);
        Map<String, String> links = Map.of(
            "first", "/api/pedidos/resumo?page=0",
            "last", "/api/pedidos/resumo?page=5"
        );

        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 67, 0, 10, 7, links, "Resumo de pedidos", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Resumo de pedidos gerado com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(2, result.data.items().size());
        assertEquals(67, result.data.totalItems());
        assertEquals(0, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(7, result.data.totalPages());
        assertEquals("Resumo de pedidos gerado com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados dos pedidos resumo
        assertEquals(1L, result.data.items().get(0).getId());
        assertEquals("João Silva", result.data.items().get(0).getClienteNome());
        assertEquals("Pizzaria do João", result.data.items().get(0).getRestauranteNome());
        assertEquals(new BigDecimal("45.90"), result.data.items().get(0).getValorTotal());
        assertEquals("CONFIRMADO", result.data.items().get(0).getStatus());
        assertEquals(2L, result.data.items().get(1).getId());
        assertEquals("Maria Santos", result.data.items().get(1).getClienteNome());
        assertEquals("Hamburgueria Express", result.data.items().get(1).getRestauranteNome());
        assertEquals(new BigDecimal("32.50"), result.data.items().get(1).getValorTotal());
        assertEquals("EM_PREPARACAO", result.data.items().get(1).getStatus());
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com dados vazios")
    void deveCriarApiResultPedidoResumoPagedComDadosVazios() {
        // Given
        List<PedidoResumoResponse> items = List.of();
        Map<String, String> links = Map.of("first", "/api/pedidos/resumo?page=0");
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 0, 0, 10, 0, links, "Nenhum pedido encontrado", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Resumo vazio";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertTrue(result.data.items().isEmpty());
        assertEquals(0, result.data.totalItems());
        assertEquals(0, result.data.totalPages());
        assertEquals("Resumo vazio", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com status de erro")
    void deveCriarApiResultPedidoResumoPagedComStatusErro() {
        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = null;
        result.message = "Erro ao gerar resumo de pedidos";
        result.success = false;

        // Then
        assertNull(result.data);
        assertEquals("Erro ao gerar resumo de pedidos", result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados nulos")
    void deveDefinirEObterDadosNulos() {
        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = null;
        result.message = null;
        result.success = false;

        // Then
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com pedido cancelado")
    void deveCriarApiResultPedidoResumoPagedComPedidoCancelado() {
        // Given
        PedidoResumoResponse pedido = new PedidoResumoResponse();
        pedido.setId(100L);
        pedido.setClienteNome("Cliente Cancelou");
        pedido.setRestauranteNome("Restaurante X");
        pedido.setValorTotal(new BigDecimal("25.00"));
        pedido.setStatus("CANCELADO");
        pedido.setDataPedido(LocalDateTime.now().minusDays(1));

        List<PedidoResumoResponse> items = List.of(pedido);
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Pedido cancelado", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Pedido cancelado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals("CANCELADO", result.data.items().get(0).getStatus());
        assertEquals(new BigDecimal("25.00"), result.data.items().get(0).getValorTotal());
        assertEquals("Pedido cancelado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com pedido finalizado")
    void deveCriarApiResultPedidoResumoPagedComPedidoFinalizado() {
        // Given
        PedidoResumoResponse pedido = new PedidoResumoResponse();
        pedido.setId(200L);
        pedido.setClienteNome("Cliente Satisfeito");
        pedido.setRestauranteNome("Restaurante Premium");
        pedido.setValorTotal(new BigDecimal("78.90"));
        pedido.setStatus("ENTREGUE");
        pedido.setDataPedido(LocalDateTime.now().minusDays(2));

        List<PedidoResumoResponse> items = List.of(pedido);
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Pedido finalizado", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Pedido finalizado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals("ENTREGUE", result.data.items().get(0).getStatus());
        assertEquals(new BigDecimal("78.90"), result.data.items().get(0).getValorTotal());
        assertEquals("Pedido finalizado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com pedido de alto valor")
    void deveCriarApiResultPedidoResumoPagedComPedidoAltoValor() {
        // Given
        PedidoResumoResponse pedido = new PedidoResumoResponse();
        pedido.setId(300L);
        pedido.setClienteNome("Cliente VIP");
        pedido.setRestauranteNome("Restaurante Luxo");
        pedido.setValorTotal(new BigDecimal("999.99"));
        pedido.setStatus("CONFIRMADO");
        pedido.setDataPedido(LocalDateTime.now().minusMinutes(30));

        List<PedidoResumoResponse> items = List.of(pedido);
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Pedido de alto valor", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Pedido de alto valor";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals("CONFIRMADO", result.data.items().get(0).getStatus());
        assertEquals(new BigDecimal("999.99"), result.data.items().get(0).getValorTotal());
        assertEquals("Pedido de alto valor", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com pedido recente")
    void deveCriarApiResultPedidoResumoPagedComPedidoRecente() {
        // Given
        LocalDateTime agora = LocalDateTime.now();
        PedidoResumoResponse pedido = new PedidoResumoResponse();
        pedido.setId(400L);
        pedido.setClienteNome("Cliente Novo");
        pedido.setRestauranteNome("Restaurante Fast");
        pedido.setValorTotal(new BigDecimal("15.50"));
        pedido.setStatus("CRIADO");
        pedido.setDataPedido(agora.minusMinutes(5));

        List<PedidoResumoResponse> items = List.of(pedido);
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Pedido recente", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Pedido recente";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals("CRIADO", result.data.items().get(0).getStatus());
        assertEquals(new BigDecimal("15.50"), result.data.items().get(0).getValorTotal());
        assertTrue(result.data.items().get(0).getDataPedido().isAfter(agora.minusMinutes(10)));
        assertEquals("Pedido recente", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultPedidoResumoPaged com desconto aplicado")
    void deveCriarApiResultPedidoResumoPagedComDescontoAplicado() {
        // Given
        PedidoResumoResponse pedido = new PedidoResumoResponse();
        pedido.setId(500L);
        pedido.setClienteNome("Cliente com Desconto");
        pedido.setRestauranteNome("Restaurante Promo");
        pedido.setValorTotal(new BigDecimal("50.00"));
        pedido.setDesconto(new BigDecimal("10.00"));
        pedido.setStatus("CONFIRMADO");
        pedido.setDataPedido(LocalDateTime.now().minusHours(1));

        List<PedidoResumoResponse> items = List.of(pedido);
        PagedResponse<PedidoResumoResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Pedido com desconto", true);

        // When
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();
        result.data = pagedData;
        result.message = "Pedido com desconto";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(new BigDecimal("50.00"), result.data.items().get(0).getValorTotal());
        assertEquals(new BigDecimal("10.00"), result.data.items().get(0).getDesconto());
        assertEquals("CONFIRMADO", result.data.items().get(0).getStatus());
        assertEquals("Pedido com desconto", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        ApiResultPedidoResumoPaged result = new ApiResultPedidoResumoPaged();

        // When
        result.message = "Primeira mensagem";
        result.message = "Segunda mensagem";
        result.success = true;
        result.success = false;
        result.success = true;

        // Then
        assertEquals("Segunda mensagem", result.message);
        assertTrue(result.success);
    }
}