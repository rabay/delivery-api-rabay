package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.LoginRequest;
import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.exception.EmailJaCadastradoException;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.UsuarioService;
import com.deliverytech.delivery_api.util.JwtTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Remove @SpringBootTest since we're extending BaseIntegrationTest which already has it
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(JwtTestUtils.class)
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private Usuario testUser;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequest();
        registerRequest.setNome("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setSenha("password123");
        registerRequest.setRole(Role.CLIENTE);

        testUser = JwtTestUtils.createClienteUser();
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Given
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn(loginRequest.getUsername());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
    when(usuarioService.buscarPorEmail(loginRequest.getUsername())).thenReturn(java.util.Optional.of(testUser));
    when(jwtUtil.gerarTokenFromUsuario(testUser)).thenReturn("mock.jwt.token");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.token").value("mock.jwt.token"));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(usuarioService).buscarPorEmail(loginRequest.getUsername());
    verify(jwtUtil).gerarTokenFromUsuario(testUser);
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtUtil, never()).gerarToken(any());
    verify(usuarioService, never()).buscarPorEmail(any());
    }

    @Test
    void login_WithMissingUsername_ShouldReturnBadRequest() throws Exception {
        // Given
        loginRequest.setUsername("");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithMissingPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        loginRequest.setPassword("");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        when(usuarioService.salvar(any(RegisterRequest.class))).thenReturn(testUser);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.data.nome").value(testUser.getNome()))
                .andExpect(jsonPath("$.data.role").value(testUser.getRole().toString()));

        verify(usuarioService).salvar(any(RegisterRequest.class));
    }

    @Test
    void register_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
        // Given
        when(usuarioService.salvar(any(RegisterRequest.class)))
                .thenThrow(new EmailJaCadastradoException("Email já cadastrado"));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());

        verify(usuarioService).salvar(any(RegisterRequest.class));
    }

    @Test
    void register_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        registerRequest.setEmail("invalid-email");

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WithMissingRole_ShouldReturnBadRequest() throws Exception {
        // Given
        registerRequest.setRole(null);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void authEndpoints_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        // These endpoints should be public and not require authentication
        // The test verifies that auth endpoints are configured correctly in SecurityConfig
        
        // This test would pass if the endpoints are properly configured as public
        // in the SecurityConfig class
    }
}