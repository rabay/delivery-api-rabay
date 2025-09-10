package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResultRelatorioClientesPaged")
class ApiResultRelatorioClientesPagedTest {

    // Classe mock para implementar RelatorioVendasClientes
    static class MockRelatorioVendasClientes implements com.deliverytech.delivery_api.projection.RelatorioVendasClientes {
        private final Long idCliente;
        private final String nomeCliente;
        private final BigDecimal totalCompras;
        private final Long quantidadePedidos;

        public MockRelatorioVendasClientes(Long idCliente, String nomeCliente, BigDecimal totalCompras, Long quantidadePedidos) {
            this.idCliente = idCliente;
            this.nomeCliente = nomeCliente;
            this.totalCompras = totalCompras;
            this.quantidadePedidos = quantidadePedidos;
        }

        @Override
        public Long getIdCliente() {
            return idCliente;
        }

        @Override
        public String getNomeCliente() {
            return nomeCliente;
        }

        @Override
        public BigDecimal getTotalCompras() {
            return totalCompras;
        }

        @Override
        public Long getQuantidadePedidos() {
            return quantidadePedidos;
        }
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com construtor padrão")
    void deveCriarApiResultRelatorioClientesPagedComConstrutorPadrao() {
        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();

        // Then
        assertNotNull(result);
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados de relatório de clientes paginado")
    void deveDefinirEObterDadosRelatorioClientesPaginado() {
        // Given
        MockRelatorioVendasClientes cliente1 = new MockRelatorioVendasClientes(
            1L, "João Silva", new BigDecimal("450.75"), 5L);
        MockRelatorioVendasClientes cliente2 = new MockRelatorioVendasClientes(
            2L, "Maria Santos", new BigDecimal("320.50"), 3L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> items = List.of(cliente1, cliente2);
        Map<String, String> links = Map.of(
            "first", "/api/relatorios/clientes?page=0",
            "last", "/api/relatorios/clientes?page=3"
        );

        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> pagedData = new PagedResponse<>(
            items, 67, 0, 10, 7, links, "Relatório de clientes", true);

        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = pagedData;
        result.message = "Relatório de clientes gerado com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(2, result.data.items().size());
        assertEquals(67, result.data.totalItems());
        assertEquals(0, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(7, result.data.totalPages());
        assertEquals("Relatório de clientes gerado com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados dos clientes
        assertEquals(1L, result.data.items().get(0).getIdCliente());
        assertEquals("João Silva", result.data.items().get(0).getNomeCliente());
        assertEquals(new BigDecimal("450.75"), result.data.items().get(0).getTotalCompras());
        assertEquals(5L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals(2L, result.data.items().get(1).getIdCliente());
        assertEquals("Maria Santos", result.data.items().get(1).getNomeCliente());
        assertEquals(new BigDecimal("320.50"), result.data.items().get(1).getTotalCompras());
        assertEquals(3L, result.data.items().get(1).getQuantidadePedidos());
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com dados vazios")
    void deveCriarApiResultRelatorioClientesPagedComDadosVazios() {
        // Given
        List<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> items = List.of();
        Map<String, String> links = Map.of("first", "/api/relatorios/clientes?page=0");
        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> pagedData = new PagedResponse<>(
            items, 0, 0, 10, 0, links, "Nenhum cliente encontrado", true);

        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
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
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com status de erro")
    void deveCriarApiResultRelatorioClientesPagedComStatusErro() {
        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = null;
        result.message = "Erro ao gerar relatório de clientes";
        result.success = false;

        // Then
        assertNull(result.data);
        assertEquals("Erro ao gerar relatório de clientes", result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados nulos")
    void deveDefinirEObterDadosNulos() {
        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = null;
        result.message = null;
        result.success = false;

        // Then
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com cliente de alto valor")
    void deveCriarApiResultRelatorioClientesPagedComClienteAltoValor() {
        // Given
        MockRelatorioVendasClientes cliente = new MockRelatorioVendasClientes(
            100L, "Cliente VIP", new BigDecimal("9999.99"), 50L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> items = List.of(cliente);
        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Cliente VIP encontrado", true);

        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = pagedData;
        result.message = "Cliente VIP encontrado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(100L, result.data.items().get(0).getIdCliente());
        assertEquals("Cliente VIP", result.data.items().get(0).getNomeCliente());
        assertEquals(new BigDecimal("9999.99"), result.data.items().get(0).getTotalCompras());
        assertEquals(50L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals("Cliente VIP encontrado", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com cliente sem compras")
    void deveCriarApiResultRelatorioClientesPagedComClienteSemCompras() {
        // Given
        MockRelatorioVendasClientes cliente = new MockRelatorioVendasClientes(
            200L, "Cliente Novo", BigDecimal.ZERO, 0L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> items = List.of(cliente);
        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Cliente sem compras", true);

        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = pagedData;
        result.message = "Cliente sem compras";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(BigDecimal.ZERO, result.data.items().get(0).getTotalCompras());
        assertEquals(0L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals("Cliente sem compras", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultRelatorioClientesPaged com cliente frequente")
    void deveCriarApiResultRelatorioClientesPagedComClienteFrequente() {
        // Given
        MockRelatorioVendasClientes cliente = new MockRelatorioVendasClientes(
            300L, "Cliente Frequente", new BigDecimal("1500.00"), 100L);

        List<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> items = List.of(cliente);
        Map<String, String> links = Map.of(
            "first", "/api/relatorios/clientes?page=0",
            "prev", "/api/relatorios/clientes?page=1",
            "self", "/api/relatorios/clientes?page=2",
            "next", "/api/relatorios/clientes?page=3",
            "last", "/api/relatorios/clientes?page=9"
        );

        PagedResponse<com.deliverytech.delivery_api.projection.RelatorioVendasClientes> pagedData = new PagedResponse<>(
            items, 500, 2, 10, 50, links, "Cliente frequente na página 3", true);

        // When
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();
        result.data = pagedData;
        result.message = "Cliente frequente na página 3";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(500, result.data.totalItems());
        assertEquals(2, result.data.page());
        assertEquals(50, result.data.totalPages());
        assertEquals(5, result.data.links().size());
        assertEquals(new BigDecimal("1500.00"), result.data.items().get(0).getTotalCompras());
        assertEquals(100L, result.data.items().get(0).getQuantidadePedidos());
        assertEquals("Cliente frequente na página 3", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        ApiResultRelatorioClientesPaged result = new ApiResultRelatorioClientesPaged();

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