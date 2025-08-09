package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Product;
import com.deliverytech.deliveryapi.domain.model.ProductCategory;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByRestaurantAndActiveTrue(Restaurant restaurant);

    List<Product> findByRestaurantAndActiveTrueAndAvailableTrue(Restaurant restaurant);

    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.available = true")
    List<Product> findAvailableProductsByRestaurantId(Long restaurantId);

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.category.id = :categoryId AND p.active = true")
    List<Product> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.id = :productId")
    Optional<Product> findByRestaurantIdAndProductId(Long restaurantId, Long productId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(Long id);

    @Query("SELECT p FROM Product p WHERE p.active = true AND :tag MEMBER OF p.tags")
    List<Product> findByTag(String tag);

    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchByNameInRestaurant(Long restaurantId, String query);
}