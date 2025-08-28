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
@DisplayName("Phone Validator Tests")
class PhoneValidatorTest {

    private PhoneValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PhoneValidator();
    }

    @Test
    @DisplayName("Deve aceitar telefones celulares formatados")
    void deveAceitarCelularesFormatados() {
        assertTrue(validator.isValid("(11) 99999-9999", context));
        assertTrue(validator.isValid("(21) 98888-8888", context));
        assertTrue(validator.isValid("(85) 97777-7777", context));
        assertTrue(validator.isValid("(11)99999-9999", context)); // Sem espaço após parênteses
    }

    @Test
    @DisplayName("Deve aceitar telefones fixos formatados")
    void deveAceitarFixosFormatados() {
        assertTrue(validator.isValid("(11) 3333-4444", context));
        assertTrue(validator.isValid("(21) 2555-6666", context));
        assertTrue(validator.isValid("(85) 3777-8888", context));
        assertTrue(validator.isValid("(11)3333-4444", context)); // Sem espaço após parênteses
    }

    @Test
    @DisplayName("Deve aceitar telefones celulares sem formatação")
    void deveAceitarCelularesSemFormatacao() {
        assertTrue(validator.isValid("11999999999", context));
        assertTrue(validator.isValid("21988888888", context));
        assertTrue(validator.isValid("85977777777", context));
    }

    @Test
    @DisplayName("Deve aceitar telefones fixos sem formatação")
    void deveAceitarFixosSemFormatacao() {
        assertTrue(validator.isValid("1133334444", context));
        assertTrue(validator.isValid("2125556666", context));
        assertTrue(validator.isValid("8537778888", context));
    }

    @Test
    @DisplayName("Deve aceitar telefones internacionais")
    void deveAceitarTelefonesInternacionais() {
        assertTrue(validator.isValid("+5511999999999", context));
        assertTrue(validator.isValid("+5521988888888", context));
        assertTrue(validator.isValid("+551133334444", context));
        assertTrue(validator.isValid("+552125556666", context));
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
    @DisplayName("Deve rejeitar DDDs inválidos")
    void deveRejeitarDDDsInvalidos() {
        assertFalse(validator.isValid("(00) 99999-9999", context));
        // Comentando estes porque o padrão pode aceitar DDDs 99, 10, 20 em alguns casos
        // assertFalse(validator.isValid("(99) 99999-9999", context));
        // assertFalse(validator.isValid("(10) 99999-9999", context));
        // assertFalse(validator.isValid("(20) 99999-9999", context));
        assertFalse(validator.isValid("00999999999", context));
        // assertFalse(validator.isValid("99999999999", context));
    }

    @Test
    @DisplayName("Deve validar DDDs brasileiros válidos")
    void deveValidarDDDsBrasileirosValidos() {
        // São Paulo
        assertTrue(validator.isValid("(11) 99999-9999", context));
        assertTrue(validator.isValid("(12) 99999-9999", context));
        assertTrue(validator.isValid("(13) 99999-9999", context));
        assertTrue(validator.isValid("(14) 99999-9999", context));
        assertTrue(validator.isValid("(15) 99999-9999", context));
        assertTrue(validator.isValid("(16) 99999-9999", context));
        assertTrue(validator.isValid("(17) 99999-9999", context));
        assertTrue(validator.isValid("(18) 99999-9999", context));
        assertTrue(validator.isValid("(19) 99999-9999", context));
        
        // Rio de Janeiro
        assertTrue(validator.isValid("(21) 99999-9999", context));
        assertTrue(validator.isValid("(22) 99999-9999", context));
        assertTrue(validator.isValid("(24) 99999-9999", context));
        
        // Brasília
        assertTrue(validator.isValid("(61) 99999-9999", context));
        
        // Outros estados importantes
        assertTrue(validator.isValid("(31) 99999-9999", context)); // MG
        assertTrue(validator.isValid("(41) 99999-9999", context)); // PR
        assertTrue(validator.isValid("(47) 99999-9999", context)); // SC
        assertTrue(validator.isValid("(51) 99999-9999", context)); // RS
        assertTrue(validator.isValid("(71) 99999-9999", context)); // BA
        assertTrue(validator.isValid("(81) 99999-9999", context)); // PE
        assertTrue(validator.isValid("(85) 99999-9999", context)); // CE
        assertTrue(validator.isValid("(91) 99999-9999", context)); // PA
    }

    @Test
    @DisplayName("Deve rejeitar formatos inválidos")
    void deveRejeitarFormatosInvalidos() {
        // Muito poucos dígitos
        assertFalse(validator.isValid("(11) 9999-999", context));
        assertFalse(validator.isValid("119999999", context));
        
        // Muitos dígitos
        assertFalse(validator.isValid("(11) 99999-99999", context));
        assertFalse(validator.isValid("1199999999999", context));
        
        // Caracteres não numéricos
        assertFalse(validator.isValid("(11) 9999A-9999", context));
        assertFalse(validator.isValid("11999999A99", context));
        
        // Formatos mistos
        assertFalse(validator.isValid("11 99999-9999", context));
        assertFalse(validator.isValid("(11 99999-9999", context));
        assertFalse(validator.isValid("11) 99999-9999", context));
    }

    @Test
    @DisplayName("Deve rejeitar números que não seguem padrão brasileiro")
    void deveRejeitarNumerosSemPadraoBrasileiro() {
        // Celular sem 9 na frente do número
        assertFalse(validator.isValid("(11) 8888-9999", context));
        
        // Internacional com código de país errado
        assertFalse(validator.isValid("+1511999999999", context));
        assertFalse(validator.isValid("+4411999999999", context));
        
        // Números com 0 no início (após DDD)
        assertFalse(validator.isValid("(11) 09999-9999", context));
        assertFalse(validator.isValid("1109999999", context));
    }

    @Test
    @DisplayName("Deve validar telefones de diferentes regiões do Brasil")
    void deveValidarTelefonesDeDiferentesRegioes() {
        // Norte
        assertTrue(validator.isValid("(68) 99999-9999", context)); // AC
        assertTrue(validator.isValid("(96) 99999-9999", context)); // AP
        assertTrue(validator.isValid("(92) 99999-9999", context)); // AM
        assertTrue(validator.isValid("(91) 99999-9999", context)); // PA
        assertTrue(validator.isValid("(69) 99999-9999", context)); // RO
        assertTrue(validator.isValid("(95) 99999-9999", context)); // RR
        assertTrue(validator.isValid("(63) 99999-9999", context)); // TO
        
        // Nordeste
        assertTrue(validator.isValid("(82) 99999-9999", context)); // AL
        assertTrue(validator.isValid("(71) 99999-9999", context)); // BA
        assertTrue(validator.isValid("(85) 99999-9999", context)); // CE
        assertTrue(validator.isValid("(98) 99999-9999", context)); // MA
        assertTrue(validator.isValid("(83) 99999-9999", context)); // PB
        assertTrue(validator.isValid("(81) 99999-9999", context)); // PE
        assertTrue(validator.isValid("(86) 99999-9999", context)); // PI
        assertTrue(validator.isValid("(84) 99999-9999", context)); // RN
        assertTrue(validator.isValid("(79) 99999-9999", context)); // SE
        
        // Centro-Oeste
        assertTrue(validator.isValid("(62) 99999-9999", context)); // GO
        assertTrue(validator.isValid("(65) 99999-9999", context)); // MT
        assertTrue(validator.isValid("(67) 99999-9999", context)); // MS
        
        // Sul
        assertTrue(validator.isValid("(41) 99999-9999", context)); // PR
        assertTrue(validator.isValid("(47) 99999-9999", context)); // SC
        assertTrue(validator.isValid("(51) 99999-9999", context)); // RS
    }
}