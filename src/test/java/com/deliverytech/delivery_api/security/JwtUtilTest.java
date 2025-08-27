package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.util.JwtTestUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "jwt.secret=my-secret-keyQWERTYUIOPASDFGHJKLZXCVBNMQWERTY1",
    "jwt.expiration=86400000"
})
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String testEmail = "test@example.com";
    private final String testSecret = "my-secret-keyQWERTYUIOPASDFGHJKLZXCVBNMQWERTY1";
    private final Long testExpiration = 86400000L;

    @BeforeEach
    void setUp() {
        // Set private fields using reflection for testing
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    void gerarToken_ShouldGenerateValidToken() {
        // When
        String token = jwtUtil.gerarToken(testEmail);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts separated by dots
    }

    @Test
    void validarToken_WithValidToken_ShouldReturnClaims() {
        // Given
        String token = jwtUtil.gerarToken(testEmail);

        // When
        Claims claims = jwtUtil.validarToken(token);

        // Then
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(testEmail);
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
    }

    @Test
    void validarToken_WithInvalidToken_ShouldReturnNull() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        Claims claims = jwtUtil.validarToken(invalidToken);

        // Then
        assertThat(claims).isNull();
    }

    @Test
    void validarToken_WithNullToken_ShouldReturnNull() {
        // When
        Claims claims = jwtUtil.validarToken(null);

        // Then
        assertThat(claims).isNull();
    }

    @Test
    void getEmailFromToken_WithValidToken_ShouldReturnEmail() {
        // Given
        String token = jwtUtil.gerarToken(testEmail);

        // When
        String email = jwtUtil.getEmailFromToken(token);

        // Then
        assertThat(email).isEqualTo(testEmail);
    }

    @Test
    void getEmailFromToken_WithInvalidToken_ShouldReturnNull() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        String email = jwtUtil.getEmailFromToken(invalidToken);

        // Then
        assertThat(email).isNull();
    }

    @Test
    void tokenExpiration_ShouldBeCorrect() {
        // Given
        String token = jwtUtil.gerarToken(testEmail);
        Claims claims = jwtUtil.validarToken(token);

        // When
        long tokenDuration = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();

        // Then
        assertThat(tokenDuration).isEqualTo(testExpiration);
    }

    @Test
    void generateMultipleTokens_ShouldBeDifferent() throws InterruptedException {
        // When
        String token1 = jwtUtil.gerarToken(testEmail);
        Thread.sleep(1000); // 1 second delay to ensure different timestamps
        String token2 = jwtUtil.gerarToken(testEmail);

        // Then
        assertThat(token1).isNotEqualTo(token2); // Different due to timestamp
    }
}