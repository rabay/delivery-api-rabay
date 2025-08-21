package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import java.util.List;

public interface PedidoService {
    Pedido criar(Pedido pedido);
    List<Pedido> buscarPorCliente(Cliente cliente);
    Pedido mudarStatus(Long id, String novoStatus);
}
