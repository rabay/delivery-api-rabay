package com.deliverytech.delivery_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.exception.EmailDuplicadoException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.mapper.ClienteMapper;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteServiceImpl")
class ClienteServiceImplTest {

  @Mock private ClienteRepository clienteRepository;

  @Mock private ClienteMapper clienteMapper;

  @InjectMocks private ClienteServiceImpl clienteService;

  private Cliente cliente;
  private ClienteRequest clienteRequest;
  private ClienteResponse clienteResponse;

  @BeforeEach
  void setUp() {
    cliente =
        Cliente.builder()
            .id(1L)
            .nome("João Silva")
            .email("joao@email.com")
            .telefone("(11) 99999-9999")
            .ativo(true)
            .excluido(false)
            .build();

    clienteRequest = new ClienteRequest();
    clienteRequest.setNome("João Silva");
    clienteRequest.setEmail("joao@email.com");
    clienteRequest.setTelefone("(11) 99999-9999");

    clienteResponse = new ClienteResponse();
    clienteResponse.setId(1L);
    clienteResponse.setNome("João Silva");
    clienteResponse.setEmail("joao@email.com");
    clienteResponse.setTelefone("(11) 99999-9999");
    clienteResponse.setAtivo(true);
  }

  @Nested
  @DisplayName("Cadastrar Cliente")
  class CadastrarClienteTests {

    @Test
    @DisplayName("Deve cadastrar cliente com dados válidos")
    void deveCadastrarClienteComDadosValidos() {
      // Arrange
      when(clienteRepository.findByEmailAndExcluidoFalse(clienteRequest.getEmail()))
          .thenReturn(Optional.empty());
      when(clienteMapper.toEntity(clienteRequest)).thenReturn(cliente);
      when(clienteRepository.save(cliente)).thenReturn(cliente);
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      ClienteResponse response = clienteService.cadastrar(clienteRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("João Silva");
      assertThat(response.getEmail()).isEqualTo("joao@email.com");
      assertThat(response.getTelefone()).isEqualTo("(11) 99999-9999");
      assertThat(response.isAtivo()).isTrue();

      verify(clienteRepository).findByEmailAndExcluidoFalse(clienteRequest.getEmail());
      verify(clienteMapper).toEntity(clienteRequest);
      verify(clienteRepository).save(cliente);
      verify(clienteMapper).toResponse(cliente);
    }

    @Test
    @DisplayName("Deve lançar EmailDuplicadoException quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
      // Arrange
      Cliente clienteExistente = Cliente.builder().id(2L).email("joao@email.com").build();

      when(clienteRepository.findByEmailAndExcluidoFalse(clienteRequest.getEmail()))
          .thenReturn(Optional.of(clienteExistente));
      when(clienteMapper.toEntity(clienteRequest)).thenReturn(cliente);

      // Act & Assert
      assertThatThrownBy(() -> clienteService.cadastrar(clienteRequest))
          .isInstanceOf(EmailDuplicadoException.class)
          .hasMessageContaining("joao@email.com");

      verify(clienteRepository).findByEmailAndExcluidoFalse(clienteRequest.getEmail());
      verify(clienteRepository, never()).save(any(Cliente.class));
    }
  }

  @Nested
  @DisplayName("Buscar Cliente por ID")
  class BuscarPorIdTests {

    @Test
    @DisplayName("Deve retornar cliente quando ID existe e não está excluído")
    void deveRetornarClienteQuandoIdExisteENaoEstaExcluido() {
      // Arrange
      when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      ClienteResponse response = clienteService.buscarPorId(1L);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getId()).isEqualTo(1L);
      assertThat(response.getNome()).isEqualTo("João Silva");

      verify(clienteRepository).findById(1L);
      verify(clienteMapper).toResponse(cliente);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando ID não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
      // Arrange
      when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> clienteService.buscarPorId(999L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Cliente");

      verify(clienteRepository).findById(999L);
      verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente está excluído")
    void deveLancarExcecaoQuandoClienteEstaExcluido() {
      // Arrange
      Cliente clienteExcluido = Cliente.builder().id(1L).excluido(true).build();

      when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExcluido));

      // Act & Assert
      assertThatThrownBy(() -> clienteService.buscarPorId(1L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Cliente foi excluído");

      verify(clienteRepository).findById(1L);
      verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }
  }

  @Nested
  @DisplayName("Buscar Cliente por Email")
  class BuscarPorEmailTests {

    @Test
    @DisplayName("Deve retornar cliente quando email existe")
    void deveRetornarClienteQuandoEmailExiste() {
      // Arrange
      when(clienteRepository.findByEmailAndExcluidoFalse("joao@email.com"))
          .thenReturn(Optional.of(cliente));
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      Optional<ClienteResponse> response = clienteService.buscarPorEmail("joao@email.com");

      // Assert
      assertThat(response).isPresent();
      assertThat(response.get().getEmail()).isEqualTo("joao@email.com");

      verify(clienteRepository).findByEmailAndExcluidoFalse("joao@email.com");
      verify(clienteMapper).toResponse(cliente);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando email não existe")
    void deveRetornarOptionalVazioQuandoEmailNaoExiste() {
      // Arrange
      when(clienteRepository.findByEmailAndExcluidoFalse("naoexiste@email.com"))
          .thenReturn(Optional.empty());

      // Act
      Optional<ClienteResponse> response = clienteService.buscarPorEmail("naoexiste@email.com");

      // Assert
      assertThat(response).isEmpty();

      verify(clienteRepository).findByEmailAndExcluidoFalse("naoexiste@email.com");
      verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }
  }

