package com.deliverytech.deliveryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança para a aplicação
 * Libera acesso aos endpoints de documentação (Swagger/OpenAPI) e Actuator para desenvolvimento
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura o PasswordEncoder para criptografar senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configura a cadeia de filtros de segurança
     * Para desenvolvimento: permite acesso a todos os endpoints sem autenticação
     * TODO: Implementar autenticação adequada para produção
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Durante desenvolvimento, permite acesso a todos os endpoints
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Desabilita CSRF para APIs REST
            
        return http.build();
    }
}
