package com.deliverytech.delivery_api.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado: " + id);
    }

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado: " + email);
    }
}
