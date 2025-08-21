package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import java.util.Optional;
import java.util.List;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> buscarAtivos();
    Cliente atualizar(Cliente cliente);
    void inativar(Long id);
}
