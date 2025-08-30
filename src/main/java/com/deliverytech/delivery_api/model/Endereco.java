package com.deliverytech.delivery_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    @JsonProperty("rua")
    @Column(name = "logradouro", length = 100)
    private String rua;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "bairro", length = 50)
    private String bairro;

    @Column(name = "cidade", length = 50)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "complemento", length = 100)
    private String complemento;
}
