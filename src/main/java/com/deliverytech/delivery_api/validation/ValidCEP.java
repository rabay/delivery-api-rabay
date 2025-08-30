package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar CEPs (Códigos de Endereçamento Postal) brasileiros.
 *
 * <p>Aceita formatos: - 00000-000 (com hífen) - 00000000 (sem hífen)
 *
 * <p>Exemplo de uso: @ValidCEP private String cep;
 */
@Documented
@Constraint(validatedBy = CEPValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCEP {

    /** Mensagem de erro padrão exibida quando a validação falha */
    String message() default "CEP deve ter o formato 00000-000 ou 00000000";

    /** Grupos de validação - permite agrupar validações */
    Class<?>[] groups() default {};

    /** Payload - permite anexar metadados à validação */
    Class<? extends Payload>[] payload() default {};
}
