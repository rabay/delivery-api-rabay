package com.deliverytech.deliveryapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Address {

    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-\\d{3}$");

    @Column(name = "address_street", nullable = false)
    private String street;

    @Column(name = "address_number", nullable = false)
    private String number;

    @Column(name = "address_complement")
    private String complement;

    @Column(name = "address_neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "address_city", nullable = false)
    private String city;

    @Column(name = "address_state", nullable = false)
    private String state;

    @Column(name = "address_postal_code", nullable = false)
    private String postalCode; // CEP

    @Column(name = "address_reference")
    private String reference;

    // Construtores
    public Address() {
    }

    public Address(String street, String number, String complement, String neighborhood,
                  String city, String state, String postalCode, String reference) {
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        setPostalCode(postalCode);
        this.reference = reference;
    }

    // Validação de CEP
    public void setPostalCode(String postalCode) {
        if (postalCode != null && !postalCode.isEmpty() && !CEP_PATTERN.matcher(postalCode).matches()) {
            throw new IllegalArgumentException("CEP inválido. Formato esperado: 00000-000");
        }
        this.postalCode = postalCode;
    }

    // Getters e Setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
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

    public String getPostalCode() {
        return postalCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    // Métodos de Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(number, address.number) &&
                Objects.equals(neighborhood, address.neighborhood) &&
                Objects.equals(city, address.city) &&
                Objects.equals(state, address.state) &&
                Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, neighborhood, city, state, postalCode);
    }

    @Override
    public String toString() {
        return street + ", " + number +
                (complement != null && !complement.isEmpty() ? ", " + complement : "") +
                " - " + neighborhood +
                " - " + city +
                "/" + state +
                " - " + postalCode;
    }
}