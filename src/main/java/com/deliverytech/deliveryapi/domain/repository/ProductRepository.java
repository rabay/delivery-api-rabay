package com.deliverytech.deliveryapi.domain.repository;

import com.deliverytech.deliveryapi.domain.model.Product;
import com.deliverytech.deliveryapi.domain.model.ProductCategory;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository para gerenciamento de produtos
 * Implementa consultas por restaurante, categoria, disponibilidade e controle de estoque
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============= CONSULTAS POR RESTAURANTE =============

    /**
     * Busca produtos ativos por restaurante
     */
    List<Product> findByRestaurantAndActiveTrue(Restaurant restaurant);

    /**
     * Busca produtos disponíveis por restaurante
     */
    List<Product> findByRestaurantAndActiveTrueAndAvailableTrue(Restaurant restaurant);

    /**
     * Busca produtos por ID do restaurante (otimizada)
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true")
    List<Product> findActiveProductsByRestaurantId(@Param("restaurantId") Long restaurantId);

    /**
     * Busca produtos disponíveis por ID do restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.available = true")
    List<Product> findAvailableProductsByRestaurantId(@Param("restaurantId") Long restaurantId);

    /**
     * Busca produtos paginados por restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true ORDER BY p.name ASC")
    Page<Product> findProductsByRestaurantIdPaginated(@Param("restaurantId") Long restaurantId, Pageable pageable);

    // ============= CONSULTAS POR CATEGORIA =============

    /**
     * Busca produtos ativos por categoria
     */
    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    /**
     * Busca produtos por categoria e restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.category.id = :categoryId AND p.active = true")
    List<Product> findByRestaurantIdAndCategoryId(@Param("restaurantId") Long restaurantId, @Param("categoryId") Long categoryId);

    /**
     * Busca produtos disponíveis por categoria e restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.category.id = :categoryId AND p.active = true AND p.available = true")
    List<Product> findAvailableByRestaurantIdAndCategoryId(@Param("restaurantId") Long restaurantId, @Param("categoryId") Long categoryId);

    /**
     * Busca produtos por nome da categoria
     */
    @Query("SELECT p FROM Product p JOIN p.category c WHERE p.active = true AND LOWER(c.name) = LOWER(:categoryName)")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    // ============= CONSULTAS POR DISPONIBILIDADE =============

    /**
     * Busca todos os produtos disponíveis
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = true")
    List<Product> findAllAvailableProducts();

    /**
     * Busca produtos indisponíveis para reabastecimento
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = false")
    List<Product> findUnavailableProducts();

    /**
     * Busca produtos com baixo estoque (simulado por disponibilidade)
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = false")
    List<Product> findLowStockProducts();

    // ============= CONSULTAS POR FAIXA DE PREÇO =============

    /**
     * Busca produtos por faixa de preço
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.available = true AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByRestaurantIdAndPriceBetween(@Param("restaurantId") Long restaurantId, 
                                                   @Param("minPrice") BigDecimal minPrice, 
                                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Produtos mais baratos por restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.available = true ORDER BY p.price ASC")
    List<Product> findCheapestProductsByRestaurant(@Param("restaurantId") Long restaurantId, Pageable pageable);

    /**
     * Produtos mais caros por restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.available = true ORDER BY p.price DESC")
    List<Product> findMostExpensiveProductsByRestaurant(@Param("restaurantId") Long restaurantId, Pageable pageable);

    // ============= CONSULTAS DE BUSCA E FILTRO =============

    /**
     * Busca produtos por nome no restaurante
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchByNameInRestaurant(@Param("restaurantId") Long restaurantId, @Param("query") String query);

    /**
     * Busca produtos por nome e descrição
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchByNameOrDescriptionInRestaurant(@Param("restaurantId") Long restaurantId, @Param("query") String query);

    /**
     * Busca avançada com múltiplos filtros
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND " +
           "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:onlyAvailable = false OR p.available = true) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> advancedSearchInRestaurant(@Param("restaurantId") Long restaurantId,
                                           @Param("query") String query,
                                           @Param("categoryId") Long categoryId,
                                           @Param("onlyAvailable") boolean onlyAvailable,
                                           @Param("minPrice") BigDecimal minPrice,
                                           @Param("maxPrice") BigDecimal maxPrice);

    // ============= CONSULTAS COM TAGS =============

    @Query("SELECT p FROM Product p WHERE p.active = true AND :tag MEMBER OF p.tags")
    List<Product> findByTag(@Param("tag") String tag);

    /**
     * Busca produtos por múltiplas tags
     */
    @Query("SELECT DISTINCT p FROM Product p WHERE p.active = true AND EXISTS " +
           "(SELECT t FROM Product p2 JOIN p2.tags t WHERE p2.id = p.id AND t IN :tags)")
    List<Product> findByTags(@Param("tags") List<String> tags);

    /**
     * Produtos mais populares por tag
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.available = true AND :tag MEMBER OF p.tags ORDER BY p.name ASC")
    List<Product> findPopularProductsByTag(@Param("tag") String tag, Pageable pageable);

    // ============= CONSULTAS DE CONTROLE DE ESTOQUE =============

    /**
     * Busca produto com lock otimista (para controle de estoque)
     */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    /**
     * Busca produto por restaurante e ID com validação
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.id = :productId")
    Optional<Product> findByRestaurantIdAndProductId(@Param("restaurantId") Long restaurantId, @Param("productId") Long productId);

    // ============= CONSULTAS DE RELATÓRIO =============

    /**
     * Conta produtos por categoria em um restaurante
     */
    @Query("SELECT c.name, COUNT(p) FROM Product p JOIN p.category c WHERE p.restaurant.id = :restaurantId AND p.active = true GROUP BY c.name ORDER BY COUNT(p) DESC")
    List<Object[]> countProductsByCategoryInRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Conta produtos por status de disponibilidade
     */
    @Query("SELECT p.available, COUNT(p) FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true GROUP BY p.available")
    List<Object[]> countProductsByAvailabilityInRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Produtos sem categoria definida
     */
    @Query("SELECT p FROM Product p WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.category IS NULL")
    List<Product> findProductsWithoutCategory(@Param("restaurantId") Long restaurantId);

    /**
     * Tempo médio de preparo por categoria
     */
    @Query("SELECT c.name, AVG(p.preparationTimeInMinutes) FROM Product p JOIN p.category c WHERE p.restaurant.id = :restaurantId AND p.active = true AND p.preparationTimeInMinutes IS NOT NULL GROUP BY c.name")
    List<Object[]> findAveragePreparationTimeByCategoryInRestaurant(@Param("restaurantId") Long restaurantId);
}