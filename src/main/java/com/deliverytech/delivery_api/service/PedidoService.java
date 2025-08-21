package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criar(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RuntimeException("Cliente obrigatório");
        }
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("NOVO");
        // Cálculo de valores e outras validações podem ser adicionados aqui
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }

    public Pedido mudarStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
