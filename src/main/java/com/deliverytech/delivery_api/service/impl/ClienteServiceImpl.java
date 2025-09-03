package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.mapper.ClienteMapper;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.ClienteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Deprecated
    public Cliente cadastrar(Cliente cliente) {
        if (clienteRepository.findByEmailAndExcluidoFalse(cliente.getEmail()).isPresent()) {
            throw new com.deliverytech.delivery_api.exception.EmailDuplicadoException(
                    cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponse cadastrar(ClienteRequest clienteRequest) {
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        if (clienteRepository.findByEmailAndExcluidoFalse(cliente.getEmail()).isPresent()) {
            throw new com.deliverytech.delivery_api.exception.EmailDuplicadoException(
                    cliente.getEmail());
        }
        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        // Validate not soft deleted
        if (Boolean.TRUE.equals(cliente.getExcluido())) {
            throw new EntityNotFoundException("Cliente foi excluído do sistema");
        }

        return clienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteResponse> buscarPorEmail(String email) {
        return clienteRepository.findByEmailAndExcluidoFalse(email).map(clienteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Deprecated
    public List<Cliente> buscarAtivos() {
        return clienteRepository.findByAtivoTrueAndExcluidoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarAtivos() {
        return clienteRepository.findByAtivoTrueAndExcluidoFalse().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarPorNome(String nome) {
        return clienteRepository.findByAtivoTrueAndExcluidoFalse().stream()
                .filter(
                        c ->
                                c.getNome() != null
                                        && c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Deprecated
    public Cliente atualizar(Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new EntityNotFoundException("Cliente", cliente.getId());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponse atualizar(Long id, ClienteRequest clienteRequest) {
        Cliente existente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        existente.setNome(clienteRequest.getNome());
        existente.setEmail(clienteRequest.getEmail());
        existente.setTelefone(clienteRequest.getTelefone());
        existente.setEndereco(clienteRequest.getEndereco());
        Cliente atualizado = clienteRepository.save(existente);
        return clienteMapper.toResponse(atualizado);
    }

    @Override
    public void inativar(Long id) {
        Cliente cliente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        if (!cliente.isAtivo() || Boolean.TRUE.equals(cliente.getExcluido())) {
            log.info(
                    "Cliente já estava inativo ou excluído: id={}, email={}",
                    cliente.getId(),
                    cliente.getEmail());
        } else {
            cliente.setAtivo(false);
            cliente.setExcluido(true);
            log.info(
                    "Cliente inativado com sucesso: id={}, email={}",
                    cliente.getId(),
                    cliente.getEmail());
        }
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponse ativarDesativarCliente(Long id) {
        Cliente cliente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        cliente.setAtivo(!cliente.isAtivo());
        Cliente atualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponse(atualizado);
    }
}
