package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.StatusUpdateRequest;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.LoggingService;
import com.deliverytech.delivery_api.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@SuppressWarnings("deprecation")
public class PedidoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PedidoService pedidoService;

  @MockBean private LoggingService loggingService;

  @MockBean private JwtUtil jwtUtil;

  @MockBean private UsuarioRepository usuarioRepository;

  @MockBean private PedidoRepository pedidoRepository;

  @Autowired private ObjectMapper objectMapper;

  private Pedido pedido;
  private PedidoResponse pedidoResponse;
  private PedidoRequest pedidoRequest;

  @BeforeEach
  void setUp() {
    // Setup Cliente
    Cliente cliente = new Cliente();
    cliente.setId(1L);
    cliente.setNome("João Silva");
    cliente.setEmail("joao@email.com");
    cliente.setTelefone("11988888888");

    // Setup Restaurante
    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);
    restaurante.setNome("Restaurante Teste");
    restaurante.setTelefone("11999999999");

    // Setup Produto
    Produto produto = new Produto();
    produto.setId(1L);
    produto.setNome("Pizza Margherita");
    produto.setPreco(BigDecimal.valueOf(25.00));

    // Setup ItemPedido
    ItemPedido itemPedido = new ItemPedido();
    itemPedido.setId(1L);
    itemPedido.setProduto(produto);
    itemPedido.setQuantidade(2);
    itemPedido.setPrecoUnitario(BigDecimal.valueOf(25.00));

    // Setup Endereco
    Endereco endereco = new Endereco();
    endereco.setRua("Rua Teste");
    endereco.setNumero("123");
    endereco.setBairro("Centro");
    endereco.setCidade("São Paulo");
    endereco.setEstado("SP");
    endereco.setCep("01234567");

    // Setup Pedido
    pedido = new Pedido();
    pedido.setId(1L);
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setEnderecoEntrega(endereco);
    pedido.setValorTotal(BigDecimal.valueOf(50.00));
    pedido.setStatus(StatusPedido.CRIADO);
    pedido.setDataPedido(LocalDateTime.now());
    pedido.setItens(List.of(itemPedido));

    // Setup PedidoResponse
    ItemPedidoResponse itemResponse =
        new ItemPedidoResponse(1L, "Pizza Margherita", 2, BigDecimal.valueOf(25.00));
    pedidoResponse =
        new PedidoResponse(
            1L,
            new com.deliverytech.delivery_api.dto.response.ClienteResumoResponse(1L, "João Silva"),
            1L,
            endereco,
            BigDecimal.valueOf(50.00),
            StatusPedido.CRIADO,
            LocalDateTime.now(),
            List.of(itemResponse));

    // Setup PedidoRequest
    ItemPedidoRequest itemRequest = new ItemPedidoRequest();
    itemRequest.setProdutoId(1L);
    itemRequest.setQuantidade(2);
    itemRequest.setPrecoUnitario(BigDecimal.valueOf(25.00));

    pedidoRequest = new PedidoRequest();
    pedidoRequest.setClienteId(1L);
    pedidoRequest.setRestauranteId(1L);
    pedidoRequest.setItens(List.of(itemRequest));
    pedidoRequest.setEnderecoEntrega(
        new com.deliverytech.delivery_api.dto.request.EnderecoRequest(
            "Rua Teste", "123", "Centro", "São Paulo", "SP", "01234567", "Apto 1"));
  }

  @Test
  void listarTodos_DeveRetornarListaPaginada() throws Exception {
    Page<PedidoResponse> page = new PageImpl<>(List.of(pedidoResponse), PageRequest.of(0, 20), 1);
    when(pedidoService.listarTodos(any(Pageable.class))).thenReturn(page);

    mockMvc
        .perform(
            get("/api/pedidos")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].id").value(1))
        .andExpect(jsonPath("$.data.items[0].cliente.nome").value("João Silva"));
  }

  @Test
  void listarTodosResumo_DeveRetornarListaResumoPaginada() throws Exception {
    Page<PedidoResponse> page = new PageImpl<>(List.of(pedidoResponse), PageRequest.of(0, 20), 1);
    when(pedidoService.listarTodos(any(Pageable.class))).thenReturn(page);
    when(pedidoService.buscarPorId(anyLong())).thenReturn(pedido);

    mockMvc
        .perform(
            get("/api/pedidos/resumo")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].id").value(1));
  }

  @Test
  void buscarPorId_DeveRetornarPedidoQuandoEncontrado() throws Exception {
    when(pedidoService.buscarPorId(1L)).thenReturn(pedido);

    mockMvc
        .perform(get("/api/pedidos/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.status").value("CRIADO"));
  }

  @Test
  void buscarPorId_DeveRetornar404QuandoNaoEncontrado() throws Exception {
    when(pedidoService.buscarPorId(1L)).thenReturn(null);

    mockMvc
        .perform(get("/api/pedidos/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Pedido não encontrado"));
  }

  @Test
  void criar_DeveCriarPedidoComSucesso() throws Exception {
    when(pedidoService.criar(any(Pedido.class))).thenReturn(pedido);

    mockMvc
        .perform(
            post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoRequest))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.status").value("CRIADO"));
  }

  @Test
  void criarPedido_DeveCriarPedidoViaDTOComSucesso() throws Exception {
    when(pedidoService.criarPedido(any(PedidoRequest.class))).thenReturn(pedidoResponse);

    mockMvc
        .perform(
            post("/api/pedidos/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoRequest))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  void atualizarStatus_DeveAtualizarStatusComSucesso() throws Exception {
    Pedido pedidoAtualizado = pedido;
    pedidoAtualizado.setStatus(StatusPedido.ENTREGUE);

    when(pedidoService.atualizarStatus(1L, StatusPedido.ENTREGUE)).thenReturn(pedidoAtualizado);
    when(pedidoService.buscarPorIdComItens(1L)).thenReturn(Optional.of(pedidoAtualizado));

    StatusUpdateRequest statusRequest = new StatusUpdateRequest();
    statusRequest.setStatus("ENTREGUE");

    mockMvc
        .perform(
            put("/api/pedidos/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("ENTREGUE"));
  }

  @Test
  void atualizarStatusPatch_DeveAtualizarStatusViaPatch() throws Exception {
    Pedido pedidoAtualizado = pedido;
    pedidoAtualizado.setStatus(StatusPedido.ENTREGUE);

    when(pedidoService.atualizarStatus(1L, StatusPedido.ENTREGUE)).thenReturn(pedidoAtualizado);

    StatusUpdateRequest statusRequest = new StatusUpdateRequest();
    statusRequest.setStatus("entregue");

    mockMvc
        .perform(
            patch("/api/pedidos/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("ENTREGUE"));
  }

  @Test
  void atualizarStatusPatch_DeveRetornar400ParaStatusInvalido() throws Exception {
    StatusUpdateRequest statusRequest = new StatusUpdateRequest();
    statusRequest.setStatus("STATUS_INVALIDO");

    mockMvc
        .perform(
            patch("/api/pedidos/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusRequest))
                .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Status inválido"));
  }

  @Test
  void cancelarPedido_DeveCancelarPedidoComSucesso() throws Exception {
    Pedido pedidoCancelado = pedido;
    pedidoCancelado.setStatus(StatusPedido.CANCELADO);

    when(pedidoService.cancelar(1L)).thenReturn(pedidoCancelado);

    mockMvc
        .perform(delete("/api/pedidos/1").contentType(MediaType.APPLICATION_JSON).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("CANCELADO"));
  }

  @Test
  void calcularTotal_DeveCalcularTotalComSucesso() throws Exception {
    when(pedidoService.calcularTotalPedido(any())).thenReturn(BigDecimal.valueOf(50.00));

    mockMvc
        .perform(
            post("/api/pedidos/calcular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.total").value(50.00))
        .andExpect(jsonPath("$.data.quantidade_itens").value(1));
  }

  @Test
  void criar_DeveRetornar400ParaRequestInvalido() throws Exception {
    PedidoRequest requestInvalido = new PedidoRequest();
    // Request sem clienteId - deve falhar validação

    mockMvc
        .perform(
            post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
                .with(csrf()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void listarTodos_DeveLidarComErroInterno() throws Exception {
    when(pedidoService.listarTodos(any(Pageable.class)))
        .thenThrow(new RuntimeException("Erro interno"));

    mockMvc
        .perform(get("/api/pedidos").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.success").value(false));
  }
}
