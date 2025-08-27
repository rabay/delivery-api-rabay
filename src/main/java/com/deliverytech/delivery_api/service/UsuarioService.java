package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UsuarioService extends UserDetailsService {
    Usuario salvar(RegisterRequest request);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> listarTodos();
    Usuario atualizar(Long id, RegisterRequest request);
    void deletar(Long id);
}