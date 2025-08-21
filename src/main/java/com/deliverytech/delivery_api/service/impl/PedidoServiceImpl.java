package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.service.PedidoService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido criar(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RuntimeException("Cliente obrigatório");
        }
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("NOVO");
        // Cálculo de valores e outras validações podem ser adicionados aqui
        return pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }

    @Override
    public Pedido mudarStatus(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}
