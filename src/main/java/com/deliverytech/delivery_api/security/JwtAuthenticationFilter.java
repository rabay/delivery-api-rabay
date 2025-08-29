package com.deliverytech.delivery_api.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.deliverytech.delivery_api.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista de endpoints públicos que não precisam de autenticação
    private final List<String> publicEndpoints = Arrays.asList(
        "/api/auth/",
        "/health",
        "/info",
        "/actuator/",
        "/db/**",
        "/v3/api-docs",
        "/swagger-ui/",
        "/swagger-resources/",
        "/webjars/",
        "/api-docs/",
        "/configuration/"
    );

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // Pular autenticação para endpoints públicos
        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        var token = this.recoverToken(request);
        if (token != null){
            try {
                var login = jwtUtil.getEmailFromToken(token);
                log.debug("Trying to authenticate user with email: {}", login);
                
                UserDetails user = usuarioRepository.findByEmail(login);
                
                if (user != null) {
                    log.debug("User found: {}", user.getUsername());
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.warn("User not found for email: {}", login);
                }
            } catch (Exception e) {
                log.error("Error during JWT authentication", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestPath) {
        return publicEndpoints.stream().anyMatch(endpoint -> {
            if (endpoint.endsWith("/**")) {
                String prefix = endpoint.substring(0, endpoint.length() - 3);
                return requestPath.startsWith(prefix);
            }
            return requestPath.startsWith(endpoint) || 
                   (endpoint.endsWith("/") && requestPath.equals(endpoint.substring(0, endpoint.length() - 1)));
        });
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}