  @Nested
  @DisplayName("Listar Clientes Ativos")
  class ListarAtivosTests {

    @Test
    @DisplayName("Deve retornar lista de clientes ativos")
    void deveRetornarListaDeClientesAtivos() {
      // Arrange
      List<Cliente> clientesAtivos = Arrays.asList(cliente);
      when(clienteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(clientesAtivos);
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      List<ClienteResponse> response = clienteService.listarAtivos();

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getNome()).isEqualTo("João Silva");

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse();
      verify(clienteMapper).toResponse(cliente);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há clientes ativos")
    void deveRetornarListaVaziaQuandoNaoHaClientesAtivos() {
      // Arrange
      when(clienteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(Arrays.asList());

      // Act
      List<ClienteResponse> response = clienteService.listarAtivos();

      // Assert
      assertThat(response).isEmpty();

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse();
      verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve retornar página de clientes ativos")
    void deveRetornarPaginaDeClientesAtivos() {
      // Arrange
      Pageable pageable = PageRequest.of(0, 10);
      Page<Cliente> clientePage = new PageImpl<>(Arrays.asList(cliente), pageable, 1);
      Page<ClienteResponse> expectedPage =
          new PageImpl<>(Arrays.asList(clienteResponse), pageable, 1);

      when(clienteRepository.findByAtivoTrueAndExcluidoFalse(pageable)).thenReturn(clientePage);
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      Page<ClienteResponse> response = clienteService.listarAtivos(pageable);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getContent()).hasSize(1);
      assertThat(response.getContent().get(0).getNome()).isEqualTo("João Silva");

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse(pageable);
      verify(clienteMapper).toResponse(cliente);
    }
  }

  @Nested
  @DisplayName("Buscar Cliente por Nome")
  class BuscarPorNomeTests {

    @Test
    @DisplayName("Deve retornar clientes que contenham o nome pesquisado")
    void deveRetornarClientesQueContenhamNomePesquisado() {
      // Arrange
      Cliente cliente2 =
          Cliente.builder()
              .id(2L)
              .nome("João Santos")
              .email("joao2@email.com")
              .ativo(true)
              .excluido(false)
              .build();

      ClienteResponse clienteResponse2 = new ClienteResponse();
      clienteResponse2.setId(2L);
      clienteResponse2.setNome("João Santos");
      clienteResponse2.setEmail("joao2@email.com");
      clienteResponse2.setAtivo(true);

      List<Cliente> clientesAtivos = Arrays.asList(cliente, cliente2);
      when(clienteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(clientesAtivos);
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);
      when(clienteMapper.toResponse(cliente2)).thenReturn(clienteResponse2);

      // Act
      List<ClienteResponse> response = clienteService.buscarPorNome("João");

      // Assert
      assertThat(response).hasSize(2);
      assertThat(response)
          .extracting("nome")
          .containsExactlyInAnyOrder("João Silva", "João Santos");

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nome não é encontrado")
    void deveRetornarListaVaziaQuandoNomeNaoEncontrado() {
      // Arrange
      when(clienteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(Arrays.asList(cliente));

      // Act
      List<ClienteResponse> response = clienteService.buscarPorNome("Maria");

      // Assert
      assertThat(response).isEmpty();

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse();
      verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve fazer busca case insensitive")
    void deveFazerBuscaCaseInsensitive() {
      // Arrange
      when(clienteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(Arrays.asList(cliente));
      when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

      // Act
      List<ClienteResponse> response = clienteService.buscarPorNome("joão");

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getNome()).isEqualTo("João Silva");

      verify(clienteRepository).findByAtivoTrueAndExcluidoFalse();
    }
  }

  @Nested
  @DisplayName("Atualizar Cliente")
  class AtualizarClienteTests {

    @Test
    @DisplayName("Deve atualizar cliente com dados válidos")
    void deveAtualizarClienteComDadosValidos() {
      // Arrange
      ClienteRequest updateRequest = new ClienteRequest();
      updateRequest.setNome("João Silva Atualizado");
      updateRequest.setEmail("joao.atualizado@email.com");
      updateRequest.setTelefone("(11) 88888-8888");

      Cliente clienteAtualizado =
          Cliente.builder()
              .id(1L)
              .nome("João Silva Atualizado")
              .email("joao.atualizado@email.com")
              .telefone("(11) 88888-8888")
              .ativo(true)
              .excluido(false)
              .build();

      ClienteResponse clienteResponseAtualizado = new ClienteResponse();
      clienteResponseAtualizado.setId(1L);
      clienteResponseAtualizado.setNome("João Silva Atualizado");
      clienteResponseAtualizado.setEmail("joao.atualizado@email.com");
      clienteResponseAtualizado.setTelefone("(11) 88888-8888");
      clienteResponseAtualizado.setAtivo(true);

      when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
      when(clienteMapper.toEntity(updateRequest)).thenReturn(clienteAtualizado);
      when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);
      when(clienteMapper.toResponse(clienteAtualizado)).thenReturn(clienteResponseAtualizado);

      // Act
      ClienteResponse response = clienteService.atualizar(1L, updateRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("João Silva Atualizado");
      assertThat(response.getEmail()).isEqualTo("joao.atualizado@email.com");

      verify(clienteRepository).findById(1L);
      verify(clienteMapper).toEntity(updateRequest);
      verify(clienteRepository).save(any(Cliente.class));
      verify(clienteMapper).toResponse(clienteAtualizado);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando tenta atualizar cliente inexistente")
    void deveLancarExcecaoQuandoAtualizarClienteInexistente() {
      // Arrange
      ClienteRequest updateRequest = new ClienteRequest();
      updateRequest.setNome("João Silva Atualizado");

      when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> clienteService.atualizar(999L, updateRequest))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Cliente");

      verify(clienteRepository).findById(999L);
      verify(clienteRepository, never()).save(any(Cliente.class));
    }
  }

  @Nested
  @DisplayName("Inativar Cliente")
  class InativarClienteTests {

    @Test
    @DisplayName("Deve inativar cliente ativo")
    void deveInativarClienteAtivo() {
      // Arrange
      when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
      when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

      // Act
      clienteService.inativar(1L);

      // Assert
      assertThat(cliente.isAtivo()).isFalse();
      assertThat(cliente.getExcluido()).isTrue();

      verify(clienteRepository).findById(1L);
      verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando tenta inativar cliente inexistente")
    void deveLancarExcecaoQuandoInativarClienteInexistente() {
      // Arrange
      when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> clienteService.inativar(999L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Cliente");

      verify(clienteRepository).findById(999L);
      verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Não deve alterar cliente já inativo")
    void naoDeveAlterarClienteJaInativo() {
      // Arrange
      Cliente clienteInativo = Cliente.builder().id(1L).ativo(false).excluido(true).build();

      when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteInativo));
      when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteInativo);

      // Act
      clienteService.inativar(1L);

      // Assert
      assertThat(clienteInativo.isAtivo()).isFalse();
      assertThat(clienteInativo.getExcluido()).isTrue();

      verify(clienteRepository).findById(1L);
      verify(clienteRepository).save(clienteInativo);
    }
  }

  @Nested
  @DisplayName("Ativar/Desativar Cliente")
  class AtivarDesativarClienteTests {

    @Test
    @DisplayName("Deve ativar cliente inativo")
    void deveAtivarClienteInativo() {
      // Arrange
      Cliente clienteInativo =
          Cliente.builder().id(1L).nome("João Silva").email("joao@email.com").ativo(false).build();

      Cliente clienteAtivado =
          Cliente.builder().id(1L).nome("João Silva").email("joao@email.com").ativo(true).build();

      ClienteResponse responseEsperado = new ClienteResponse();
      responseEsperado.setId(1L);
      responseEsperado.setNome("João Silva");
      responseEsperado.setEmail("joao@email.com");
      responseEsperado.setAtivo(true);

      when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteInativo));
      when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtivado);
      when(clienteMapper.toResponse(clienteAtivado)).thenReturn(responseEsperado);

      // Act
      ClienteResponse response = clienteService.ativarDesativarCliente(1L);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.isAtivo()).isTrue();

      verify(clienteRepository).findById(1L);
      verify(clienteRepository).save(any(Cliente.class));
      verify(clienteMapper).toResponse(clienteAtivado);
    }

    @Test
    @DisplayName("Deve desativar cliente ativo")
    void deveDesativarClienteAtivo() {
      // Arrange
      Cliente clienteDesativado =
          Cliente.builder().id(1L).nome("João Silva").email("joao@email.com").ativo(false).build();

      ClienteResponse responseEsperado = new ClienteResponse();
      responseEsperado.setId(1L);
      responseEsperado.setNome("João Silva");
      responseEsperado.setEmail("joao@email.com");
      responseEsperado.setAtivo(false);

      when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
      when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteDesativado);
      when(clienteMapper.toResponse(clienteDesativado)).thenReturn(responseEsperado);

      // Act
      ClienteResponse response = clienteService.ativarDesativarCliente(1L);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.isAtivo()).isFalse();

      verify(clienteRepository).findById(1L);
      verify(clienteRepository).save(any(Cliente.class));
      verify(clienteMapper).toResponse(clienteDesativado);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando cliente não existe")
    void deveLancarExcecaoQuandoClienteNaoExiste() {
      // Arrange
      when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> clienteService.ativarDesativarCliente(999L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Cliente");

      verify(clienteRepository).findById(999L);
      verify(clienteRepository, never()).save(any(Cliente.class));
    }
  }
}
