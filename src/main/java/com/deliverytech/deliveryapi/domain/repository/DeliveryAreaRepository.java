package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.DeliveryArea;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {

    List<DeliveryArea> findByRestaurant(Restaurant restaurant);

    List<DeliveryArea> findByRestaurantAndActiveTrue(Restaurant restaurant);

    @Query("SELECT da FROM DeliveryArea da WHERE da.restaurant.id = :restaurantId")
    List<DeliveryArea> findByRestaurantId(Long restaurantId);

    @Query("SELECT da FROM DeliveryArea da WHERE da.restaurant.id = :restaurantId AND da.active = true")
    List<DeliveryArea> findActiveByRestaurantId(Long restaurantId);

    @Query("SELECT da FROM DeliveryArea da WHERE da.zipCode = :zipCode AND da.active = true")
    List<DeliveryArea> findActiveByZipCode(String zipCode);

    @Query("SELECT da FROM DeliveryArea da WHERE da.neighborhood = :neighborhood AND da.city = :city AND da.state = :state AND da.active = true")
    List<DeliveryArea> findActiveByNeighborhoodAndCityAndState(String neighborhood, String city, String state);

    @Query("SELECT da FROM DeliveryArea da WHERE da.restaurant.id = :restaurantId AND da.zipCode = :zipCode AND da.active = true")
    Optional<DeliveryArea> findActiveByRestaurantIdAndZipCode(Long restaurantId, String zipCode);

    @Query("SELECT da FROM DeliveryArea da WHERE da.restaurant.id = :restaurantId AND da.neighborhood = :neighborhood AND da.city = :city AND da.state = :state AND da.active = true")
    Optional<DeliveryArea> findActiveByRestaurantIdAndNeighborhoodAndCityAndState(Long restaurantId, String neighborhood, String city, String state);

    @Query("SELECT COUNT(da) FROM DeliveryArea da WHERE da.restaurant.id = :restaurantId")
    Long countByRestaurantId(Long restaurantId);
}