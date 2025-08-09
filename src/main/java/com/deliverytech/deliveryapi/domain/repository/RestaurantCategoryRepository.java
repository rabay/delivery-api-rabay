package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {

    List<RestaurantCategory> findByActiveTrue();

    Optional<RestaurantCategory> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT rc FROM RestaurantCategory rc WHERE LOWER(rc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<RestaurantCategory> searchByName(String searchTerm);

    // TODO: Fix Hibernate 6.3.x compatibility issue - these queries reference removed relationships
    // @Query("SELECT rc FROM RestaurantCategory rc JOIN rc.restaurants r WHERE r.id = :restaurantId")
    // List<RestaurantCategory> findByRestaurantId(Long restaurantId);

    // @Query("SELECT COUNT(r) FROM Restaurant r JOIN r.categories rc WHERE rc.id = :categoryId")
    // Long countRestaurantsByCategory(Long categoryId);
}