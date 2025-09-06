package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar valores monetários.
 *
 * <p>Verifica: - Valor não pode ser negativo - Valor mínimo de R$ 0,01 - Valor máximo de R$
 * 999.999,99 - Máximo de 2 casas decimais
 *
 * <p>Exemplo de uso: @ValidPrice private BigDecimal preco;
 */
@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {

  /** Mensagem de erro padrão exibida quando a validação falha */
  String message() default "Preço deve ser positivo, mínimo R$ 0,01 e máximo R$ 999.999,99";

  /** Valor mínimo permitido (em centavos) */
  long min() default 1; // R$ 0,01

  /** Valor máximo permitido (em centavos) */
  long max() default 99999999; // R$ 999.999,99

  /** Grupos de validação - permite agrupar validações */
  Class<?>[] groups() default {};

  /** Payload - permite anexar metadados à validação */
  Class<? extends Payload>[] payload() default {};
}
