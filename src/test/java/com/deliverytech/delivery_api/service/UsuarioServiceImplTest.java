package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.exception.EmailJaCadastradoException;
import com.deliverytech.delivery_api.exception.UsuarioNaoEncontradoException;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;
import com.deliverytech.delivery_api.util.JwtTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private RegisterRequest registerRequest;
    private Usuario testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setNome("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setSenha("password123");
        registerRequest.setRole(Role.CLIENTE);

        testUser = JwtTestUtils.createTestUsuario("test@example.com", Role.CLIENTE);
    }

    @Test
    void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {
        // Given
        when(usuarioRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);

        // When
        UserDetails userDetails = usuarioService.loadUserByUsername(testUser.getEmail());

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(testUser.getEmail());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        verify(usuarioRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void loadUserByUsername_WithNonExistingUser_ShouldThrowException() {
        // Given
        String email = "nonexistent@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        // When & Then
        assertThrows(UsernameNotFoundException.class, 
                () -> usuarioService.loadUserByUsername(email));
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void salvar_WithValidData_ShouldCreateUser() {
        // Given
        when(usuarioRepository.findUsuarioByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getSenha()))
                .thenReturn("$2a$10$encoded.password");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUser);

        // When
        Usuario savedUser = usuarioService.salvar(registerRequest);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        verify(usuarioRepository).findUsuarioByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getSenha());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void salvar_WithExistingEmail_ShouldThrowException() {
        // Given
        when(usuarioRepository.findUsuarioByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(EmailJaCadastradoException.class,
                () -> usuarioService.salvar(registerRequest));
        verify(usuarioRepository).findUsuarioByEmail(registerRequest.getEmail());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorEmail_WithExistingUser_ShouldReturnUser() {
        // Given
        when(usuarioRepository.findUsuarioByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));

        // When
        Optional<Usuario> result = usuarioService.buscarPorEmail(testUser.getEmail());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(testUser.getEmail());
        verify(usuarioRepository).findUsuarioByEmail(testUser.getEmail());
    }

    @Test
    void buscarPorEmail_WithNonExistingUser_ShouldReturnEmpty() {
        // Given
        String email = "nonexistent@example.com";
        when(usuarioRepository.findUsuarioByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> result = usuarioService.buscarPorEmail(email);

        // Then
        assertThat(result).isEmpty();
        verify(usuarioRepository).findUsuarioByEmail(email);
    }

    @Test
    void buscarPorId_WithExistingUser_ShouldReturnUser() {
        // Given
        when(usuarioRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        // When
        Optional<Usuario> result = usuarioService.buscarPorId(testUser.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testUser.getId());
        verify(usuarioRepository).findById(testUser.getId());
    }

    @Test
    void deletar_WithExistingUser_ShouldDeactivateUser() {
        // Given
        when(usuarioRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUser);

        // When
        usuarioService.deletar(testUser.getId());

        // Then
        verify(usuarioRepository).findById(testUser.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deletar_WithNonExistingUser_ShouldThrowException() {
        // Given
        Long userId = 999L;
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioService.deletar(userId));
        verify(usuarioRepository).findById(userId);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizar_WithValidData_ShouldUpdateUser() {
        // Given
        when(usuarioRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUser);

        // When
        Usuario updatedUser = usuarioService.atualizar(testUser.getId(), registerRequest);

        // Then
        assertThat(updatedUser).isNotNull();
        verify(usuarioRepository).findById(testUser.getId());
        verify(usuarioRepository).save(any(Usuario.class));
    }
}