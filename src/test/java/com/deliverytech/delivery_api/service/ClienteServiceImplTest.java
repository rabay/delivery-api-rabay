package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.impl.ClienteServiceImpl;
import com.deliverytech.delivery_api.mapper.ClienteMapper;
import com.deliverytech.delivery_api.exception.EmailDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteServiceImpl;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteService = clienteServiceImpl;
    }

    @Test
    @DisplayName("Deve cadastrar cliente com dados válidos")
    void deveCadastrarClienteQuandoDadosValidos() {
        ClienteRequest request = new ClienteRequest();
        request.setNome("João");
        request.setEmail("joao@email.com");
    Cliente cliente = new Cliente();
    cliente.setId(1L);
    cliente.setNome("João");
    cliente.setEmail("joao@email.com");
    when(clienteRepository.findByEmailAndExcluidoFalse(request.getEmail())).thenReturn(Optional.empty());
    when(clienteMapper.toEntity(request)).thenReturn(cliente);
    when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
    when(clienteMapper.toResponse(cliente)).thenReturn(new ClienteResponse() {{ setNome("João"); setEmail("joao@email.com"); }});
    ClienteResponse response = clienteService.cadastrar(request);
    assertNotNull(response);
    assertEquals("João", response.getNome());
    assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar cliente com e-mail duplicado")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        ClienteRequest request = new ClienteRequest();
        request.setEmail("duplicado@email.com");
    Cliente cliente = new Cliente();
    cliente.setId(99L);
    cliente.setNome("Duplicado");
    cliente.setEmail("duplicado@email.com");
    when(clienteRepository.findByEmailAndExcluidoFalse(request.getEmail())).thenReturn(Optional.of(cliente));
    when(clienteMapper.toEntity(request)).thenReturn(cliente);
    assertThrows(EmailDuplicadoException.class, () -> clienteService.cadastrar(request));
    }

    @Test
    @DisplayName("Deve buscar cliente por e-mail existente")
    void deveBuscarClientePorEmailExistente() {
        String email = "busca@email.com";
    Cliente cliente = new Cliente();
    cliente.setId(2L);
    cliente.setNome("Maria");
    cliente.setEmail(email);
    ClienteResponse clienteResponse = new ClienteResponse();
    clienteResponse.setNome("Maria");
    clienteResponse.setEmail(email);
    when(clienteRepository.findByEmailAndExcluidoFalse(email)).thenReturn(Optional.of(cliente));
    when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);
    Optional<ClienteResponse> response = clienteService.buscarPorEmail(email);
    assertTrue(response.isPresent());
    assertEquals("Maria", response.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente por e-mail inexistente")
    void deveRetornarVazioQuandoBuscarEmailInexistente() {
        String email = "naoexiste@email.com";
    when(clienteRepository.findByEmailAndExcluidoFalse(email)).thenReturn(Optional.empty());
    Optional<ClienteResponse> response = clienteService.buscarPorEmail(email);
    assertTrue(response.isEmpty());
    }

    // Adicione outros testes para atualizar, inativar, ativar/desativar, etc., conforme a interface
}
