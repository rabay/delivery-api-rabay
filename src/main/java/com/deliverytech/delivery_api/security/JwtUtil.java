package com.deliverytech.delivery_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  // Gera um token JWT para o usuário
  public String gerarToken(String email) {
    Date agora = new Date();
    Date dataExpiracao = new Date(agora.getTime() + expiration);
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));

    return Jwts.builder()
        .subject(email)
        .issuedAt(agora)
        .expiration(dataExpiracao)
        .signWith(key)
        .compact();
  }

  // Gera um token JWT incluindo claims customizados (userId, role, restauranteId)
  public String gerarTokenFromUsuario(com.deliverytech.delivery_api.model.Usuario usuario) {
    Date agora = new Date();
    Date dataExpiracao = new Date(agora.getTime() + expiration);
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));

  var builder = Jwts.builder()
        .subject(usuario.getEmail())
        .issuedAt(agora)
        .expiration(dataExpiracao)
        .claim("userId", usuario.getId())
        .claim("role", usuario.getRole() != null ? usuario.getRole().name() : null)
        .claim("restauranteId", usuario.getRestauranteId())
        .signWith(key);

    return builder.compact();
  }

  // Valida um token JWT e retorna as claims
  public Claims validarToken(String token) {
  if (token == null) return null;
  SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));

  // Let exceptions from jjwt propagate (ExpiredJwtException, SignatureException, MalformedJwtException, etc.)
  return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  // Extrai o email (subject) do token
  public String getEmailFromToken(String token) {
    try {
      Claims claims = validarToken(token);
      return claims != null ? claims.getSubject() : null;
    } catch (JwtException e) {
      // Invalid token -> cannot extract email
      return null;
    }
  }
}
