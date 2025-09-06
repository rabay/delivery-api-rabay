package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar endereços de email.
 *
 * <p>Além da validação padrão de formato de email, verifica: - Comprimento máximo de 255 caracteres
 * - Domínios válidos - Formato RFC 5322 compliant
 *
 * <p>Exemplo de uso: @ValidEmail private String email;
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Email deve ter um formato válido e não exceder 255 caracteres";

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
