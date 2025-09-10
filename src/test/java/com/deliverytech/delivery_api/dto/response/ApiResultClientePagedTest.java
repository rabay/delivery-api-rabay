package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para ApiResultClientePaged")
class ApiResultClientePagedTest {

    @Test
    @DisplayName("Deve criar ApiResultClientePaged com construtor padrão")
    void deveCriarApiResultClientePagedComConstrutorPadrao() {
        // When
        ApiResultClientePaged result = new ApiResultClientePaged();

        // Then
        assertNotNull(result);
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success); // valor padrão boolean é false
    }

    @Test
    @DisplayName("Deve definir e obter dados de paginação")
    void deveDefinirEObterDadosPaginacao() {
        // Given
        ClienteResponse cliente1 = new ClienteResponse();
        cliente1.setId(1L);
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@example.com");
        cliente1.setAtivo(true);

        ClienteResponse cliente2 = new ClienteResponse();
        cliente2.setId(2L);
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@example.com");
        cliente2.setAtivo(true);

        List<ClienteResponse> items = List.of(cliente1, cliente2);
        Map<String, String> links = Map.of(
            "first", "/api/clientes?page=0",
            "last", "/api/clientes?page=2"
        );

        PagedResponse<ClienteResponse> pagedData = new PagedResponse<>(
            items, 25, 0, 10, 3, links, "Página retornada", true);

        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = pagedData;
        result.message = "Consulta realizada com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(2, result.data.items().size());
        assertEquals(25, result.data.totalItems());
        assertEquals(0, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(3, result.data.totalPages());
        assertEquals("Consulta realizada com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados dos clientes
        assertEquals("João Silva", result.data.items().get(0).getNome());
        assertEquals("Maria Santos", result.data.items().get(1).getNome());
    }

    @Test
    @DisplayName("Deve criar ApiResultClientePaged com dados vazios")
    void deveCriarApiResultClientePagedComDadosVazios() {
        // Given
        List<ClienteResponse> items = List.of();
        Map<String, String> links = Map.of("first", "/api/clientes?page=0");
        PagedResponse<ClienteResponse> pagedData = new PagedResponse<>(
            items, 0, 0, 10, 0, links, "Nenhum cliente encontrado", true);

        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = pagedData;
        result.message = "Consulta sem resultados";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertTrue(result.data.items().isEmpty());
        assertEquals(0, result.data.totalItems());
        assertEquals(0, result.data.totalPages());
        assertEquals("Consulta sem resultados", result.message);
        assertTrue(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultClientePaged com status de erro")
    void deveCriarApiResultClientePagedComStatusErro() {
        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = null;
        result.message = "Erro interno do servidor";
        result.success = false;

        // Then
        assertNull(result.data);
        assertEquals("Erro interno do servidor", result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve definir e obter dados nulos")
    void deveDefinirEObterDadosNulos() {
        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = null;
        result.message = null;
        result.success = false;

        // Then
        assertNull(result.data);
        assertNull(result.message);
        assertFalse(result.success);
    }

    @Test
    @DisplayName("Deve criar ApiResultClientePaged com dados de uma página específica")
    void deveCriarApiResultClientePagedComDadosPaginaEspecifica() {
        // Given
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(15L);
        cliente.setNome("Pedro Oliveira");
        cliente.setEmail("pedro@example.com");
        cliente.setTelefone("11987654321");
        cliente.setEndereco("Rua Teste, 123");
        cliente.setAtivo(true);

        List<ClienteResponse> items = List.of(cliente);
        Map<String, String> links = Map.of(
            "first", "/api/clientes?page=0",
            "prev", "/api/clientes?page=1",
            "self", "/api/clientes?page=2",
            "next", "/api/clientes?page=3",
            "last", "/api/clientes?page=4"
        );

        PagedResponse<ClienteResponse> pagedData = new PagedResponse<>(
            items, 50, 2, 10, 5, links, "Página 3 de 5", true);

        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = pagedData;
        result.message = "Página 3 retornada com sucesso";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertEquals(50, result.data.totalItems());
        assertEquals(2, result.data.page());
        assertEquals(10, result.data.size());
        assertEquals(5, result.data.totalPages());
        assertEquals(5, result.data.links().size()); // first, prev, self, next, last
        assertEquals("Página 3 retornada com sucesso", result.message);
        assertTrue(result.success);

        // Verificar dados do cliente
        ClienteResponse clienteResult = result.data.items().get(0);
        assertEquals(15L, clienteResult.getId());
        assertEquals("Pedro Oliveira", clienteResult.getNome());
        assertEquals("pedro@example.com", clienteResult.getEmail());
        assertEquals("11987654321", clienteResult.getTelefone());
        assertEquals("Rua Teste, 123", clienteResult.getEndereco());
        assertTrue(clienteResult.isAtivo());
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        ApiResultClientePaged result = new ApiResultClientePaged();

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

    @Test
    @DisplayName("Deve criar ApiResultClientePaged com dados de cliente inativo")
    void deveCriarApiResultClientePagedComDadosClienteInativo() {
        // Given
        ClienteResponse cliente = new ClienteResponse();
        cliente.setId(99L);
        cliente.setNome("Cliente Inativo");
        cliente.setEmail("inativo@example.com");
        cliente.setAtivo(false);

        List<ClienteResponse> items = List.of(cliente);
        PagedResponse<ClienteResponse> pagedData = new PagedResponse<>(
            items, 1, 0, 10, 1, Map.of(), "Cliente encontrado", true);

        // When
        ApiResultClientePaged result = new ApiResultClientePaged();
        result.data = pagedData;
        result.message = "Cliente inativo encontrado";
        result.success = true;

        // Then
        assertNotNull(result.data);
        assertEquals(1, result.data.items().size());
        assertFalse(result.data.items().get(0).isAtivo());
        assertEquals("Cliente inativo encontrado", result.message);
        assertTrue(result.success);
    }
}