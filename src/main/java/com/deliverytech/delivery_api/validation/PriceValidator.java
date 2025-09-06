package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementa a interface para validar a anotação @ValidPrice Valida valores monetários (BigDecimal)
 * para preços
 */
public class PriceValidator implements ConstraintValidator<ValidPrice, BigDecimal> {

  private BigDecimal minValue;
  private BigDecimal maxValue;

  /** Inicializa o validador com os parâmetros da anotação */
  @Override
  public void initialize(ValidPrice constraintAnnotation) {
    // Converte valores de centavos para reais
    this.minValue = BigDecimal.valueOf(constraintAnnotation.min()).divide(BigDecimal.valueOf(100));
    this.maxValue = BigDecimal.valueOf(constraintAnnotation.max()).divide(BigDecimal.valueOf(100));
  }

  /** Método que contém a regra de validação 'value' é o valor do campo anotado com @ValidPrice */
  @Override
  public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {

    // Se o valor for nulo, considera válido
    // Porque a anotação @NotNull deve ser usada para validar presença
    if (value == null) {
      return true;
    }

    // Verifica se o valor é negativo
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      return false;
    }

    // Verifica se está dentro do range permitido
    if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
      return false;
    }

    // Verifica se tem no máximo 2 casas decimais
    if (value.scale() > 2) {
      return false;
    }

    // Se tem casas decimais, verifica se são válidas (não há fração menor que centavo)
    if (value.scale() > 0) {
      BigDecimal rounded = value.setScale(2, RoundingMode.HALF_UP);
      if (value.compareTo(rounded) != 0) {
        return false;
      }
    }

    return true;
  }
}
