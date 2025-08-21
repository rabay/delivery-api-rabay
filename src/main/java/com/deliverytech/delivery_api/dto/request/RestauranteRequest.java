package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class RestauranteRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @NotBlank
    private String endereco;

    @DecimalMin("0.0")
    @NotNull(message = "Taxa de entrega é obrigatória")
    @Positive(message = "Taxa de entrega deve ser positiva")
    private BigDecimal taxaEntrega;

    @NotNull
    @Min(1)
    private Integer tempoEntregaMinutos;

    private String telefone;
    private String email;

    @Positive(message = "Avaliação deve ser positiva")
    @DecimalMax(value = "5.0", message = "Avaliação máxima é 5.0")
    private BigDecimal avaliacao;

    public RestauranteRequest() {}

    public RestauranteRequest(String nome, String categoria, String endereco, BigDecimal taxaEntrega, Integer tempoEntregaMinutos, String telefone, String email, BigDecimal avaliacao) {
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.telefone = telefone;
        this.email = email;
        this.avaliacao = avaliacao;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public BigDecimal getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(BigDecimal taxaEntrega) { this.taxaEntrega = taxaEntrega; }

    public Integer getTempoEntregaMinutos() { return tempoEntregaMinutos; }
    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) { this.tempoEntregaMinutos = tempoEntregaMinutos; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getAvaliacao() { return avaliacao; }
    public void setAvaliacao(BigDecimal avaliacao) { this.avaliacao = avaliacao; }
}
