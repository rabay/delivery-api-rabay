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

@DisplayName("Testes para EnderecoRequest")
class EnderecoRequestTest {

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
    @DisplayName("Deve criar EnderecoRequest válido com todos os campos obrigatórios")
    void deveCriarEnderecoRequestValido() {
      // Given
      EnderecoRequest request = new EnderecoRequest();
      request.setRua("Rua das Flores");
      request.setNumero("123");
      request.setBairro("Centro");
      request.setCidade("São Paulo");
      request.setEstado("SP");
      request.setCep("01234-567");
      request.setComplemento("Apto 101");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getRua()).isEqualTo("Rua das Flores");
      assertThat(request.getNumero()).isEqualTo("123");
      assertThat(request.getBairro()).isEqualTo("Centro");
      assertThat(request.getCidade()).isEqualTo("São Paulo");
      assertThat(request.getEstado()).isEqualTo("SP");
      assertThat(request.getCep()).isEqualTo("01234-567");
      assertThat(request.getComplemento()).isEqualTo("Apto 101");
    }

    @Test
    @DisplayName("Deve criar EnderecoRequest válido sem complemento")
    void deveCriarEnderecoRequestValidoSemComplemento() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getComplemento()).isNull();
    }

    @Test
    @DisplayName("Deve aceitar CEP sem hífen")
    void deveAceitarCepSemHifen() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("01234567");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Rua")
  class CenariosErroRua {

    @Test
    @DisplayName("Deve rejeitar rua nula")
    void deveRejeitarRuaNula() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setRua(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Rua é obrigatória");
    }

    @Test
    @DisplayName("Deve rejeitar rua vazia")
    void deveRejeitarRuaVazia() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setRua("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Rua é obrigatória");
    }

    @Test
    @DisplayName("Deve aceitar rua com exatamente 100 caracteres")
    void deveAceitarRuaComExatamente100Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String rua100Caracteres = "A".repeat(100);
      request.setRua(rua100Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar rua com mais de 100 caracteres")
    void deveRejeitarRuaComMaisDe100Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String rua101Caracteres = "A".repeat(101);
      request.setRua(rua101Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Rua não pode ter mais de 100 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Número")
  class CenariosErroNumero {

    @Test
    @DisplayName("Deve rejeitar número nulo")
    void deveRejeitarNumeroNulo() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setNumero(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Número é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar número vazio")
    void deveRejeitarNumeroVazio() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setNumero("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Número é obrigatório");
    }

    @Test
    @DisplayName("Deve aceitar número com letras e caracteres especiais")
    void deveAceitarNumeroComLetrasECaracteresEspeciais() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setNumero("123-A");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar número com exatamente 10 caracteres")
    void deveAceitarNumeroComExatamente10Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setNumero("1234567890");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar número com mais de 10 caracteres")
    void deveRejeitarNumeroComMaisDe10Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setNumero("12345678901");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Número não pode ter mais de 10 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Bairro")
  class CenariosErroBairro {

    @Test
    @DisplayName("Deve rejeitar bairro nulo")
    void deveRejeitarBairroNulo() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setBairro(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Bairro é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar bairro vazio")
    void deveRejeitarBairroVazio() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setBairro("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Bairro é obrigatório");
    }

    @Test
    @DisplayName("Deve aceitar bairro com exatamente 50 caracteres")
    void deveAceitarBairroComExatamente50Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String bairro50Caracteres = "A".repeat(50);
      request.setBairro(bairro50Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar bairro com mais de 50 caracteres")
    void deveRejeitarBairroComMaisDe50Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String bairro51Caracteres = "A".repeat(51);
      request.setBairro(bairro51Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Bairro não pode ter mais de 50 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Cidade")
  class CenariosErroCidade {

    @Test
    @DisplayName("Deve rejeitar cidade nula")
    void deveRejeitarCidadeNula() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCidade(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Cidade é obrigatória");
    }

    @Test
    @DisplayName("Deve rejeitar cidade vazia")
    void deveRejeitarCidadeVazia() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCidade("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Cidade é obrigatória");
    }

    @Test
    @DisplayName("Deve aceitar cidade com exatamente 50 caracteres")
    void deveAceitarCidadeComExatamente50Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String cidade50Caracteres = "A".repeat(50);
      request.setCidade(cidade50Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar cidade com mais de 50 caracteres")
    void deveRejeitarCidadeComMaisDe50Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String cidade51Caracteres = "A".repeat(51);
      request.setCidade(cidade51Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Cidade não pode ter mais de 50 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Estado")
  class CenariosErroEstado {

    @Test
    @DisplayName("Deve rejeitar estado nulo")
    void deveRejeitarEstadoNulo() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Estado é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar estado vazio")
    void deveRejeitarEstadoVazio() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("Estado é obrigatório");
    }

    @Test
    @DisplayName("Deve aceitar estado válido - SP")
    void deveAceitarEstadoValidoSP() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("SP");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar estado válido - RJ")
    void deveAceitarEstadoValidoRJ() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("RJ");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar estado válido - MG")
    void deveAceitarEstadoValidoMG() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("MG");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar estado inválido")
    void deveRejeitarEstadoInvalido() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("XX");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Estado deve ser um código UF válido do Brasil");
    }

    @Test
    @DisplayName("Deve rejeitar estado com mais de 2 caracteres")
    void deveRejeitarEstadoComMaisDe2Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setEstado("SPX");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Estado deve ser um código UF válido do Brasil");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - CEP")
  class CenariosErroCep {

    @Test
    @DisplayName("Deve rejeitar CEP nulo")
    void deveRejeitarCepNulo() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("CEP é obrigatório");
    }

    @Test
    @DisplayName("Deve rejeitar CEP vazio")
    void deveRejeitarCepVazio() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("CEP é obrigatório");
    }

    @Test
    @DisplayName("Deve aceitar CEP com hífen")
    void deveAceitarCepComHifen() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("01234-567");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar CEP sem hífen")
    void deveAceitarCepSemHifen() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("01234567");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar CEP com formato inválido")
    void deveRejeitarCepComFormatoInvalido() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("012345");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("CEP deve ter o formato 00000-000 ou 00000000");
    }

    @Test
    @DisplayName("Deve rejeitar CEP com caracteres não numéricos")
    void deveRejeitarCepComCaracteresNaoNumericos() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setCep("0123A-567");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("CEP deve ter o formato 00000-000 ou 00000000");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Complemento")
  class CenariosErroComplemento {

    @Test
    @DisplayName("Deve aceitar complemento nulo")
    void deveAceitarComplementoNulo() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setComplemento(null);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar complemento vazio")
    void deveAceitarComplementoVazio() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      request.setComplemento("");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve aceitar complemento com exatamente 100 caracteres")
    void deveAceitarComplementoComExatamente100Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String complemento100Caracteres = "A".repeat(100);
      request.setComplemento(complemento100Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar complemento com mais de 100 caracteres")
    void deveRejeitarComplementoComMaisDe100Caracteres() {
      // Given
      EnderecoRequest request = criarEnderecoRequestBasico();
      String complemento101Caracteres = "A".repeat(101);
      request.setComplemento(complemento101Caracteres);

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .isEqualTo("Complemento não pode ter mais de 100 caracteres");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Múltiplas validações")
  class CenariosErroMultiplasValidacoes {

    @Test
    @DisplayName("Deve acumular múltiplas violações de validação")
    void deveAcumularMultiplasViolacoes() {
      // Given
      EnderecoRequest request = new EnderecoRequest();
      // Todos os campos obrigatórios são nulos ou inválidos
      request.setRua("");
      request.setNumero("");
      request.setBairro("");
      request.setCidade("");
      request.setEstado("XX");
      request.setCep("12345");

      // When
      Set<ConstraintViolation<EnderecoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSizeGreaterThan(1);
      // Deve ter violações para rua, numero, bairro, cidade, estado e cep
    }
  }

  // Método auxiliar para criar um EnderecoRequest básico válido
  private EnderecoRequest criarEnderecoRequestBasico() {
    EnderecoRequest request = new EnderecoRequest();
    request.setRua("Rua das Flores");
    request.setNumero("123");
    request.setBairro("Centro");
    request.setCidade("São Paulo");
    request.setEstado("SP");
    request.setCep("01234-567");
    return request;
  }
}
