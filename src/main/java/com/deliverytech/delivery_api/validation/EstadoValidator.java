package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementa a interface para validar a anotação @ValidEstado Valida strings que representam
 * códigos de estado brasileiros (UF)
 */
public class EstadoValidator implements ConstraintValidator<ValidEstado, String> {

  // Set com todos os códigos UF válidos do Brasil
  private static final Set<String> VALID_ESTADOS =
      new HashSet<>(
          Arrays.asList(
              "AC", // Acre
              "AL", // Alagoas
              "AP", // Amapá
              "AM", // Amazonas
              "BA", // Bahia
              "CE", // Ceará
              "DF", // Distrito Federal
              "ES", // Espírito Santo
              "GO", // Goiás
              "MA", // Maranhão
              "MT", // Mato Grosso
              "MS", // Mato Grosso do Sul
              "MG", // Minas Gerais
              "PA", // Pará
              "PB", // Paraíba
              "PR", // Paraná
              "PE", // Pernambuco
              "PI", // Piauí
              "RJ", // Rio de Janeiro
              "RN", // Rio Grande do Norte
              "RS", // Rio Grande do Sul
              "RO", // Rondônia
              "RR", // Roraima
              "SC", // Santa Catarina
              "SP", // São Paulo
              "SE", // Sergipe
              "TO" // Tocantins
              ));

  /** Método que contém a regra de validação 'value' é o valor do campo anotado com @ValidEstado */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Se o valor for nulo ou vazio, considera válido
    // Porque a anotação @NotNull deve ser usada para validar presença
    if (value == null || value.isEmpty()) {
      return true;
    }

    // Remove espaços em branco e converte para maiúsculo
    String cleanValue = value.trim().toUpperCase();

    // Verifica se tem exatamente 2 caracteres após trim
    if (cleanValue.length() != 2) {
      return false;
    }

    // Verifica se contém apenas letras (sem espaços no meio)
    if (!cleanValue.matches("[A-Z]{2}")) {
      return false;
    }

    // Verifica se é um estado válido
    return VALID_ESTADOS.contains(cleanValue);
  }
}
