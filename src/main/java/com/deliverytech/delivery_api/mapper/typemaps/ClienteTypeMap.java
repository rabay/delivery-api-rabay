package com.deliverytech.delivery_api.mapper.typemaps;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ClienteTypeMap {
    
    private final ModelMapper modelMapper;
    
    public ClienteTypeMap(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @PostConstruct
    public void configure() {
        // Cliente to ClienteResponse mapping
        TypeMap<Cliente, ClienteResponse> clienteToResponse = modelMapper.createTypeMap(Cliente.class, ClienteResponse.class);
        clienteToResponse.addMapping(Cliente::getId, ClienteResponse::setId);
        clienteToResponse.addMapping(Cliente::getNome, ClienteResponse::setNome);
        clienteToResponse.addMapping(Cliente::getEmail, ClienteResponse::setEmail);
        clienteToResponse.addMapping(Cliente::getTelefone, ClienteResponse::setTelefone);
        clienteToResponse.addMapping(Cliente::getEndereco, ClienteResponse::setEndereco);
        clienteToResponse.addMapping(Cliente::isAtivo, ClienteResponse::setAtivo);
        
        // ClienteRequest to Cliente mapping
        TypeMap<ClienteRequest, Cliente> requestToCliente = modelMapper.createTypeMap(ClienteRequest.class, Cliente.class);
        requestToCliente.addMapping(ClienteRequest::getNome, Cliente::setNome);
        requestToCliente.addMapping(ClienteRequest::getEmail, Cliente::setEmail);
        requestToCliente.addMapping(ClienteRequest::getTelefone, Cliente::setTelefone);
        requestToCliente.addMapping(ClienteRequest::getEndereco, Cliente::setEndereco);
        // Set ativo to true by default for new clients
        requestToCliente.addMapping(src -> true, Cliente::setAtivo);
    }
}