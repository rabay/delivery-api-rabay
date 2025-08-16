package com.deliverytech.deliveryapi.dto;

import com.deliverytech.deliveryapi.domain.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para representação de endereço nas respostas da API
 */
@Schema(description = "Dados de endereço")
public record AddressDTO(
        @Schema(description = "Nome da rua", example = "Rua das Flores")
        String street,
        @Schema(description = "Número do endereço", example = "123")
        String number,
        @Schema(description = "Complemento", example = "Apto 45")
        String complement,
        @Schema(description = "Bairro", example = "Centro")
        String neighborhood,
        @Schema(description = "Cidade", example = "São Paulo")
        String city,
        @Schema(description = "Estado", example = "SP")
        String state,
        @Schema(description = "CEP", example = "01234-567")
        String postalCode,
        @Schema(description = "Ponto de referência", example = "Próximo ao shopping")
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
