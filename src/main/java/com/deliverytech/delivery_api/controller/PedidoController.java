package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.StatusUpdateRequest;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@Tag(
        name = "Pedidos",
        description = "Criação, consulta e atualização de pedidos realizados pelos clientes.")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Listar todos os pedidos",
            description = "Retorna todos os pedidos cadastrados no sistema.")
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        try {
            List<Pedido> pedidos = pedidoService.listarTodos();
            List<PedidoResponse> responses = pedidos.stream().map(this::mapToResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            logger.error("Erro ao listar todos os pedidos: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna um pedido pelo seu identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(mapToResponse(pedido));
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null
                    && ex.getMessage().toLowerCase().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Criar novo pedido",
            description = "Cria um novo pedido para um cliente em um restaurante.")
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest pedidoRequest) {
        try {
            logger.debug("Recebido PedidoRequest: {}", pedidoRequest);
            Pedido pedido = mapToEntity(pedidoRequest);
            logger.debug("Pedido mapeado: {}", pedido);
            Pedido novo = pedidoService.criar(pedido);
            PedidoResponse response = mapToResponse(novo);
            logger.info(
                    "Pedido criado com sucesso: id={} status={}",
                    response.getId(),
                    response.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            logger.error("Erro de negócio ao criar pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao criar pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Atualizar status do pedido",
            description =
                    "Atualiza o status de um pedido existente (ex: CRIADO, ENTREGUE, CANCELADO).")
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id, @Valid @RequestBody StatusUpdateRequest statusUpdateRequest) {
        StatusPedido status = StatusPedido.valueOf(statusUpdateRequest.getStatus());
        Pedido atualizado = pedidoService.atualizarStatus(id, status);
        // Buscar novamente o pedido com itens para evitar LazyInitializationException
        return pedidoService
                .buscarPorIdComItens(atualizado.getId())
                .map(pedidoComItens -> ResponseEntity.ok(mapToResponse(pedidoComItens)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id) {
        try {
            Pedido cancelado = pedidoService.cancelar(id);
            PedidoResponse response = mapToResponse(cancelado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            logger.error("Erro ao cancelar pedido {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao cancelar pedido {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(
            summary = "Calcular total do pedido",
            description = "Calcula o total de um pedido sem persisti-lo no banco de dados.")
    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcularTotal(
            @Valid @RequestBody PedidoRequest pedidoRequest) {
        try {
            java.math.BigDecimal total =
                    pedidoService.calcularTotalPedido(pedidoRequest.getItens());
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("total", total);
            response.put("quantidade_itens", pedidoRequest.getItens().size());
            response.put("clienteId", pedidoRequest.getClienteId());
            response.put("restauranteId", pedidoRequest.getRestauranteId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            logger.error("Erro ao calcular total do pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao calcular total do pedido: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Métodos utilitários de mapeamento
    private Pedido mapToEntity(PedidoRequest dto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(new com.deliverytech.delivery_api.model.Cliente());
        pedido.getCliente().setId(dto.getClienteId());
        pedido.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        pedido.getRestaurante().setId(dto.getRestauranteId());
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setItens(
                dto.getItens().stream()
                        .map(
                                itemDto -> {
                                    com.deliverytech.delivery_api.model.ItemPedido item =
                                            new com.deliverytech.delivery_api.model.ItemPedido();
                                    item.setProduto(
                                            new com.deliverytech.delivery_api.model.Produto());
                                    item.getProduto().setId(itemDto.getProdutoId());
                                    item.setQuantidade(itemDto.getQuantidade());
                                    return item;
                                })
                        .toList());
        return pedido;
    }

    private PedidoResponse mapToResponse(Pedido pedido) {
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
                                                new ItemPedidoResponse(
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