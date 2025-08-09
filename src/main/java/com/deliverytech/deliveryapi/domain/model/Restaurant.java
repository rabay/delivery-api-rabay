package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String phone;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private String logo;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean open = false;

    // @ElementCollection - TODO: Fix Hibernate 6.3.x compatibility issue
    // @CollectionTable(name = "restaurant_categories", joinColumns = @JoinColumn(name = "restaurant_id"))
    // @Column(name = "category")
    // private Set<String> categories = new HashSet<>();

    // TODO: Restaurar BusinessHours após resolver problema de compatibilidade com Hibernate 6.3.x
    // @ElementCollection
    // @CollectionTable(name = "restaurant_business_hours", joinColumns = @JoinColumn(name = "restaurant_id"))
    // private Set<BusinessHours> businessHours = new HashSet<>();

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "delivery_fee", nullable = false))
    private Money deliveryFee;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "min_order_value"))
    private Money minimumOrderValue;

    @Column(name = "avg_delivery_time_minutes")
    private Integer averageDeliveryTimeInMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Construtores
    public Restaurant() {
        this.deliveryFee = Money.zero();
        this.minimumOrderValue = Money.zero();
    }

    public Restaurant(String name, String description, String cnpj, String phone, 
                     Address address, User owner) {
        this();
        this.name = name;
        this.description = description;
        this.cnpj = cnpj;
        this.phone = phone;
        this.address = address;
        this.owner = owner;
    }

    // Métodos de negócio
    // TODO: Fix Hibernate 6.3.x compatibility issue
    // public void addCategory(String category) {
    //     this.categories.add(category);
    // }

    // public void removeCategory(String category) {
    //     this.categories.remove(category);
    // }

    // TODO: Restaurar métodos BusinessHours após resolver problema de compatibilidade
    /*
    public void addBusinessHours(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
        BusinessHours hours = new BusinessHours(dayOfWeek, openTime, closeTime);
        this.businessHours.add(hours);
    }

    public void removeBusinessHours(BusinessHours hours) {
        this.businessHours.remove(hours);
    }
    */

    public boolean isOpenNow() {
        // TODO: Implementar com tabela separada para BusinessHours
        return active && open;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    // TODO: Fix Hibernate 6.3.x compatibility issue
    // public Set<String> getCategories() {
    //     return categories;
    // }

    // public void setCategories(Set<String> categories) {
    //     this.categories = categories;
    // }

    // TODO: Restaurar getters/setters BusinessHours
    /*
    public Set<BusinessHours> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(Set<BusinessHours> businessHours) {
        this.businessHours = businessHours;
    }
    */

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Money deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Money getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue(Money minimumOrderValue) {
        this.minimumOrderValue = minimumOrderValue;
    }

    public Integer getAverageDeliveryTimeInMinutes() {
        return averageDeliveryTimeInMinutes;
    }

    public void setAverageDeliveryTimeInMinutes(Integer averageDeliveryTimeInMinutes) {
        this.averageDeliveryTimeInMinutes = averageDeliveryTimeInMinutes;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Classe interna para representar horários de funcionamento
    @Embeddable
    public static class BusinessHours {
        
        @Enumerated(EnumType.STRING)
        @Column(name = "day_of_week", nullable = false)
        private DayOfWeek dayOfWeek;
        
        @Column(name = "open_time", nullable = false)
        private LocalTime openTime;
        
        @Column(name = "close_time", nullable = false)
        private LocalTime closeTime;

        // Construtores
        public BusinessHours() {
        }

        public BusinessHours(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
            this.dayOfWeek = dayOfWeek;
            this.openTime = openTime;
            this.closeTime = closeTime;
        }

        // Getters e Setters
        public DayOfWeek getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public LocalTime getOpenTime() {
            return openTime;
        }

        public void setOpenTime(LocalTime openTime) {
            this.openTime = openTime;
        }

        public LocalTime getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(LocalTime closeTime) {
            this.closeTime = closeTime;
        }

        // Métodos de Object
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BusinessHours that = (BusinessHours) o;
            return dayOfWeek == that.dayOfWeek &&
                    openTime.equals(that.openTime) &&
                    closeTime.equals(that.closeTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dayOfWeek, openTime, closeTime);
        }
    }
}