package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
public class ClienteRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min=10, max = 100, message = "Não pode conter menos de 10 ou mais de 100 caracteres")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Email
    @Size(max = 255, message = "Email não pode ter mais de 255 caracteres")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço não pode ter mais de 255 caracteres")
    private String endereco;

    public ClienteRequest() {}

    public ClienteRequest(String nome, String telefone, String email, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
