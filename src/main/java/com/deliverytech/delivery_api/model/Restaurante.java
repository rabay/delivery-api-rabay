package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private BigDecimal taxaEntrega;
    private String telefone;
    private String email;
    private Integer tempoEntregaMinutos;
    private boolean ativo;
    private BigDecimal avaliacao;
    private Boolean excluido = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public BigDecimal getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(BigDecimal taxaEntrega) { this.taxaEntrega = taxaEntrega; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getTempoEntregaMinutos() { return tempoEntregaMinutos; }
    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) { this.tempoEntregaMinutos = tempoEntregaMinutos; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public BigDecimal getAvaliacao() { return avaliacao; }
    public void setAvaliacao(BigDecimal avaliacao) { this.avaliacao = avaliacao; }
    public Boolean getExcluido() { return excluido; }
    public void setExcluido(Boolean excluido) { this.excluido = excluido; }
}
