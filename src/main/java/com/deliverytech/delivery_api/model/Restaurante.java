package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(length = 255)
    private String endereco;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal taxaEntrega;
    
    @Column(length = 15)
    private String telefone;
    
    @Column(unique = true, length = 100)
    private String email;
    
    private Integer tempoEntregaMinutos;
    
    private boolean ativo;
    
    @DecimalMin(value = "0.0", message = "Avaliação deve ser positiva")
    @DecimalMax(value = "5.0", message = "Avaliação máxima é 5.0")
    @Column(precision = 3, scale = 1)
    private BigDecimal avaliacao;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean excluido = false;
}
