package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Testes para SecurityUtils")
class SecurityUtilsTest {

    private Usuario usuarioAdmin;
    private Usuario usuarioCliente;
    private Usuario usuarioRestaurante;

    @BeforeEach
    void setUp() {
        // Criar usuários de teste
        usuarioAdmin = Usuario.builder()
                .id(1L)
                .email("admin@test.com")
                .role(Role.ADMIN)
                .build();

        usuarioCliente = Usuario.builder()
                .id(2L)
                .email("cliente@test.com")
                .role(Role.CLIENTE)
                .build();

        usuarioRestaurante = Usuario.builder()
                .id(3L)
                .email("restaurante@test.com")
                .role(Role.RESTAURANTE)
                .build();
    }

    @AfterEach
    void tearDown() {
        // Limpar contexto de segurança após cada teste
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve retornar usuário atual quando autenticado")
    void deveRetornarUsuarioAtualQuandoAutenticado() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioAdmin, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When
        var currentUser = SecurityUtils.getCurrentUser();

        // Then
        assertThat(currentUser).isPresent();
        assertThat(currentUser.get()).isEqualTo(usuarioAdmin);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando não autenticado")
    void deveRetornarOptionalEmptyQuandoNaoAutenticado() {
        // When
        var currentUser = SecurityUtils.getCurrentUser();

        // Then
        assertThat(currentUser).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar ID do usuário atual quando autenticado")
    void deveRetornarIdUsuarioAtualQuandoAutenticado() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioCliente, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When
        Long userId = SecurityUtils.getCurrentUserId();

        // Then
        assertThat(userId).isEqualTo(2L);
    }

    @Test
    @DisplayName("Deve lançar exception quando tenta obter ID sem autenticação")
    void deveLancarExceptionQuandoTentaObterIdSemAutenticacao() {
        // When & Then
        assertThatThrownBy(() -> SecurityUtils.getCurrentUserId())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuário não autenticado");
    }

    @Test
    @DisplayName("Deve verificar se usuário tem role específica")
    void deveVerificarSeUsuarioTemRoleEspecifica() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioAdmin, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When & Then
        assertThat(SecurityUtils.hasRole("ADMIN")).isTrue();
        assertThat(SecurityUtils.hasRole("CLIENTE")).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se usuário é admin")
    void deveVerificarSeUsuarioEAdmin() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioAdmin, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When & Then
        assertThat(SecurityUtils.isAdmin()).isTrue();
        assertThat(SecurityUtils.isCliente()).isFalse();
        assertThat(SecurityUtils.isRestaurante()).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se usuário é cliente")
    void deveVerificarSeUsuarioECliente() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioCliente, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When & Then
        assertThat(SecurityUtils.isCliente()).isTrue();
        assertThat(SecurityUtils.isAdmin()).isFalse();
        assertThat(SecurityUtils.isRestaurante()).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se usuário é restaurante")
    void deveVerificarSeUsuarioERestaurante() {
        // Given
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(usuarioRestaurante, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // When & Then
        assertThat(SecurityUtils.isRestaurante()).isTrue();
        assertThat(SecurityUtils.isAdmin()).isFalse();
        assertThat(SecurityUtils.isCliente()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para verificações de role quando não autenticado")
    void deveRetornarFalseParaVerificacoesRoleQuandoNaoAutenticado() {
        // When & Then
        assertThat(SecurityUtils.isAdmin()).isFalse();
        assertThat(SecurityUtils.isCliente()).isFalse();
        assertThat(SecurityUtils.isRestaurante()).isFalse();
        assertThat(SecurityUtils.hasRole("ADMIN")).isFalse();
    }
}
