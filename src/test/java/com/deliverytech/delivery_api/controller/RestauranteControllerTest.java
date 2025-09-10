package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.request.StatusRequest;
import com.deliverytech.delivery_api.dto.response.*;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.LoggingService;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
      "spring.jpa.hibernate.ddl-auto=create-drop"
    })
@WithMockUser(
    username = "admin",
    roles = {"ADMIN"})
class RestauranteControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private RestauranteService restauranteService;

  @MockBean private ProdutoService produtoService;

  @SuppressWarnings("deprecation")
  @MockBean
  private LoggingService loggingService;

  @SuppressWarnings("deprecation")
  @MockBean
  private JwtUtil jwtUtil;

  @SuppressWarnings("deprecation")
  @MockBean
  private UsuarioRepository usuarioRepository;

  @MockBean private PedidoService pedidoService;

  private Restaurante restaurante;
  private RestauranteRequest restauranteRequest;

  @BeforeEach
  void setUp() {
    restaurante = new Restaurante();
    restaurante.setId(1L);
    restaurante.setNome("Restaurante Teste");
    restaurante.setCategoria("Italiana");
    restaurante.setEndereco("Rua Teste, 123");
    restaurante.setTaxaEntrega(BigDecimal.valueOf(5.00));
    restaurante.setTelefone("11999999999");
    restaurante.setEmail("teste@restaurante.com");
    restaurante.setTempoEntregaMinutos(30);
    restaurante.setAtivo(true);
    restaurante.setAvaliacao(BigDecimal.valueOf(4.5));
    restaurante.setExcluido(false);

    restauranteRequest = new RestauranteRequest();
    restauranteRequest.setNome("Novo Restaurante");
    restauranteRequest.setCategoria("Brasileira");
    restauranteRequest.setEndereco("Rua Nova, 456");
    restauranteRequest.setTaxaEntrega(BigDecimal.valueOf(7.50));
    restauranteRequest.setTelefone("11988888888");
    restauranteRequest.setEmail("novo@restaurante.com");
    restauranteRequest.setTempoEntregaMinutos(45);
  }

  @Test
  @DisplayName("Deve criar restaurante com sucesso")
  void deveCriarRestauranteComSucesso() throws Exception {
    when(restauranteService.cadastrar(any(RestauranteRequest.class))).thenReturn(restaurante);

    mockMvc
        .perform(
            post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteRequest))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Restaurante criado com sucesso"))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"));

    verify(restauranteService).cadastrar(any(RestauranteRequest.class));
  }

  @Test
  @DisplayName("Deve retornar erro ao criar restaurante com dados inválidos")
  void deveRetornarErroAoCriarRestauranteComDadosInvalidos() throws Exception {
    RestauranteRequest requestInvalido = new RestauranteRequest();
    // Request sem campos obrigatórios

    mockMvc
        .perform(
            post("/api/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
                .with(csrf()))
        .andExpect(status().isBadRequest());

    verify(restauranteService, never()).cadastrar(any(RestauranteRequest.class));
  }

  @Test
  @DisplayName("Deve listar restaurantes paginados")
  void deveListarRestaurantesPaginados() throws Exception {
    List<RestauranteResponse> restaurantes =
        Arrays.asList(
            new RestauranteResponse(
                1L,
                "Rest 1",
                "Italiana",
                "Rua 1",
                BigDecimal.valueOf(5.0),
                "11999999999",
                "rest1@email.com",
                30,
                BigDecimal.valueOf(4.5),
                true),
            new RestauranteResponse(
                2L,
                "Rest 2",
                "Chinesa",
                "Rua 2",
                BigDecimal.valueOf(6.0),
                "11888888888",
                "rest2@email.com",
                25,
                BigDecimal.valueOf(4.2),
                true));

    Page<RestauranteResponse> page = new PageImpl<>(restaurantes, PageRequest.of(0, 20), 2);
    when(restauranteService.buscarRestaurantesDisponiveis(any())).thenReturn(page);

    mockMvc
        .perform(get("/api/restaurantes").param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items").isArray())
        .andExpect(jsonPath("$.data.items.length()").value(2))
        .andExpect(jsonPath("$.data.totalItems").value(2));

    verify(restauranteService).buscarRestaurantesDisponiveis(any());
  }

  @Test
  @DisplayName("Deve buscar restaurante por ID existente")
  void deveBuscarRestaurantePorIdExistente() throws Exception {
    when(restauranteService.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

    mockMvc
        .perform(get("/api/restaurantes/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"));

    verify(restauranteService).buscarPorId(1L);
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar restaurante por ID inexistente")
  void deveRetornar404AoBuscarRestaurantePorIdInexistente() throws Exception {
    when(restauranteService.buscarPorId(999L)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/restaurantes/999"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Restaurante não encontrado"));

    verify(restauranteService).buscarPorId(999L);
  }

  @Test
  @DisplayName("Deve atualizar restaurante com sucesso")
  void deveAtualizarRestauranteComSucesso() throws Exception {
    Restaurante restauranteAtualizado = new Restaurante();
    restauranteAtualizado.setId(1L);
    restauranteAtualizado.setNome("Restaurante Atualizado");
    restauranteAtualizado.setCategoria("Brasileira");
    restauranteAtualizado.setAtivo(true);

    when(restauranteService.atualizar(eq(1L), any(RestauranteRequest.class)))
        .thenReturn(restauranteAtualizado);

    mockMvc
        .perform(
            put("/api/restaurantes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Restaurante atualizado com sucesso"))
        .andExpect(jsonPath("$.data.nome").value("Restaurante Atualizado"));

    verify(restauranteService).atualizar(eq(1L), any(RestauranteRequest.class));
  }

  @Test
  @DisplayName("Deve inativar restaurante com sucesso")
  void deveInativarRestauranteComSucesso() throws Exception {
    doNothing().when(restauranteService).inativar(1L);

    mockMvc
        .perform(delete("/api/restaurantes/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Restaurante inativado com sucesso"));

    verify(restauranteService).inativar(1L);
  }

  @Test
  @DisplayName("Deve buscar restaurantes por categoria")
  void deveBuscarRestaurantesPorCategoria() throws Exception {
    List<RestauranteResponse> restaurantes =
        Arrays.asList(
            new RestauranteResponse(
                1L,
                "Pizza Place",
                "Italiana",
                "Rua 1",
                BigDecimal.valueOf(5.0),
                "11999999999",
                "pizza@email.com",
                30,
                BigDecimal.valueOf(4.5),
                true));

    Page<RestauranteResponse> page = new PageImpl<>(restaurantes, PageRequest.of(0, 20), 1);
    when(restauranteService.buscarRestaurantesPorCategoria(eq("Italiana"), any())).thenReturn(page);

    mockMvc
        .perform(get("/api/restaurantes/categoria/Italiana").param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].categoria").value("Italiana"));

    verify(restauranteService).buscarRestaurantesPorCategoria(eq("Italiana"), any());
  }

  @Test
  @DisplayName("Deve alterar status do restaurante")
  void deveAlterarStatusDoRestaurante() throws Exception {
    StatusRequest statusRequest = new StatusRequest();
    statusRequest.setAtivo(false);

    Restaurante restauranteInativo = new Restaurante();
    restauranteInativo.setId(1L);
    restauranteInativo.setNome("Restaurante Teste");
    restauranteInativo.setAtivo(false);

    when(restauranteService.alterarStatus(1L, false)).thenReturn(restauranteInativo);

    mockMvc
        .perform(
            patch("/api/restaurantes/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Status atualizado"))
        .andExpect(jsonPath("$.data.ativo").value(false));

    verify(restauranteService).alterarStatus(1L, false);
  }

  @Test
  @DisplayName("Deve calcular taxa de entrega com sucesso")
  void deveCalcularTaxaEntregaComSucesso() throws Exception {
    BigDecimal taxa = BigDecimal.valueOf(8.50);
    when(restauranteService.calcularTaxaEntrega(1L, "01000-000")).thenReturn(taxa);

    mockMvc
        .perform(get("/api/restaurantes/1/taxa-entrega/01000-000"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.restauranteId").value(1))
        .andExpect(jsonPath("$.data.cep").value("01000-000"))
        .andExpect(jsonPath("$.data.taxaEntrega").value(8.5));

    verify(restauranteService).calcularTaxaEntrega(1L, "01000-000");
  }

  @Test
  @DisplayName("Deve retornar erro ao calcular taxa de entrega para restaurante inexistente")
  void deveRetornarErroAoCalcularTaxaEntregaParaRestauranteInexistente() throws Exception {
    when(restauranteService.calcularTaxaEntrega(999L, "01000-000"))
        .thenThrow(new RuntimeException("Restaurante não encontrado"));

    mockMvc
        .perform(get("/api/restaurantes/999/taxa-entrega/01000-000"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(
            jsonPath("$.message")
                .value("Restaurante não encontrado ou indisponível: Restaurante não encontrado"));

    verify(restauranteService).calcularTaxaEntrega(999L, "01000-000");
  }

  @Test
  @DisplayName("Deve buscar restaurantes próximos ao CEP")
  void deveBuscarRestaurantesProximosAoCep() throws Exception {
    List<RestauranteResponse> restaurantes =
        Arrays.asList(
            new RestauranteResponse(
                1L,
                "Rest Próximo",
                "Italiana",
                "Rua 1",
                BigDecimal.valueOf(5.0),
                "11999999999",
                "proximo@email.com",
                30,
                BigDecimal.valueOf(4.5),
                true));

    Page<RestauranteResponse> page = new PageImpl<>(restaurantes, PageRequest.of(0, 20), 1);
    when(restauranteService.buscarProximos(eq("01000-000"), any())).thenReturn(page);

    mockMvc
        .perform(get("/api/restaurantes/proximos/01000-000").param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Restaurantes próximos obtidos"));

    verify(restauranteService).buscarProximos(eq("01000-000"), any());
  }

  @Test
  @DisplayName("Deve listar pedidos do restaurante")
  void deveListarPedidosDoRestaurante() throws Exception {
    List<PedidoResponse> pedidos =
        Arrays.asList(
            new PedidoResponse(
                1L,
                null,
                null,
                null,
                BigDecimal.valueOf(25.00),
                null,
                StatusPedido.CRIADO,
                LocalDateTime.now(),
                Arrays.asList(
                    new ItemPedidoResponse(1L, "Produto 1", 1, BigDecimal.valueOf(25.00)))));

    Page<PedidoResponse> page = new PageImpl<>(pedidos, PageRequest.of(0, 20), 1);
    when(pedidoService.buscarPedidosPorRestaurante(eq(1L), any())).thenReturn(page);

    mockMvc
        .perform(get("/api/restaurantes/1/pedidos").param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Pedidos do restaurante obtidos"));

    verify(pedidoService).buscarPedidosPorRestaurante(eq(1L), any());
  }

  @Test
  @DisplayName("Deve listar produtos do restaurante")
  void deveListarProdutosDoRestaurante() throws Exception {
    when(restauranteService.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

    List<ProdutoResponse> produtos =
        Arrays.asList(
            new ProdutoResponse(
                1L,
                "Pizza Margherita",
                "Pizza",
                "Deliciosa pizza",
                BigDecimal.valueOf(35.00),
                new RestauranteResumoResponse(
                    1L,
                    "Restaurante Teste",
                    "Italiana",
                    BigDecimal.valueOf(5.0),
                    30,
                    BigDecimal.valueOf(4.5),
                    true),
                true,
                10));

    Page<ProdutoResponse> page = new PageImpl<>(produtos, PageRequest.of(0, 20), 1);
    when(produtoService.buscarProdutosPorRestaurante(eq(1L), any())).thenReturn(page);

    mockMvc
        .perform(get("/api/restaurantes/1/produtos").param("page", "0").param("size", "20"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Produtos do restaurante obtidos"))
        .andExpect(jsonPath("$.data.items[0].nome").value("Pizza Margherita"));

    verify(restauranteService).buscarPorId(1L);
    verify(produtoService).buscarProdutosPorRestaurante(eq(1L), any());
  }

  @Test
  @DisplayName("Deve retornar erro ao listar produtos de restaurante inexistente")
  void deveRetornarErroAoListarProdutosDeRestauranteInexistente() throws Exception {
    when(restauranteService.buscarPorId(999L)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/restaurantes/999/produtos"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Restaurante não encontrado"));

    verify(restauranteService).buscarPorId(999L);
    verify(produtoService, never()).buscarProdutosPorRestaurante(anyLong(), any());
  }
}
