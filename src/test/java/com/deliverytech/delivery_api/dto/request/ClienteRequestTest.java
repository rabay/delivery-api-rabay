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

@DisplayName("Testes para ClienteRequest")
class ClienteRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  @DisplayName("Cenários de sucesso")
  class CenariosSucesso {

    @Test
    @DisplayName("Deve criar ClienteRequest válido com todos os campos obrigatórios")
    void deveCriarClienteRequestValido() {
      // Given
      ClienteRequest request = new ClienteRequest();
      request.setNome("João Silva");
      request.setTelefone("11987654321");
      request.setEmail("joao.silva@example.com");
      request.setEndereco("Rua das Flores, 123 - São Paulo/SP");
      request.setSenha("senhaSegura123");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getNome()).isEqualTo("João Silva");
      assertThat(request.getTelefone()).isEqualTo("11987654321");
      assertThat(request.getEmail()).isEqualTo("joao.silva@example.com");
      assertThat(request.getEndereco()).isEqualTo("Rua das Flores, 123 - São Paulo/SP");
      assertThat(request.getSenha()).isEqualTo("senhaSegura123");
    }

    @Test
    @DisplayName("Deve criar ClienteRequest válido sem senha")
    void deveCriarClienteRequestValidoSemSenha() {
      // Given
      ClienteRequest request = new ClienteRequest();
      request.setNome("Maria Santos");
      request.setTelefone("11876543210");
      request.setEmail("maria.santos@example.com");
      request.setEndereco("Av. Paulista, 456 - São Paulo/SP");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getSenha()).isNull();
    }

    @Test
    @DisplayName("Deve aceitar nome com caracteres especiais")
    void deveAceitarNomeComCaracteresEspeciais() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome("José da Silva Santos");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Nome")
  class CenariosErroNome {

    @Test
    @DisplayName("Deve rejeitar nome nulo")
    void deveRejeitarNomeNulo() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome(null);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Nome é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar nome vazio")
    void deveRejeitarNomeVazio() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome("");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(2);
      Set<String> messages =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(java.util.stream.Collectors.toSet());
      assertThat(messages).contains("Nome é obrigatório");
      assertThat(messages).contains("Nome deve ter entre 2 e 100 caracteres");
    }

    @Test
    @DisplayName("Deve rejeitar nome com apenas espaços")
    void deveRejeitarNomeComApenasEspacos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome("   ");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Nome é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar nome com menos de 2 caracteres")
    void deveRejeitarNomeComMenosDe2Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome("A");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Nome deve ter entre 2 e 100 caracteres");
    }

    @Test
    @DisplayName("Deve aceitar nome com exatamente 2 caracteres")
    void deveAceitarNomeComExatamente2Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setNome("Jo");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar nome com exatamente 100 caracteres")
    void deveAceitarNomeComExatamente100Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      String nome100Caracteres = "A".repeat(100);
      request.setNome(nome100Caracteres);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar nome com mais de 100 caracteres")
    void deveRejeitarNomeComMaisDe100Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      String nome101Caracteres = "A".repeat(101);
      request.setNome(nome101Caracteres);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Nome deve ter entre 2 e 100 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Telefone")
  class CenariosErroTelefone {

    @Test
    @DisplayName("Deve rejeitar telefone nulo")
    void deveRejeitarTelefoneNulo() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone(null);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Telefone é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar telefone vazio")
    void deveRejeitarTelefoneVazio() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(2);
      Set<String> messages =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(java.util.stream.Collectors.toSet());
      assertThat(messages).contains("Telefone é obrigatório");
      assertThat(messages).contains("Telefone deve ter 10 ou 11 dígitos");
    }

    @Test
    @DisplayName("Deve aceitar telefone com 10 dígitos")
    void deveAceitarTelefoneCom10Digitos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("1123456789");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar telefone com 11 dígitos")
    void deveAceitarTelefoneCom11Digitos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("11987654321");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar telefone com menos de 10 dígitos")
    void deveRejeitarTelefoneComMenosDe10Digitos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("123456789");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Telefone deve ter 10 ou 11 dígitos");
    }

    @Test
    @DisplayName("Deve rejeitar telefone com mais de 11 dígitos")
    void deveRejeitarTelefoneComMaisDe11Digitos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("119876543210");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Telefone deve ter 10 ou 11 dígitos");
    }

    @Test
    @DisplayName("Deve rejeitar telefone com caracteres não numéricos")
    void deveRejeitarTelefoneComCaracteresNaoNumericos() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setTelefone("11-98765-4321");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Telefone deve ter 10 ou 11 dígitos");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Email")
  class CenariosErroEmail {

    @Test
    @DisplayName("Deve rejeitar email nulo")
    void deveRejeitarEmailNulo() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEmail(null);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar email vazio")
    void deveRejeitarEmailVazio() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEmail("");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Email é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar email com formato inválido")
    void deveRejeitarEmailComFormatoInvalido() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEmail("email-invalido");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Email deve ter formato válido");
    }

    @Test
    @DisplayName("Deve aceitar email válido")
    void deveAceitarEmailValido() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEmail("usuario@dominio.com");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar email com subdomínio")
    void deveAceitarEmailComSubdominio() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEmail("usuario@sub.dominio.com.br");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar email com mais de 100 caracteres")
    void deveRejeitarEmailComMaisDe100Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      String email101Caracteres = "a".repeat(91) + "@dominio.com"; // 91 + 12 = 103 caracteres
      request.setEmail(email101Caracteres);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

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
  @DisplayName("Cenários de erro - Endereço")
  class CenariosErroEndereco {

    @Test
    @DisplayName("Deve rejeitar endereço nulo")
    void deveRejeitarEnderecoNulo() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEndereco(null);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Endereço é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar endereço vazio")
    void deveRejeitarEnderecoVazio() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      request.setEndereco("");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Endereço é obrigatório");
    }

    @Test
    @DisplayName("Deve aceitar endereço com exatamente 255 caracteres")
    void deveAceitarEnderecoComExatamente255Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      String endereco255Caracteres = "A".repeat(255);
      request.setEndereco(endereco255Caracteres);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar endereço com mais de 255 caracteres")
    void deveRejeitarEnderecoComMaisDe255Caracteres() {
      // Given
      ClienteRequest request = criarClienteRequestBasico();
      String endereco256Caracteres = "A".repeat(256);
      request.setEndereco(endereco256Caracteres);

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Endereço não pode ter mais de 255 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Múltiplas validações")
  class CenariosErroMultiplasValidacoes {

    @Test
    @DisplayName("Deve acumular múltiplas violações de validação")
    void deveAcumularMultiplasViolacoes() {
      // Given
      ClienteRequest request = new ClienteRequest();
      // Todos os campos obrigatórios são nulos ou inválidos
      request.setNome("");
      request.setTelefone("");
      request.setEmail("email-invalido");
      request.setEndereco("");

      // When
      Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSizeGreaterThan(1);
      // Deve ter violações para nome, telefone, email e endereço
    }
  }

  // Método auxiliar para criar um ClienteRequest básico válido
  private ClienteRequest criarClienteRequestBasico() {
    ClienteRequest request = new ClienteRequest();
    request.setNome("João Silva");
    request.setTelefone("11987654321");
    request.setEmail("joao.silva@example.com");
    request.setEndereco("Rua das Flores, 123 - São Paulo/SP");
    return request;
  }
}
