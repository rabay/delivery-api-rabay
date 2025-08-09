package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.OrderStatus;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByCode(String code);

    List<Order> findByCustomer(User customer);

    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByDeliveryPerson(User deliveryPerson);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status IN :statuses")
    List<Order> findByStatusIn(List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = :status")
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status IN :statuses")
    List<Order> findByRestaurantIdAndStatusIn(Long restaurantId, List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.createdAt DESC")
    Page<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC")
    Page<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deliveryPerson.id = :deliveryPersonId ORDER BY o.createdAt DESC")
    Page<Order> findByDeliveryPersonIdOrderByCreatedAtDesc(Long deliveryPersonId, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status IN :activeStatuses")
    Long countActiveOrders(List<OrderStatus> activeStatuses);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :restaurantId AND o.createdAt BETWEEN :startDate AND :endDate")
    Long countOrdersByRestaurantAndDateRange(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.deliveryPerson IS NULL")
    List<Order> findOrdersReadyForDelivery(OrderStatus status);
}