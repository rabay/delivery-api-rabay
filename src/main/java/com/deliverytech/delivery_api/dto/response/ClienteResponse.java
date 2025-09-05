package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta para cliente.", title = "Cliente Response DTO")
public class ClienteResponse {
    @Schema(description = "ID único do cliente.", example = "1")
    private Long id;
    
    @Schema(description = "Nome completo do cliente.", example = "João Silva")
    private String nome;
    
    @Schema(description = "Email do cliente.", example = "joao.silva@example.com")
    private String email;
    
    @Schema(description = "Telefone do cliente.", example = "11987654321")
    private String telefone;
    
    @Schema(description = "Endereço completo do cliente.", example = "Rua das Flores, 123 - São Paulo/SP")
    private String endereco;
    
    @Schema(description = "Indica se o cliente está ativo.", example = "true")
    private boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}