package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar tempo de entrega em minutos.
 *
 * <p>Verifica: - Tempo mínimo: 5 minutos - Tempo máximo: 240 minutos (4 horas) - Deve ser múltiplo
 * de 5 minutos
 *
 * <p>Exemplo de uso: @ValidTempoEntrega private Integer tempoEntregaMinutos;
 */
@Documented
@Constraint(validatedBy = TempoEntregaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTempoEntrega {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Tempo de entrega deve estar entre 5 e 240 minutos e ser múltiplo de 5";

  /** Tempo mínimo em minutos */
  int min() default 5;

  /** Tempo máximo em minutos */
  int max() default 240;

  /** Intervalo obrigatório (deve ser múltiplo deste valor) */
  int interval() default 5;

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
