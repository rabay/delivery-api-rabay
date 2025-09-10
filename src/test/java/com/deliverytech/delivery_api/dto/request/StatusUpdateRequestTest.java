package com.deliverytech.delivery_api.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StatusUpdateRequest Validation Tests")
class StatusUpdateRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  @DisplayName("Cenários de Sucesso")
  class SuccessScenarios {

    @Test
    @DisplayName("Deve aceitar status válido")
    void shouldAcceptValidStatus() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus("ENTREGUE");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar todos os status válidos do enum StatusPedido")
    void shouldAcceptAllValidStatusValues() {
      // Given
      String[] validStatuses = {
        "CRIADO", "CONFIRMADO", "EM_PREPARACAO", "PRONTO", "EM_ENTREGA", "ENTREGUE", "CANCELADO"
      };

      for (String status : validStatuses) {
        StatusUpdateRequest request = new StatusUpdateRequest();
        request.setStatus(status);

        // When
        Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).withFailMessage("Status '%s' deveria ser válido", status).isEmpty();
      }
    }

    @Test
    @DisplayName("Deve aceitar status com caracteres especiais")
    void shouldAcceptStatusWithSpecialCharacters() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus("EM_PREPARACAO");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Status")
  class StatusValidationTests {

    @Test
    @DisplayName("Deve rejeitar status nulo")
    void shouldRejectNullStatus() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus(null);

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Status é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("status");
    }

    @Test
    @DisplayName("Deve rejeitar status vazio")
    void shouldRejectEmptyStatus() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus("");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Status é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("status");
    }

    @Test
    @DisplayName("Deve rejeitar status com apenas espaços")
    void shouldRejectBlankStatus() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus("   ");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Status é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("status");
    }

    @Test
    @DisplayName("Deve aceitar status com exatamente 50 caracteres")
    void shouldAcceptStatusWithMaximumLength() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      String status50Chars = "A".repeat(50);
      request.setStatus(status50Chars);

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar status com mais de 50 caracteres")
    void shouldAcceptStatusWithMoreThan50Characters() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      String status60Chars = "A".repeat(60);
      request.setStatus(status60Chars);

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Cenários com Múltiplas Validações")
  class MultipleValidationScenarios {

    @Test
    @DisplayName("Deve aceitar StatusUpdateRequest completamente válido")
    void shouldAcceptCompleteValidStatusUpdateRequest() {
      // Given
      StatusUpdateRequest request = new StatusUpdateRequest();
      request.setStatus("CONFIRMADO");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve validar corretamente múltiplas instâncias")
    void shouldValidateMultipleInstancesCorrectly() {
      // Given
      StatusUpdateRequest validRequest = new StatusUpdateRequest("ENTREGUE");
      StatusUpdateRequest invalidRequest = new StatusUpdateRequest("");

      // When
      Set<ConstraintViolation<StatusUpdateRequest>> validViolations =
          validator.validate(validRequest);
      Set<ConstraintViolation<StatusUpdateRequest>> invalidViolations =
          validator.validate(invalidRequest);

      // Then
      assertThat(validViolations).isEmpty();
      assertThat(invalidViolations).hasSize(1);
    }
  }
}
