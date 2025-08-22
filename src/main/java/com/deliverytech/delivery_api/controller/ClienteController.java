
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.service.ClienteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações de cadastro, consulta, atualização e inativação de clientes.")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente ativo no sistema.")
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse novo = clienteService.cadastrar(clienteRequest);
        return ResponseEntity.status(201).body(novo);
    }


    @Operation(summary = "Listar clientes ativos", description = "Retorna todos os clientes ativos cadastrados.")
    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listarAtivos();
    }


    @Operation(summary = "Buscar cliente por e-mail", description = "Consulta um cliente pelo e-mail.")
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarPorEmail(@PathVariable String email) {
        return clienteService.buscarPorEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse atualizado = clienteService.atualizar(id, clienteRequest);
        return ResponseEntity.ok(atualizado);
    }


    @Operation(summary = "Inativar cliente", description = "Inativa um cliente pelo seu ID, tornando-o indisponível para operações.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
