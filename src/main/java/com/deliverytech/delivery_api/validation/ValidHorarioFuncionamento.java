package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar horários de funcionamento no formato HH:MM-HH:MM.
 *
 * <p>Exemplo de uso: @ValidHorarioFuncionamento private String horarioFuncionamento;
 */
@Documented
@Constraint(validatedBy = HorarioFuncionamentoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHorarioFuncionamento {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Horário de funcionamento deve ter o formato HH:MM-HH:MM";

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
