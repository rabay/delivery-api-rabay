package com.deliverytech.delivery_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para criação/atualização de cliente.", title = "Cliente Request DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {
  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  @Schema(
      description = "Nome completo do cliente. Deve ter entre 2 e 100 caracteres.",
      example = "João Silva",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String nome;

  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
  @Schema(
      description = "Telefone do cliente. Deve ter 10 ou 11 dígitos numéricos.",
      example = "11987654321",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String telefone;

  @Email(message = "Email deve ter formato válido")
  @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
  @NotBlank(message = "Email é obrigatório")
  @Schema(
      description = "Email do cliente. Deve ser único e válido. Máximo de 100 caracteres.",
      example = "joao.silva@example.com",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @NotBlank(message = "Endereço é obrigatório")
  @Size(max = 255, message = "Endereço não pode ter mais de 255 caracteres")
  @Schema(
      description = "Endereço completo do cliente. Máximo de 255 caracteres.",
      example = "Rua das Flores, 123 - São Paulo/SP",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String endereco;

  @Schema(
      description = "Senha do cliente. Deve ser fornecida apenas na criação.",
      example = "senhaSegura123")
  private String senha;

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
