package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.exception.EmailJaCadastradoException;
import com.deliverytech.delivery_api.exception.UsuarioNaoEncontradoException;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioServiceImpl")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .role(Role.CLIENTE)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .restauranteId(1L)
                .build();

        registerRequest = new RegisterRequest();
        registerRequest.setNome("João Silva");
        registerRequest.setEmail("joao@email.com");
        registerRequest.setSenha("senha123");
        registerRequest.setRole(Role.CLIENTE);
        registerRequest.setRestauranteId(1L);
    }

    @Nested
    @DisplayName("Load User by Username")
    class LoadUserByUsernameTests {

        @Test
        @DisplayName("Deve retornar UserDetails quando usuário existe")
        void deveRetornarUserDetailsQuandoUsuarioExiste() {
            // Arrange
            when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(usuario);

            // Act
            UserDetails userDetails = usuarioService.loadUserByUsername("joao@email.com");

            // Assert
            assertThat(userDetails).isNotNull();
            assertThat(userDetails.getUsername()).isEqualTo("joao@email.com");

            verify(usuarioRepository).findByEmail("joao@email.com");
        }

        @Test
        @DisplayName("Deve lançar UsernameNotFoundException quando usuário não existe")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            // Arrange
            when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.loadUserByUsername("naoexiste@email.com"))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("Usuário não encontrado: naoexiste@email.com");

            verify(usuarioRepository).findByEmail("naoexiste@email.com");
        }
    }

    @Nested
    @DisplayName("Salvar Usuário")
    class SalvarUsuarioTests {

        @Test
        @DisplayName("Deve salvar usuário com dados válidos")
        void deveSalvarUsuarioComDadosValidos() {
            // Arrange
            Usuario usuarioSalvo = Usuario.builder()
                    .id(1L)
                    .nome("João Silva")
                    .email("joao@email.com")
                    .senha("encodedPassword")
                    .role(Role.CLIENTE)
                    .ativo(true)
                    .dataCriacao(LocalDateTime.now())
                    .restauranteId(1L)
                    .build();

            when(usuarioRepository.findUsuarioByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode("senha123")).thenReturn("encodedPassword");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            // Act
            Usuario response = usuarioService.salvar(registerRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getNome()).isEqualTo("João Silva");
            assertThat(response.getEmail()).isEqualTo("joao@email.com");
            assertThat(response.getRole()).isEqualTo(Role.CLIENTE);
            assertThat(response.getAtivo()).isTrue();

            verify(usuarioRepository).findUsuarioByEmail(registerRequest.getEmail());
            verify(passwordEncoder).encode("senha123");
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve lançar EmailJaCadastradoException quando email já existe")
        void deveLancarExcecaoQuandoEmailJaExiste() {
            // Arrange
            when(usuarioRepository.findUsuarioByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(usuario));

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.salvar(registerRequest))
                    .isInstanceOf(EmailJaCadastradoException.class)
                    .hasMessageContaining("joao@email.com");

            verify(usuarioRepository).findUsuarioByEmail(registerRequest.getEmail());
            verify(passwordEncoder, never()).encode(anyString());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Buscar por Email")
    class BuscarPorEmailTests {

        @Test
        @DisplayName("Deve retornar usuário quando email existe")
        void deveRetornarUsuarioQuandoEmailExiste() {
            // Arrange
            when(usuarioRepository.findUsuarioByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

            // Act
            Optional<Usuario> response = usuarioService.buscarPorEmail("joao@email.com");

            // Assert
            assertThat(response).isPresent();
            assertThat(response.get().getNome()).isEqualTo("João Silva");

            verify(usuarioRepository).findUsuarioByEmail("joao@email.com");
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando email não existe")
        void deveRetornarOptionalVazioQuandoEmailNaoExiste() {
            // Arrange
            when(usuarioRepository.findUsuarioByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

            // Act
            Optional<Usuario> response = usuarioService.buscarPorEmail("naoexiste@email.com");

            // Assert
            assertThat(response).isEmpty();

            verify(usuarioRepository).findUsuarioByEmail("naoexiste@email.com");
        }
    }

    @Nested
    @DisplayName("Buscar por ID")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve retornar usuário quando ID existe")
        void deveRetornarUsuarioQuandoIdExiste() {
            // Arrange
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            // Act
            Optional<Usuario> response = usuarioService.buscarPorId(1L);

            // Assert
            assertThat(response).isPresent();
            assertThat(response.get().getId()).isEqualTo(1L);

            verify(usuarioRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando ID não existe")
        void deveRetornarOptionalVazioQuandoIdNaoExiste() {
            // Arrange
            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Optional<Usuario> response = usuarioService.buscarPorId(999L);

            // Assert
            assertThat(response).isEmpty();

            verify(usuarioRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Listar Todos")
    class ListarTodosTests {

        @Test
        @DisplayName("Deve retornar lista de todos os usuários")
        void deveRetornarListaDeTodosUsuarios() {
            // Arrange
            List<Usuario> usuarios = Arrays.asList(usuario);
            when(usuarioRepository.findAll()).thenReturn(usuarios);

            // Act
            List<Usuario> response = usuarioService.listarTodos();

            // Assert
            assertThat(response).hasSize(1);
            assertThat(response.get(0).getNome()).isEqualTo("João Silva");

            verify(usuarioRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há usuários")
        void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
            // Arrange
            when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

            // Act
            List<Usuario> response = usuarioService.listarTodos();

            // Assert
            assertThat(response).isEmpty();

            verify(usuarioRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Atualizar Usuário")
    class AtualizarUsuarioTests {

        @Test
        @DisplayName("Deve atualizar usuário com dados válidos")
        void deveAtualizarUsuarioComDadosValidos() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");
            updateRequest.setEmail("joao.atualizado@email.com");
            updateRequest.setSenha("novaSenha123");
            updateRequest.setRole(Role.RESTAURANTE);
            updateRequest.setRestauranteId(2L);

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(1L)
                    .nome("João Silva Atualizado")
                    .email("joao.atualizado@email.com")
                    .senha("encodedNovaSenha")
                    .role(Role.RESTAURANTE)
                    .ativo(true)
                    .restauranteId(2L)
                    .build();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.findUsuarioByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode("novaSenha123")).thenReturn("encodedNovaSenha");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            // Act
            Usuario response = usuarioService.atualizar(1L, updateRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getNome()).isEqualTo("João Silva Atualizado");
            assertThat(response.getEmail()).isEqualTo("joao.atualizado@email.com");
            assertThat(response.getRole()).isEqualTo(Role.RESTAURANTE);

            verify(usuarioRepository).findById(1L);
            verify(usuarioRepository).findUsuarioByEmail(updateRequest.getEmail());
            verify(passwordEncoder).encode("novaSenha123");
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve atualizar usuário sem alterar senha quando senha é nula")
        void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaNula() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");
            updateRequest.setEmail("joao.atualizado@email.com");
            updateRequest.setSenha(null);
            updateRequest.setRole(Role.RESTAURANTE);
            updateRequest.setRestauranteId(2L);

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(1L)
                    .nome("João Silva Atualizado")
                    .email("joao.atualizado@email.com")
                    .senha("senha123") // Mantém senha original
                    .role(Role.RESTAURANTE)
                    .ativo(true)
                    .restauranteId(2L)
                    .build();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.findUsuarioByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            // Act
            Usuario response = usuarioService.atualizar(1L, updateRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getNome()).isEqualTo("João Silva Atualizado");
            assertThat(response.getEmail()).isEqualTo("joao.atualizado@email.com");

            verify(passwordEncoder, never()).encode(anyString());
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve atualizar usuário sem alterar senha quando senha é vazia")
        void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaVazia() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");
            updateRequest.setEmail("joao.atualizado@email.com");
            updateRequest.setSenha("");
            updateRequest.setRole(Role.RESTAURANTE);
            updateRequest.setRestauranteId(2L);

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(1L)
                    .nome("João Silva Atualizado")
                    .email("joao.atualizado@email.com")
                    .senha("senha123") // Mantém senha original
                    .role(Role.RESTAURANTE)
                    .ativo(true)
                    .restauranteId(2L)
                    .build();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.findUsuarioByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            // Act
            Usuario response = usuarioService.atualizar(1L, updateRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getNome()).isEqualTo("João Silva Atualizado");

            verify(passwordEncoder, never()).encode(anyString());
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve atualizar usuário sem alterar email quando email é o mesmo")
        void deveAtualizarUsuarioSemAlterarEmailQuandoEmailMesmo() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");
            updateRequest.setEmail("joao@email.com"); // Mesmo email
            updateRequest.setSenha("novaSenha123");
            updateRequest.setRole(Role.RESTAURANTE);
            updateRequest.setRestauranteId(2L);

            Usuario usuarioAtualizado = Usuario.builder()
                    .id(1L)
                    .nome("João Silva Atualizado")
                    .email("joao@email.com")
                    .senha("encodedNovaSenha")
                    .role(Role.RESTAURANTE)
                    .ativo(true)
                    .restauranteId(2L)
                    .build();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.encode("novaSenha123")).thenReturn("encodedNovaSenha");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

            // Act
            Usuario response = usuarioService.atualizar(1L, updateRequest);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getNome()).isEqualTo("João Silva Atualizado");

            verify(usuarioRepository, never()).findUsuarioByEmail(anyString());
            verify(passwordEncoder).encode("novaSenha123");
            verify(usuarioRepository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontradoException quando usuário não existe")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");

            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.atualizar(999L, updateRequest))
                    .isInstanceOf(UsuarioNaoEncontradoException.class);

            verify(usuarioRepository).findById(999L);
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Deve lançar EmailJaCadastradoException quando novo email já existe")
        void deveLancarExcecaoQuandoNovoEmailJaExiste() {
            // Arrange
            RegisterRequest updateRequest = new RegisterRequest();
            updateRequest.setNome("João Silva Atualizado");
            updateRequest.setEmail("email.existente@email.com");

            Usuario outroUsuario = Usuario.builder()
                    .id(2L)
                    .email("email.existente@email.com")
                    .build();

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.findUsuarioByEmail(updateRequest.getEmail()))
                    .thenReturn(Optional.of(outroUsuario));

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.atualizar(1L, updateRequest))
                    .isInstanceOf(EmailJaCadastradoException.class)
                    .hasMessageContaining("email.existente@email.com");

            verify(usuarioRepository).findById(1L);
            verify(usuarioRepository).findUsuarioByEmail(updateRequest.getEmail());
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }

    @Nested
    @DisplayName("Deletar Usuário")
    class DeletarUsuarioTests {

        @Test
        @DisplayName("Deve fazer soft delete do usuário")
        void deveFazerSoftDeleteDoUsuario() {
            // Arrange
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

            // Act
            usuarioService.deletar(1L);

            // Assert
            assertThat(usuario.getAtivo()).isFalse();

            verify(usuarioRepository).findById(1L);
            verify(usuarioRepository).save(usuario);
        }

        @Test
        @DisplayName("Deve lançar UsuarioNaoEncontradoException quando usuário não existe")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            // Arrange
            when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.deletar(999L))
                    .isInstanceOf(UsuarioNaoEncontradoException.class);

            verify(usuarioRepository).findById(999L);
            verify(usuarioRepository, never()).save(any(Usuario.class));
        }
    }
}