
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.service.ClienteService;
import com.deliverytech.delivery_api.service.PedidoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operações de cadastro, consulta, atualização e inativação de clientes.")
public class ClienteController {
    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    public ClienteController(ClienteService clienteService, PedidoService pedidoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }


    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente ativo no sistema.")
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse novo = clienteService.cadastrar(clienteRequest);
        return ResponseEntity.status(201).body(novo);
    }


    @Operation(summary = "Listar clientes ativos", description = "Retorna todos os clientes ativos cadastrados.")
    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listarAtivos();
    }


    @Operation(summary = "Buscar cliente por e-mail", description = "Consulta um cliente pelo e-mail.")
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarPorEmail(@PathVariable String email) {
        return clienteService.buscarPorEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Buscar cliente por ID", description = "Consulta um cliente pelo seu identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            ClienteResponse cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse atualizado = clienteService.atualizar(id, clienteRequest);
        return ResponseEntity.ok(atualizado);
    }


    @Operation(summary = "Inativar cliente", description = "Inativa um cliente pelo seu ID, tornando-o indisponível para operações.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar/Desativar cliente", description = "Alterna o status ativo/inativo de um cliente.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClienteResponse> alterarStatus(@PathVariable Long id) {
        ClienteResponse atualizado = clienteService.ativarDesativarCliente(id);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Listar pedidos do cliente", description = "Retorna todos os pedidos realizados por um cliente específico.")
    @GetMapping("/{clienteId}/pedidos")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosDoCliente(@PathVariable Long clienteId) {
        try {
            // Carregar pedidos com itens e produtos para evitar problemas de lazy loading ao mapear para DTOs
            List<com.deliverytech.delivery_api.model.Pedido> pedidos = pedidoService.buscarPorClienteComItens(clienteId);
            List<PedidoResponse> responses = pedidos == null ? List.of() : pedidos.stream().map(this::mapToResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException ex) {
            // Logar a exceção para diagnóstico e manter contrato da API retornando lista vazia
            return ResponseEntity.ok(List.of());
        } catch (Exception ex) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private PedidoResponse mapToResponse(com.deliverytech.delivery_api.model.Pedido pedido) {
        return new PedidoResponse(
            pedido.getId(),
            (pedido.getCliente() != null)
                ? new com.deliverytech.delivery_api.dto.response.ClienteResumoResponse(
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome())
                : null,
            pedido.getRestaurante() != null ? pedido.getRestaurante().getId() : null,
            pedido.getEnderecoEntrega(),
            pedido.getValorTotal(),
            pedido.getStatus(),
            pedido.getDataPedido(),
            pedido.getItens() != null ? pedido.getItens().stream().map(item ->
                new com.deliverytech.delivery_api.dto.response.ItemPedidoResponse(
                    item.getProduto() != null ? item.getProduto().getId() : null,
                    item.getProduto() != null ? item.getProduto().getNome() : null,
                    item.getQuantidade(),
                    item.getPrecoUnitario()
                )
            ).toList() : List.of()
        );
    }
}
