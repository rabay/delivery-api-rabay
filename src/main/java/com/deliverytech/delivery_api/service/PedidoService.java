package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
  // Métodos existentes com Entity
  Pedido criar(Pedido pedido);

  Pedido buscarPorId(Long id);

  List<Pedido> buscarPorCliente(Long clienteId);

  List<Pedido> buscarPorRestaurante(Long restauranteId);

  org.springframework.data.domain.Page<Pedido> buscarPorCliente(
      Long clienteId, org.springframework.data.domain.Pageable pageable);

  org.springframework.data.domain.Page<Pedido> buscarPorRestaurante(
      Long restauranteId, org.springframework.data.domain.Pageable pageable);

  List<Pedido> buscarPorStatus(StatusPedido status);

  Pedido atualizarStatus(Long id, StatusPedido status);

  Pedido confirmar(Long id);

  Pedido cancelar(Long pedidoId);

  Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade);

  BigDecimal calcularTotal(Pedido pedido);

  BigDecimal calcularTotalPedido(List<ItemPedidoRequest> itens);

  List<Pedido> listarComFiltros(StatusPedido status, LocalDate dataInicio, LocalDate dataFim);

  List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

  Optional<Pedido> buscarPorIdComItens(Long id);

  List<Pedido> buscarPorClienteComItens(Long clienteId);

  List<Pedido> listarTodos();

  org.springframework.data.domain.Page<PedidoResponse> listarTodos(
      org.springframework.data.domain.Pageable pageable);

  org.springframework.data.domain.Page<PedidoResponse> buscarPedidosPorCliente(
      Long clienteId, org.springframework.data.domain.Pageable pageable);

  void deletar(Long id);

  // Novos métodos com DTOs
  PedidoResponse criarPedido(PedidoRequest pedidoRequest);

  // Métodos para controle de estoque
  void validarEstoqueItens(Pedido pedido);
}
