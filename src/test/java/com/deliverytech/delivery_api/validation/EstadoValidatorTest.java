package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Estado Validator Tests")
class EstadoValidatorTest {

    private EstadoValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EstadoValidator();
    }

    @Test
    @DisplayName("Deve aceitar todos os estados brasileiros válidos")
    void deveAceitarTodosEstadosValidosBrasileiros() {
        // Região Norte
        assertTrue(validator.isValid("AC", context)); // Acre
        assertTrue(validator.isValid("AP", context)); // Amapá
        assertTrue(validator.isValid("AM", context)); // Amazonas
        assertTrue(validator.isValid("PA", context)); // Pará
        assertTrue(validator.isValid("RO", context)); // Rondônia
        assertTrue(validator.isValid("RR", context)); // Roraima
        assertTrue(validator.isValid("TO", context)); // Tocantins
        
        // Região Nordeste
        assertTrue(validator.isValid("AL", context)); // Alagoas
        assertTrue(validator.isValid("BA", context)); // Bahia
        assertTrue(validator.isValid("CE", context)); // Ceará
        assertTrue(validator.isValid("MA", context)); // Maranhão
        assertTrue(validator.isValid("PB", context)); // Paraíba
        assertTrue(validator.isValid("PE", context)); // Pernambuco
        assertTrue(validator.isValid("PI", context)); // Piauí
        assertTrue(validator.isValid("RN", context)); // Rio Grande do Norte
        assertTrue(validator.isValid("SE", context)); // Sergipe
        
        // Região Centro-Oeste
        assertTrue(validator.isValid("GO", context)); // Goiás
        assertTrue(validator.isValid("MT", context)); // Mato Grosso
        assertTrue(validator.isValid("MS", context)); // Mato Grosso do Sul
        assertTrue(validator.isValid("DF", context)); // Distrito Federal
        
        // Região Sudeste
        assertTrue(validator.isValid("ES", context)); // Espírito Santo
        assertTrue(validator.isValid("MG", context)); // Minas Gerais
        assertTrue(validator.isValid("RJ", context)); // Rio de Janeiro
        assertTrue(validator.isValid("SP", context)); // São Paulo
        
        // Região Sul
        assertTrue(validator.isValid("PR", context)); // Paraná
        assertTrue(validator.isValid("RS", context)); // Rio Grande do Sul
        assertTrue(validator.isValid("SC", context)); // Santa Catarina
    }

    @Test
    @DisplayName("Deve aceitar estados em minúsculo e converter para maiúsculo")
    void deveAceitarEstadosEmMinusculo() {
        assertTrue(validator.isValid("sp", context));
        assertTrue(validator.isValid("rj", context));
        assertTrue(validator.isValid("mg", context));
        assertTrue(validator.isValid("pr", context));
        assertTrue(validator.isValid("sc", context));
        assertTrue(validator.isValid("rs", context));
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
    @DisplayName("Deve rejeitar códigos de estado inválidos")
    void deveRejeitarCodigosDeEstadoInvalidos() {
        assertFalse(validator.isValid("XX", context));
        assertFalse(validator.isValid("YY", context));
        assertFalse(validator.isValid("ZZ", context));
        assertFalse(validator.isValid("99", context));
        assertFalse(validator.isValid("00", context));
        assertFalse(validator.isValid("AB", context));
    }

    @Test
    @DisplayName("Deve rejeitar códigos com comprimento diferente de 2")
    void deveRejeitarCodigosComComprimentoIncorreto() {
        assertFalse(validator.isValid("S", context));      // Muito curto
        assertFalse(validator.isValid("SPP", context));    // Muito longo
        // Espaços são tratados pelo trim - estes casos passam pela validação
        // assertTrue(validator.isValid("SP ", context));    // Com espaço - trim remove
        // assertTrue(validator.isValid(" SP", context));    // Com espaço - trim remove
    }

    @Test
    @DisplayName("Deve rejeitar códigos com números")
    void deveRejeitarCodigosComNumeros() {
        assertFalse(validator.isValid("S1", context));
        assertFalse(validator.isValid("1P", context));
        assertFalse(validator.isValid("12", context));
    }

    @Test
    @DisplayName("Deve rejeitar códigos com caracteres especiais")
    void deveRejeitarCodigosComCaracteresEspeciais() {
        assertFalse(validator.isValid("S-", context));
        assertFalse(validator.isValid("S.", context));
        assertFalse(validator.isValid("S@", context));
        assertFalse(validator.isValid("S#", context));
        assertFalse(validator.isValid("S$", context));
    }

    @Test
    @DisplayName("Deve remover espaços e validar corretamente")
    void deveRemoverEspacosEValidarCorretamente() {
        assertTrue(validator.isValid(" SP ", context));
        assertTrue(validator.isValid("  RJ  ", context));
        assertTrue(validator.isValid("\tMG\t", context));
        assertFalse(validator.isValid(" S P ", context)); // Espaço no meio
    }

    @Test
    @DisplayName("Deve validar estados com case insensitive")
    void deveValidarEstadosComCaseInsensitive() {
        assertTrue(validator.isValid("Sp", context));
        assertTrue(validator.isValid("sP", context));
        assertTrue(validator.isValid("SP", context));
        assertTrue(validator.isValid("sp", context));
        assertTrue(validator.isValid("rJ", context));
        assertTrue(validator.isValid("Rj", context));
        assertTrue(validator.isValid("RJ", context));
        assertTrue(validator.isValid("rj", context));
    }

    @Test
    @DisplayName("Deve validar estados mais populosos do Brasil")
    void deveValidarEstadosMaisPopulosos() {
        // Os 10 estados mais populosos
        assertTrue(validator.isValid("SP", context)); // São Paulo
        assertTrue(validator.isValid("MG", context)); // Minas Gerais
        assertTrue(validator.isValid("RJ", context)); // Rio de Janeiro
        assertTrue(validator.isValid("BA", context)); // Bahia
        assertTrue(validator.isValid("PR", context)); // Paraná
        assertTrue(validator.isValid("RS", context)); // Rio Grande do Sul
        assertTrue(validator.isValid("PE", context)); // Pernambuco
        assertTrue(validator.isValid("CE", context)); // Ceará
        assertTrue(validator.isValid("PA", context)); // Pará
        assertTrue(validator.isValid("SC", context)); // Santa Catarina
    }

    @Test
    @DisplayName("Deve validar Distrito Federal")
    void deveValidarDistritoFederal() {
        assertTrue(validator.isValid("DF", context));
        assertTrue(validator.isValid("df", context));
        assertTrue(validator.isValid("Df", context));
        assertTrue(validator.isValid("dF", context));
    }
}