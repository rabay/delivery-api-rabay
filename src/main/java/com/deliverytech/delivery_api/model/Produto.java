package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private boolean disponivel;
    @ManyToOne
    private Restaurante restaurante;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
}
