package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.User;
import com.deliverytech.deliveryapi.domain.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento de usuários (clientes, restaurantes, entregadores)
 * Implementa consultas específicas por e-mail, status ativo e tipo de usuário
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ============= CONSULTAS POR EMAIL =============
    
    /**
     * Busca usuário por e-mail (unique)
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe usuário com e-mail específico
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuário por e-mail ignorando case
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    // ============= CONSULTAS POR STATUS ATIVO =============

    /**
     * Busca todos os usuários ativos
     */
    List<User> findByActiveTrue();

    /**
     * Busca usuários ativos por tipo
     */
    List<User> findByActiveTrueAndUserType(UserType userType);

    /**
     * Busca clientes ativos paginado
     */
    @Query("SELECT u FROM User u WHERE u.active = true AND u.userType = com.deliverytech.deliveryapi.domain.model.UserType.CUSTOMER")
    Page<User> findActiveCustomers(Pageable pageable);

    /**
     * Busca clientes inativos para reativação
     */
    @Query("SELECT u FROM User u WHERE u.active = false AND u.userType = com.deliverytech.deliveryapi.domain.model.UserType.CUSTOMER")
    List<User> findInactiveCustomers();

    // ============= CONSULTAS POR TIPO DE USUÁRIO =============

    List<User> findByUserType(UserType userType);

    /**
     * Busca todos os entregadores ativos disponíveis
     */
    @Query("SELECT u FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.DELIVERY_PERSON AND u.active = true")
    List<User> findAllActiveDeliveryPersons();

    /**
     * Busca proprietários de restaurantes
     */
    @Query("SELECT u FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.RESTAURANT AND u.active = true")
    List<User> findActiveRestaurantOwners();

    // ============= CONSULTAS POR PERFIL/ROLE =============

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);

    // ============= CONSULTAS DE RELATÓRIO =============

    /**
     * Conta clientes cadastrados em um período
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.CUSTOMER AND u.createdAt BETWEEN :startDate AND :endDate")
    Long countCustomersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Busca clientes com e-mail verificado
     */
    @Query("SELECT u FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.CUSTOMER AND u.emailVerified = true AND u.active = true")
    List<User> findVerifiedActiveCustomers();

    /**
     * Busca clientes por cidade (para análise geográfica)
     */
    @Query("SELECT u FROM User u WHERE u.userType = com.deliverytech.deliveryapi.domain.model.UserType.CUSTOMER AND u.address.city = :city AND u.active = true")
    List<User> findActiveCustomersByCity(@Param("city") String city);

    /**
     * Busca usuários com último login recente
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since AND u.active = true")
    List<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
}