package com.deliverytech.delivery_api.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para LoginRequest")
class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar LoginRequest com construtor padrão")
    void deveCriarLoginRequestComConstrutorPadrao() {
        // Given & When
        LoginRequest loginRequest = new LoginRequest();

        // Then
        assertNotNull(loginRequest);
        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve definir e obter username")
    void deveDefinirEObterUsername() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        String username = "usuario@example.com";

        // When
        loginRequest.setUsername(username);

        // Then
        assertEquals(username, loginRequest.getUsername());
    }

    @Test
    @DisplayName("Deve definir e obter password")
    void deveDefinirEObterPassword() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        String password = "senhaSegura123";

        // When
        loginRequest.setPassword(password);

        // Then
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve criar LoginRequest completo")
    void deveCriarLoginRequestCompleto() {
        // Given
        String username = "admin@delivery.com";
        String password = "admin123";

        // When
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Then
        assertNotNull(loginRequest);
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve permitir valores null")
    void devePermitirValoresNull() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");

        // When
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        // Then
        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve manter valores após múltiplas definições")
    void deveManterValoresAposMultiplasDefinicoes() {
        // Given
        LoginRequest loginRequest = new LoginRequest();

        // When
        loginRequest.setUsername("user1@example.com");
        loginRequest.setUsername("user2@example.com");
        loginRequest.setPassword("pass1");
        loginRequest.setPassword("pass2");

        // Then
        assertEquals("user2@example.com", loginRequest.getUsername());
        assertEquals("pass2", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve criar LoginRequest com email válido")
    void deveCriarLoginRequestComEmailValido() {
        // Given
        String username = "cliente@delivery.com";
        String password = "cliente123";

        // When
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Then
        assertNotNull(loginRequest);
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve criar LoginRequest com dados de restaurante")
    void deveCriarLoginRequestComDadosRestaurante() {
        // Given
        String username = "restaurante@pizzaria.com";
        String password = "restaurante456";

        // When
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Then
        assertNotNull(loginRequest);
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Deve criar LoginRequest com dados de admin")
    void deveCriarLoginRequestComDadosAdmin() {
        // Given
        String username = "admin@delivery.com";
        String password = "admin789";

        // When
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // Then
        assertNotNull(loginRequest);
        assertEquals(username, loginRequest.getUsername());
        assertEquals(password, loginRequest.getPassword());
    }

    @Nested
    @DisplayName("Cenários de validação")
    class CenariosValidacao {

        @Test
        @DisplayName("Deve aceitar LoginRequest válido")
        void deveAceitarLoginRequestValido() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("usuario@exemplo.com");
            request.setPassword("senha123");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com username nulo")
        void deveRejeitarUsernameNulo() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername(null);
            request.setPassword("senha123");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .contains("Username é obrigatório");
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com username vazio")
        void deveRejeitarUsernameVazio() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("");
            request.setPassword("senha123");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .contains("Username é obrigatório");
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com username apenas espaços")
        void deveRejeitarUsernameApenasEspacos() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("   ");
            request.setPassword("senha123");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .contains("Username é obrigatório");
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com password nulo")
        void deveRejeitarPasswordNulo() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("usuario@exemplo.com");
            request.setPassword(null);

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .contains("Password é obrigatório");
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com password vazio")
        void deveRejeitarPasswordVazio() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("usuario@exemplo.com");
            request.setPassword("");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .contains("Password é obrigatório");
        }

        @Test
        @DisplayName("Deve rejeitar LoginRequest com múltiplos campos inválidos")
        void deveRejeitarMultiplosCamposInvalidos() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername(null);
            request.setPassword("");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(2);
            var messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
            assertThat(messages).contains("Username é obrigatório", "Password é obrigatório");
        }
    }

    @Nested
    @DisplayName("Cenários de segurança")
    class CenariosSeguranca {

        @Test
        @DisplayName("Deve aceitar password com caracteres especiais")
        void deveAceitarPasswordComCaracteresEspeciais() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("usuario@exemplo.com");
            request.setPassword("P@ssw0rd!#$%");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar email com subdomínio")
        void deveAceitarEmailComSubdominio() {
            // Given
            LoginRequest request = new LoginRequest();
            request.setUsername("usuario@sub.dominio.com");
            request.setPassword("senha123");

            // When
            var violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }
    }
}