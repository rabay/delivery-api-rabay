package com.deliverytech.delivery_api.validation;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CEP Validator Tests")
class CEPValidatorTest {

  private CEPValidator validator;

  @Mock private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    validator = new CEPValidator();
  }

  @Test
  @DisplayName("Deve aceitar CEP no formato 00000-000")
  void deveAceitarCEPComHifen() {
    assertTrue(validator.isValid("12345-678", context));
    assertTrue(validator.isValid("01234-567", context));
    assertTrue(validator.isValid("98765-432", context));
  }

  @Test
  @DisplayName("Deve aceitar CEP no formato 00000000")
  void deveAceitarCEPSemHifen() {
    assertTrue(validator.isValid("12345678", context));
    assertTrue(validator.isValid("01234567", context));
    assertTrue(validator.isValid("98765432", context));
  }

  @Test
  @DisplayName("Deve aceitar valores nulos")
  void deveAceitarValoresNulos() {
    assertTrue(validator.isValid(null, context));
  }

  @Test
  @DisplayName("Deve aceitar strings vazias")
  void deveAceitarStringsVazias() {
    assertTrue(validator.isValid("", context));
    assertTrue(validator.isValid("   ", context));
  }

  @Test
  @DisplayName("Deve rejeitar CEP com formato inválido")
  void deveRejeitarCEPComFormatoInvalido() {
    // Muito poucos dígitos
    assertFalse(validator.isValid("1234-567", context));
    assertFalse(validator.isValid("1234567", context));

    // Muitos dígitos
    assertFalse(validator.isValid("123456-789", context));
    assertFalse(validator.isValid("123456789", context));

    // Hífen na posição errada
    assertFalse(validator.isValid("1234-5678", context));
    assertFalse(validator.isValid("123456-78", context));

    // Múltiplos hífens
    assertFalse(validator.isValid("12-345-678", context));
    assertFalse(validator.isValid("12345--678", context));
  }

  @Test
  @DisplayName("Deve rejeitar CEP com caracteres não numéricos")
  void deveRejeitarCEPComCaracteresNaoNumericos() {
    assertFalse(validator.isValid("1234A-678", context));
    assertFalse(validator.isValid("ABCDE-FGH", context));
    assertFalse(validator.isValid("12345.678", context));
    assertFalse(validator.isValid("12345 678", context));
    assertFalse(validator.isValid("12345/678", context));
  }

  @Test
  @DisplayName("Deve rejeitar CEP com espaços extras")
  void deveRejeitarCEPComEspacosExtras() {
    assertFalse(validator.isValid(" 12345-678", context));
    assertFalse(validator.isValid("12345-678 ", context));
    assertFalse(validator.isValid("12 345-678", context));
    assertFalse(validator.isValid("12345- 678", context));
    assertFalse(validator.isValid("12345 -678", context));
  }

  @Test
  @DisplayName("Deve validar CEPs reais brasileiros")
  void deveValidarCEPsReaisBrasileiros() {
    // CEPs reais de capitais brasileiras
    assertTrue(validator.isValid("01310-100", context)); // São Paulo - SP
    assertTrue(validator.isValid("20040-020", context)); // Rio de Janeiro - RJ
    assertTrue(validator.isValid("70040-010", context)); // Brasília - DF
    assertTrue(validator.isValid("30112-000", context)); // Belo Horizonte - MG
    assertTrue(validator.isValid("80060-000", context)); // Curitiba - PR
    assertTrue(validator.isValid("90010-150", context)); // Porto Alegre - RS

    // Mesmos CEPs sem hífen
    assertTrue(validator.isValid("01310100", context));
    assertTrue(validator.isValid("20040020", context));
    assertTrue(validator.isValid("70040010", context));
    assertTrue(validator.isValid("30112000", context));
    assertTrue(validator.isValid("80060000", context));
    assertTrue(validator.isValid("90010150", context));
  }
}
