package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa uma categoria de restaurante no sistema, como "Italiana", "Japonesa", "Fast Food", etc.
 */
@Entity
@Table(name = "restaurant_categories")
public class RestaurantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 255)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    // TODO: Fix Hibernate 6.3.x compatibility issue
    // @ManyToMany(mappedBy = "categories")
    // private Set<Restaurant> restaurants = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Construtor padrão necessário para JPA.
     */
    public RestaurantCategory() {
    }

    /**
     * Construtor com campos obrigatórios.
     *
     * @param name Nome da categoria
     */
    public RestaurantCategory(String name) {
        this.name = name;
        this.active = true;
    }

    /**
     * Construtor com nome e descrição.
     *
     * @param name        Nome da categoria
     * @param description Descrição da categoria
     */
    public RestaurantCategory(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    /**
     * Adiciona um restaurante a esta categoria.
     *
     * @param restaurant Restaurante a ser adicionado
     */
    // TODO: Fix Hibernate 6.3.x compatibility issue
    // public void addRestaurant(Restaurant restaurant) {
    //     this.restaurants.add(restaurant);
    // }

    /**
     * Remove um restaurante desta categoria.
     *
     * @param restaurant Restaurante a ser removido
     */
    // public void removeRestaurant(Restaurant restaurant) {
    //     this.restaurants.remove(restaurant);
    // }

    /**
     * Ativa a categoria.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Desativa a categoria.
     */
    public void deactivate() {
        this.active = false;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // TODO: Fix Hibernate 6.3.x compatibility issue
    // public Set<Restaurant> getRestaurants() {
    //     return restaurants;
    // }

    // public void setRestaurants(Set<Restaurant> restaurants) {
    //     this.restaurants = restaurants;
    // }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantCategory that = (RestaurantCategory) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RestaurantCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}