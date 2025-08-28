package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Avaliacao Validator Tests")
class AvaliacaoValidatorTest {

    private AvaliacaoValidator validator;

    @Mock
    private ValidAvaliacao constraintAnnotation;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new AvaliacaoValidator();
        
        // Configuração padrão da anotação
        when(constraintAnnotation.min()).thenReturn(1.0);
        when(constraintAnnotation.max()).thenReturn(5.0);
        when(constraintAnnotation.increment()).thenReturn(0.5);
        
        validator.initialize(constraintAnnotation);
    }

    @Test
    @DisplayName("Deve aceitar todas as avaliações válidas com incremento de 0.5")
    void deveAceitarTodasAvaliacoesValidasComIncrementoDe05() {
        assertTrue(validator.isValid(new BigDecimal("1.0"), context));
        assertTrue(validator.isValid(new BigDecimal("1.5"), context));
        assertTrue(validator.isValid(new BigDecimal("2.0"), context));
        assertTrue(validator.isValid(new BigDecimal("2.5"), context));
        assertTrue(validator.isValid(new BigDecimal("3.0"), context));
        assertTrue(validator.isValid(new BigDecimal("3.5"), context));
        assertTrue(validator.isValid(new BigDecimal("4.0"), context));
        assertTrue(validator.isValid(new BigDecimal("4.5"), context));
        assertTrue(validator.isValid(new BigDecimal("5.0"), context));
    }

    @Test
    @DisplayName("Deve aceitar valores nulos")
    void deveAceitarValoresNulos() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Deve aceitar avaliações nos limites mínimo e máximo")
    void deveAceitarAvaliacoesNosLimitesMinimoeMaximo() {
        assertTrue(validator.isValid(new BigDecimal("1.0"), context)); // Mínimo
        assertTrue(validator.isValid(new BigDecimal("5.0"), context)); // Máximo
    }

    @Test
    @DisplayName("Deve rejeitar avaliações abaixo do mínimo")
    void deveRejeitarAvaliacoesAbaixoDoMinimo() {
        assertFalse(validator.isValid(new BigDecimal("0.5"), context));
        assertFalse(validator.isValid(new BigDecimal("0.0"), context));
        assertFalse(validator.isValid(new BigDecimal("-1.0"), context));
    }

    @Test
    @DisplayName("Deve rejeitar avaliações acima do máximo")
    void deveRejeitarAvaliacoesAcimaDoMaximo() {
        assertFalse(validator.isValid(new BigDecimal("5.5"), context));
        assertFalse(validator.isValid(new BigDecimal("6.0"), context));
        assertFalse(validator.isValid(new BigDecimal("10.0"), context));
    }

    @Test
    @DisplayName("Deve rejeitar avaliações que não são múltiplos do incremento")
    void deveRejeitarAvaliacoesQueNaoSaoMultiplosDoIncremento() {
        assertFalse(validator.isValid(new BigDecimal("1.1"), context));
        assertFalse(validator.isValid(new BigDecimal("1.3"), context));
        assertFalse(validator.isValid(new BigDecimal("2.2"), context));
        assertFalse(validator.isValid(new BigDecimal("2.7"), context));
        assertFalse(validator.isValid(new BigDecimal("3.1"), context));
        assertFalse(validator.isValid(new BigDecimal("4.9"), context));
    }

    @Test
    @DisplayName("Deve rejeitar avaliações com mais de uma casa decimal")
    void deveRejeitarAvaliacoesComMaisDeUmaCasaDecimal() {
        assertFalse(validator.isValid(new BigDecimal("1.50"), context));  // 2 casas decimais
        assertFalse(validator.isValid(new BigDecimal("2.000"), context)); // 3 casas decimais
        assertFalse(validator.isValid(new BigDecimal("3.125"), context)); // 3 casas decimais
        assertFalse(validator.isValid(new BigDecimal("4.9999"), context)); // 4 casas decimais
    }

    @Test
    @DisplayName("Deve aceitar avaliações sem casa decimal")
    void deveAceitarAvaliacoesSemCasaDecimal() {
        assertTrue(validator.isValid(new BigDecimal("1"), context));
        assertTrue(validator.isValid(new BigDecimal("2"), context));
        assertTrue(validator.isValid(new BigDecimal("3"), context));
        assertTrue(validator.isValid(new BigDecimal("4"), context));
        assertTrue(validator.isValid(new BigDecimal("5"), context));
    }

    @Test
    @DisplayName("Deve validar sistema de estrelas típico")
    void deveValidarSistemaDeEstrelastipico() {
        // Sistema de 1 a 5 estrelas com meio ponto
        assertTrue(validator.isValid(new BigDecimal("1.0"), context)); // ⭐
        assertTrue(validator.isValid(new BigDecimal("1.5"), context)); // ⭐✨
        assertTrue(validator.isValid(new BigDecimal("2.0"), context)); // ⭐⭐
        assertTrue(validator.isValid(new BigDecimal("2.5"), context)); // ⭐⭐✨
        assertTrue(validator.isValid(new BigDecimal("3.0"), context)); // ⭐⭐⭐
        assertTrue(validator.isValid(new BigDecimal("3.5"), context)); // ⭐⭐⭐✨
        assertTrue(validator.isValid(new BigDecimal("4.0"), context)); // ⭐⭐⭐⭐
        assertTrue(validator.isValid(new BigDecimal("4.5"), context)); // ⭐⭐⭐⭐✨
        assertTrue(validator.isValid(new BigDecimal("5.0"), context)); // ⭐⭐⭐⭐⭐
    }

    @Test
    @DisplayName("Deve validar configuração personalizada")
    void deveValidarConfiguracaoPersonalizada() {
        // Reconfigura para escala 0-10 com incremento de 1.0
        when(constraintAnnotation.min()).thenReturn(0.0);
        when(constraintAnnotation.max()).thenReturn(10.0);
        when(constraintAnnotation.increment()).thenReturn(1.0);
        
        validator.initialize(constraintAnnotation);
        
        // Valores dentro do novo range e incremento
        assertTrue(validator.isValid(new BigDecimal("0.0"), context));
        assertTrue(validator.isValid(new BigDecimal("1.0"), context));
        assertTrue(validator.isValid(new BigDecimal("5.0"), context));
        assertTrue(validator.isValid(new BigDecimal("10.0"), context));
        
        // Valores fora do novo range
        assertFalse(validator.isValid(new BigDecimal("-1.0"), context));
        assertFalse(validator.isValid(new BigDecimal("11.0"), context));
        
        // Valores que não são múltiplos do novo incremento
        assertFalse(validator.isValid(new BigDecimal("0.5"), context));
        assertFalse(validator.isValid(new BigDecimal("1.5"), context));
        assertFalse(validator.isValid(new BigDecimal("2.3"), context));
    }

    @Test
    @DisplayName("Deve validar incremento de 0.1 (mais granular)")
    void deveValidarIncrementoDe01() {
        when(constraintAnnotation.increment()).thenReturn(0.1);
        validator.initialize(constraintAnnotation);
        
        assertTrue(validator.isValid(new BigDecimal("1.0"), context));
        assertTrue(validator.isValid(new BigDecimal("1.1"), context));
        assertTrue(validator.isValid(new BigDecimal("1.2"), context));
        assertTrue(validator.isValid(new BigDecimal("1.3"), context));
        assertTrue(validator.isValid(new BigDecimal("4.9"), context));
        assertTrue(validator.isValid(new BigDecimal("5.0"), context));
        
        // Ainda deve rejeitar valores que não são múltiplos de 0.1
        assertFalse(validator.isValid(new BigDecimal("1.05"), context));
        assertFalse(validator.isValid(new BigDecimal("1.15"), context));
        assertFalse(validator.isValid(new BigDecimal("2.33"), context));
    }

    @Test
    @DisplayName("Deve validar incremento de 1.0 (apenas números inteiros)")
    void deveValidarIncrementoDe10() {
        when(constraintAnnotation.increment()).thenReturn(1.0);
        validator.initialize(constraintAnnotation);
        
        assertTrue(validator.isValid(new BigDecimal("1.0"), context));
        assertTrue(validator.isValid(new BigDecimal("2.0"), context));
        assertTrue(validator.isValid(new BigDecimal("3.0"), context));
        assertTrue(validator.isValid(new BigDecimal("4.0"), context));
        assertTrue(validator.isValid(new BigDecimal("5.0"), context));
        
        // Deve rejeitar valores com casas decimais
        assertFalse(validator.isValid(new BigDecimal("1.5"), context));
        assertFalse(validator.isValid(new BigDecimal("2.1"), context));
        assertFalse(validator.isValid(new BigDecimal("3.9"), context));
        assertFalse(validator.isValid(new BigDecimal("4.5"), context));
    }

    @Test
    @DisplayName("Deve validar avaliações comuns de delivery")
    void deveValidarAvaliacoesComunsDeDelivery() {
        // Avaliações típicas que usuários costumam dar
        assertTrue(validator.isValid(new BigDecimal("3.0"), context)); // Média
        assertTrue(validator.isValid(new BigDecimal("4.0"), context)); // Boa
        assertTrue(validator.isValid(new BigDecimal("4.5"), context)); // Muito boa
        assertTrue(validator.isValid(new BigDecimal("5.0"), context)); // Excelente
        
        // Avaliações baixas
        assertTrue(validator.isValid(new BigDecimal("1.0"), context)); // Muito ruim
        assertTrue(validator.isValid(new BigDecimal("1.5"), context)); // Ruim
        assertTrue(validator.isValid(new BigDecimal("2.0"), context)); // Ruim-média
        assertTrue(validator.isValid(new BigDecimal("2.5"), context)); // Abaixo da média
    }

    @Test
    @DisplayName("Deve rejeitar precisão excessiva")
    void deveRejeitarPrecisaoExcessiva() {
        // Valores com mais precisão do que permitido
        assertFalse(validator.isValid(new BigDecimal("1.49999"), context));
        assertFalse(validator.isValid(new BigDecimal("2.50001"), context));
        assertFalse(validator.isValid(new BigDecimal("3.500000001"), context));
        assertFalse(validator.isValid(new BigDecimal("4.499999999"), context));
    }

    @Test
    @DisplayName("Deve validar casos extremos de arredondamento")
    void deveValidarCasosExtremosDeArredondamento() {
        // Valores que quando arredondados ficam iguais aos originais
        assertTrue(validator.isValid(new BigDecimal("1.0").setScale(1), context));
        assertTrue(validator.isValid(new BigDecimal("2.5").setScale(1), context));
        assertTrue(validator.isValid(new BigDecimal("5.0").setScale(1), context));
        
        // Valores que quando arredondados ficam diferentes dos originais
        assertFalse(validator.isValid(new BigDecimal("1.4999").setScale(4), context));
        assertFalse(validator.isValid(new BigDecimal("2.5001").setScale(4), context));
    }
}