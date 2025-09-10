package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Implementa a interface para validar a anotação @ValidHorarioFuncionamento Valida strings que
 * representam horários de funcionamento no formato HH:MM-HH:MM
 */
public class HorarioFuncionamentoValidator
    implements ConstraintValidator<ValidHorarioFuncionamento, String> {

  // Padrão regex para formato HH:MM-HH:MM (exige duas casas para horas, 00-23)
  private static final Pattern HORARIO_PATTERN =
      Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d-(?:[01]\\d|2[0-3]):[0-5]\\d$");

  /**
   * Método que contém a regra de validação 'value' é o valor do campo anotado
   * com @ValidHorarioFuncionamento
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Se o valor for nulo ou vazio, considera válido
    // Porque a anotação @NotNull deve ser usada para validar presença
    if (value == null || value.trim().isEmpty()) {
      return true;
    }

    // Valida o formato HH:MM-HH:MM
    if (!HORARIO_PATTERN.matcher(value).matches()) {
      return false;
    }

    // Extrai os horários de início e fim
    String[] partes = value.split("-");
    String inicio = partes[0];
    String fim = partes[1];

    // Converte para minutos para comparar
    int minutosInicio = converterParaMinutos(inicio);
    int minutosFim = converterParaMinutos(fim);

    // Verifica se o horário de fim é maior que o de início
    return minutosFim > minutosInicio;
  }

  /** Converte horário HH:MM para minutos totais do dia */
  private int converterParaMinutos(String horario) {
    String[] partes = horario.split(":");
    int horas = Integer.parseInt(partes[0]);
    int minutos = Integer.parseInt(partes[1]);
    return horas * 60 + minutos;
  }
}
