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
    public Cliente cadastrar(com.deliverytech.delivery_api.dto.request.ClienteRequest clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.getNome());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setAtivo(true);
        // Adapte para outros campos se existirem no modelo
        return cadastrar(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Override
    public List<Cliente> buscarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    @Override
    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    @Override
    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> todos = clienteRepository.findAll();
        return todos.stream()
            .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(nome.toLowerCase()))
            .toList();
    }

    @Override
    public Cliente atualizar(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new RuntimeException("Cliente não encontrado");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente atualizar(Long id, com.deliverytech.delivery_api.dto.request.ClienteRequest clienteRequest) {
        Cliente existente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        existente.setNome(clienteRequest.getNome());
        existente.setEmail(clienteRequest.getEmail());
        // Adapte para outros campos se existirem no modelo
        return clienteRepository.save(existente);
    }

    @Override
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    public Cliente ativarDesativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setAtivo(!cliente.isAtivo());
        return clienteRepository.save(cliente);
    }
}
