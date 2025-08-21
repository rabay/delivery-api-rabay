package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.ClienteService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> buscarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    @Override
    public Cliente atualizar(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new RuntimeException("Cliente não encontrado");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
}
