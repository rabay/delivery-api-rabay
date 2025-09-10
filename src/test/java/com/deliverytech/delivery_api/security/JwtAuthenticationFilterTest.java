package com.deliverytech.delivery_api.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery_api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  private JwtUtil jwtUtil;

  private JwtAuthenticationFilter filter;

  @Mock private UsuarioRepository usuarioRepository;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil();
    // secret must be a base64url string used in other tests
    String testSecret = "my-secret-keyQWERTYUIOPASDFGHJKLZXCVBNMQWERTY1";
    ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
    ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);

    filter = new JwtAuthenticationFilter(jwtUtil, usuarioRepository);
  }

  @Test
  void validToken_shouldPopulateSecurityContext_and_continueChain()
      throws ServletException, IOException {
    // Arrange
    String token = jwtUtil.gerarToken("admin@test.com");
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + token);
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // usuarioRepository.findByEmail is expected to be called; return null to simulate absence or a
    // UserDetails implementation in integration tests
    when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(null);

    // Act
    filter.doFilterInternal(request, response, chain);

    // Assert
    assertThat(response.getStatus()).isEqualTo(200);
    verify(usuarioRepository, atLeastOnce()).findByEmail("admin@test.com");
  }

  @Test
  void expiredToken_shouldReturn401_with_reason_token_expired()
      throws ServletException, IOException {
    // Create expired token
    SecretKey key =
        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
            io.jsonwebtoken.io.Decoders.BASE64URL.decode(
                (String) ReflectionTestUtils.getField(jwtUtil, "secret")));
    Date issuedAt = new Date(System.currentTimeMillis() - 2000L);
    Date expiredAt = new Date(System.currentTimeMillis() - 1000L);

    String expiredToken =
        io.jsonwebtoken.Jwts.builder()
            .subject("admin@test.com")
            .issuedAt(issuedAt)
            .expiration(expiredAt)
            .signWith(key)
            .compact();

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + expiredToken);
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    filter.doFilterInternal(request, response, (jakarta.servlet.FilterChain) chain);

    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getContentAsString())
        .contains(com.deliverytech.delivery_api.security.JwtErrorReason.TOKEN_EXPIRED.getCode());
  }

  @Test
  void malformedToken_shouldReturn401_with_reason_malformed_token()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer " + "malformed.token");
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    filter.doFilterInternal(request, response, (jakarta.servlet.FilterChain) chain);

    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getContentAsString())
        .contains(com.deliverytech.delivery_api.security.JwtErrorReason.MALFORMED_TOKEN.getCode());
  }
}
