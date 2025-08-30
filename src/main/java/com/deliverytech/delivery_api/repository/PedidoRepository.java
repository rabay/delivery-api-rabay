package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.projection.RelatorioVendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByRestauranteId(Long restauranteId);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    @Query(
            "SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto WHERE p.id"
                + " = :id")
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);

    @Query(
            "SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto WHERE"
                + " p.cliente.id = :clienteId")
    List<Pedido> findByClienteIdWithItens(@Param("clienteId") Long clienteId);

    @Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto")
    List<Pedido> findAllWithItens();

    List<Pedido> findByStatusAndDataPedidoBetween(
            StatusPedido status, LocalDateTime inicio, LocalDateTime fim);

    List<Pedido> findByDataPedidoGreaterThanEqual(LocalDateTime data);

    List<Pedido> findByDataPedidoLessThanEqual(LocalDateTime data);

    // === CONSULTAS CUSTOMIZADAS ===
    @Query(
            "SELECT p.restaurante.nome as nomeRestaurante, SUM(p.valorTotal) as totalVendas,"
                + " COUNT(p.id) as quantidadePedidos FROM Pedido p GROUP BY p.restaurante.nome"
                + " ORDER BY totalVendas DESC")
    List<RelatorioVendas> calcularTotalVendasPorRestaurante();

    @Query(
            "SELECT p.restaurante.nome as nomeRestaurante, SUM(p.valorTotal) as totalVendas,"
                + " COUNT(p.id) as quantidadePedidos FROM Pedido p WHERE p.dataPedido BETWEEN"
                + " :inicio AND :fim GROUP BY p.restaurante.nome ORDER BY totalVendas DESC")
    List<RelatorioVendas> calcularTotalVendasPorRestaurante(
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fim") java.time.LocalDateTime fim);

    @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor ORDER BY p.valorTotal DESC")
    List<Pedido> buscarPedidosComValorAcimaDe(@Param("valor") java.math.BigDecimal valor);

    @Query(
            "SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim AND p.status ="
                + " :status ORDER BY p.dataPedido DESC")
    List<Pedido> relatorioPedidosPorPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") StatusPedido status);
}
