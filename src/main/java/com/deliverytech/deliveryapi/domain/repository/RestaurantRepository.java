package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByActiveTrue();

    List<Restaurant> findByActiveTrueAndOpenTrue();

    List<Restaurant> findByOwner(User owner);

    // TODO: Fix Hibernate 6.3.x compatibility issue - these queries reference removed categories relationship
    // @Query("SELECT r FROM Restaurant r WHERE r.active = true AND :category MEMBER OF r.categories")
    // List<Restaurant> findByCategory(String category);

    // @Query("SELECT DISTINCT c FROM Restaurant r JOIN r.categories c WHERE r.active = true ORDER BY c")
    // List<String> findAllCategories();

    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Restaurant> searchByName(String query);

    // @Query("SELECT r FROM Restaurant r WHERE r.active = true AND " +
    //        "(LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
    //        ":query MEMBER OF r.categories)")
    // List<Restaurant> searchByNameOrCategory(String query);
}