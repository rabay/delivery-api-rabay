package com.deliverytech.delivery_api.util;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.security.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;

/**
 * JWT Test Utilities for creating test tokens and authentication contexts
 */
public class JwtTestUtils {

    private static final String TEST_SECRET = "my-secret-keyQWERTYUIOPASDFGHJKLZXCVBNMQWERTY1";
    private static final Long TEST_EXPIRATION = 86400000L; // 24 hours

    /**
     * Creates a test JWT utility instance
     */
    public static JwtUtil createTestJwtUtil() {
        return new TestJwtUtil();
    }

    /**
     * Creates a test Usuario for authentication
     */
    public static Usuario createTestUsuario(String email, Role role) {
        return Usuario.builder()
                .id(1L)
                .nome("Test User")
                .email(email)
                .senha("$2a$10$encrypted.password.hash")
                .role(role)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    /**
     * Creates an admin test user
     */
    public static Usuario createAdminUser() {
        return createTestUsuario("admin@test.com", Role.ADMIN);
    }

    /**
     * Creates a cliente test user
     */
    public static Usuario createClienteUser() {
        return createTestUsuario("cliente@test.com", Role.CLIENTE);
    }

    /**
     * Creates a restaurante test user
     */
    public static Usuario createRestauranteUser() {
        return createTestUsuario("restaurante@test.com", Role.RESTAURANTE);
    }

    /**
     * Sets up authentication context with the given user
     */
    public static void setupAuthenticationContext(Usuario usuario) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario, null, usuario.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * Clears the authentication context
     */
    public static void clearAuthenticationContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Test implementation of JwtUtil for testing purposes
     */
    private static class TestJwtUtil extends JwtUtil {
        // This uses reflection to set the private fields for testing
        // In a real scenario, you might want to use @TestPropertySource
        // or create a test configuration
    }
}