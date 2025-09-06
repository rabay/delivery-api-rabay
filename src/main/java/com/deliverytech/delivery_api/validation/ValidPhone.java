package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar números de telefone brasileiros.
 *
 * <p>Aceita formatos: - (11) 99999-9999 (celular com DDD) - (11) 9999-9999 (fixo com DDD) -
 * 11999999999 (celular sem formatação) - 1199999999 (fixo sem formatação) - +5511999999999 (com
 * código do país)
 *
 * <p>Exemplo de uso: @ValidPhone private String telefone;
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Telefone deve ter um formato válido brasileiro";

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
