package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario Entity Tests")
class UsuarioTest {

    @Nested
    @DisplayName("Constructor and Builder Tests")
    class ConstructorAndBuilderTests {

        @Test
        @DisplayName("Should create Usuario with no-args constructor")
        void shouldCreateUsuarioWithNoArgsConstructor() {
            // When
            Usuario usuario = new Usuario();

            // Then
            assertNotNull(usuario);
            assertNull(usuario.getId());
            assertNull(usuario.getNome());
            assertNull(usuario.getEmail());
            assertNull(usuario.getSenha());
            assertNull(usuario.getRole());
            assertNull(usuario.getAtivo());
            assertNull(usuario.getDataCriacao());
            assertNull(usuario.getRestauranteId());
        }

        @Test
        @DisplayName("Should create Usuario with all-args constructor")
        void shouldCreateUsuarioWithAllArgsConstructor() {
            // Given
            Long id = 1L;
            String nome = "João Silva";
            String email = "joao@email.com";
            String senha = "senha123";
            Role role = Role.CLIENTE;
            Boolean ativo = true;
            LocalDateTime dataCriacao = LocalDateTime.now();
            Long restauranteId = 2L;

            // When
            Usuario usuario = new Usuario(id, nome, email, senha, role, ativo, dataCriacao, restauranteId);

            // Then
            assertEquals(id, usuario.getId());
            assertEquals(nome, usuario.getNome());
            assertEquals(email, usuario.getEmail());
            assertEquals(senha, usuario.getSenha());
            assertEquals(role, usuario.getRole());
            assertEquals(ativo, usuario.getAtivo());
            assertEquals(dataCriacao, usuario.getDataCriacao());
            assertEquals(restauranteId, usuario.getRestauranteId());
        }

        @Test
        @DisplayName("Should create Usuario using builder pattern")
        void shouldCreateUsuarioUsingBuilderPattern() {
            // Given
            LocalDateTime dataCriacao = LocalDateTime.now();

            // When
            Usuario usuario = Usuario.builder()
                    .id(1L)
                    .nome("Maria Santos")
                    .email("maria@email.com")
                    .senha("senha456")
                    .role(Role.RESTAURANTE)
                    .ativo(true)
                    .dataCriacao(dataCriacao)
                    .restauranteId(3L)
                    .build();

            // Then
            assertEquals(1L, usuario.getId());
            assertEquals("Maria Santos", usuario.getNome());
            assertEquals("maria@email.com", usuario.getEmail());
            assertEquals("senha456", usuario.getSenha());
            assertEquals(Role.RESTAURANTE, usuario.getRole());
            assertTrue(usuario.getAtivo());
            assertEquals(dataCriacao, usuario.getDataCriacao());
            assertEquals(3L, usuario.getRestauranteId());
        }
    }

    @Nested
    @DisplayName("UserDetails Implementation Tests")
    class UserDetailsImplementationTests {

        @Test
        @DisplayName("Should return email as username")
        void shouldReturnEmailAsUsername() {
            // Given
            Usuario usuario = Usuario.builder()
                    .email("teste@email.com")
                    .build();

            // When
            String username = usuario.getUsername();

            // Then
            assertEquals("teste@email.com", username);
        }

        @Test
        @DisplayName("Should return senha as password")
        void shouldReturnSenhaAsPassword() {
            // Given
            Usuario usuario = Usuario.builder()
                    .senha("minhaSenha")
                    .build();

            // When
            String password = usuario.getPassword();

            // Then
            assertEquals("minhaSenha", password);
        }

        @Test
        @DisplayName("Should return role as single authority")
        void shouldReturnRoleAsSingleAuthority() {
            // Given
            Usuario usuario = Usuario.builder()
                    .role(Role.ADMIN)
                    .build();

            // When
            Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

            // Then
            assertNotNull(authorities);
            assertEquals(1, authorities.size());
            assertEquals(Role.ADMIN, authorities.iterator().next());
        }

        @Test
        @DisplayName("Should return ativo status for account non expired")
        void shouldReturnAtivoStatusForAccountNonExpired() {
            // Given
            Usuario usuarioAtivo = Usuario.builder().ativo(true).build();
            Usuario usuarioInativo = Usuario.builder().ativo(false).build();

            // When & Then
            assertTrue(usuarioAtivo.isAccountNonExpired());
            assertFalse(usuarioInativo.isAccountNonExpired());
        }

        @Test
        @DisplayName("Should return ativo status for account non locked")
        void shouldReturnAtivoStatusForAccountNonLocked() {
            // Given
            Usuario usuarioAtivo = Usuario.builder().ativo(true).build();
            Usuario usuarioInativo = Usuario.builder().ativo(false).build();

            // When & Then
            assertTrue(usuarioAtivo.isAccountNonLocked());
            assertFalse(usuarioInativo.isAccountNonLocked());
        }

