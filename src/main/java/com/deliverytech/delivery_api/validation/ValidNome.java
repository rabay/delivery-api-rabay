package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar nomes próprios.
 *
 * <p>Verifica: - Não pode conter números - Não pode conter caracteres especiais (exceto acentos,
 * hífens e apostrofes) - Não pode ter espaços consecutivos - Não pode começar ou terminar com
 * espaço - Comprimento entre 2 e 100 caracteres
 *
 * <p>Exemplo de uso: @ValidNome private String nome;
 */
@Documented
@Constraint(validatedBy = NomeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNome {

    /** Mensagem de erro padrão exibida quando a validação falha */
    String message() default "Nome deve conter apenas letras, acentos, hífens e apostrofes";

    /** Comprimento mínimo do nome */
    int min() default 2;

    /** Comprimento máximo do nome */
    int max() default 100;

    /** Grupos de validação - permite agrupar validações */
    Class<?>[] groups() default {};

    /** Payload - permite anexar metadados à validação */
    Class<? extends Payload>[] payload() default {};
}
