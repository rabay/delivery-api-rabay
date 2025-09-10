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
@DisplayName("Nome Validator Tests")
class NomeValidatorTest {

  private NomeValidator validator;

  @Mock private ValidNome constraintAnnotation;

  @Mock private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    validator = new NomeValidator();

    // Configuração padrão da anotação
    when(constraintAnnotation.min()).thenReturn(2);
    when(constraintAnnotation.max()).thenReturn(100);

    validator.initialize(constraintAnnotation);
  }

  @Test
  @DisplayName("Deve aceitar nomes válidos simples")
  void deveAceitarNomesValidosSimples() {
    assertTrue(validator.isValid("João", context));
    assertTrue(validator.isValid("Maria", context));
    assertTrue(validator.isValid("Pedro", context));
    assertTrue(validator.isValid("Ana", context));
  }

  @Test
  @DisplayName("Deve aceitar nomes compostos")
  void deveAceitarNomesCompostos() {
    assertTrue(validator.isValid("João Silva", context));
    assertTrue(validator.isValid("Maria da Silva", context));
    assertTrue(validator.isValid("Pedro dos Santos", context));
    assertTrue(validator.isValid("Ana Paula Souza", context));
    assertTrue(validator.isValid("José Carlos de Oliveira", context));
  }

  @Test
  @DisplayName("Deve aceitar nomes com acentos")
  void deveAceitarNomesComAcentos() {
    assertTrue(validator.isValid("José", context));
    assertTrue(validator.isValid("André", context));
    assertTrue(validator.isValid("Mônica", context));
    assertTrue(validator.isValid("Cláudia", context));
    assertTrue(validator.isValid("Antônio", context));
    assertTrue(validator.isValid("François", context));
  }

  @Test
  @DisplayName("Deve aceitar nomes com hífens")
  void deveAceitarNomesComHifens() {
    assertTrue(validator.isValid("Maria-José", context));
    assertTrue(validator.isValid("João-Paulo", context));
    assertTrue(validator.isValid("Anne-Marie", context));
    assertTrue(validator.isValid("Jean-Claude", context));
  }

  @Test
  @DisplayName("Deve aceitar nomes com apostrofes")
  void deveAceitarNomesComApostrofes() {
    assertTrue(validator.isValid("D'Angelo", context));
    assertTrue(validator.isValid("O'Connor", context));
    assertTrue(validator.isValid("D'Silva", context));
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
  }

  @Test
  @DisplayName("Deve aceitar nomes com uma letra")
  void deveAceitarNomesComUmaLetra() {
    // Quando min = 1
    when(constraintAnnotation.min()).thenReturn(1);
    validator.initialize(constraintAnnotation);

    assertTrue(validator.isValid("A", context));
    assertTrue(validator.isValid("I", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes muito curtos")
  void deveRejeitarNomesMuitoCurtos() {
    assertFalse(validator.isValid("A", context)); // Menor que min=2
    // String vazia é aceita pelo validator (usar @NotNull/@NotBlank para validar presença)
    // assertFalse(validator.isValid("", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes muito longos")
  void deveRejeitarNomesMuitoLongos() {
    String nomeLongo = "A".repeat(101); // Maior que max=100
    assertFalse(validator.isValid(nomeLongo, context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com números")
  void deveRejeitarNomesComNumeros() {
    assertFalse(validator.isValid("João123", context));
    assertFalse(validator.isValid("Maria2", context));
    assertFalse(validator.isValid("Pedro1Silva", context));
    assertFalse(validator.isValid("Ana3", context));
    assertFalse(validator.isValid("123João", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com caracteres especiais inválidos")
  void deveRejeitarNomesComCaracteresEspeciaisInvalidos() {
    assertFalse(validator.isValid("João@", context));
    assertFalse(validator.isValid("Maria#", context));
    assertFalse(validator.isValid("Pedro$", context));
    assertFalse(validator.isValid("Ana%", context));
    assertFalse(validator.isValid("José&", context));
    assertFalse(validator.isValid("Carlos*", context));
    assertFalse(validator.isValid("Lucas+", context));
    assertFalse(validator.isValid("Bruno=", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com espaços nas bordas")
  void deveRejeitarNomesComEspacosNasBordas() {
    assertFalse(validator.isValid(" João", context));
    assertFalse(validator.isValid("João ", context));
    assertFalse(validator.isValid(" João ", context));
    assertFalse(validator.isValid("  Maria  ", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com espaços consecutivos")
  void deveRejeitarNomesComEspacosConsecutivos() {
    assertFalse(validator.isValid("João  Silva", context));
    assertFalse(validator.isValid("Maria   da Silva", context));
    assertFalse(validator.isValid("Pedro    Santos", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com hífens consecutivos")
  void deveRejeitarNomesComHifensConsecutivos() {
    assertFalse(validator.isValid("João--Paulo", context));
    assertFalse(validator.isValid("Maria--José", context));
    assertFalse(validator.isValid("Anne--Marie", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes com apostrofes consecutivos")
  void deveRejeitarNomesComApostrofesConsecutivos() {
    assertFalse(validator.isValid("D''Angelo", context));
    assertFalse(validator.isValid("O''Connor", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes que começam com hífen ou apostrofe")
  void deveRejeitarNomesQueComecamComHifenOuApostrofe() {
    assertFalse(validator.isValid("-João", context));
    assertFalse(validator.isValid("'Maria", context));
    assertFalse(validator.isValid("-Paulo", context));
    assertFalse(validator.isValid("'Connor", context));
  }

  @Test
  @DisplayName("Deve rejeitar nomes que terminam com hífen ou apostrofe")
  void deveRejeitarNomesQueTerminamComHifenOuApostrofe() {
    assertFalse(validator.isValid("João-", context));
    assertFalse(validator.isValid("Maria'", context));
    assertFalse(validator.isValid("Paulo-", context));
    assertFalse(validator.isValid("Connor'", context));
  }

  @Test
  @DisplayName("Deve validar nomes brasileiros comuns")
  void deveValidarNomesBrasileirosComuns() {
    assertTrue(validator.isValid("João da Silva", context));
    assertTrue(validator.isValid("Maria dos Santos", context));
    assertTrue(validator.isValid("José Carlos", context));
    assertTrue(validator.isValid("Ana Paula", context));
    assertTrue(validator.isValid("Paulo Roberto", context));
    assertTrue(validator.isValid("Luiz Fernando", context));
    assertTrue(validator.isValid("Carlos Eduardo", context));
    assertTrue(validator.isValid("Francisco de Assis", context));
  }

  @Test
  @DisplayName("Deve validar nomes internacionais")
  void deveValidarNomesInternacionais() {
    assertTrue(validator.isValid("François Dubois", context));
    assertTrue(validator.isValid("Jean-Claude Van Damme", context));
    assertTrue(validator.isValid("Marie-Anne Dupont", context));
    assertTrue(validator.isValid("O'Connor", context));
    assertTrue(validator.isValid("D'Angelo", context));
    assertTrue(validator.isValid("MacDonald", context));
  }

  @Test
  @DisplayName("Deve validar configuração personalizada de comprimento")
  void deveValidarConfiguracaoPersonalizadaDeComprimento() {
    // Reconfigura para um range diferente
    when(constraintAnnotation.min()).thenReturn(5);
    when(constraintAnnotation.max()).thenReturn(20);

    validator.initialize(constraintAnnotation);

    // Valores dentro do novo range
    assertTrue(validator.isValid("João Silva", context)); // 10 chars
    assertTrue(validator.isValid("Maria Santos", context)); // 12 chars

    // Valores fora do novo range
    assertFalse(validator.isValid("Ana", context)); // 3 chars (muito curto)
    assertFalse(validator.isValid("Francisco de Assis Santos", context)); // Muito longo
  }
}
