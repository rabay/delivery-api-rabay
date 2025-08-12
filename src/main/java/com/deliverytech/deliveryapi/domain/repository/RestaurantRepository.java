package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import com.deliverytech.deliveryapi.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento de restaurantes
 * Implementa consultas por nome, categoria, localização e filtros específicos
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // ============= CONSULTAS BÁSICAS =============

    /**
     * Busca por nome (case-insensitive)
     */
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    /**
     * Busca restaurantes ativos
     */
    List<Restaurant> findByActiveTrue();

    /**
     * Busca restaurantes abertos
     */
    List<Restaurant> findByOpenTrue();

    /**
     * Busca restaurantes ativos e abertos
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.open = true")
    List<Restaurant> findActiveAndOpen();

    /**
     * Busca restaurantes ativos e abertos (método alternativo usado no service)
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.open = true")
    List<Restaurant> findByActiveTrueAndOpenTrue();

    /**
     * Busca por proprietário
     */
    List<Restaurant> findByOwner(User owner);

    /**
     * Busca por CNPJ
     */
    Optional<Restaurant> findByCnpj(String cnpj);

    // ============= CONSULTAS POR CATEGORIA =============

    /**
     * Busca por categoria específica
     */
    @Query("SELECT r FROM Restaurant r JOIN r.categories c WHERE c = :category AND r.active = true")
    List<Restaurant> findByCategory(@Param("category") RestaurantCategory category);

    /**
     * Busca por múltiplas categorias
     */
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.categories c WHERE c.id IN :categoryIds AND r.active = true")
    List<Restaurant> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    /**
     * Busca por nome da categoria
     */
    @Query("SELECT r FROM Restaurant r JOIN r.categories c WHERE LOWER(c.name) = LOWER(:categoryName) AND r.active = true")
    List<Restaurant> findByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Busca restaurantes por categoria usando ID
     */
    @Query("SELECT r FROM Restaurant r JOIN r.categories c WHERE c.id = :categoryId AND r.active = true")
    List<Restaurant> findByCategoryId(@Param("categoryId") Long categoryId);

    // ============= CONSULTAS POR LOCALIZAÇÃO =============

    /**
     * Busca por cidade
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.address.city) = LOWER(:city) AND r.active = true")
    List<Restaurant> findByCity(@Param("city") String city);

    /**
     * Busca por estado
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.address.state) = LOWER(:state) AND r.active = true")
    List<Restaurant> findByState(@Param("state") String state);

    /**
     * Busca por bairro
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.address.neighborhood) LIKE LOWER(CONCAT('%', :neighborhood, '%')) AND r.active = true")
    List<Restaurant> findByNeighborhood(@Param("neighborhood") String neighborhood);

    /**
     * Busca por CEP
     */
    @Query("SELECT r FROM Restaurant r WHERE r.address.postalCode = :postalCode AND r.active = true")
    List<Restaurant> findByZipCode(@Param("postalCode") String postalCode);

    // ============= CONSULTAS POR FILTROS DE VALOR =============

    /**
     * Restaurantes por valor mínimo do pedido
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND (:maxMinOrder IS NULL OR r.minimumOrderValue.amount <= :maxMinOrder)")
    List<Restaurant> findByMinOrderValueLessThanEqual(@Param("maxMinOrder") BigDecimal maxMinOrder);

    /**
     * Busca por faixa de taxa de entrega
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.deliveryFee.amount BETWEEN :minFee AND :maxFee")
    List<Restaurant> findByDeliveryFeeBetween(@Param("minFee") BigDecimal minFee, @Param("maxFee") BigDecimal maxFee);

    /**
     * Restaurantes com entrega gratuita
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.deliveryFee.amount = 0")
    List<Restaurant> findWithFreeDelivery();

    /**
     * Restaurantes com taxa de entrega baixa
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.deliveryFee.amount <= :maxFee ORDER BY r.deliveryFee.amount ASC")
    List<Restaurant> findWithLowDeliveryFee(@Param("maxFee") BigDecimal maxFee);

    // ============= CONSULTAS POR TEMPO DE ENTREGA =============

    /**
     * Restaurantes com entrega mais rápida
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.open = true ORDER BY r.averageDeliveryTimeInMinutes ASC")
    List<Restaurant> findFastestDeliveryRestaurants(Pageable pageable);

    /**
     * Restaurantes por tempo máximo de entrega
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.open = true AND r.averageDeliveryTimeInMinutes <= :maxDeliveryTime ORDER BY r.averageDeliveryTimeInMinutes ASC")
    List<Restaurant> findByMaxDeliveryTime(@Param("maxDeliveryTime") Integer maxDeliveryTime);

    // ============= CONSULTAS AVANÇADAS E COMBINADAS =============

    /**
     * Busca avançada por múltiplos critérios
     */
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN r.categories c WHERE " +
           "r.active = true AND " +
           "(:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:city IS NULL OR LOWER(r.address.city) = LOWER(:city)) AND " +
           "(:categoryId IS NULL OR c.id = :categoryId) AND " +
           "(:maxDeliveryFee IS NULL OR r.deliveryFee.amount <= :maxDeliveryFee) AND " +
           "(:maxMinOrder IS NULL OR r.minimumOrderValue.amount <= :maxMinOrder)")
    List<Restaurant> findByMultipleCriteria(
        @Param("name") String name,
        @Param("city") String city,
        @Param("categoryId") Long categoryId,
        @Param("maxDeliveryFee") BigDecimal maxDeliveryFee,
        @Param("maxMinOrder") BigDecimal maxMinOrder
    );

    /**
     * Restaurantes próximos por CEP (simulação)
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND " +
           "r.address.postalCode LIKE CONCAT(SUBSTRING(:postalCode, 1, 5), '%')")
    List<Restaurant> findNearbyByZipCode(@Param("postalCode") String postalCode);

    /**
     * Restaurantes populares (simulação baseada em nome)
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND r.open = true ORDER BY r.name ASC")
    List<Restaurant> findPopularRestaurants(Pageable pageable);

    // ============= CONSULTAS DE RELATÓRIOS E ANALYTICS =============

    /**
     * Conta restaurantes por cidade
     */
    @Query("SELECT r.address.city, COUNT(r) FROM Restaurant r WHERE r.active = true GROUP BY r.address.city ORDER BY COUNT(r) DESC")
    List<Object[]> countRestaurantsByCity();

    /**
     * Conta restaurantes por categoria
     */
    @Query("SELECT c.name, COUNT(r) FROM Restaurant r JOIN r.categories c WHERE r.active = true GROUP BY c.name ORDER BY COUNT(r) DESC")
    List<Object[]> countRestaurantsByCategory();

    /**
     * Restaurantes por faixa de taxa de entrega
     */
    @Query("SELECT " +
           "CASE " +
           "  WHEN r.deliveryFee.amount = 0 THEN 'Grátis' " +
           "  WHEN r.deliveryFee.amount <= 5 THEN 'Baixa (até R$ 5,00)' " +
           "  WHEN r.deliveryFee.amount <= 10 THEN 'Média (R$ 5,01 - R$ 10,00)' " +
           "  ELSE 'Alta (acima de R$ 10,00)' " +
           "END as faixaEntrega, " +
           "COUNT(r) " +
           "FROM Restaurant r WHERE r.active = true " +
           "GROUP BY " +
           "CASE " +
           "  WHEN r.deliveryFee.amount = 0 THEN 'Grátis' " +
           "  WHEN r.deliveryFee.amount <= 5 THEN 'Baixa (até R$ 5,00)' " +
           "  WHEN r.deliveryFee.amount <= 10 THEN 'Média (R$ 5,01 - R$ 10,00)' " +
           "  ELSE 'Alta (acima de R$ 10,00)' " +
           "END " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> getDeliveryFeeDistribution();

    /**
     * Taxa média de entrega por cidade
     */
    @Query("SELECT r.address.city, AVG(r.deliveryFee.amount) FROM Restaurant r WHERE r.active = true GROUP BY r.address.city ORDER BY AVG(r.deliveryFee.amount)")
    List<Object[]> getAverageDeliveryFeeByCity();

    /**
     * Restaurantes sem pedido mínimo
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND (r.minimumOrderValue.amount = 0 OR r.minimumOrderValue.amount IS NULL)")
    List<Restaurant> findWithoutMinimumOrder();

    // ============= CONSULTAS ESPECÍFICAS PARA NEGÓCIO =============

    /**
     * Restaurantes recomendados (exemplo: ativos, abertos, com entrega rápida)
     */
    @Query("SELECT r FROM Restaurant r WHERE " +
           "r.active = true AND " +
           "r.open = true AND " +
           "r.averageDeliveryTimeInMinutes <= 60 AND " +
           "r.deliveryFee.amount <= 10 " +
           "ORDER BY r.deliveryFee.amount ASC, r.averageDeliveryTimeInMinutes ASC")
    List<Restaurant> findRecommendedRestaurants(Pageable pageable);

    /**
     * Busca para sistema de busca/autocomplete
     */
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN r.categories c WHERE " +
           "r.active = true AND (" +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
           ") ORDER BY r.name ASC")
    List<Restaurant> searchRestaurants(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Busca por nome (método usado no service)
     */
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Restaurant> searchByName(@Param("query") String query);

    /**
     * Busca por nome ou categoria (método usado no service)
     */
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN r.categories c WHERE " +
           "r.active = true AND (" +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
           ")")
    List<Restaurant> searchByNameOrCategory(@Param("query") String query);

    /**
     * Lista todas as categorias em uso
     */
    @Query("SELECT DISTINCT c FROM Restaurant r JOIN r.categories c WHERE r.active = true ORDER BY c.name")
    List<RestaurantCategory> findAllCategoriesInUse();

    /**
     * Restaurantes por proprietário (paginado)
     */
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :ownerId ORDER BY r.createdAt DESC")
    Page<Restaurant> findByOwnerIdOrderByCreatedAtDesc(@Param("ownerId") Long ownerId, Pageable pageable);
}