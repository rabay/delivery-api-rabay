package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.User;
import com.deliverytech.deliveryapi.domain.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByUserType(UserType userType);

    @Query("SELECT u FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.DELIVERY_PERSON AND u.active = true")
    List<User> findAllActiveDeliveryPersons();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(String role);
}