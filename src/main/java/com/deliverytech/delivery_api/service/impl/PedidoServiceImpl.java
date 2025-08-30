package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.mapper.PedidoMapper;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.PedidoService;

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

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.pedidoMapper = pedidoMapper;
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
                throw new RuntimeException("Pedido deve conter ao menos um item.");
            }
            BigDecimal total = BigDecimal.ZERO;
            for (ItemPedido item : pedido.getItens()) {
                Long produtoId = item.getProduto() != null ? item.getProduto().getId() : null;
                if (produtoId == null) {
                    log.warn("ID do produto é nulo ao criar pedido.");
                    throw new RuntimeException("ID do produto não pode ser nulo.");
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
            pedido.setValorTotal(total);
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
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
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
                                    return new RuntimeException("Pedido não encontrado");
                                });
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            log.warn("Tentativa de atualizar status de pedido já cancelado: pedidoId={}", id);
            throw new RuntimeException("Pedido já está cancelado");
        }
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            log.warn("Tentativa de atualizar status de pedido já entregue: pedidoId={}", id);
            throw new RuntimeException("Não é possível atualizar um pedido já entregue");
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
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cancelar(Long pedidoId) {
        Pedido pedido =
                pedidoRepository
                        .findById(pedidoId)
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new RuntimeException("Não é possível cancelar um pedido já entregue");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido já está cancelado");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        Pedido pedido =
                pedidoRepository
                        .findById(pedidoId)
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Produto produto =
                produtoRepository
                        .findById(produtoId)
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
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
                                            new RuntimeException(
                                                    "Produto não encontrado - ID: "
                                                            + itemRequest.getProdutoId()));
            if (!Boolean.TRUE.equals(produto.getDisponivel())) {
                throw new RuntimeException(
                        "Produto não está disponível - ID: " + itemRequest.getProdutoId());
            }
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
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
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
                            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            if (Boolean.TRUE.equals(cliente.getExcluido()) || !cliente.isAtivo()) {
                throw new RuntimeException("Cliente inativo ou excluído do sistema");
            }

            // Validate restaurante exists and not excluded
            Restaurante restaurante =
                    restauranteRepository
                            .findById(pedidoRequest.getRestauranteId())
                            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

            if (Boolean.TRUE.equals(restaurante.getExcluido()) || !restaurante.isAtivo()) {
                throw new RuntimeException("Restaurante inativo ou excluído do sistema");
            }

            // Validate itens
            if (pedidoRequest.getItens() == null || pedidoRequest.getItens().isEmpty()) {
                throw new RuntimeException("Pedido deve conter ao menos um item");
            }

            // Validate all produtos are available and not excluded
            for (ItemPedidoRequest itemRequest : pedidoRequest.getItens()) {
                Produto produto =
                        produtoRepository
                                .findById(itemRequest.getProdutoId())
                                .orElseThrow(
                                        () ->
                                                new RuntimeException(
                                                        "Produto não encontrado: ID "
                                                                + itemRequest.getProdutoId()));

                if (Boolean.TRUE.equals(produto.getExcluido())
                        || !Boolean.TRUE.equals(produto.getDisponivel())) {
                    throw new RuntimeException(
                            "Produto indisponível ou excluído: " + produto.getNome());
                }

                // Validate produto belongs to the same restaurant
                if (!produto.getRestaurante().getId().equals(pedidoRequest.getRestauranteId())) {
                    throw new RuntimeException(
                            "Produto não pertence ao restaurante selecionado: "
                                    + produto.getNome());
                }
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
}
