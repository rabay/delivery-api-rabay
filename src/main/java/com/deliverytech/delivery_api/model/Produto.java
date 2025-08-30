package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 50, message = "Categoria não pode ter mais de 50 caracteres")
    @Column(length = 50)
    private String categoria;

    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    @Builder.Default
    private Boolean disponivel = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean excluido = false;

    @NotNull(message = "Restaurante é obrigatório")
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
}
