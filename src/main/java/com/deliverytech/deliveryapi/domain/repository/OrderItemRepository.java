package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.OrderItem;
import com.deliverytech.deliveryapi.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = :productId")
    List<OrderItem> findByProductId(Long productId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.restaurant.id = :restaurantId")
    List<OrderItem> findByRestaurantId(Long restaurantId);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long countByProductId(Long productId);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.id = :productId")
    Long sumQuantityByProductId(Long productId);

    // TODO: Fix HQL query to use limit parameter or remove it
    // @Query("SELECT oi.product, COUNT(oi) FROM OrderItem oi WHERE oi.order.restaurant.id = :restaurantId GROUP BY oi.product ORDER BY COUNT(oi) DESC")
    // List<Object[]> findMostOrderedProductsByRestaurant(Long restaurantId, int limit);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.product = :product AND oi.order.status = 'DELIVERED'")
    List<OrderItem> findDeliveredItemsByProduct(Product product);
}