package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.util.WithMockJwtUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve retornar 200 ao buscar pedido existente com autenticação")
    @WithMockJwtUser(email = "admin@test.com", role = Role.ADMIN)
    void deveBuscarPedidoExistente() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoService.buscarPorId(1L)).thenReturn(pedido);
        
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk());
                
        verify(pedidoService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar pedido inexistente com autenticação")
    @WithMockJwtUser(email = "admin@test.com", role = Role.ADMIN)
    void deveRetornar404PedidoInexistente() throws Exception {
        when(pedidoService.buscarPorId(999L)).thenThrow(new RuntimeException("Pedido não encontrado"));
        
        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());
                
        verify(pedidoService).buscarPorId(999L);
    }
    
    @Test
    @DisplayName("Deve retornar 403 ao tentar acessar sem autenticação")
    void deveRetornar403SemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isForbidden());
                
        verify(pedidoService, never()).buscarPorId(any());
    }
    
    @Test
    @DisplayName("Cliente deve conseguir acessar seus próprios pedidos")
    @WithMockJwtUser(email = "cliente@test.com", role = Role.CLIENTE, id = 1L)
    void clienteDeveAcessarPropriosPedidos() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoService.buscarPorClienteComItens(1L)).thenReturn(java.util.List.of(pedido));
        
        mockMvc.perform(get("/api/clientes/1/pedidos"))
                .andExpect(status().isOk());
                
        verify(pedidoService).buscarPorClienteComItens(1L);
    }
}
