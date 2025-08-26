
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.StatusUpdateRequest;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.deliverytech.delivery_api.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Criação, consulta e atualização de pedidos realizados pelos clientes.")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido pelo seu identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(mapToResponse(pedido));
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido para um cliente em um restaurante.")
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest pedidoRequest) {
        try {
            logger.debug("Recebido PedidoRequest: {}", pedidoRequest);
            Pedido pedido = mapToEntity(pedidoRequest);
            logger.debug("Pedido mapeado: {}", pedido);
            Pedido novo = pedidoService.criar(pedido);
            PedidoResponse response = mapToResponse(novo);
            logger.info("Pedido criado com sucesso: id={} status={}", response.getId(), response.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            logger.error("Erro de negócio ao criar pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao criar pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Listar pedidos de um cliente", description = "Retorna todos os pedidos realizados por um cliente específico.")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> buscarPorCliente(@PathVariable Long clienteId) {
        try {
            // Carregar pedidos com itens e produtos para evitar problemas de lazy loading ao mapear para DTOs
            List<Pedido> pedidos = pedidoService.buscarPorClienteComItens(clienteId);
            List<PedidoResponse> responses = pedidos == null ? List.of() : pedidos.stream().map(this::mapToResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (RuntimeException ex) {
            // Logar a exceção para diagnóstico e manter contrato da API retornando lista vazia
            logger.error("Erro ao buscar pedidos para cliente {}: {}", clienteId, ex.getMessage(), ex);
            return ResponseEntity.ok(List.of());
        } catch (Exception ex) {
            logger.error("Erro inesperado ao buscar pedidos para cliente {}: {}", clienteId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido existente (ex: CRIADO, ENTREGUE, CANCELADO).")
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest statusUpdateRequest) {
    StatusPedido status = StatusPedido.valueOf(statusUpdateRequest.getStatus());
    Pedido atualizado = pedidoService.atualizarStatus(id, status);
    // Buscar novamente o pedido com itens para evitar LazyInitializationException
    return pedidoService.buscarPorIdComItens(atualizado.getId())
        .map(pedidoComItens -> ResponseEntity.ok(mapToResponse(pedidoComItens)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Métodos utilitários de mapeamento
    private Pedido mapToEntity(PedidoRequest dto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(new com.deliverytech.delivery_api.model.Cliente());
        pedido.getCliente().setId(dto.getClienteId());
        pedido.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        pedido.getRestaurante().setId(dto.getRestauranteId());
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setItens(dto.getItens().stream().map(itemDto -> {
            com.deliverytech.delivery_api.model.ItemPedido item = new com.deliverytech.delivery_api.model.ItemPedido();
            item.setProduto(new com.deliverytech.delivery_api.model.Produto());
            item.getProduto().setId(itemDto.getProdutoId());
            item.setQuantidade(itemDto.getQuantidade());
            return item;
        }).toList());
        return pedido;
    }

    private PedidoResponse mapToResponse(Pedido pedido) {
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
                new ItemPedidoResponse(
                    item.getProduto() != null ? item.getProduto().getId() : null,
                    item.getProduto() != null ? item.getProduto().getNome() : null,
                    item.getQuantidade(),
                    item.getPrecoUnitario()
                )
            ).toList() : List.of()
        );
    }
}