        @Test
        @DisplayName("Should return ativo status for credentials non expired")
        void shouldReturnAtivoStatusForCredentialsNonExpired() {
            // Given
            Usuario usuarioAtivo = Usuario.builder().ativo(true).build();
            Usuario usuarioInativo = Usuario.builder().ativo(false).build();

            // When & Then
            assertTrue(usuarioAtivo.isCredentialsNonExpired());
            assertFalse(usuarioInativo.isCredentialsNonExpired());
        }

        @Test
        @DisplayName("Should return ativo status for enabled")
        void shouldReturnAtivoStatusForEnabled() {
            // Given
            Usuario usuarioAtivo = Usuario.builder().ativo(true).build();
            Usuario usuarioInativo = Usuario.builder().ativo(false).build();

            // When & Then
            assertTrue(usuarioAtivo.isEnabled());
            assertFalse(usuarioInativo.isEnabled());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when same id")
        void shouldBeEqualWhenSameId() {
            // Given
            Usuario usuario1 = Usuario.builder().id(1L).build();
            Usuario usuario2 = Usuario.builder().id(1L).build();

            // When & Then
            assertEquals(usuario1, usuario2);
            assertEquals(usuario1.hashCode(), usuario2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different id")
        void shouldNotBeEqualWhenDifferentId() {
            // Given
            Usuario usuario1 = Usuario.builder().id(1L).build();
            Usuario usuario2 = Usuario.builder().id(2L).build();

            // When & Then
            assertNotEquals(usuario1, usuario2);
        }

        @Test
        @DisplayName("Should not be equal when one has null id")
        void shouldNotBeEqualWhenOneHasNullId() {
            // Given
            Usuario usuario1 = Usuario.builder().id(1L).build();
            Usuario usuario2 = Usuario.builder().id(null).build();

            // When & Then
            assertNotEquals(usuario1, usuario2);
        }

        @Test
        @DisplayName("Should be equal when both have null id but same other fields")
        void shouldBeEqualWhenBothHaveNullIdButSameOtherFields() {
            // Given
            Usuario usuario1 = Usuario.builder()
                    .id(null)
                    .nome("João")
                    .email("joao@email.com")
                    .build();
            Usuario usuario2 = Usuario.builder()
                    .id(null)
                    .nome("João")
                    .email("joao@email.com")
                    .build();

            // When & Then
            assertEquals(usuario1, usuario2);
            assertEquals(usuario1.hashCode(), usuario2.hashCode());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterAndSetterTests {

        @Test
        @DisplayName("Should set and get all fields correctly")
        void shouldSetAndGetAllFieldsCorrectly() {
            // Given
            Usuario usuario = new Usuario();
            LocalDateTime dataCriacao = LocalDateTime.now();

            // When
            usuario.setId(1L);
            usuario.setNome("Carlos Silva");
            usuario.setEmail("carlos@email.com");
            usuario.setSenha("senha789");
            usuario.setRole(Role.CLIENTE);
            usuario.setAtivo(true);
            usuario.setDataCriacao(dataCriacao);
            usuario.setRestauranteId(5L);

            // Then
            assertEquals(1L, usuario.getId());
            assertEquals("Carlos Silva", usuario.getNome());
            assertEquals("carlos@email.com", usuario.getEmail());
            assertEquals("senha789", usuario.getSenha());
            assertEquals(Role.CLIENTE, usuario.getRole());
            assertTrue(usuario.getAtivo());
            assertEquals(dataCriacao, usuario.getDataCriacao());
            assertEquals(5L, usuario.getRestauranteId());
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should handle restaurante association correctly")
        void shouldHandleRestauranteAssociationCorrectly() {
            // Given
            Usuario usuarioCliente = Usuario.builder()
                    .role(Role.CLIENTE)
                    .restauranteId(null) // Cliente não tem restaurante
                    .build();

            Usuario usuarioRestaurante = Usuario.builder()
                    .role(Role.RESTAURANTE)
                    .restauranteId(10L) // Restaurante tem ID associado
                    .build();

            // Then
            assertNull(usuarioCliente.getRestauranteId());
            assertEquals(10L, usuarioRestaurante.getRestauranteId());
        }

        @Test
        @DisplayName("Should handle dataCriacao timestamp")
        void shouldHandleDataCriacaoTimestamp() {
            // Given
            LocalDateTime now = LocalDateTime.now();
            Usuario usuario = Usuario.builder()
                    .dataCriacao(now)
                    .build();

            // Then
            assertEquals(now, usuario.getDataCriacao());
        }
    }
}