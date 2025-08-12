package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.OrderStatus;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento de pedidos
 * Implementa consultas por cliente, status, data e relatórios analíticos
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ============= CONSULTAS BÁSICAS =============

    /**
     * Busca pedido por código único
     */
    Optional<Order> findByCode(String code);

    /**
     * Busca pedidos por cliente
     */
    List<Order> findByCustomer(User customer);

    /**
     * Busca pedidos por restaurante
     */
    List<Order> findByRestaurant(Restaurant restaurant);

    /**
     * Busca pedidos por entregador
     */
    List<Order> findByDeliveryPerson(User deliveryPerson);

    // ============= CONSULTAS POR STATUS =============

    /**
     * Busca pedidos por status específico
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Busca pedidos por múltiplos status
     */
    @Query("SELECT o FROM Order o WHERE o.status IN :statuses")
    List<Order> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    /**
     * Busca pedidos por restaurante e status
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = :status")
    List<Order> findByRestaurantIdAndStatus(@Param("restaurantId") Long restaurantId, @Param("status") OrderStatus status);

    /**
     * Busca pedidos por restaurante e múltiplos status
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status IN :statuses")
    List<Order> findByRestaurantIdAndStatusIn(@Param("restaurantId") Long restaurantId, @Param("statuses") List<OrderStatus> statuses);

    /**
     * Busca pedidos ativos (não finalizados)
     */
    @Query("SELECT o FROM Order o WHERE o.status NOT IN (com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED, com.deliverytech.deliveryapi.domain.model.OrderStatus.CANCELLED)")
    List<Order> findActiveOrders();

    /**
     * Pedidos prontos para entrega (sem entregador)
     */
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.deliveryPerson IS NULL")
    List<Order> findOrdersReadyForDelivery(@Param("status") OrderStatus status);

    // ============= CONSULTAS POR CLIENTE =============

    /**
     * Histórico de pedidos do cliente (paginado)
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC")
    Page<Order> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId, Pageable pageable);

    /**
     * Pedidos recentes do cliente
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.createdAt >= :since ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByCustomer(@Param("customerId") Long customerId, @Param("since") LocalDateTime since);

    /**
     * Último pedido do cliente
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC LIMIT 1")
    Optional<Order> findLastOrderByCustomer(@Param("customerId") Long customerId);

    /**
     * Pedidos do cliente em um restaurante específico
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC")
    List<Order> findByCustomerIdAndRestaurantId(@Param("customerId") Long customerId, @Param("restaurantId") Long restaurantId);

    // ============= CONSULTAS POR RESTAURANTE =============

    /**
     * Pedidos do restaurante (paginado)
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC")
    Page<Order> findByRestaurantIdOrderByCreatedAtDesc(@Param("restaurantId") Long restaurantId, Pageable pageable);

    /**
     * Pedidos pendentes do restaurante
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status IN (com.deliverytech.deliveryapi.domain.model.OrderStatus.PENDING, com.deliverytech.deliveryapi.domain.model.OrderStatus.CONFIRMED) ORDER BY o.createdAt ASC")
    List<Order> findPendingOrdersByRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Pedidos em preparo do restaurante
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.PREPARING ORDER BY o.createdAt ASC")
    List<Order> findOrdersInPreparationByRestaurant(@Param("restaurantId") Long restaurantId);

    // ============= CONSULTAS POR ENTREGADOR =============

    /**
     * Pedidos do entregador (paginado)
     */
    @Query("SELECT o FROM Order o WHERE o.deliveryPerson.id = :deliveryPersonId ORDER BY o.createdAt DESC")
    Page<Order> findByDeliveryPersonIdOrderByCreatedAtDesc(@Param("deliveryPersonId") Long deliveryPersonId, Pageable pageable);

    /**
     * Pedidos ativos do entregador
     */
    @Query("SELECT o FROM Order o WHERE o.deliveryPerson.id = :deliveryPersonId AND o.status IN (com.deliverytech.deliveryapi.domain.model.OrderStatus.OUT_FOR_DELIVERY)")
    List<Order> findActiveDeliveriesByDeliveryPerson(@Param("deliveryPersonId") Long deliveryPersonId);

    // ============= CONSULTAS POR FILTRO DE DATA =============

    /**
     * Pedidos em um período específico
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Pedidos de um cliente em um período
     */
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByCustomerAndDateRange(@Param("customerId") Long customerId, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Pedidos de um restaurante em um período
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByRestaurantAndDateRange(@Param("restaurantId") Long restaurantId, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * Pedidos por status e período
     */
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByStatusAndDateRange(@Param("status") OrderStatus status, 
                                        @Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);

    // ============= CONSULTAS DE RELATÓRIO E ANALYTICS =============

    /**
     * Conta pedidos ativos
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status IN :activeStatuses")
    Long countActiveOrders(@Param("activeStatuses") List<OrderStatus> activeStatuses);

    /**
     * Conta pedidos por restaurante em um período
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :restaurantId AND o.createdAt BETWEEN :startDate AND :endDate")
    Long countOrdersByRestaurantAndDateRange(@Param("restaurantId") Long restaurantId, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Receita total por restaurante em um período
     */
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByRestaurantAndDateRange(@Param("restaurantId") Long restaurantId, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Valor médio dos pedidos por restaurante
     */
    @Query("SELECT AVG(o.total) FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED")
    Optional<BigDecimal> getAverageOrderValueByRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Top 10 clientes por valor total gasto
     */
    @Query("SELECT o.customer, SUM(o.total) as totalSpent FROM Order o WHERE o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED GROUP BY o.customer ORDER BY totalSpent DESC")
    Page<Object[]> findTopCustomersByTotalSpent(Pageable pageable);

    /**
     * Restaurantes mais populares por número de pedidos
     */
    @Query("SELECT o.restaurant, COUNT(o) as orderCount FROM Order o WHERE o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED GROUP BY o.restaurant ORDER BY orderCount DESC")
    Page<Object[]> findMostPopularRestaurantsByOrderCount(Pageable pageable);

    /**
     * Distribuição de pedidos por status
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status ORDER BY COUNT(o) DESC")
    List<Object[]> getOrderCountByStatus();

    /**
     * Distribuição de pedidos por horário do dia
     */
    @Query("SELECT EXTRACT(HOUR FROM o.createdAt), COUNT(o) FROM Order o GROUP BY EXTRACT(HOUR FROM o.createdAt) ORDER BY EXTRACT(HOUR FROM o.createdAt)")
    List<Object[]> getOrderCountByHour();

    /**
     * Tempo médio de entrega por restaurante
     */
    @Query("SELECT o.restaurant.id, AVG(TIMESTAMPDIFF(MINUTE, o.createdAt, o.deliveryEndDate)) FROM Order o WHERE o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED AND o.deliveryEndDate IS NOT NULL GROUP BY o.restaurant.id")
    List<Object[]> getAverageDeliveryTimeByRestaurant();

    /**
     * Pedidos cancelados em um período (para análise)
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.CANCELLED AND o.createdAt BETWEEN :startDate AND :endDate")
    Long countCancelledOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Taxa de conversão por restaurante (pedidos entregues vs total)
     */
    @Query("SELECT o.restaurant.id, " +
           "COUNT(CASE WHEN o.status = com.deliverytech.deliveryapi.domain.model.OrderStatus.DELIVERED THEN 1 END) as delivered, " +
           "COUNT(o) as total " +
           "FROM Order o GROUP BY o.restaurant.id")
    List<Object[]> getConversionRateByRestaurant();
}