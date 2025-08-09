package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.Review;
import com.deliverytech.deliveryapi.domain.model.ReviewType;
import com.deliverytech.deliveryapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCustomer(User customer);

    Optional<Review> findByOrderAndType(Order order, ReviewType type);

    List<Review> findByType(ReviewType type);

    @Query("SELECT r FROM Review r WHERE r.type = :type AND r.order.restaurant.id = :restaurantId")
    List<Review> findRestaurantReviews(ReviewType type, Long restaurantId);

    @Query("SELECT r FROM Review r WHERE r.type = :type AND r.order.deliveryPerson.id = :deliveryPersonId")
    List<Review> findDeliveryPersonReviews(ReviewType type, Long deliveryPersonId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.type = :type AND r.order.restaurant.id = :restaurantId")
    Double getAverageRatingForRestaurant(ReviewType type, Long restaurantId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.type = :type AND r.order.deliveryPerson.id = :deliveryPersonId")
    Double getAverageRatingForDeliveryPerson(ReviewType type, Long deliveryPersonId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.type = :type AND r.order.restaurant.id = :restaurantId AND r.rating = :rating")
    Long countRestaurantReviewsByRating(ReviewType type, Long restaurantId, Integer rating);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.type = :type AND r.order.deliveryPerson.id = :deliveryPersonId AND r.rating = :rating")
    Long countDeliveryPersonReviewsByRating(ReviewType type, Long deliveryPersonId, Integer rating);
}