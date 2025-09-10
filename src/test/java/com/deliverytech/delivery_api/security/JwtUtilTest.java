package com.deliverytech.delivery_api.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

// ...existing code...

@ExtendWith(MockitoExtension.class)
@TestPropertySource(
    properties = {
      "jwt.secret=my-secret-keyQWERTYUIOPASDFGHJKLZXCVBNMQWERTY1",
      "jwt.expiration=86400000"
    })
class JwtUtilTest {

  @InjectMocks private JwtUtil jwtUtil;

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
    // Then
    org.junit.jupiter.api.Assertions.assertThrows(
        io.jsonwebtoken.JwtException.class, () -> jwtUtil.validarToken(invalidToken));
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
  void validarToken_WithExpiredToken_ShouldThrowExpiredJwtException() {
    // Build a token with expiration in the past using the same secret
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(testSecret));
    Date issuedAt = new Date(System.currentTimeMillis() - 2000L);
    Date expiredAt = new Date(System.currentTimeMillis() - 1000L);

    String expiredToken =
        Jwts.builder()
            .subject(testEmail)
            .issuedAt(issuedAt)
            .expiration(expiredAt)
            .signWith(key)
            .compact();

    // When / Then
    org.junit.jupiter.api.Assertions.assertThrows(
        io.jsonwebtoken.ExpiredJwtException.class, () -> jwtUtil.validarToken(expiredToken));
  }

  @Test
  void validarToken_WithMalformedToken_ShouldThrow() {
    String malformed = "abc.def"; // not a valid JWT (only two parts)

    org.junit.jupiter.api.Assertions.assertThrows(
        io.jsonwebtoken.JwtException.class, () -> jwtUtil.validarToken(malformed));
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
