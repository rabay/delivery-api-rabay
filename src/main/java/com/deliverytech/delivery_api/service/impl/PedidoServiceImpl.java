package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.PedidoService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Pedido criar(Pedido pedido) {
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(BigDecimal.ZERO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Override
    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Pedido> buscarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido confirmar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(StatusPedido.CONFIRMADO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cancelar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
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
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Produto produto = produtoRepository.findById(produtoId)
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
    public BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pedido.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calcularTotalPedido(List<ItemPedidoRequest> itens) {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoRequest itemRequest : itens) {
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado - ID: " + itemRequest.getProdutoId()));
            if (!produto.getAtivo()) {
                throw new RuntimeException("Produto não está disponível - ID: " + itemRequest.getProdutoId());
            }
            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal quantidade = BigDecimal.valueOf(itemRequest.getQuantidade());
            BigDecimal subtotal = precoUnitario.multiply(quantidade);
            total = total.add(subtotal);
        }
        return total;
    }

    @Override
    public List<Pedido> listarComFiltros(StatusPedido status, LocalDate dataInicio, LocalDate dataFim) {
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
            return pedidoRepository.findByStatusAndDataPedidoBetween(status, inicioDateTime, fimDateTime);
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
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    @Override
    public Optional<Pedido> buscarPorIdComItens(Long id) {
        return pedidoRepository.findByIdWithItens(id);
    }

    @Override
    public List<Pedido> buscarPorClienteComItens(Long clienteId) {
        return pedidoRepository.findByClienteIdWithItens(clienteId);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedidoRepository.delete(pedido);
    }
}
