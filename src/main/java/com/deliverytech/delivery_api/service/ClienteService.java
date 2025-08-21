package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import java.util.Optional;
import java.util.List;

// Adaptação para alinhar com o projeto de referência
public interface ClienteService {
    Cliente cadastrar(Cliente cliente); // manter para compatibilidade
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> buscarAtivos();
    Cliente atualizar(Cliente cliente);
    void inativar(Long id);

    // Métodos do projeto de referência
    Cliente cadastrar(com.deliverytech.delivery_api.dto.request.ClienteRequest clienteRequest);
    Optional<Cliente> buscarPorEmail(String email);
    List<Cliente> listarAtivos();
    List<Cliente> buscarPorNome(String nome);
    Cliente atualizar(Long id, com.deliverytech.delivery_api.dto.request.ClienteRequest clienteRequest);
    Cliente ativarDesativarCliente(Long id);
}
