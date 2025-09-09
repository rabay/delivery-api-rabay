package com.deliverytech.delivery_api.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HorarioFuncionamentoValidatorTest {

  private static ValidatorFactory factory;
  private static Validator validator;

  @BeforeAll
  static void setup() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @AfterAll
  static void tearDown() {
    if (factory != null) {
      factory.close();
    }
  }

  static class TestBean {
    @ValidHorarioFuncionamento
    String horario;

    TestBean(String horario) {
      this.horario = horario;
    }
  }

  @Test
  void validHorario_noViolations() {
    TestBean b = new TestBean("08:00-18:00");
    var violations = validator.validate(b);
    assertTrue(violations.isEmpty(), "Expected no violations for valid horario");
  }

  @Test
  void nullHorario_isValid() {
    TestBean b = new TestBean(null);
    assertTrue(validator.validate(b).isEmpty());
  }

  @Test
  void emptyHorario_isValid() {
    TestBean b = new TestBean("");
    assertTrue(validator.validate(b).isEmpty());
  }

  @Test
  void invalidFormat_violation() {
    TestBean b = new TestBean("8:00-18:00");
    assertFalse(validator.validate(b).isEmpty());
  }

  @Test
  void invalidRange_endBeforeStart_violation() {
    TestBean b = new TestBean("18:00-08:00");
    assertFalse(validator.validate(b).isEmpty());
  }

  @Test
  void sameStartEnd_violation() {
    TestBean b = new TestBean("09:00-09:00");
    assertFalse(validator.validate(b).isEmpty());
  }

  @Test
  void invalidHours_violation() {
    TestBean b = new TestBean("25:00-26:00");
    assertFalse(validator.validate(b).isEmpty());
  }
}
