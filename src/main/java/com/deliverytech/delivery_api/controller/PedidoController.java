package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.service.PedidoService;
import com.deliverytech.delivery_api.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        Pedido novo = pedidoService.criar(pedido);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        Pedido atualizado = pedidoService.atualizarStatus(id, com.deliverytech.delivery_api.model.StatusPedido.valueOf(status));
        return ResponseEntity.ok(atualizado);
    }
}
