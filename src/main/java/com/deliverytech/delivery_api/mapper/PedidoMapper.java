package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ItemPedidoRequest;
import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResumoResponse;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.model.*;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PedidoMapper {

    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;

    public Pedido toEntity(PedidoRequest dto) {
        Cliente cliente =
                clienteRepository
                        .findById(dto.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Restaurante restaurante =
                restauranteRepository
                        .findById(dto.getRestauranteId())
                        .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        Pedido pedido =
                Pedido.builder()
                        .cliente(cliente)
                        .restaurante(restaurante)
                        .enderecoEntrega(dto.getEnderecoEntrega())
                        .build();

        // Convert items
        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            List<ItemPedido> itens =
                    dto.getItens().stream()
                            .map(itemDto -> toItemEntity(itemDto, pedido))
                            .collect(Collectors.toList());
            pedido.setItens(itens);
        }

        return pedido;
    }

    private ItemPedido toItemEntity(ItemPedidoRequest dto, Pedido pedido) {
        Produto produto =
                produtoRepository
                        .findById(dto.getProdutoId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return ItemPedido.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(dto.getQuantidade())
                .precoUnitario(produto.getPreco()) // Use current product price
                .build();
    }

    public PedidoResponse toResponse(Pedido entity) {
        ClienteResumoResponse clienteResumo = null;
        if (entity.getCliente() != null) {
            clienteResumo = new ClienteResumoResponse();
            clienteResumo.setId(entity.getCliente().getId());
            clienteResumo.setNome(entity.getCliente().getNome());
        }

        List<ItemPedidoResponse> itensResponse = null;
        if (entity.getItens() != null) {
            itensResponse =
                    entity.getItens().stream()
                            .map(this::toItemResponse)
                            .collect(Collectors.toList());
        }

        PedidoResponse response = new PedidoResponse();
        response.setId(entity.getId());
        response.setCliente(clienteResumo);
        response.setRestauranteId(
                entity.getRestaurante() != null ? entity.getRestaurante().getId() : null);
        response.setEnderecoEntrega(entity.getEnderecoEntrega());
        response.setValorTotal(entity.getValorTotal());
        response.setStatus(entity.getStatus());
        response.setDataPedido(entity.getDataPedido());
        response.setItens(itensResponse);

        return response;
    }

    private ItemPedidoResponse toItemResponse(ItemPedido entity) {
        ItemPedidoResponse response = new ItemPedidoResponse();
        response.setProdutoId(entity.getProduto() != null ? entity.getProduto().getId() : null);
        response.setNomeProduto(entity.getProduto() != null ? entity.getProduto().getNome() : null);
        response.setQuantidade(entity.getQuantidade());
        response.setPrecoUnitario(entity.getPrecoUnitario());
        return response;
    }
}
