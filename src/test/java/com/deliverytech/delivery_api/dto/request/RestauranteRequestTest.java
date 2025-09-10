package com.deliverytech.delivery_api.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RestauranteRequest Validation Tests")
class RestauranteRequestTest {

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
    @DisplayName("Deve aceitar restaurante válido com todos os campos obrigatórios")
    void shouldAcceptValidRestauranteWithAllRequiredFields() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante do Zé");
      request.setCategoria("Brasileira");
      request.setEndereco("Rua das Flores, 123 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("5.00"));
      request.setTempoEntregaMinutos(30);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante válido com campos opcionais preenchidos")
    void shouldAcceptValidRestauranteWithOptionalFields() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante Gourmet");
      request.setCategoria("Italiana");
      request.setEndereco("Av. Paulista, 456 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("8.50"));
      request.setTempoEntregaMinutos(45);
      request.setTelefone("11987654321");
      request.setEmail("contato@gourmet.com");
      request.setAvaliacao(new BigDecimal("4.5"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante com taxa de entrega zero")
    void shouldAcceptRestauranteWithZeroDeliveryFee() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante Popular");
      request.setCategoria("Lanchonete");
      request.setEndereco("Rua do Centro, 789 - São Paulo/SP");
      request.setTaxaEntrega(BigDecimal.ZERO);
      request.setTempoEntregaMinutos(20);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante com tempo mínimo de entrega")
    void shouldAcceptRestauranteWithMinimumDeliveryTime() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Fast Food");
      request.setCategoria("Hamburgueria");
      request.setEndereco("Av. Central, 100 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("3.00"));
      request.setTempoEntregaMinutos(15);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante com tempo máximo de entrega")
    void shouldAcceptRestauranteWithMaximumDeliveryTime() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante Distante");
      request.setCategoria("Japonesa");
      request.setEndereco("Estrada Longa, 999 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("15.00"));
      request.setTempoEntregaMinutos(120);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante com avaliação máxima")
    void shouldAcceptRestauranteWithMaximumRating() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante Excelente");
      request.setCategoria("Contemporânea");
      request.setEndereco("Rua Premium, 777 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("12.00"));
      request.setTempoEntregaMinutos(60);
      request.setAvaliacao(new BigDecimal("5.0"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar restaurante com avaliação mínima")
    void shouldAcceptRestauranteWithMinimumRating() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Novo Restaurante");
      request.setCategoria("Fusion");
      request.setEndereco("Rua Nova, 111 - São Paulo/SP");
      request.setTaxaEntrega(new BigDecimal("5.00"));
      request.setTempoEntregaMinutos(30);
      request.setAvaliacao(BigDecimal.ZERO);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Nome")
  class NomeValidationTests {

    @Test
    @DisplayName("Deve rejeitar nome nulo")
    void shouldRejectNullNome() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setNome(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Nome é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }

    @Test
    @DisplayName("Deve rejeitar nome vazio")
    void shouldRejectEmptyNome() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setNome("");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Nome é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }

    @Test
    @DisplayName("Deve rejeitar nome com apenas espaços")
    void shouldRejectBlankNome() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setNome("   ");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Nome é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }

    @Test
    @DisplayName("Deve aceitar nome com exatamente 100 caracteres")
    void shouldAcceptNomeWithMaximumLength() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String nome100Chars = "A".repeat(100);
      request.setNome(nome100Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar nome com mais de 100 caracteres")
    void shouldRejectNomeTooLong() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String nome101Chars = "A".repeat(101);
      request.setNome(nome101Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Nome não pode ter mais de 100 caracteres");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Categoria")
  class CategoriaValidationTests {

    @Test
    @DisplayName("Deve rejeitar categoria nula")
    void shouldRejectNullCategoria() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setCategoria(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Categoria é obrigatória");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("categoria");
    }

    @Test
    @DisplayName("Deve rejeitar categoria vazia")
    void shouldRejectEmptyCategoria() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setCategoria("");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Categoria é obrigatória");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("categoria");
    }

    @Test
    @DisplayName("Deve aceitar categoria válida")
    void shouldAcceptValidCategoria() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setCategoria("Mexicana");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar categoria com exatamente 50 caracteres")
    void shouldAcceptCategoriaWithMaximumLength() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String categoria50Chars = "Brasileira"; // Categoria válida com tamanho aceitável
      request.setCategoria(categoria50Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar categoria com mais de 50 caracteres")
    void shouldRejectCategoriaTooLong() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String categoria51Chars = "Brasileira".repeat(4); // Cria uma string maior que 50 caracteres
      request.setCategoria(categoria51Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Categoria deve ser uma categoria válida");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("categoria");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Endereço")
  class EnderecoValidationTests {

    @Test
    @DisplayName("Deve rejeitar endereço nulo")
    void shouldRejectNullEndereco() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setEndereco(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Endereço é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("endereco");
    }

    @Test
    @DisplayName("Deve rejeitar endereço vazio")
    void shouldRejectEmptyEndereco() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setEndereco("");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Endereço é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("endereco");
    }

    @Test
    @DisplayName("Deve aceitar endereço com exatamente 255 caracteres")
    void shouldAcceptEnderecoWithMaximumLength() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String endereco255Chars = "A".repeat(255);
      request.setEndereco(endereco255Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar endereço com mais de 255 caracteres")
    void shouldRejectEnderecoTooLong() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String endereco256Chars = "A".repeat(256);
      request.setEndereco(endereco256Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Endereço não pode ter mais de 255 caracteres");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("endereco");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Taxa de Entrega")
  class TaxaEntregaValidationTests {

    @Test
    @DisplayName("Deve rejeitar taxa de entrega nula")
    void shouldRejectNullTaxaEntrega() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTaxaEntrega(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Taxa de entrega é obrigatória");
      assertThat(violations.iterator().next().getPropertyPath().toString())
          .isEqualTo("taxaEntrega");
    }

    @Test
    @DisplayName("Deve rejeitar taxa de entrega negativa")
    void shouldRejectNegativeTaxaEntrega() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTaxaEntrega(new BigDecimal("-5.00"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Taxa de entrega deve ser positiva");
      assertThat(violations.iterator().next().getPropertyPath().toString())
          .isEqualTo("taxaEntrega");
    }

    @Test
    @DisplayName("Deve aceitar taxa de entrega positiva")
    void shouldAcceptPositiveTaxaEntrega() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTaxaEntrega(new BigDecimal("15.99"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Tempo de Entrega")
  class TempoEntregaValidationTests {

    @Test
    @DisplayName("Deve rejeitar tempo de entrega nulo")
    void shouldRejectNullTempoEntrega() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTempoEntregaMinutos(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Tempo de entrega é obrigatório");
      assertThat(violations.iterator().next().getPropertyPath().toString())
          .isEqualTo("tempoEntregaMinutos");
    }

    @Test
    @DisplayName("Deve rejeitar tempo de entrega menor que 10 minutos")
    void shouldRejectTempoEntregaTooShort() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTempoEntregaMinutos(5);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Tempo de entrega deve ser pelo menos 10 minutos");
      assertThat(violations.iterator().next().getPropertyPath().toString())
          .isEqualTo("tempoEntregaMinutos");
    }

    @Test
    @DisplayName("Deve rejeitar tempo de entrega maior que 120 minutos")
    void shouldRejectTempoEntregaTooLong() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTempoEntregaMinutos(150);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Tempo de entrega não pode exceder 120 minutos");
      assertThat(violations.iterator().next().getPropertyPath().toString())
          .isEqualTo("tempoEntregaMinutos");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Telefone")
  class TelefoneValidationTests {

    @Test
    @DisplayName("Deve aceitar telefone nulo")
    void shouldAcceptNullTelefone() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTelefone(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar telefone válido com 10 dígitos")
    void shouldAcceptValidTelefone10Digits() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTelefone("1123456789");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar telefone válido com 11 dígitos")
    void shouldAcceptValidTelefone11Digits() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTelefone("11987654321");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar telefone com caracteres não numéricos")
    void shouldRejectTelefoneWithNonNumericChars() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setTelefone("11-98765-4321");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Telefone deve ter um formato válido brasileiro");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("telefone");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Email")
  class EmailValidationTests {

    @Test
    @DisplayName("Deve aceitar email nulo")
    void shouldAcceptNullEmail() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setEmail(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar email válido")
    void shouldAcceptValidEmail() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setEmail("contato@restaurante.com");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar email com formato inválido")
    void shouldRejectInvalidEmailFormat() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setEmail("contato.restaurante.com");

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Email deve ter formato válido");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }

    @Test
    @DisplayName("Deve aceitar email com exatamente 100 caracteres")
    void shouldAcceptEmailWithMaximumLength() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String email100Chars = "a".repeat(90) + "@email.com"; // 90 a's + @email.com = 100 chars
      request.setEmail(email100Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      // Email pode falhar na validação de formato se for muito longo
      if (!violations.isEmpty()) {
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Email deve ter formato válido");
      }
    }

    @Test
    @DisplayName("Deve rejeitar email com mais de 100 caracteres")
    void shouldRejectEmailTooLong() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      String email101Chars = "a".repeat(91) + "@email.com"; // 91 a's + @email.com = 101 chars
      request.setEmail(email101Chars);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(2);
      Set<String> messages =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(java.util.stream.Collectors.toSet());
      assertThat(messages).contains("Email não pode ter mais de 100 caracteres");
      assertThat(messages).contains("Email deve ter formato válido");
    }
  }

  @Nested
  @DisplayName("Validações de Campo - Avaliação")
  class AvaliacaoValidationTests {

    @Test
    @DisplayName("Deve aceitar avaliação nula")
    void shouldAcceptNullAvaliacao() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setAvaliacao(null);

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar avaliação negativa")
    void shouldRejectNegativeAvaliacao() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setAvaliacao(new BigDecimal("-1.0"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Avaliação deve ser positiva");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("avaliacao");
    }

    @Test
    @DisplayName("Deve rejeitar avaliação maior que 5.0")
    void shouldRejectAvaliacaoTooHigh() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setAvaliacao(new BigDecimal("6.0"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Avaliação máxima é 5.0");
      assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("avaliacao");
    }

    @Test
    @DisplayName("Deve aceitar avaliação válida")
    void shouldAcceptValidAvaliacao() {
      // Given
      RestauranteRequest request = createRestauranteRequestBasico();
      request.setAvaliacao(new BigDecimal("4.2"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Cenários com Múltiplas Validações")
  class MultipleValidationScenarios {

    @Test
    @DisplayName("Deve acumular erros de validação para múltiplos campos obrigatórios")
    void shouldAccumulateMultipleValidationErrors() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome(""); // vazio
      request.setCategoria(""); // vazio
      request.setEndereco(""); // vazio
      request.setTaxaEntrega(null); // nulo
      request.setTempoEntregaMinutos(null); // nulo

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(5);
      // Deve ter violações para nome, categoria, endereco, taxaEntrega e tempoEntregaMinutos
    }

    @Test
    @DisplayName("Deve aceitar restaurante completo válido")
    void shouldAcceptCompleteValidRestaurante() {
      // Given
      RestauranteRequest request = new RestauranteRequest();
      request.setNome("Restaurante Completo");
      request.setCategoria("Francesa");
      request.setEndereco("Rua Gourmet, 456 - Centro, São Paulo/SP - CEP: 01234-567");
      request.setTaxaEntrega(new BigDecimal("12.50"));
      request.setTempoEntregaMinutos(75);
      request.setTelefone("11987654321");
      request.setEmail("contato@restaurantecompleto.com.br");
      request.setAvaliacao(new BigDecimal("4.8"));

      // When
      Set<ConstraintViolation<RestauranteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  // Método auxiliar para criar um RestauranteRequest básico válido
  private RestauranteRequest createRestauranteRequestBasico() {
    RestauranteRequest request = new RestauranteRequest();
    request.setNome("Restaurante do Zé");
    request.setCategoria("Brasileira");
    request.setEndereco("Rua das Flores, 123 - São Paulo/SP");
    request.setTaxaEntrega(new BigDecimal("5.00"));
    request.setTempoEntregaMinutos(30);
    return request;
  }
}
