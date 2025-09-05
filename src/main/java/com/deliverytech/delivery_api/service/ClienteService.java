package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    // Métodos compatíveis para uso interno/legado
    @Deprecated
    com.deliverytech.delivery_api.model.Cliente cadastrar(
            com.deliverytech.delivery_api.model.Cliente cliente);

    @Deprecated
    List<com.deliverytech.delivery_api.model.Cliente> buscarAtivos();

    @Deprecated
    com.deliverytech.delivery_api.model.Cliente atualizar(
            com.deliverytech.delivery_api.model.Cliente cliente);

    // Métodos novos com DTOs
    ClienteResponse cadastrar(ClienteRequest clienteRequest);

    ClienteResponse buscarPorId(Long id);

    Optional<ClienteResponse> buscarPorEmail(String email);

    List<ClienteResponse> listarAtivos();
        org.springframework.data.domain.Page<ClienteResponse> listarAtivos(org.springframework.data.domain.Pageable pageable);

    List<ClienteResponse> buscarPorNome(String nome);

    ClienteResponse atualizar(Long id, ClienteRequest clienteRequest);

    void inativar(Long id);

    ClienteResponse ativarDesativarCliente(Long id);
}
