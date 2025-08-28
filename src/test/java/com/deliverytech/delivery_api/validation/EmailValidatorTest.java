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
@DisplayName("Email Validator Tests")
class EmailValidatorTest {

    private EmailValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @Test
    @DisplayName("Deve aceitar emails válidos")
    void deveAceitarEmailsValidos() {
        assertTrue(validator.isValid("user@example.com", context));
        assertTrue(validator.isValid("test.email@domain.co.uk", context));
        assertTrue(validator.isValid("user123@test-domain.org", context));
        assertTrue(validator.isValid("email_with_underscores@domain.net", context));
        assertTrue(validator.isValid("user+tag@domain.com", context));
        assertTrue(validator.isValid("a@b.co", context));
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
    @DisplayName("Deve rejeitar emails sem @")
    void deveRejeitarEmailsSemArroba() {
        assertFalse(validator.isValid("invalidemail.com", context));
        assertFalse(validator.isValid("user.domain.com", context));
        assertFalse(validator.isValid("plaintext", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails com múltiplos @")
    void deveRejeitarEmailsComMultiplosArroba() {
        assertFalse(validator.isValid("user@@domain.com", context));
        assertFalse(validator.isValid("user@domain@.com", context));
        assertFalse(validator.isValid("@user@domain.com", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails sem domínio")
    void deveRejeitarEmailsSemDominio() {
        assertFalse(validator.isValid("user@", context));
        assertFalse(validator.isValid("user@.", context));
        assertFalse(validator.isValid("user@.com", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails sem parte local")
    void deveRejeitarEmailsSemParteLocal() {
        assertFalse(validator.isValid("@domain.com", context));
        assertFalse(validator.isValid(".@domain.com", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails com pontos consecutivos")
    void deveRejeitarEmailsComPontosConsecutivos() {
        assertFalse(validator.isValid("user..email@domain.com", context));
        assertFalse(validator.isValid("user@domain..com", context));
        assertFalse(validator.isValid("user.@domain.com", context));
        assertFalse(validator.isValid("user@.domain.com", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails que começam ou terminam com ponto")
    void deveRejeitarEmailsComPontoNasBordas() {
        assertFalse(validator.isValid(".user@domain.com", context));
        assertFalse(validator.isValid("user.@domain.com", context));
        assertFalse(validator.isValid("user@.domain.com", context));
        assertFalse(validator.isValid("user@domain.com.", context));
    }

    @Test
    @DisplayName("Deve rejeitar emails muito longos")
    void deveRejeitarEmailsMuitoLongos() {
        // Email com mais de 255 caracteres
        String longEmail = "a".repeat(250) + "@domain.com";
        assertFalse(validator.isValid(longEmail, context));
        
        // Parte local com mais de 64 caracteres
        String longLocalPart = "a".repeat(65) + "@domain.com";
        assertFalse(validator.isValid(longLocalPart, context));
        
        // Parte do domínio com mais de 253 caracteres
        String longDomain = "user@" + "a".repeat(250) + ".com";
        assertFalse(validator.isValid(longDomain, context));
    }

    @Test
    @DisplayName("Deve rejeitar domínios sem TLD")
    void deveRejeitarDominiosSemTLD() {
        assertFalse(validator.isValid("user@domain", context));
        assertFalse(validator.isValid("user@localhost", context));
    }

    @Test
    @DisplayName("Deve rejeitar TLDs muito curtos")
    void deveRejeitarTLDsMuitoCurtos() {
        assertFalse(validator.isValid("user@domain.c", context));
        assertFalse(validator.isValid("user@domain.1", context));
    }

    @Test
    @DisplayName("Deve validar emails brasileiros comuns")
    void deveValidarEmailsBrasileirosComuns() {
        assertTrue(validator.isValid("usuario@gmail.com", context));
        assertTrue(validator.isValid("test@hotmail.com", context));
        assertTrue(validator.isValid("contato@empresa.com.br", context));
        assertTrue(validator.isValid("suporte@deliverytech.com.br", context));
        assertTrue(validator.isValid("admin@restaurante.org.br", context));
    }

    @Test
    @DisplayName("Deve rejeitar caracteres especiais inválidos")
    void deveRejeitarCaracteresEspeciaisInvalidos() {
        assertFalse(validator.isValid("user@domain.com!", context));
        assertFalse(validator.isValid("user#@domain.com", context));
        assertFalse(validator.isValid("user$@domain.com", context));
        assertFalse(validator.isValid("user%@domain.com", context));
        assertFalse(validator.isValid("user&@domain.com", context));
        assertFalse(validator.isValid("user*@domain.com", context));
    }
}