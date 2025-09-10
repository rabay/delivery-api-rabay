package com.deliverytech.delivery_api.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tempo Entrega Validator Tests")
class TempoEntregaValidatorTest {

  private TempoEntregaValidator validator;

  @Mock private ValidTempoEntrega constraintAnnotation;

  @Mock private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    validator = new TempoEntregaValidator();

    // Configuração padrão da anotação
    when(constraintAnnotation.min()).thenReturn(5);
    when(constraintAnnotation.max()).thenReturn(240);
    when(constraintAnnotation.interval()).thenReturn(5);

    validator.initialize(constraintAnnotation);
  }

  @Test
  @DisplayName("Deve aceitar tempos válidos múltiplos de 5")
  void deveAceitarTemposValidosMultiplosDe5() {
    assertTrue(validator.isValid(5, context));
    assertTrue(validator.isValid(10, context));
    assertTrue(validator.isValid(15, context));
    assertTrue(validator.isValid(20, context));
    assertTrue(validator.isValid(25, context));
    assertTrue(validator.isValid(30, context));
    assertTrue(validator.isValid(45, context));
    assertTrue(validator.isValid(60, context));
    assertTrue(validator.isValid(90, context));
    assertTrue(validator.isValid(120, context));
  }

  @Test
  @DisplayName("Deve aceitar valores nulos")
  void deveAceitarValoresNulos() {
    assertTrue(validator.isValid(null, context));
  }

  @Test
  @DisplayName("Deve aceitar tempos no limite mínimo e máximo")
  void deveAceitarTemposNoLimiteMinimoEMaximo() {
    assertTrue(validator.isValid(5, context)); // Mínimo
    assertTrue(validator.isValid(240, context)); // Máximo
  }

  @Test
  @DisplayName("Deve rejeitar tempos abaixo do mínimo")
  void deveRejeitarTemposAbaixoDoMinimo() {
    assertFalse(validator.isValid(0, context));
    assertFalse(validator.isValid(1, context));
    assertFalse(validator.isValid(4, context));
  }

  @Test
  @DisplayName("Deve rejeitar tempos acima do máximo")
  void deveRejeitarTemposAcimaDoMaximo() {
    assertFalse(validator.isValid(241, context));
    assertFalse(validator.isValid(300, context));
    assertFalse(validator.isValid(500, context));
  }

  @Test
  @DisplayName("Deve rejeitar tempos que não são múltiplos do intervalo")
  void deveRejeitarTemposQueNaoSaoMultiplosDoIntervalo() {
    assertFalse(validator.isValid(7, context)); // Não é múltiplo de 5
    assertFalse(validator.isValid(13, context)); // Não é múltiplo de 5
    assertFalse(validator.isValid(27, context)); // Não é múltiplo de 5
    assertFalse(validator.isValid(33, context)); // Não é múltiplo de 5
    assertFalse(validator.isValid(42, context)); // Não é múltiplo de 5
  }

  @Test
  @DisplayName("Deve validar tempos de entrega realistas para delivery")
  void deveValidarTemposDeEntregaRealistasParaDelivery() {
    // Tempos muito rápidos (delivery próximo)
    assertTrue(validator.isValid(10, context));
    assertTrue(validator.isValid(15, context));

    // Tempos normais
    assertTrue(validator.isValid(30, context));
    assertTrue(validator.isValid(45, context));
    assertTrue(validator.isValid(60, context));

    // Tempos longos mas realistas
    assertTrue(validator.isValid(90, context));
    assertTrue(validator.isValid(120, context));

    // Tempos muito longos para casos especiais
    assertTrue(validator.isValid(180, context)); // 3 horas
    assertTrue(validator.isValid(240, context)); // 4 horas
  }

  @Test
  @DisplayName("Deve validar tempos de 15 em 15 para períodos longos")
  void deveValidarTemposDe15Em15ParaPeriodosLongos() {
    // Para tempos muito longos, aceita múltiplos de 15
    assertTrue(validator.isValid(135, context)); // 2h15min
    assertTrue(validator.isValid(150, context)); // 2h30min
    assertTrue(validator.isValid(165, context)); // 2h45min
    assertTrue(validator.isValid(180, context)); // 3h
    assertTrue(validator.isValid(195, context)); // 3h15min
    assertTrue(validator.isValid(210, context)); // 3h30min
    assertTrue(validator.isValid(225, context)); // 3h45min
    assertTrue(validator.isValid(240, context)); // 4h
  }

  @Test
  @DisplayName("Deve validar tempos de 30 em 30 para períodos muito longos")
  void deveValidarTemposDe30Em30ParaPeriodosMuitoLongos() {
    assertTrue(validator.isValid(150, context)); // 2h30min
    assertTrue(validator.isValid(180, context)); // 3h
    assertTrue(validator.isValid(210, context)); // 3h30min
    assertTrue(validator.isValid(240, context)); // 4h
  }

  @Test
  @DisplayName("Deve validar configuração personalizada")
  void deveValidarConfiguracaoPersonalizada() {
    // Reconfigura para um range e intervalo diferentes
    when(constraintAnnotation.min()).thenReturn(10);
    when(constraintAnnotation.max()).thenReturn(60);
    when(constraintAnnotation.interval()).thenReturn(10);

    validator.initialize(constraintAnnotation);

    // Valores dentro do novo range e intervalo
    assertTrue(validator.isValid(10, context));
    assertTrue(validator.isValid(20, context));
    assertTrue(validator.isValid(30, context));
    assertTrue(validator.isValid(40, context));
    assertTrue(validator.isValid(50, context));
    assertTrue(validator.isValid(60, context));

    // Valores fora do novo range
    assertFalse(validator.isValid(5, context)); // Abaixo do mínimo
    assertFalse(validator.isValid(70, context)); // Acima do máximo

    // Valores que não são múltiplos do novo intervalo
    assertFalse(validator.isValid(15, context)); // Não é múltiplo de 10
    assertFalse(validator.isValid(25, context)); // Não é múltiplo de 10
    assertFalse(validator.isValid(35, context)); // Não é múltiplo de 10
  }

  @Test
  @DisplayName("Deve aceitar intervalo zero (sem restrição de múltiplo)")
  void deveAceitarIntervaloZero() {
    when(constraintAnnotation.interval()).thenReturn(0);
    validator.initialize(constraintAnnotation);

    // Com intervalo 0, deve aceitar qualquer valor no range
    assertTrue(validator.isValid(7, context));
    assertTrue(validator.isValid(13, context));
    assertTrue(validator.isValid(27, context));
    assertTrue(validator.isValid(33, context));
    assertTrue(validator.isValid(42, context));
  }

  @Test
  @DisplayName("Deve validar tempos típicos de diferentes tipos de estabelecimento")
  void deveValidarTemposTipicosDeDelivery() {
    // Fast food - rápido
    assertTrue(validator.isValid(15, context));
    assertTrue(validator.isValid(20, context));
    assertTrue(validator.isValid(25, context));

    // Restaurante - normal
    assertTrue(validator.isValid(30, context));
    assertTrue(validator.isValid(45, context));
    assertTrue(validator.isValid(60, context));

    // Comida especial - demorado
    assertTrue(validator.isValid(75, context));
    assertTrue(validator.isValid(90, context));
    assertTrue(validator.isValid(120, context));

    // Casos especiais - muito demorado
    assertTrue(validator.isValid(150, context));
    assertTrue(validator.isValid(180, context));
    assertTrue(validator.isValid(240, context));
  }

  @Test
  @DisplayName("Deve rejeitar tempos negativos")
  void deveRejeitarTemposNegativos() {
    assertFalse(validator.isValid(-1, context));
    assertFalse(validator.isValid(-5, context));
    assertFalse(validator.isValid(-10, context));
  }

  @Test
  @DisplayName("Deve validar horário de pico vs horário normal")
  void deveValidarHorarioDePicoVsHorarioNormal() {
    // Horário normal - tempos menores
    assertTrue(validator.isValid(20, context));
    assertTrue(validator.isValid(30, context));
    assertTrue(validator.isValid(45, context));

    // Horário de pico - tempos maiores
    assertTrue(validator.isValid(60, context));
    assertTrue(validator.isValid(75, context));
    assertTrue(validator.isValid(90, context));
    assertTrue(validator.isValid(120, context));
  }
}
