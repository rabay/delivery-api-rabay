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
@DisplayName("Price Validator Tests")
class PriceValidatorTest {

    private PriceValidator validator;

    @Mock
    private ValidPrice constraintAnnotation;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PriceValidator();
        
        // Configuração padrão da anotação
        when(constraintAnnotation.min()).thenReturn(1L); // R$ 0,01
        when(constraintAnnotation.max()).thenReturn(99999999L); // R$ 999.999,99
        
        validator.initialize(constraintAnnotation);
    }

    @Test
    @DisplayName("Deve aceitar preços válidos")
    void deveAceitarPrecosValidos() {
        assertTrue(validator.isValid(new BigDecimal("0.01"), context));
        assertTrue(validator.isValid(new BigDecimal("1.00"), context));
        assertTrue(validator.isValid(new BigDecimal("15.50"), context));
        assertTrue(validator.isValid(new BigDecimal("99.99"), context));
        assertTrue(validator.isValid(new BigDecimal("999.99"), context));
        assertTrue(validator.isValid(new BigDecimal("9999.99"), context));
    }

    @Test
    @DisplayName("Deve aceitar valores nulos")
    void deveAceitarValoresNulos() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Deve aceitar preços inteiros")
    void deveAceitarPrecosInteiros() {
        assertTrue(validator.isValid(new BigDecimal("1"), context));
        assertTrue(validator.isValid(new BigDecimal("10"), context));
        assertTrue(validator.isValid(new BigDecimal("100"), context));
        assertTrue(validator.isValid(new BigDecimal("1000"), context));
    }

    @Test
    @DisplayName("Deve aceitar preços com uma casa decimal")
    void deveAceitarPrecosComUmaCasaDecimal() {
        assertTrue(validator.isValid(new BigDecimal("1.5"), context));
        assertTrue(validator.isValid(new BigDecimal("10.5"), context));
        assertTrue(validator.isValid(new BigDecimal("99.9"), context));
    }

    @Test
    @DisplayName("Deve aceitar preços com duas casas decimais")
    void deveAceitarPrecosComDuasCasasDecimais() {
        assertTrue(validator.isValid(new BigDecimal("1.50"), context));
        assertTrue(validator.isValid(new BigDecimal("10.99"), context));
        assertTrue(validator.isValid(new BigDecimal("99.95"), context));
    }

    @Test
    @DisplayName("Deve rejeitar preços negativos")
    void deveRejeitarPrecosNegativos() {
        assertFalse(validator.isValid(new BigDecimal("-1.00"), context));
        assertFalse(validator.isValid(new BigDecimal("-0.01"), context));
        assertFalse(validator.isValid(new BigDecimal("-10.50"), context));
    }

    @Test
    @DisplayName("Deve rejeitar preço zero quando mínimo é maior que zero")
    void deveRejeitarPrecoZero() {
        assertFalse(validator.isValid(new BigDecimal("0.00"), context));
        assertFalse(validator.isValid(BigDecimal.ZERO, context));
    }

    @Test
    @DisplayName("Deve rejeitar preços abaixo do mínimo")
    void deveRejeitarPrecosAbaixoDoMinimo() {
        assertFalse(validator.isValid(new BigDecimal("0.005"), context));
        assertFalse(validator.isValid(new BigDecimal("0.009"), context));
    }

    @Test
    @DisplayName("Deve rejeitar preços acima do máximo")
    void deveRejeitarPrecosAcimaDoMaximo() {
        assertFalse(validator.isValid(new BigDecimal("1000000.00"), context));
        assertFalse(validator.isValid(new BigDecimal("999999999.99"), context));
    }

    @Test
    @DisplayName("Deve rejeitar preços com mais de duas casas decimais")
    void deveRejeitarPrecosComMaisDeDuasCasasDecimais() {
        assertFalse(validator.isValid(new BigDecimal("1.999"), context));
        assertFalse(validator.isValid(new BigDecimal("10.9999"), context));
        assertFalse(validator.isValid(new BigDecimal("99.99999"), context));
    }

    @Test
    @DisplayName("Deve validar preços comuns de delivery")
    void deveValidarPrecosComensDeDelivery() {
        // Preços típicos de comida
        assertTrue(validator.isValid(new BigDecimal("5.50"), context));   // Bebida
        assertTrue(validator.isValid(new BigDecimal("12.90"), context));  // Lanche
        assertTrue(validator.isValid(new BigDecimal("25.00"), context));  // Pizza pequena
        assertTrue(validator.isValid(new BigDecimal("45.50"), context));  // Pizza grande
        assertTrue(validator.isValid(new BigDecimal("89.99"), context));  // Combo
        assertTrue(validator.isValid(new BigDecimal("3.50"), context));   // Taxa de entrega
    }

    @Test
    @DisplayName("Deve validar limite máximo configurado")
    void deveValidarLimiteMaximoConfigurado() {
        // Testa exatamente o valor máximo (R$ 999.999,99)
        assertTrue(validator.isValid(new BigDecimal("999999.99"), context));
        
        // Testa um centavo acima
        assertFalse(validator.isValid(new BigDecimal("1000000.00"), context));
    }

    @Test
    @DisplayName("Deve validar limite mínimo configurado")
    void deveValidarLimiteMinimoConfigurado() {
        // Testa exatamente o valor mínimo (R$ 0,01)
        assertTrue(validator.isValid(new BigDecimal("0.01"), context));
        
        // Testa um centavo abaixo
        assertFalse(validator.isValid(new BigDecimal("0.00"), context));
    }

    @Test
    @DisplayName("Deve validar preços com configuração personalizada")
    void deveValidarPrecosComConfiguracaoPersonalizada() {
        // Reconfigura para um range diferente
        when(constraintAnnotation.min()).thenReturn(500L); // R$ 5,00
        when(constraintAnnotation.max()).thenReturn(10000L); // R$ 100,00
        
        validator.initialize(constraintAnnotation);
        
        // Valores dentro do novo range
        assertTrue(validator.isValid(new BigDecimal("5.00"), context));
        assertTrue(validator.isValid(new BigDecimal("50.00"), context));
        assertTrue(validator.isValid(new BigDecimal("100.00"), context));
        
        // Valores fora do novo range
        assertFalse(validator.isValid(new BigDecimal("4.99"), context));
        assertFalse(validator.isValid(new BigDecimal("100.01"), context));
    }
}