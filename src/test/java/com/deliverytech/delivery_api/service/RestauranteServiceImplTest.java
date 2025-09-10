package com.deliverytech.delivery_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.mapper.RestauranteMapper;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;
import java.math.BigDecimal;
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
@DisplayName("RestauranteServiceImpl")
class RestauranteServiceImplTest {

  @Mock private RestauranteRepository restauranteRepository;

  @Mock private RestauranteMapper restauranteMapper;

  @InjectMocks private RestauranteServiceImpl restauranteService;

  private Restaurante restaurante;
  private RestauranteRequest restauranteRequest;
  private RestauranteResponse restauranteResponse;

  @BeforeEach
  void setUp() {
    restaurante =
        Restaurante.builder()
            .id(1L)
            .nome("Restaurante Teste")
            .categoria("Italiana")
            .taxaEntrega(new BigDecimal("5.00"))
            .telefone("(11) 99999-9999")
            .email("teste@email.com")
            .tempoEntregaMinutos(30)
            .avaliacao(new BigDecimal("4.5"))
            .ativo(true)
            .excluido(false)
            .build();

    restauranteRequest = new RestauranteRequest();
    restauranteRequest.setNome("Restaurante Teste");
    restauranteRequest.setCategoria("Italiana");
    restauranteRequest.setTaxaEntrega(new BigDecimal("5.00"));
    restauranteRequest.setTelefone("(11) 99999-9999");
    restauranteRequest.setEmail("teste@email.com");
    restauranteRequest.setTempoEntregaMinutos(30);
    restauranteRequest.setAvaliacao(new BigDecimal("4.5"));

    restauranteResponse = new RestauranteResponse();
    restauranteResponse.setId(1L);
    restauranteResponse.setNome("Restaurante Teste");
    restauranteResponse.setCategoria("Italiana");
    restauranteResponse.setTaxaEntrega(new BigDecimal("5.00"));
    restauranteResponse.setTelefone("(11) 99999-9999");
    restauranteResponse.setEmail("teste@email.com");
    restauranteResponse.setTempoEntregaMinutos(30);
    restauranteResponse.setAvaliacao(new BigDecimal("4.5"));
    restauranteResponse.setAtivo(true);
  }

  @Nested
  @DisplayName("Cadastrar Restaurante")
  class CadastrarRestauranteTests {

