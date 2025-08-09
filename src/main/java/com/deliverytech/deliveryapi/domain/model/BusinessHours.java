package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Representa os horários de funcionamento de um restaurante para um dia específico da semana.
 * Esta classe é um Value Object (objeto de valor) que é incorporado na entidade Restaurant.
 */
@Embeddable
public class BusinessHours {

    @NotNull
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @NotNull
    @Column(name = "opening_time")
    private LocalTime openingTime;

    @NotNull
    @Column(name = "closing_time")
    private LocalTime closingTime;

    /**
     * Construtor padrão necessário para JPA.
     */
    public BusinessHours() {
    }

    /**
     * Construtor com todos os campos.
     *
     * @param dayOfWeek   Dia da semana
     * @param openingTime Horário de abertura
     * @param closingTime Horário de fechamento
     */
    public BusinessHours(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        validate();
    }

    /**
     * Verifica se o horário atual está dentro do período de funcionamento para o dia da semana especificado.
     *
     * @param currentDayOfWeek Dia da semana atual
     * @param currentTime      Hora atual
     * @return true se estiver aberto, false caso contrário
     */
    public boolean isOpenNow(DayOfWeek currentDayOfWeek, LocalTime currentTime) {
        return this.dayOfWeek.equals(currentDayOfWeek) &&
                (currentTime.equals(openingTime) || currentTime.isAfter(openingTime)) &&
                (currentTime.isBefore(closingTime));
    }

    /**
     * Valida se os horários de funcionamento são consistentes.
     * O horário de abertura deve ser anterior ao horário de fechamento.
     */
    private void validate() {
        if (openingTime != null && closingTime != null && openingTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("O horário de abertura deve ser anterior ao horário de fechamento");
        }
    }

    // Getters e Setters

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessHours that = (BusinessHours) o;

        if (dayOfWeek != that.dayOfWeek) return false;
        if (!openingTime.equals(that.openingTime)) return false;
        return closingTime.equals(that.closingTime);
    }

    @Override
    public int hashCode() {
        int result = dayOfWeek.hashCode();
        result = 31 * result + openingTime.hashCode();
        result = 31 * result + closingTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return dayOfWeek + ": " + openingTime + " - " + closingTime;
    }
}