package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import java.time.LocalDateTime;

/**
 * Representa uma área de entrega para um restaurante, definida por um CEP ou bairro.
 */
@Entity
@Table(name = "delivery_areas")
public class DeliveryArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 9)
    @Column(name = "zip_code")
    private String zipCode;

    @Size(max = 100)
    private String neighborhood;

    @Size(max = 100)
    private String city;

    @Size(max = 2)
    private String state;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "delivery_fee_amount"))
    })
    private Money deliveryFee;

    @Column(name = "estimated_time_in_minutes")
    private Integer estimatedTimeInMinutes;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Construtor padrão necessário para JPA.
     */
    public DeliveryArea() {
    }

    /**
     * Construtor com campos obrigatórios.
     *
     * @param name       Nome da área de entrega
     * @param restaurant Restaurante associado
     */
    public DeliveryArea(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
        this.active = true;
    }

    /**
     * Construtor com campos obrigatórios e taxa de entrega.
     *
     * @param name        Nome da área de entrega
     * @param restaurant  Restaurante associado
     * @param deliveryFee Taxa de entrega
     */
    public DeliveryArea(String name, Restaurant restaurant, Money deliveryFee) {
        this.name = name;
        this.restaurant = restaurant;
        this.deliveryFee = deliveryFee;
        this.active = true;
    }

    /**
     * Ativa a área de entrega.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Desativa a área de entrega.
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Money deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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

        DeliveryArea that = (DeliveryArea) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DeliveryArea{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", restaurant=" + (restaurant != null ? restaurant.getId() : null) +
                ", active=" + active +
                '}';
    }
}