    @Test
    @DisplayName("Deve cadastrar restaurante com dados válidos")
    void deveCadastrarRestauranteComDadosValidos() {
      // Arrange
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

      // Act
      Restaurante response = restauranteService.cadastrar(restauranteRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("Restaurante Teste");
      assertThat(response.getCategoria()).isEqualTo("Italiana");
      assertThat(response.getTaxaEntrega()).isEqualByComparingTo(new BigDecimal("5.00"));
      assertThat(response.isAtivo()).isTrue();

      verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
      // Arrange
      RestauranteRequest requestInvalido = new RestauranteRequest();
      requestInvalido.setNome(null);
      requestInvalido.setCategoria("Italiana");

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.cadastrar(requestInvalido))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("O nome do restaurante é obrigatório");

      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando nome é vazio")
    void deveLancarExcecaoQuandoNomeVazio() {
      // Arrange
      RestauranteRequest requestInvalido = new RestauranteRequest();
      requestInvalido.setNome("");
      requestInvalido.setCategoria("Italiana");

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.cadastrar(requestInvalido))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("O nome do restaurante é obrigatório");

      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve cadastrar restaurante com DTO usando mapper")
    void deveCadastrarRestauranteComDtoUsandoMapper() {
      // Arrange
      when(restauranteMapper.toEntity(restauranteRequest)).thenReturn(restaurante);
      when(restauranteRepository.save(restaurante)).thenReturn(restaurante);
      when(restauranteMapper.toResponse(restaurante)).thenReturn(restauranteResponse);

      // Act
      RestauranteResponse response = restauranteService.cadastrarRestaurante(restauranteRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("Restaurante Teste");
      assertThat(response.getCategoria()).isEqualTo("Italiana");

      verify(restauranteMapper).toEntity(restauranteRequest);
      verify(restauranteRepository).save(restaurante);
      verify(restauranteMapper).toResponse(restaurante);
    }
  }

  @Nested
  @DisplayName("Buscar Restaurante por ID")
  class BuscarPorIdTests {

    @Test
    @DisplayName("Deve retornar restaurante quando ID existe")
    void deveRetornarRestauranteQuandoIdExiste() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      Optional<Restaurante> response = restauranteService.buscarPorId(1L);

      // Assert
      assertThat(response).isPresent();
      assertThat(response.get().getNome()).isEqualTo("Restaurante Teste");

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando ID não existe")
    void deveRetornarOptionalVazioQuandoIdNaoExiste() {
      // Arrange
      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act
      Optional<Restaurante> response = restauranteService.buscarPorId(999L);

      // Assert
      assertThat(response).isEmpty();

      verify(restauranteRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve retornar restaurante com DTO quando ID existe")
    void deveRetornarRestauranteComDtoQuandoIdExiste() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
      when(restauranteMapper.toResponse(restaurante)).thenReturn(restauranteResponse);

      // Act
      RestauranteResponse response = restauranteService.buscarRestaurantePorId(1L);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("Restaurante Teste");

      verify(restauranteRepository).findById(1L);
      verify(restauranteMapper).toResponse(restaurante);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando ID não existe (DTO)")
    void deveLancarExcecaoQuandoIdNaoExisteDto() {
      // Arrange
      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.buscarRestaurantePorId(999L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Restaurante");

      verify(restauranteRepository).findById(999L);
      verify(restauranteMapper, never()).toResponse(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando restaurante está excluído")
    void deveLancarExcecaoQuandoRestauranteExcluido() {
      // Arrange
      Restaurante restauranteExcluido = Restaurante.builder().id(1L).excluido(true).build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteExcluido));

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.buscarRestaurantePorId(1L))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Restaurante foi excluído");

      verify(restauranteRepository).findById(1L);
      verify(restauranteMapper, never()).toResponse(any(Restaurante.class));
    }
  }

  @Nested
  @DisplayName("Listar Restaurantes")
  class ListarRestaurantesTests {

    @Test
    @DisplayName("Deve retornar lista de todos os restaurantes")
    void deveRetornarListaDeTodosRestaurantes() {
      // Arrange
      List<Restaurante> restaurantes = Arrays.asList(restaurante);
      when(restauranteRepository.findAll()).thenReturn(restaurantes);

      // Act
      List<Restaurante> response = restauranteService.listarTodos();

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getNome()).isEqualTo("Restaurante Teste");

      verify(restauranteRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista de restaurantes ativos")
    void deveRetornarListaDeRestaurantesAtivos() {
      // Arrange
      List<Restaurante> restaurantesAtivos = Arrays.asList(restaurante);
      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(restaurantesAtivos);

      // Act
      List<Restaurante> response = restauranteService.listarAtivos();

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).isAtivo()).isTrue();

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse();
    }

    @Test
    @DisplayName("Deve retornar página de restaurantes ativos")
    void deveRetornarPaginaDeRestaurantesAtivos() {
      // Arrange
      Pageable pageable = PageRequest.of(0, 10);
      Page<Restaurante> restaurantePage = new PageImpl<>(Arrays.asList(restaurante), pageable, 1);

      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse(pageable))
          .thenReturn(restaurantePage);

      // Act
      Page<Restaurante> response = restauranteService.listarAtivos(pageable);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getContent()).hasSize(1);

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse(pageable);
    }

    @Test
    @DisplayName("Deve retornar restaurantes disponíveis com DTO")
    void deveRetornarRestaurantesDisponiveisComDto() {
      // Arrange
      List<Restaurante> restaurantesAtivos = Arrays.asList(restaurante);
      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(restaurantesAtivos);
      when(restauranteMapper.toResponse(restaurante)).thenReturn(restauranteResponse);

      // Act
      List<RestauranteResponse> response = restauranteService.buscarRestaurantesDisponiveis();

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getNome()).isEqualTo("Restaurante Teste");

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse();
      verify(restauranteMapper).toResponse(restaurante);
    }
  }

  @Nested
  @DisplayName("Buscar por Categoria")
  class BuscarPorCategoriaTests {

    @Test
    @DisplayName("Deve retornar restaurantes por categoria")
    void deveRetornarRestaurantesPorCategoria() {
      // Arrange
      List<Restaurante> restaurantesCategoria = Arrays.asList(restaurante);
      when(restauranteRepository.findByCategoriaAndExcluidoFalse("Italiana"))
          .thenReturn(restaurantesCategoria);

      // Act
      List<Restaurante> response = restauranteService.buscarPorCategoria("Italiana");

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getCategoria()).isEqualTo("Italiana");

