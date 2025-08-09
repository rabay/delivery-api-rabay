package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true, updatable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_person_id")
    private User deliveryPerson;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "delivery_street"))
    @AttributeOverride(name = "number", column = @Column(name = "delivery_number"))
    @AttributeOverride(name = "complement", column = @Column(name = "delivery_complement"))
    @AttributeOverride(name = "neighborhood", column = @Column(name = "delivery_neighborhood"))
    @AttributeOverride(name = "city", column = @Column(name = "delivery_city"))
    @AttributeOverride(name = "state", column = @Column(name = "delivery_state"))
    @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code"))
    @AttributeOverride(name = "reference", column = @Column(name = "delivery_reference"))
    private Address deliveryAddress;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "subtotal"))
    private Money subtotal;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "delivery_fee"))
    private Money deliveryFee;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "total"))
    private Money total;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "confirmation_date")
    private LocalDateTime confirmationDate;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @Column(name = "preparation_start_date")
    private LocalDateTime preparationStartDate;

    @Column(name = "preparation_end_date")
    private LocalDateTime preparationEndDate;

    @Column(name = "delivery_start_date")
    private LocalDateTime deliveryStartDate;

    @Column(name = "delivery_end_date")
    private LocalDateTime deliveryEndDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Construtores
    public Order() {
        this.code = generateOrderCode();
        this.status = OrderStatus.PENDING;
        this.subtotal = Money.zero();
        this.deliveryFee = Money.zero();
        this.total = Money.zero();
    }

    public Order(User customer, Restaurant restaurant, Address deliveryAddress) {
        this();
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = restaurant.getDeliveryFee();
        calculateTotal();
    }

    // Métodos de negócio
    private String generateOrderCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void addItem(Product product, int quantity, String notes) {
        // TODO: Implementar com repositório OrderItemRepository
        calculateTotal();
    }

    public void removeItem(OrderItem item) {
        // TODO: Implementar com repositório OrderItemRepository
        calculateTotal();
    }

    public void calculateTotal() {
        // Para agora, vamos usar valores fixos para evitar erros
        // TODO: Implementar cálculo baseado em consulta ao banco
        if (this.subtotal == null) {
            this.subtotal = Money.zero();
        }
        this.total = subtotal.add(deliveryFee);
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Pedido não pode ser confirmado no status atual: " + status);
        }
        this.status = OrderStatus.CONFIRMED;
        this.confirmationDate = LocalDateTime.now();
    }

    public void startPreparation() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Pedido não pode iniciar preparação no status atual: " + status);
        }
        this.status = OrderStatus.PREPARING;
        this.preparationStartDate = LocalDateTime.now();
    }

    public void finishPreparation() {
        if (status != OrderStatus.PREPARING) {
            throw new IllegalStateException("Pedido não pode finalizar preparação no status atual: " + status);
        }
        this.status = OrderStatus.READY;
        this.preparationEndDate = LocalDateTime.now();
    }

    public void startDelivery(User deliveryPerson) {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Pedido não pode iniciar entrega no status atual: " + status);
        }
        this.deliveryPerson = deliveryPerson;
        this.status = OrderStatus.OUT_FOR_DELIVERY;
        this.deliveryStartDate = LocalDateTime.now();
    }

    public void deliver() {
        if (status != OrderStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Pedido não pode ser entregue no status atual: " + status);
        }
        this.status = OrderStatus.DELIVERED;
        this.deliveryEndDate = LocalDateTime.now();
    }

    public void cancel() {
        if (status == OrderStatus.PREPARING || status == OrderStatus.READY || 
            status == OrderStatus.OUT_FOR_DELIVERY || status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Pedido não pode ser cancelado no status atual: " + status);
        }
        this.status = OrderStatus.CANCELLED;
        this.cancellationDate = LocalDateTime.now();
    }

    public boolean canCancel() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(User deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    // TODO: Implementar getItems() usando repositório OrderItemRepository
    // public List<OrderItem> getItems() { ... }

    public Money getSubtotal() {
        return subtotal;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Money deliveryFee) {
        this.deliveryFee = deliveryFee;
        calculateTotal();
    }

    public Money getTotal() {
        return total;
    }

    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }

    public LocalDateTime getPreparationStartDate() {
        return preparationStartDate;
    }

    public LocalDateTime getPreparationEndDate() {
        return preparationEndDate;
    }

    public LocalDateTime getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public LocalDateTime getDeliveryEndDate() {
        return deliveryEndDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}