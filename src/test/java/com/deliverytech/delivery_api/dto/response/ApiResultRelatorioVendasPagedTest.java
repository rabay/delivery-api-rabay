package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResultRelatorioVendasPaged")
class ApiResultRelatorioVendasPagedTest {

    // Classe mock para implementar RelatorioVendas
    static class MockRelatorioVendas implements com.deliverytech.delivery_api.projection.RelatorioVendas {
        private final String nomeRestaurante;
        private final BigDecimal totalVendas;
        private final Long quantidadePedidos;

        public MockRelatorioVendas(String nomeRestaurante, BigDecimal totalVendas, Long quantidadePedidos) {
            this.nomeRestaurante = nomeRestaurante;
            this.totalVendas = totalVendas;
            this.quantidadePedidos = quantidadePedidos;
        }

        @Override
        public String getNomeRestaurante() {
            return nomeRestaurante;
        }

        @Override
        public BigDecimal getTotalVendas() {
            return totalVendas;
        }

        @Override
        public Long getQuantidadePedidos() {
            return quantidadePedidos;
        }
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioVendasPaged com construtor padrão")
    void deveCriarApiResultRelatorioVendasPagedComConstrutorPadrao() {
        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();

        // Then
        assertNotNull(result);
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados de relatório de vendas paginado")
    void deveDefinirEObterDadosRelatorioVendasPaginado() {
        // Given
        MockRelatorioVendas relatorio1 = new MockRelatorioVendas(
            "Pizzaria do João", new BigDecimal("1250.50"), 25L);
        MockRelatorioVendas relatorio2 = new MockRelatorioVendas(
            "Hamburgueria Express", new BigDecimal("890.75"), 18L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendas> items = List.of(relatorio1, relatorio2);
        Map<String, String> links = Map.of(
            "first", "/api/relatorios/vendas?page=0",
            "last", "/api/relatorios/vendas?page=2"
        );

        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendas> pagedData = new PagedResponse<>(
            items, 43, 0, 10, 5, links, "Relatório de vendas", true);

        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = pagedData;
        result.message = "Relatório de vendas gerado com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(2, result.data.items().size());
        assertEquals(43, result.data.totalItems());
        assertEquals(0, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(5, result.data.totalPages());
        assertEquals("Relatório de vendas gerado com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados dos relatórios
        assertEquals("Pizzaria do João", result.data.items().get(0).getNomeRestaurante());
        assertEquals(new BigDecimal("1250.50"), result.data.items().get(0).getTotalVendas());
        assertEquals(25L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals("Hamburgueria Express", result.data.items().get(1).getNomeRestaurante());
        assertEquals(new BigDecimal("890.75"), result.data.items().get(1).getTotalVendas());
        assertEquals(18L, result.data.items().get(1).getQuantidadePedidos());
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioVendasPaged com dados vazios")
    void deveCriarApiResultRelatorioVendasPagedComDadosVazios() {
        // Given
        List<com.deliverytech.delivery_api.projection.RelatorioVendas> items = List.of();
        Map<String, String> links = Map.of("first", "/api/relatorios/vendas?page=0");
        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendas> pagedData = new PagedResponse<>(
            items, 0, 0, 10, 0, links, "Nenhum dado encontrado", true);

        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = pagedData;
        result.message = "Relatório vazio";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertTrue(result.data.items().isEmpty());
        assertEquals(0, result.data.totalItems());
        assertEquals(0, result.data.totalPages());
        assertEquals("Relatório vazio", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioVendasPaged com status de erro")
    void deveCriarApiResultRelatorioVendasPagedComStatusErro() {
        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = null;
        result.message = "Erro ao gerar relatório de vendas";
        result.success = false;

        // Then
        assertNull(result.data);
        assertEquals("Erro ao gerar relatório de vendas", result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados nulos")
    void deveDefinirEObterDadosNulos() {
        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = null;
        result.message = null;
        result.success = false;

        // Then
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioVendasPaged com relatório de uma página específica")
    void deveCriarApiResultRelatorioVendasPagedComRelatorioPaginaEspecifica() {
        // Given
        MockRelatorioVendas relatorio = new MockRelatorioVendas(
            "Restaurante Premium", new BigDecimal("5000.00"), 100L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendas> items = List.of(relatorio);
        Map<String, String> links = Map.of(
            "first", "/api/relatorios/vendas?page=0",
            "prev", "/api/relatorios/vendas?page=1",
            "self", "/api/relatorios/vendas?page=2",
            "next", "/api/relatorios/vendas?page=3",
            "last", "/api/relatorios/vendas?page=4"
        );

        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendas> pagedData = new PagedResponse<>(
            items, 500, 2, 10, 50, links, "Página 3 de 50", true);

        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = pagedData;
        result.message = "Página específica do relatório";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(500, result.data.totalItems());
        assertEquals(2, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(50, result.data.totalPages());
        assertEquals(5, result.data.links().size());
        assertEquals("Página específica do relatório", result.message);
        assertTrue(result.success);

        // Verificar dados do relatório
        assertEquals("Restaurante Premium", result.data.items().get(0).getNomeRestaurante());
        assertEquals(new BigDecimal("5000.00"), result.data.items().get(0).getTotalVendas());
        assertEquals(100L, result.data.items().get(0).getQuantidadePedidos());
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioVendasPaged com valores zero")
    void deveCriarApiResultRelatorioVendasPagedComValoresZero() {
        // Given
        MockRelatorioVendas relatorio = new MockRelatorioVendas(
            "Novo Restaurante", BigDecimal.ZERO, 0L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendas> items = List.of(relatorio);
        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendas> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Restaurante sem vendas", true);

        // When
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();
        result.data = pagedData;
        result.message = "Restaurante sem vendas";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(BigDecimal.ZERO, result.data.items().get(0).getTotalVendas());
        assertEquals(0L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals("Restaurante sem vendas", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        ApiResultRelatorioVendasPaged result = new ApiResultRelatorioVendasPaged();

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