      verify(restauranteRepository).findByCategoriaAndExcluidoFalse("Italiana");
    }

    @Test
    @DisplayName("Deve retornar restaurantes por categoria com DTO")
    void deveRetornarRestaurantesPorCategoriaComDto() {
      // Arrange
      List<Restaurante> restaurantesCategoria = Arrays.asList(restaurante);
      when(restauranteRepository.findByCategoriaAndExcluidoFalse("Italiana"))
          .thenReturn(restaurantesCategoria);
      when(restauranteMapper.toResponse(restaurante)).thenReturn(restauranteResponse);

      // Act
      List<RestauranteResponse> response =
          restauranteService.buscarRestaurantesPorCategoria("Italiana");

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getCategoria()).isEqualTo("Italiana");

      verify(restauranteRepository).findByCategoriaAndExcluidoFalse("Italiana");
      verify(restauranteMapper).toResponse(restaurante);
    }

    @Test
    @DisplayName("Deve retornar página de restaurantes por categoria")
    void deveRetornarPaginaDeRestaurantesPorCategoria() {
      // Arrange
      Pageable pageable = PageRequest.of(0, 10);
      Page<Restaurante> restaurantePage = new PageImpl<>(Arrays.asList(restaurante), pageable, 1);

      when(restauranteRepository.findByCategoriaAndExcluidoFalse("Italiana", pageable))
          .thenReturn(restaurantePage);
      when(restauranteMapper.toResponse(restaurante)).thenReturn(restauranteResponse);

      // Act
      Page<RestauranteResponse> response =
          restauranteService.buscarRestaurantesPorCategoria("Italiana", pageable);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getContent()).hasSize(1);

      verify(restauranteRepository).findByCategoriaAndExcluidoFalse("Italiana", pageable);
      verify(restauranteMapper).toResponse(restaurante);
    }
  }

  @Nested
  @DisplayName("Buscar por Avaliação")
  class BuscarPorAvaliacaoTests {

    @Test
    @DisplayName("Deve retornar restaurantes com avaliação mínima")
    void deveRetornarRestaurantesComAvaliacaoMinima() {
      // Arrange
      BigDecimal minAvaliacao = new BigDecimal("4.0");
      List<Restaurante> restaurantesAvaliados = Arrays.asList(restaurante);

      when(restauranteRepository.findByAvaliacaoGreaterThanEqualAndExcluidoFalse(minAvaliacao))
          .thenReturn(restaurantesAvaliados);

      // Act
      List<Restaurante> response = restauranteService.buscarPorAvaliacao(minAvaliacao);

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getAvaliacao()).isGreaterThanOrEqualTo(minAvaliacao);

      verify(restauranteRepository).findByAvaliacaoGreaterThanEqualAndExcluidoFalse(minAvaliacao);
    }
  }

  @Nested
  @DisplayName("Buscar por Taxa de Entrega")
  class BuscarPorTaxaEntregaTests {

    @Test
    @DisplayName("Deve retornar restaurantes com taxa máxima")
    void deveRetornarRestaurantesComTaxaMaxima() {
      // Arrange
      BigDecimal maxTaxa = new BigDecimal("10.00");
      List<Restaurante> restaurantesTaxa = Arrays.asList(restaurante);

      when(restauranteRepository.findByTaxaEntregaLessThanEqualAndExcluidoFalse(maxTaxa))
          .thenReturn(restaurantesTaxa);

      // Act
      List<Restaurante> response = restauranteService.buscarPorTaxaEntrega(maxTaxa);

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getTaxaEntrega()).isLessThanOrEqualTo(maxTaxa);

      verify(restauranteRepository).findByTaxaEntregaLessThanEqualAndExcluidoFalse(maxTaxa);
    }
  }

  @Nested
  @DisplayName("Calcular Taxa de Entrega")
  class CalcularTaxaEntregaTests {

    @Test
    @DisplayName("Deve calcular taxa normal para região 1")
    void deveCalcularTaxaNormalParaRegiao1() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "11234-567");

      // Assert
      assertThat(taxa).isEqualByComparingTo(new BigDecimal("5.00"));

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve calcular taxa com multiplicador 1.20 para regiões 2-5")
    void deveCalcularTaxaComMultiplicador120ParaRegioes2a5() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "21234-567");

      // Assert
      assertThat(taxa).isEqualByComparingTo(new BigDecimal("6.00"));

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve calcular taxa com multiplicador 1.50 para regiões 6-9")
    void deveCalcularTaxaComMultiplicador150ParaRegioes6a9() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "61234-567");

      // Assert
      assertThat(taxa).isEqualByComparingTo(new BigDecimal("7.50"));

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve calcular taxa com multiplicador 2.00 para outras regiões")
    void deveCalcularTaxaComMultiplicador200ParaOutrasRegioes() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "01234-567");

      // Assert
      assertThat(taxa).isEqualByComparingTo(new BigDecimal("10.00"));

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
      // Arrange
      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.calcularTaxaEntrega(999L, "01234-567"))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Restaurante");

      verify(restauranteRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando restaurante não está ativo")
    void deveLancarExcecaoQuandoRestauranteNaoAtivo() {
      // Arrange
      Restaurante restauranteInativo =
          Restaurante.builder().id(1L).ativo(false).taxaEntrega(new BigDecimal("5.00")).build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteInativo));

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.calcularTaxaEntrega(1L, "01234-567"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Restaurante não está disponível");

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar taxa base quando CEP é inválido")
    void deveRetornarTaxaBaseQuandoCepInvalido() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "ABCDE-123");

      // Assert
      assertThat(taxa).isEqualByComparingTo(new BigDecimal("5.00"));

      verify(restauranteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve usar BigDecimal.ZERO quando taxaEntrega é nula")
    void deveUsarZeroQuandoTaxaEntregaNula() {
      // Arrange
      Restaurante restauranteSemTaxa =
          Restaurante.builder().id(1L).ativo(true).taxaEntrega(null).build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteSemTaxa));

      // Act
      BigDecimal taxa = restauranteService.calcularTaxaEntrega(1L, "01234-567");

      // Assert
      assertThat(taxa).isEqualByComparingTo(BigDecimal.ZERO);

      verify(restauranteRepository).findById(1L);
    }
  }

  @Nested
  @DisplayName("Buscar Próximos")
  class BuscarProximosTests {

    @Test
    @DisplayName("Deve retornar todos os restaurantes para regiões 1-5")
    void deveRetornarTodosRestaurantesParaRegioes1a5() {
      // Arrange
      List<Restaurante> restaurantesAtivos = Arrays.asList(restaurante);
      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(restaurantesAtivos);

      // Act
      List<Restaurante> response = restauranteService.buscarProximos("01234-567");

      // Assert
      assertThat(response).hasSize(1);

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse();
    }

    @Test
    @DisplayName("Deve filtrar restaurantes com taxa <= 10.00 para regiões 6+")
    void deveFiltrarRestaurantesComTaxaBaixaParaRegioes6Mais() {
      // Arrange
      Restaurante restauranteCaro =
          Restaurante.builder()
              .id(2L)
              .nome("Restaurante Caro")
              .taxaEntrega(new BigDecimal("15.00"))
              .ativo(true)
              .excluido(false)
              .build();

      List<Restaurante> restaurantesAtivos = Arrays.asList(restaurante, restauranteCaro);
      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(restaurantesAtivos);

      // Act
      List<Restaurante> response = restauranteService.buscarProximos("61234-567");

      // Assert
      assertThat(response).hasSize(1);
      assertThat(response.get(0).getNome()).isEqualTo("Restaurante Teste");

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse();
    }

    @Test
    @DisplayName("Deve retornar todos os restaurantes quando CEP é inválido")
    void deveRetornarTodosRestaurantesQuandoCepInvalido() {
      // Arrange
      List<Restaurante> restaurantesAtivos = Arrays.asList(restaurante);
      when(restauranteRepository.findByAtivoTrueAndExcluidoFalse()).thenReturn(restaurantesAtivos);

      // Act
      List<Restaurante> response = restauranteService.buscarProximos("ABCDE-123");

      // Assert
      assertThat(response).hasSize(1);

      verify(restauranteRepository).findByAtivoTrueAndExcluidoFalse();
    }
  }

  @Nested
  @DisplayName("Atualizar Restaurante")
  class AtualizarRestauranteTests {

    @Test
    @DisplayName("Deve atualizar restaurante com dados válidos")
    void deveAtualizarRestauranteComDadosValidos() {
      // Arrange
      RestauranteRequest updateRequest = new RestauranteRequest();
      updateRequest.setNome("Restaurante Atualizado");
      updateRequest.setCategoria("Japonesa");
      updateRequest.setTaxaEntrega(new BigDecimal("7.00"));

      Restaurante restauranteAtualizado =
          Restaurante.builder()
              .id(1L)
              .nome("Restaurante Atualizado")
              .categoria("Japonesa")
              .taxaEntrega(new BigDecimal("7.00"))
              .ativo(true)
              .excluido(false)
              .build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteAtualizado);

      // Act
      Restaurante response = restauranteService.atualizar(1L, updateRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("Restaurante Atualizado");
      assertThat(response.getCategoria()).isEqualTo("Japonesa");

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
      // Arrange
      RestauranteRequest updateRequest = new RestauranteRequest();
      updateRequest.setNome("Restaurante Atualizado");

      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.atualizar(999L, updateRequest))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Restaurante");

      verify(restauranteRepository).findById(999L);
      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve atualizar restaurante com DTO")
    void deveAtualizarRestauranteComDto() {
      // Arrange
      RestauranteRequest updateRequest = new RestauranteRequest();
      updateRequest.setNome("Restaurante Atualizado");
      updateRequest.setCategoria("Japonesa");

      Restaurante restauranteAtualizado =
          Restaurante.builder()
              .id(1L)
              .nome("Restaurante Atualizado")
              .categoria("Japonesa")
              .ativo(true)
              .excluido(false)
              .build();

      RestauranteResponse responseEsperado =
          RestauranteResponse.builder()
              .id(1L)
              .nome("Restaurante Atualizado")
              .categoria("Japonesa")
              .ativo(true)
              .build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteAtualizado);
      when(restauranteMapper.toResponse(restauranteAtualizado)).thenReturn(responseEsperado);

      // Act
      RestauranteResponse response = restauranteService.atualizarRestaurante(1L, updateRequest);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.getNome()).isEqualTo("Restaurante Atualizado");

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository).save(any(Restaurante.class));
      verify(restauranteMapper).toResponse(restauranteAtualizado);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando restaurante está excluído")
    void deveLancarExcecaoQuandoRestauranteExcluido() {
      // Arrange
      Restaurante restauranteExcluido = Restaurante.builder().id(1L).excluido(true).build();

      RestauranteRequest updateRequest = new RestauranteRequest();
      updateRequest.setNome("Restaurante Atualizado");

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteExcluido));

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.atualizarRestaurante(1L, updateRequest))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Não é possível atualizar restaurante excluído");

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }
  }

  @Nested
  @DisplayName("Inativar Restaurante")
  class InativarRestauranteTests {

    @Test
    @DisplayName("Deve inativar restaurante existente")
    void deveInativarRestauranteExistente() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

      // Act
      restauranteService.inativar(1L);

      // Assert
      assertThat(restaurante.isAtivo()).isFalse();
      assertThat(restaurante.getExcluido()).isTrue();

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository).save(restaurante);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
      // Arrange
      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.inativar(999L))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Restaurante");

      verify(restauranteRepository).findById(999L);
      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }
  }

  @Nested
  @DisplayName("Alterar Status")
  class AlterarStatusTests {

    @Test
    @DisplayName("Deve ativar restaurante")
    void deveAtivarRestaurante() {
      // Arrange
      Restaurante restauranteInativo = Restaurante.builder().id(1L).ativo(false).build();

      Restaurante restauranteAtivado = Restaurante.builder().id(1L).ativo(true).build();

      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteInativo));
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteAtivado);

      // Act
      Restaurante response = restauranteService.alterarStatus(1L, true);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.isAtivo()).isTrue();

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve desativar restaurante")
    void deveDesativarRestaurante() {
      // Arrange
      when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
      when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

      // Act
      Restaurante response = restauranteService.alterarStatus(1L, false);

      // Assert
      assertThat(response).isNotNull();
      assertThat(response.isAtivo()).isFalse();

      verify(restauranteRepository).findById(1L);
      verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
      // Arrange
      when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> restauranteService.alterarStatus(999L, true))
          .isInstanceOf(EntityNotFoundException.class)
          .hasMessageContaining("Restaurante");

      verify(restauranteRepository).findById(999L);
      verify(restauranteRepository, never()).save(any(Restaurante.class));
    }
  }
}
