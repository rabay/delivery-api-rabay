package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementa a interface para validar a anotação @ValidAvaliacao Valida valores BigDecimal que
 * representam avaliações/ratings
 */
public class AvaliacaoValidator implements ConstraintValidator<ValidAvaliacao, BigDecimal> {

    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal increment;

    /** Inicializa o validador com os parâmetros da anotação */
    @Override
    public void initialize(ValidAvaliacao constraintAnnotation) {
        this.minValue = BigDecimal.valueOf(constraintAnnotation.min());
        this.maxValue = BigDecimal.valueOf(constraintAnnotation.max());
        this.increment = BigDecimal.valueOf(constraintAnnotation.increment());
    }

    /**
     * Método que contém a regra de validação 'value' é o valor do campo anotado com @ValidAvaliacao
     */
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {

        // Se o valor for nulo, considera válido
        // Porque a anotação @NotNull deve ser usada para validar presença
        if (value == null) {
            return true;
        }

        // Verifica se está dentro do range mínimo e máximo
        if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            return false;
        }

        // Verifica se tem no máximo 1 casa decimal
        if (value.scale() > 1) {
            return false;
        }

        // Arredonda para 1 casa decimal para comparação
        BigDecimal rounded = value.setScale(1, RoundingMode.HALF_UP);

        // Verifica se o valor é exatamente igual ao arredondado
        if (value.compareTo(rounded) != 0) {
            return false;
        }

        // Verifica se é um múltiplo válido do incremento
        // Ex: se increment = 0.5, valores válidos são 1.0, 1.5, 2.0, 2.5, etc.
        BigDecimal difference = rounded.subtract(minValue);
        BigDecimal remainder = difference.remainder(increment);

        // O resto da divisão deve ser zero (múltiplo exato)
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            return false;
        }

        // Validações específicas para sistema de avaliação
        return isValidRatingValue(rounded);
    }

    /** Verifica se o valor é uma avaliação válida no contexto de sistema de rating */
    private boolean isValidRatingValue(BigDecimal rating) {
        // Para flexibilidade, aceita qualquer valor que passed as outras validações
        // As regras de negócio específicas podem ser implementadas nas anotações
        return true;
    }
}
