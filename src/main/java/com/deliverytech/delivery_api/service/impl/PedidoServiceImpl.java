package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.exception.ProdutoIndisponivelException;
import com.deliverytech.delivery_api.mapper.PedidoMapper;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.ProdutoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final PedidoMapper pedidoMapper;
    private final ProdutoService produtoService;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            PedidoMapper pedidoMapper,
            ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.pedidoMapper = pedidoMapper;
        this.produtoService = produtoService;
    }

    @Override
    public Pedido criar(Pedido pedido) {
        try {
            pedido.setStatus(StatusPedido.CRIADO);
            pedido.setDataPedido(LocalDateTime.now());
            if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
                log.warn(
                        "Tentativa de criar pedido sem itens. ClienteId={}, RestauranteId= {}",
                        pedido.getCliente() != null ? pedido.getCliente().getId() : null,
                        pedido.getRestaurante() != null ? pedido.getRestaurante().getId() : null);
                throw new BusinessException("Pedido deve conter ao menos um item.");
            }
            
            // Validate stock for all items before processing
            validarEstoqueItens(pedido);
            
            BigDecimal total = BigDecimal.ZERO;
            for (ItemPedido item : pedido.getItens()) {
                Long produtoId = item.getProduto() != null ? item.getProduto().getId() : null;
                if (produtoId == null) {
                    log.warn("ID do produto é nulo ao criar pedido.");
                    throw new BusinessException("ID do produto não pode ser nulo.");
                }
                Produto produto =
                        produtoRepository
                                .findById(produtoId)
                                .orElseThrow(
                                        () -> {
                                            log.warn(
                                                    "Produto não encontrado ao criar pedido:"
                                                        + " produtoId={}",
                                                    produtoId);
                                            return new RuntimeException(
                                                    "Produto não encontrado: ID " + produtoId);
                                        });
                if (Boolean.TRUE.equals(produto.getExcluido())
                        || !Boolean.TRUE.equals(produto.getDisponivel())) {
                    log.warn(
                            "Produto indisponível ou excluído ao criar pedido: produtoId={}",
                            produto.getId());
                    throw new RuntimeException(
                            "Produto indisponível ou excluído: ID " + produto.getId());
                }
                item.setProduto(produto);
                item.setPrecoUnitario(produto.getPreco());
                // assume ItemPedido has método setSubtotal() or compute internally
                try {
                    item.setSubtotal();
                } catch (Exception ignore) {
                    // se não existir, calcular via campos
                }
                BigDecimal itemSubtotal =
                        item.getSubtotal() != null
                                ? item.getSubtotal()
                                : item.getPrecoUnitario()
                                        .multiply(BigDecimal.valueOf(item.getQuantidade()));
                total = total.add(itemSubtotal);
                item.setPedido(pedido);
            }
            
            // Add delivery fee to total
            if (pedido.getRestaurante() != null && pedido.getRestaurante().getTaxaEntrega() != null) {
                total = total.add(pedido.getRestaurante().getTaxaEntrega());
            }
            
            // Apply discount if available
            if (pedido.getDesconto() != null) {
                total = total.subtract(pedido.getDesconto());
                // Ensure total doesn't go negative
                if (total.compareTo(BigDecimal.ZERO) < 0) {
                    total = BigDecimal.ZERO;
                }
            }
            
            pedido.setValorTotal(total);
            
            // Reserve stock for all items
            produtoService.reservarEstoque(pedido);
            
            return pedidoRepository.save(pedido);
        } catch (Exception e) {
            log.error("Erro ao criar pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        if (pedidos == null) {
            log.debug("findByClienteId retornou null para clienteId={}", clienteId);
            return List.of();
        }
        return pedidos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido =
                pedidoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> {
                                    log.warn(
                                            "Tentativa de atualizar status de pedido inexistente:"
                                                + " pedidoId={}",
                                            id);
                                    return new EntityNotFoundException("Pedido", id);
                                });
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            log.warn("Tentativa de atualizar status de pedido já cancelado: pedidoId={}", id);
            throw new BusinessException("Pedido já está cancelado");
        }
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            log.warn("Tentativa de atualizar status de pedido já entregue: pedidoId={}", id);
            throw new BusinessException("Não é possível atualizar um pedido já entregue");
        }
        
        // If canceling a confirmed order, restore stock
        if (status == StatusPedido.CANCELADO && pedido.getStatus() == StatusPedido.CONFIRMADO) {
            produtoService.cancelarReservaEstoque(pedido);
        }
        
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido confirmar(Long id) {
        Pedido pedido =
                pedidoRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(StatusPedido.CONFIRMADO);
        
        // Confirm stock reduction (in this implementation, stock is already reduced during reservation)
        produtoService.confirmarEstoque(pedido);
        
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cancelar(Long pedidoId) {
        Pedido pedido =
                pedidoRepository
                        .findById(pedidoId)
                        .orElseThrow(() -> new EntityNotFoundException("Pedido", pedidoId));
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido já está cancelado");
        }
        
        // Restore stock if order was confirmed
        if (pedido.getStatus() == StatusPedido.CONFIRMADO) {
            produtoService.cancelarReservaEstoque(pedido);
        }
        
        pedido.setStatus(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        Pedido pedido =
                pedidoRepository
                        .findById(pedidoId)
                        .orElseThrow(() -> new EntityNotFoundException("Pedido", pedidoId));
        Produto produto =
                produtoRepository
                        .findById(produtoId)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        // Validate stock for the new item
        produtoService.validarEstoque(produto, quantidade);
        
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        if (pedido.getItens() == null) {
            pedido.setItens(new ArrayList<>());
        }
        pedido.getItens().add(item);
        BigDecimal novoTotal = calcularTotal(pedido);
        pedido.setValorTotal(novoTotal);
        
        // Reserve stock for the new item
        produtoService.reservarEstoque(pedido);
        
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pedido.getItens().stream()
                .map(
                        item ->
                                item.getPrecoUnitario()
                                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(List<ItemPedidoRequest> itens) {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoRequest itemRequest : itens) {
            Produto produto =
                    produtoRepository
                            .findById(itemRequest.getProdutoId())
                            .orElseThrow(
                                    () ->
                                            new EntityNotFoundException("Produto", itemRequest.getProdutoId()));
            if (!Boolean.TRUE.equals(produto.getDisponivel())) {
                throw new RuntimeException(
                        "Produto não está disponível - ID: " + itemRequest.getProdutoId());
            }
            
            // Validate stock for the item
            produtoService.validarEstoque(produto, itemRequest.getQuantidade());
            
            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal quantidade = BigDecimal.valueOf(itemRequest.getQuantidade());
            BigDecimal subtotal = precoUnitario.multiply(quantidade);
            total = total.add(subtotal);
        }
        return total;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarComFiltros(
            StatusPedido status, LocalDate dataInicio, LocalDate dataFim) {
        if (status == null && dataInicio == null && dataFim == null) {
            return pedidoRepository.findAll();
        }
        LocalDateTime inicioDateTime = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime fimDateTime = dataFim != null ? dataFim.atTime(23, 59, 59) : null;
        if (status != null && inicioDateTime == null && fimDateTime == null) {
            return pedidoRepository.findByStatus(status);
        }
        if (status == null && inicioDateTime != null && fimDateTime != null) {
            return pedidoRepository.findByDataPedidoBetween(inicioDateTime, fimDateTime);
        }
        if (status != null && inicioDateTime != null && fimDateTime != null) {
            return pedidoRepository.findByStatusAndDataPedidoBetween(
                    status, inicioDateTime, fimDateTime);
        }
        if (inicioDateTime != null && fimDateTime == null) {
            return pedidoRepository.findByDataPedidoGreaterThanEqual(inicioDateTime);
        }
        if (inicioDateTime == null && fimDateTime != null) {
            return pedidoRepository.findByDataPedidoLessThanEqual(fimDateTime);
        }
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorIdComItens(Long id) {
        return pedidoRepository.findByIdWithItens(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorClienteComItens(Long clienteId) {
        return pedidoRepository.findByClienteIdWithItens(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        Pedido pedido =
                pedidoRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Pedido", id));
        pedidoRepository.delete(pedido);
    }

    // ===== NOVO MÉTODO COM DTO =====

    @Override
    public PedidoResponse criarPedido(PedidoRequest pedidoRequest) {
        try {
            // Validate cliente exists and not excluded
            Cliente cliente =
                    clienteRepository
                            .findById(pedidoRequest.getClienteId())
                            .orElseThrow(() -> new EntityNotFoundException("Cliente", pedidoRequest.getClienteId()));

            if (Boolean.TRUE.equals(cliente.getExcluido()) || !cliente.isAtivo()) {
                throw new BusinessException("Cliente inativo ou excluído do sistema");
            }

            // Validate delivery address is provided
            if (pedidoRequest.getEnderecoEntrega() == null) {
                throw new BusinessException("Endereço de entrega é obrigatório");
            }

            // Validate restaurante exists and not excluded
            Restaurante restaurante =
                    restauranteRepository
                            .findById(pedidoRequest.getRestauranteId())
                            .orElseThrow(() -> new EntityNotFoundException("Restaurante", pedidoRequest.getRestauranteId()));

            if (Boolean.TRUE.equals(restaurante.getExcluido()) || !restaurante.isAtivo()) {
                throw new BusinessException("Restaurante inativo ou excluído do sistema");
            }

            // Validate itens
            if (pedidoRequest.getItens() == null || pedidoRequest.getItens().isEmpty()) {
                throw new BusinessException("Pedido deve conter ao menos um item");
            }

            // Validate all produtos are available and not excluded
            for (ItemPedidoRequest itemRequest : pedidoRequest.getItens()) {
                Produto produto =
                        produtoRepository
                                .findById(itemRequest.getProdutoId())
                                .orElseThrow(
                                        () ->
                                                new EntityNotFoundException("Produto", "ID", itemRequest.getProdutoId().toString()));

                if (Boolean.TRUE.equals(produto.getExcluido())
                        || !Boolean.TRUE.equals(produto.getDisponivel())) {
                    throw new BusinessException(
                            "Produto indisponível ou excluído: " + produto.getNome());
                }

                // Validate produto belongs to the same restaurant
                if (!produto.getRestaurante().getId().equals(pedidoRequest.getRestauranteId())) {
                    throw new BusinessException(
                            "Produto não pertence ao restaurante selecionado: "
                                    + produto.getNome());
                }
                
                // Validate stock for the item
                produtoService.validarEstoque(produto, itemRequest.getQuantidade());
            }

            // Convert DTO to Entity
            Pedido pedido = pedidoMapper.toEntity(pedidoRequest);

            // Create the pedido using existing business logic
            Pedido pedidoCriado = criar(pedido);

            // Convert back to DTO response
            return pedidoMapper.toResponse(pedidoCriado);

        } catch (Exception e) {
            log.error("Erro ao criar pedido via DTO: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    // ===== MÉTODO PARA VALIDAÇÃO DE ESTOQUE =====
    
    @Override
    public void validarEstoqueItens(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            Long produtoId = item.getProduto() != null ? item.getProduto().getId() : null;
            if (produtoId == null) {
                throw new BusinessException("ID do produto não pode ser nulo.");
            }
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new EntityNotFoundException("Produto", "ID", produtoId.toString()));
            produtoService.validarEstoque(produto, item.getQuantidade());
        }
    }
}