package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public class Money {

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // Construtores
    public Money() {
        this.amount = BigDecimal.ZERO;
    }

    public Money(BigDecimal amount) {
        setAmount(amount);
    }

    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Money(String amount) {
        this(new BigDecimal(amount));
    }

    // Métodos de negócio
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier));
    }

    public Money percentage(BigDecimal percentage) {
        return new Money(this.amount.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
    }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isEqualTo(Money other) {
        return this.amount.compareTo(other.amount) == 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    // Getters e Setters
    public BigDecimal getAmount() {
        return amount;
    }

    private void setAmount(BigDecimal amount) {
        if (amount == null) {
            this.amount = BigDecimal.ZERO;
        } else {
            // Garante que o valor sempre terá duas casas decimais
            this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    // Métodos de Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "R$ " + amount.toString();
    }

    // Factory methods
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
}