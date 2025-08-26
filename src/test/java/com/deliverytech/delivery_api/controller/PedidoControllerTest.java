package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControllerTest {
    @Mock
    private PedidoService pedidoService;
    @InjectMocks
    private PedidoController pedidoController;
    private MockMvc mockMvc;

    public PedidoControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar pedido existente")
    void deveBuscarPedidoExistente() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoService.buscarPorId(1L)).thenReturn(pedido);
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar pedido inexistente")
    void deveRetornar404PedidoInexistente() throws Exception {
        when(pedidoService.buscarPorId(999L)).thenThrow(new RuntimeException("Pedido não encontrado"));
        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());
    }
}
