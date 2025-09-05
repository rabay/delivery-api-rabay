package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta resumida para cliente.", title = "Cliente Resumo Response DTO")
public class ClienteResumoResponse {
    @Schema(description = "ID único do cliente.", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente.", example = "João Silva")
    private String nome;

    public ClienteResumoResponse() {}

    public ClienteResumoResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

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
}