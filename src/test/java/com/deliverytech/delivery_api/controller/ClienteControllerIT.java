package com.deliverytech.delivery_api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClienteControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ClienteRepository clienteRepository;

  @Autowired private PedidoRepository pedidoRepository;

  @Autowired private RestauranteRepository restauranteRepository;

  private Cliente clienteTest;
  private Restaurante restauranteTest;

  @BeforeEach
  void setUp() {
    // Os dados já são carregados pelo TestDataLoader
    // Apenas buscar as entidades existentes para os testes
    clienteTest =
        clienteRepository
            .findByEmailAndExcluidoFalse("cliente1@test.com")
            .orElseThrow(() -> new RuntimeException("Cliente de teste não encontrado"));
    restauranteTest =
        restauranteRepository
            .findById(1L)
            .orElseThrow(() -> new RuntimeException("Restaurante de teste não encontrado"));
  }

  private Pedido criarPedidoTest() {
    Pedido pedido = new Pedido();
    pedido.setCliente(clienteTest);
    pedido.setRestaurante(restauranteTest);
    pedido.setStatus(StatusPedido.CRIADO);
    pedido.setDataPedido(LocalDateTime.now());
    pedido.setValorTotal(BigDecimal.valueOf(25.90));
    return pedidoRepository.save(pedido);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void criarCliente_DeveRetornarClienteCriado() throws Exception {
    ClienteRequest request = new ClienteRequest();
    request.setNome("Maria Santos");
    request.setEmail("maria.santos@email.com");
    request.setTelefone("11888888888");
    request.setEndereco("Rua Maria Santos, 456");

    mockMvc
        .perform(
            post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("Cliente criado")))
        .andExpect(jsonPath("$.data.nome", is("Maria Santos")))
        .andExpect(jsonPath("$.data.email", is("maria.santos@email.com")))
        .andExpect(jsonPath("$.data.telefone", is("11888888888")))
        .andExpect(jsonPath("$.data.ativo", is(true)));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void criarCliente_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
    ClienteRequest request = new ClienteRequest();
    request.setNome(""); // Nome vazio
    request.setEmail("email-invalido"); // Email inválido
    request.setTelefone("123"); // Telefone inválido

    mockMvc
        .perform(
            post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void listarClientes_DeveRetornarListaPaginada() throws Exception {
    // Criar mais alguns clientes para teste de paginação
    Cliente cliente2 = new Cliente();
    cliente2.setNome("Pedro Oliveira");
    cliente2.setEmail("pedro@email.com");
    cliente2.setTelefone("11777777777");
    cliente2.setAtivo(true);
    clienteRepository.save(cliente2);

    mockMvc
        .perform(get("/api/clientes").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.data.items", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$.data.totalItems", greaterThanOrEqualTo(2)))
        .andExpect(jsonPath("$.data.page", is(0)))
        .andExpect(jsonPath("$.data.links.first", notNullValue()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarClientePorId_DeveRetornarCliente() throws Exception {
    mockMvc
        .perform(get("/api/clientes/{id}", clienteTest.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.data.id", is(clienteTest.getId().intValue())))
        .andExpect(jsonPath("$.data.nome", is(clienteTest.getNome())))
        .andExpect(jsonPath("$.data.email", is(clienteTest.getEmail())));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarClientePorId_ComIdInexistente_DeveRetornarNotFound() throws Exception {
    mockMvc
        .perform(get("/api/clientes/{id}", 99999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success", is(false)))
        .andExpect(jsonPath("$.message", containsString("Cliente não encontrado")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarClientePorEmail_DeveRetornarCliente() throws Exception {
    mockMvc
        .perform(get("/api/clientes/email/{email}", clienteTest.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.data.id", is(clienteTest.getId().intValue())))
        .andExpect(jsonPath("$.data.nome", is(clienteTest.getNome())))
        .andExpect(jsonPath("$.data.email", is(clienteTest.getEmail())));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarClientePorEmail_ComEmailInexistente_DeveRetornarNotFound() throws Exception {
    mockMvc
        .perform(get("/api/clientes/email/{email}", "naoexiste@email.com"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success", is(false)))
        .andExpect(jsonPath("$.message", containsString("Cliente não encontrado")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void atualizarCliente_DeveRetornarClienteAtualizado() throws Exception {
    ClienteRequest request = new ClienteRequest();
    request.setNome("João Silva Atualizado");
    request.setEmail("joao.atualizado@email.com");
    request.setTelefone("11999999999");
    request.setEndereco("Rua João Silva, 789");

    mockMvc
        .perform(
            put("/api/clientes/{id}", clienteTest.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("Cliente atualizado")))
        .andExpect(jsonPath("$.data.nome", is("João Silva Atualizado")))
        .andExpect(jsonPath("$.data.email", is("joao.atualizado@email.com")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void atualizarCliente_ComIdInexistente_DeveRetornarNotFound() throws Exception {
    ClienteRequest request = new ClienteRequest();
    request.setNome("Cliente Atualizado");
    request.setEmail("atualizado@email.com");
    request.setTelefone("11999999999");
    request.setEndereco("Rua Atualizada, 123");

    mockMvc
        .perform(
            put("/api/clientes/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success", is(false)))
        .andExpect(jsonPath("$.message", containsString("Recurso")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void alterarStatusCliente_DeveRetornarClienteComStatusAlterado() throws Exception {
    mockMvc
        .perform(patch("/api/clientes/{id}/status", clienteTest.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("Status do cliente alterado")))
        .andExpect(jsonPath("$.data.id", is(clienteTest.getId().intValue())));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void alterarStatusCliente_ComIdInexistente_DeveRetornarNotFound() throws Exception {
    mockMvc
        .perform(patch("/api/clientes/{id}/status", 99999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success", is(false)))
        .andExpect(jsonPath("$.message", containsString("Recurso")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deletarCliente_DeveRetornarSucesso() throws Exception {
    mockMvc
        .perform(delete("/api/clientes/{id}", clienteTest.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("Cliente inativado")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void deletarCliente_ComIdInexistente_DeveRetornarNotFound() throws Exception {
    mockMvc
        .perform(delete("/api/clientes/{id}", 99999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success", is(false)))
        .andExpect(jsonPath("$.message", containsString("Recurso")));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarPedidosDoCliente_DeveRetornarPedidosPaginados() throws Exception {
    // Criar alguns pedidos para o cliente
    criarPedidoTest();
    Pedido pedido2 = new Pedido();
    pedido2.setCliente(clienteTest);
    pedido2.setRestaurante(restauranteTest);
    pedido2.setStatus(StatusPedido.CONFIRMADO);
    pedido2.setDataPedido(LocalDateTime.now().minusDays(1));
    pedido2.setValorTotal(BigDecimal.valueOf(50.00));
    pedidoRepository.save(pedido2);

    mockMvc
        .perform(
            get("/api/clientes/{clienteId}/pedidos", clienteTest.getId())
                .param("page", "0")
                .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.data.items", hasSize(greaterThanOrEqualTo(2))))
        .andExpect(jsonPath("$.data.totalItems", greaterThanOrEqualTo(2)))
        .andExpect(jsonPath("$.data.links.first", notNullValue()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarPedidosDoCliente_ComClienteIdInvalido_DeveRetornarBadRequest()
      throws Exception {
    mockMvc
        .perform(
            get("/api/clientes/{clienteId}/pedidos", -1L).param("page", "0").param("size", "10"))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void buscarPedidosDoCliente_ComClienteInexistente_DeveRetornarNotFound() throws Exception {
    mockMvc
        .perform(
            get("/api/clientes/{clienteId}/pedidos", 99999L).param("page", "0").param("size", "10"))
        .andExpect(status().isOk());
  }
}
