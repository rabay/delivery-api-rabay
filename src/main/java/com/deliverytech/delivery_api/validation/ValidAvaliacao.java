package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar avaliações/ratings.
 * 
 * Verifica:
 * - Valor entre 1.0 e 5.0
 * - Máximo de 1 casa decimal
 * - Incrementos de 0.5 (1.0, 1.5, 2.0, 2.5, 3.0, etc.)
 * 
 * Exemplo de uso:
 * @ValidAvaliacao
 * private BigDecimal avaliacao;
 */
@Documented
@Constraint(validatedBy = AvaliacaoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAvaliacao {
    
    /**
     * Mensagem de erro padrão exibida quando a validação falha
     */
    String message() default "Avaliação deve estar entre 1.0 e 5.0 com incrementos de 0.5";
    
    /**
     * Valor mínimo da avaliação
     */
    double min() default 1.0;
    
    /**
     * Valor máximo da avaliação
     */
    double max() default 5.0;
    
    /**
     * Incremento permitido
     */
    double increment() default 0.5;
    
    /**
     * Grupos de validação - permite agrupar validações
     */
    Class<?>[] groups() default {};
    
    /**
     * Payload - permite anexar metadados à validação
     */
    Class<? extends Payload>[] payload() default {};
}