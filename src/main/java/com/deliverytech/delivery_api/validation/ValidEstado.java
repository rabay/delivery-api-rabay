package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar códigos de estado brasileiros.
 *
 * <p>Aceita apenas códigos UF válidos: AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB,
 * PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO
 *
 * <p>Exemplo de uso: @ValidEstado private String estado;
 */
@Documented
@Constraint(validatedBy = EstadoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEstado {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Estado deve ser um código UF válido do Brasil";

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
