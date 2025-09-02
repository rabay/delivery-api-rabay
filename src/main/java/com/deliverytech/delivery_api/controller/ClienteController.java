package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.service.ClienteService;
import com.deliverytech.delivery_api.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(
        name = "Clientes",
        description = "Operações de cadastro, consulta, atualização e inativação de clientes.")
public class ClienteController {
    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    public ClienteController(ClienteService clienteService, PedidoService pedidoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Cadastrar novo cliente",
            description = "Cria um novo cliente ativo no sistema.")
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(
            @Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse novo = clienteService.cadastrar(clienteRequest);
        return ResponseEntity.status(201).body(novo);
    }

    @Operation(
            summary = "Listar clientes ativos",
            description = "Retorna todos os clientes ativos cadastrados.")
    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listarAtivos();
    }

    @Operation(
            summary = "Buscar cliente por e-mail",
            description = "Consulta um cliente pelo e-mail.")
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarPorEmail(@PathVariable String email) {
        return clienteService
                .buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Consulta um cliente pelo seu identificador único.")
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            System.out.println("ClienteController.buscarPorId called with id: " + id);
            ClienteResponse cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            System.err.println("RuntimeException in buscarPorId: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza os dados de um cliente existente.")
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse atualizado = clienteService.atualizar(id, clienteRequest);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(
            summary = "Inativar cliente",
            description = "Inativa um cliente pelo seu ID, tornando-o indisponível para operações.")
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar/Desativar cliente",
            description = "Alterna o status ativo/inativo de um cliente.")
    @PatchMapping("/{id:[0-9]+}/status")
    public ResponseEntity<ClienteResponse> alterarStatus(@PathVariable Long id) {
        ClienteResponse atualizado = clienteService.ativarDesativarCliente(id);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(
            summary = "Listar pedidos do cliente",
            description = "Retorna todos os pedidos realizados por um cliente específico.")
    @GetMapping("/{clienteId:[0-9]+}/pedidos")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosDoCliente(
            @PathVariable Long clienteId) {
        try {
            // Log the clienteId to see what's being passed
            System.out.println("Received request for pedidos of clienteId: " + clienteId);
            System.out.println("clienteId class: " + clienteId.getClass().getName());
            System.out.println("clienteId value: " + clienteId);
            
            // Validate that clienteId is not null or negative
            if (clienteId == null || clienteId <= 0) {
                System.out.println("Invalid clienteId: " + clienteId);
                return ResponseEntity.badRequest().build();
            }
            
            // Carregar pedidos com itens e produtos para evitar problemas de lazy loading ao mapear
            // para DTOs
            System.out.println("Calling pedidoService.buscarPorClienteComItens with clienteId: " + clienteId);
            List<com.deliverytech.delivery_api.model.Pedido> pedidos =
                    pedidoService.buscarPorClienteComItens(clienteId);
            System.out.println("pedidoService returned " + (pedidos != null ? pedidos.size() : "null") + " pedidos");
            List<PedidoResponse> responses =
                    pedidos == null
                            ? List.of()
                            : pedidos.stream().map(this::mapToResponse).toList();
            System.out.println("Mapped to " + responses.size() + " PedidoResponse objects");
            return ResponseEntity.ok(responses);
        } catch (RuntimeException ex) {
            // Logar a exceção para diagnóstico e manter contrato da API retornando lista vazia
            System.err.println("RuntimeException in buscarPedidosDoCliente: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.ok(List.of());
        } catch (Exception ex) {
            // Log the exception for debugging
            System.err.println("Exception in buscarPedidosDoCliente: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private PedidoResponse mapToResponse(com.deliverytech.delivery_api.model.Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                (pedido.getCliente() != null)
                        ? new com.deliverytech.delivery_api.dto.response.ClienteResumoResponse(
                                pedido.getCliente().getId(), pedido.getCliente().getNome())
                        : null,
                pedido.getRestaurante() != null ? pedido.getRestaurante().getId() : null,
                pedido.getEnderecoEntrega(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getItens() != null
                        ? pedido.getItens().stream()
                                .map(
                                        item ->
                                                new com.deliverytech.delivery_api.dto.response
                                                        .ItemPedidoResponse(
                                                        item.getProduto() != null
                                                                ? item.getProduto().getId()
                                                                : null,
                                                        item.getProduto() != null
                                                                ? item.getProduto().getNome()
                                                                : null,
                                                        item.getQuantidade(),
                                                        item.getPrecoUnitario()))
                                .toList()
                        : List.of());
    }
}