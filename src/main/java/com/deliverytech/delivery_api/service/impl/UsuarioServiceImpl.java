package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.exception.EmailJaCadastradoException;
import com.deliverytech.delivery_api.exception.UsuarioNaoEncontradoException;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails user = usuarioRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
    return user;
  }

  @Override
  public Usuario salvar(RegisterRequest request) {
    // Check if email already exists
    if (usuarioRepository.findUsuarioByEmail(request.getEmail()).isPresent()) {
      throw new EmailJaCadastradoException(request.getEmail());
    }

    Usuario usuario =
        Usuario.builder()
            .nome(request.getNome())
            .email(request.getEmail())
            .senha(passwordEncoder.encode(request.getSenha()))
            .role(request.getRole())
            .ativo(true)
            .dataCriacao(LocalDateTime.now())
            .restauranteId(request.getRestauranteId())
            .build();

    return usuarioRepository.save(usuario);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> buscarPorEmail(String email) {
    return usuarioRepository.findUsuarioByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> buscarPorId(Long id) {
    return usuarioRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> listarTodos() {
    return usuarioRepository.findAll();
  }

  @Override
  public Usuario atualizar(Long id, RegisterRequest request) {
    Usuario usuario =
        usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));

    // Check if email is being changed and if it already exists
    if (!usuario.getEmail().equals(request.getEmail())) {
      if (usuarioRepository.findUsuarioByEmail(request.getEmail()).isPresent()) {
        throw new EmailJaCadastradoException(request.getEmail());
      }
      usuario.setEmail(request.getEmail());
    }

    usuario.setNome(request.getNome());
    usuario.setRole(request.getRole());
    usuario.setRestauranteId(request.getRestauranteId());

    // Only encode password if it's being changed
    if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
      usuario.setSenha(passwordEncoder.encode(request.getSenha()));
    }

    return usuarioRepository.save(usuario);
  }

  @Override
  public void deletar(Long id) {
    Usuario usuario =
        usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));

    usuario.setAtivo(false); // Soft delete
    usuarioRepository.save(usuario);
  }
}
