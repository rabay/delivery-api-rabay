package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UsuarioRepository usuarioRepository;

  // All authorization decisions are handled by SecurityConfig
  // This filter only handles JWT authentication

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
    this.jwtUtil = jwtUtil;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestPath = request.getRequestURI();
    String method = request.getMethod();

    log.debug("Processing request: {} {}", method, requestPath);

    // Let Spring Security handle authorization decisions entirely
    // This filter only processes authentication for requests that require it

    var token = this.recoverToken(request);
    if (token != null) {
      try {
        var claims = jwtUtil.validarToken(token);

        if (claims == null) {
          log.warn("Invalid or expired JWT token");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response
              .getWriter()
              .write(
                  "{\"message\":\"Token inválido ou expirado\",\"reason\":\""
                      + JwtErrorReason.INVALID_TOKEN.getCode()
                      + "\"}");
          return; // stop filter chain
        }

        var login = jwtUtil.getEmailFromToken(token);
        log.debug("Trying to authenticate user with email: {}", login);

        UserDetails user = usuarioRepository.findByEmail(login);

        if (user != null) {
          log.debug("User found: {}, authorities: {}", user.getUsername(), user.getAuthorities());
          var authentication =
              new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
          log.debug("Authentication set in SecurityContext");
        } else {
          log.warn("User not found for email: {}", login);
        }
      } catch (io.jsonwebtoken.ExpiredJwtException eje) {
        log.warn("JWT expired", eje);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response
            .getWriter()
            .write(
                "{\"message\":\"Token expirado\",\"reason\":\""
                    + JwtErrorReason.TOKEN_EXPIRED.getCode()
                    + "\"}");
        return;
      } catch (io.jsonwebtoken.security.SignatureException se) {
        log.warn("JWT signature invalid", se);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response
            .getWriter()
            .write(
                "{\"message\":\"Assinatura inválida\",\"reason\":\""
                    + JwtErrorReason.INVALID_SIGNATURE.getCode()
                    + "\"}");
        return;
      } catch (io.jsonwebtoken.MalformedJwtException mfe) {
        log.warn("JWT malformed", mfe);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response
            .getWriter()
            .write(
                "{\"message\":\"Token malformado\",\"reason\":\""
                    + JwtErrorReason.MALFORMED_TOKEN.getCode()
                    + "\"}");
        return;
      } catch (Exception e) {
        log.error("Error during JWT authentication", e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response
            .getWriter()
            .write(
                "{\"message\":\"Erro de autenticação JWT\",\"reason\":\""
                    + JwtErrorReason.AUTH_ERROR.getCode()
                    + "\"}");
        return;
      }
    } else {
      log.debug("No JWT token found in request");
    }

    // Log the current authentication status
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      log.debug(
          "Current authentication: {} with authorities: {}",
          authentication.getPrincipal(),
          authentication.getAuthorities());
    } else {
      log.debug("No authentication found in SecurityContext");
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }
}
