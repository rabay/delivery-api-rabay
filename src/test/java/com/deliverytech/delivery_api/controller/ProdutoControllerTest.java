package com.deliverytech.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.LoggingService;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
public class ProdutoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProdutoService produtoService;

  @MockBean private LoggingService loggingService;

  @MockBean private JwtUtil jwtUtil;

  @MockBean private UsuarioRepository usuarioRepository;

  @MockBean private PedidoRepository pedidoRepository;

  @Autowired private ObjectMapper objectMapper;

  private Produto produto;
  private ProdutoResponse produtoResponse;
  private ProdutoRequest produtoRequest;

  @BeforeEach
  void setUp() {
    // Setup Produto
    produto = new Produto();
    produto.setId(1L);
    produto.setNome("Pizza Margherita");
    produto.setDescricao("Pizza tradicional com molho de tomate, queijo e manjericão");
    produto.setPreco(BigDecimal.valueOf(25.00));
    produto.setCategoria("PIZZA");
    produto.setDisponivel(true);

    // Setup ProdutoResponse
    produtoResponse =
        ProdutoResponse.builder()
            .id(1L)
            .nome("Pizza Margherita")
            .categoria("PIZZA")
            .descricao("Pizza tradicional com molho de tomate, queijo e manjericão")
            .preco(BigDecimal.valueOf(25.00))
            .disponivel(true)
            .build();

    // Setup ProdutoRequest
    produtoRequest = new ProdutoRequest();
    produtoRequest.setNome("Pizza Margherita");
    produtoRequest.setDescricao("Pizza tradicional com molho de tomate, queijo e manjericão");
    produtoRequest.setPreco(BigDecimal.valueOf(25.00));
    produtoRequest.setCategoria("PIZZA");
    produtoRequest.setDisponivel(true);
    produtoRequest.setRestauranteId(1L); // Campo obrigatório
    produtoRequest.setQuantidadeEstoque(10); // Campo obrigatório
  }

  @Test
  void criar_DeveCriarProdutoComSucesso() throws Exception {
    when(produtoService.cadastrar(any(ProdutoRequest.class))).thenReturn(produtoResponse);

    mockMvc
        .perform(
            post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoRequest))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.nome").value("Pizza Margherita"))
        .andExpect(jsonPath("$.data.categoria").value("PIZZA"));
  }

  @Test
  void listar_DeveRetornarListaPaginada() throws Exception {
    Page<ProdutoResponse> page = new PageImpl<>(List.of(produtoResponse), PageRequest.of(0, 20), 1);
    when(produtoService.buscarDisponiveis(any(Pageable.class))).thenReturn(page);

    mockMvc
        .perform(
            get("/api/produtos")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].id").value(1))
        .andExpect(jsonPath("$.data.items[0].nome").value("Pizza Margherita"));
  }

  @Test
  void buscarPorId_DeveRetornarProdutoQuandoEncontrado() throws Exception {
    when(produtoService.buscarProdutoPorId(1L)).thenReturn(produtoResponse);

    mockMvc
        .perform(get("/api/produtos/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.nome").value("Pizza Margherita"));
  }

  @Test
  void buscarPorId_DeveRetornar404QuandoNaoEncontrado() throws Exception {
    when(produtoService.buscarProdutoPorId(1L))
        .thenThrow(new RuntimeException("Produto não encontrado"));

    mockMvc
        .perform(get("/api/produtos/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Produto não encontrado"));
  }

  @Test
  void atualizar_DeveAtualizarProdutoComSucesso() throws Exception {
    ProdutoResponse produtoAtualizado =
        ProdutoResponse.builder()
            .id(1L)
            .nome("Pizza Margherita Especial")
            .categoria("PIZZA")
            .descricao("Pizza especial com ingredientes premium")
            .preco(BigDecimal.valueOf(30.00))
            .disponivel(true)
            .build();

    when(produtoService.atualizar(1L, produtoRequest)).thenReturn(produtoAtualizado);

    mockMvc
        .perform(
            put("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoRequest))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.nome").value("Pizza Margherita Especial"));
  }

  @Test
  void atualizar_DeveRetornar404QuandoNaoEncontrado() throws Exception {
    when(produtoService.atualizar(1L, produtoRequest))
        .thenThrow(new RuntimeException("Produto não encontrado"));

    mockMvc
        .perform(
            put("/api/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoRequest))
                .with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Produto não encontrado"));
  }

  @Test
  void deletar_DeveDeletarProdutoComSucesso() throws Exception {
    mockMvc
        .perform(delete("/api/produtos/1").contentType(MediaType.APPLICATION_JSON).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Produto removido com sucesso"));
  }

  @Test
  void deletar_DeveRetornar404QuandoNaoEncontrado() throws Exception {
    doThrow(new RuntimeException("Produto não encontrado")).when(produtoService).deletar(1L);

    mockMvc
        .perform(delete("/api/produtos/1").contentType(MediaType.APPLICATION_JSON).with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Produto não encontrado"));
  }

  @Test
  void alterarDisponibilidade_DeveAlterarDisponibilidadeComSucesso() throws Exception {
    Map<String, Boolean> requestBody = Map.of("disponivel", false);

    doNothing().when(produtoService).alterarDisponibilidade(1L, false);

    mockMvc
        .perform(
            patch("/api/produtos/1/disponibilidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.produtoId").value(1))
        .andExpect(jsonPath("$.data.disponivel").value(false));
  }

  @Test
  void alterarDisponibilidade_DeveRetornar400QuandoCampoAusente() throws Exception {
    Map<String, Boolean> requestBody = Map.of(); // Campo 'disponivel' ausente

    mockMvc
        .perform(
            patch("/api/produtos/1/disponibilidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Campo 'disponivel' ausente"));
  }

  @Test
  void alterarDisponibilidade_DeveRetornar404QuandoNaoEncontrado() throws Exception {
    Map<String, Boolean> requestBody = Map.of("disponivel", true);

    doThrow(new RuntimeException("Produto não encontrado"))
        .when(produtoService)
        .alterarDisponibilidade(1L, true);

    mockMvc
        .perform(
            patch("/api/produtos/1/disponibilidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Produto não encontrado"));
  }

  @Test
  void buscarPorNome_DeveRetornarProdutosEncontrados() throws Exception {
    Page<ProdutoResponse> page = new PageImpl<>(List.of(produtoResponse), PageRequest.of(0, 20), 1);
    when(produtoService.buscarProdutosPorNome("Pizza", PageRequest.of(0, 20))).thenReturn(page);

    mockMvc
        .perform(
            get("/api/produtos/buscar")
                .param("nome", "Pizza")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].nome").value("Pizza Margherita"));
  }

  @Test
  void buscarPorCategoria_DeveRetornarProdutosPorCategoria() throws Exception {
    Page<ProdutoResponse> page = new PageImpl<>(List.of(produtoResponse), PageRequest.of(0, 20), 1);
    when(produtoService.buscarProdutosPorCategoria("PIZZA", PageRequest.of(0, 20)))
        .thenReturn(page);

    mockMvc
        .perform(
            get("/api/produtos/categoria/PIZZA")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.items[0].categoria").value("PIZZA"));
  }

  @Test
  void criar_DeveRetornar400ParaRequestInvalido() throws Exception {
    ProdutoRequest requestInvalido = new ProdutoRequest();
    // Request sem nome - deve falhar validação

    mockMvc
        .perform(
            post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
                .with(csrf()))
        .andExpect(status().isBadRequest());
  }
}
