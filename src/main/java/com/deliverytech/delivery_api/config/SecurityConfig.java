package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    // Public endpoints - Authentication not required
                    .requestMatchers("/api/auth/login", "/api/auth/register")
                    .permitAll()
                    // Actuator endpoints
                    .requestMatchers("/actuator/prometheus", "/actuator/info", "/actuator/health")
                    .permitAll()
                    .requestMatchers("/actuator/**")
                    .hasRole("ADMIN")
                    // Database management endpoints - Public for testing/development
                    .requestMatchers("/db/**")
                    .permitAll()
                    // Client and Restaurant creation - Admin role required
                    .requestMatchers(HttpMethod.POST, "/api/clientes")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/restaurantes")
                    .hasRole("ADMIN")
                    // Public restaurant endpoints
                    .requestMatchers(
                        "/api/restaurantes/*/taxa-entrega/*", "/api/restaurantes/*/produtos")
                    .permitAll()
                    // Public produto endpoints - ONLY GET requests
                    .requestMatchers("/api/produtos")
                    .permitAll()
                    .requestMatchers("/api/produtos/categoria/*")
                    .permitAll()
                    .requestMatchers("/api/produtos/*/disponibilidade")
                    .permitAll()
                    // Public cliente endpoints - ONLY GET requests
                    .requestMatchers(HttpMethod.GET, "/api/clientes")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/clientes/{id}")
                    .permitAll()
                    // Public cliente endpoints - ONLY GET requests for pedidos
                    .requestMatchers("/api/clientes/*/pedidos")
                    .permitAll()
                    // Public produto endpoints - Allow retrieval of individual products
                    .requestMatchers(HttpMethod.GET, "/api/produtos/{id}")
                    .permitAll()
                    // Admin-only endpoints
                    .requestMatchers("/api/pedidos")
                    .hasRole("ADMIN")
                    // Swagger/OpenAPI endpoints - Documentation access
                    .requestMatchers(
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-ui/index.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/api-docs/**",
                        "/configuration/ui",
                        "/configuration/security")
                    .permitAll()
                    // All other endpoints require authentication
                    .anyRequest()
                    .authenticated())
        .headers(
            headers ->
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // For database
        // admin
        // interfaces
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}
