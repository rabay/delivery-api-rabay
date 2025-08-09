package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.ProductCategory;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByRestaurantAndActiveTrue(Restaurant restaurant);

    @Query("SELECT pc FROM ProductCategory pc WHERE pc.restaurant.id = :restaurantId AND pc.active = true ORDER BY pc.displayOrder, pc.name")
    List<ProductCategory> findByRestaurantIdOrderByDisplayOrder(Long restaurantId);

    @Query("SELECT pc FROM ProductCategory pc WHERE pc.restaurant.id = :restaurantId AND pc.name = :name")
    Optional<ProductCategory> findByRestaurantIdAndName(Long restaurantId, String name);

    @Query("SELECT pc FROM ProductCategory pc WHERE pc.restaurant.id = :restaurantId AND pc.id = :categoryId")
    Optional<ProductCategory> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId);

    boolean existsByRestaurantIdAndName(Long restaurantId, String name);
}