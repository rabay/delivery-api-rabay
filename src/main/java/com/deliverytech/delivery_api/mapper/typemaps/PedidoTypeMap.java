package com.deliverytech.delivery_api.mapper.typemaps;

import com.deliverytech.delivery_api.dto.response.PedidoResumoResponse;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PedidoTypeMap {
    
    private final ModelMapper modelMapper;
    
    public PedidoTypeMap(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @PostConstruct
    public void configure() {
        // Pedido to PedidoResumoResponse mapping
        TypeMap<Pedido, PedidoResumoResponse> pedidoToResumo = modelMapper.createTypeMap(Pedido.class, PedidoResumoResponse.class);
        
        // Map cliente nome using method reference approach
        pedidoToResumo.addMapping(this::getClienteNome, PedidoResumoResponse::setClienteNome);
        
        // Map restaurante nome using method reference approach
        pedidoToResumo.addMapping(this::getRestauranteNome, PedidoResumoResponse::setRestauranteNome);
        
        pedidoToResumo.addMapping(Pedido::getValorTotal, PedidoResumoResponse::setValorTotal);
        pedidoToResumo.addMapping(Pedido::getDesconto, PedidoResumoResponse::setDesconto);
        
        // Map status using method reference approach
        pedidoToResumo.addMapping(this::getStatusName, PedidoResumoResponse::setStatus);
        
        pedidoToResumo.addMapping(Pedido::getDataPedido, PedidoResumoResponse::setDataPedido);
    }
    
    private String getClienteNome(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        return cliente != null ? cliente.getNome() : null;
    }
    
    private String getRestauranteNome(Pedido pedido) {
        Restaurante restaurante = pedido.getRestaurante();
        return restaurante != null ? restaurante.getNome() : null;
    }
    
    private String getStatusName(Pedido pedido) {
        StatusPedido status = pedido.getStatus();
        return status != null ? status.name() : null;
    }
}