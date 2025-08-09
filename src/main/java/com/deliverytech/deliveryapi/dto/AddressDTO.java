package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.Address;

/**
 * DTO para representação de endereço nas respostas da API
 */
public record AddressDTO(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        String reference
) {
    /**
     * Converte uma entidade Address para AddressDTO
     */
    public static AddressDTO from(Address address) {
        if (address == null) {
            return null;
        }
        
        return new AddressDTO(
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getReference()
        );
    }
}
