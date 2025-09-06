package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.mapper.RestauranteMapper;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.RestauranteService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {
  private final RestauranteRepository restauranteRepository;
  private final RestauranteMapper restauranteMapper;

  public RestauranteServiceImpl(
      RestauranteRepository restauranteRepository, RestauranteMapper restauranteMapper) {
    this.restauranteRepository = restauranteRepository;
    this.restauranteMapper = restauranteMapper;
  }

  @Override
  public Restaurante cadastrar(RestauranteRequest restauranteRequest) {
    if (restauranteRequest.getNome() == null || restauranteRequest.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("O nome do restaurante é obrigatório.");
    }
    Restaurante restaurante = new Restaurante();
    restaurante.setNome(restauranteRequest.getNome());
    restaurante.setCategoria(restauranteRequest.getCategoria());
    restaurante.setEndereco(restauranteRequest.getEndereco());
    restaurante.setTaxaEntrega(restauranteRequest.getTaxaEntrega());
    restaurante.setTelefone(restauranteRequest.getTelefone());
    restaurante.setEmail(restauranteRequest.getEmail());
    restaurante.setTempoEntregaMinutos(restauranteRequest.getTempoEntregaMinutos());
    restaurante.setAvaliacao(restauranteRequest.getAvaliacao());
    restaurante.setAtivo(true);
    return restauranteRepository.save(restaurante);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Restaurante> buscarPorId(Long id) {
    return restauranteRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> listarTodos() {
    return restauranteRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> listarAtivos() {
    return restauranteRepository.findByAtivoTrueAndExcluidoFalse();
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<Restaurante> listarAtivos(
      org.springframework.data.domain.Pageable pageable) {
    return restauranteRepository.findByAtivoTrueAndExcluidoFalse(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> buscarPorCategoria(String categoria) {
    return restauranteRepository.findByCategoriaAndExcluidoFalse(categoria);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao) {
    return restauranteRepository.findByAvaliacaoGreaterThanEqualAndExcluidoFalse(minAvaliacao);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa) {
    return restauranteRepository.findByTaxaEntregaLessThanEqualAndExcluidoFalse(maxTaxa);
  }

  @Override
  public Restaurante atualizar(Long id, RestauranteRequest atualizado) {
    return restauranteRepository
        .findById(id)
        .map(
            r -> {
              r.setNome(atualizado.getNome());
              r.setCategoria(atualizado.getCategoria());
              r.setEndereco(atualizado.getEndereco());
              r.setTaxaEntrega(atualizado.getTaxaEntrega());
              r.setTelefone(atualizado.getTelefone());
              r.setEmail(atualizado.getEmail());
              r.setTempoEntregaMinutos(atualizado.getTempoEntregaMinutos());
              r.setAvaliacao(atualizado.getAvaliacao());
              return restauranteRepository.save(r);
            })
        .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));
  }

  @Override
  public void inativar(Long id) {
    restauranteRepository
        .findById(id)
        .ifPresentOrElse(
            restaurante -> {
              restaurante.setAtivo(false);
              restaurante.setExcluido(true);
              restauranteRepository.save(restaurante);
            },
            () -> {
              throw new EntityNotFoundException("Restaurante", "ID", String.valueOf(id));
            });
  }

  @Override
  public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
    Restaurante restaurante =
        restauranteRepository
            .findById(restauranteId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Restaurante", "ID", String.valueOf(restauranteId)));
    if (!restaurante.isAtivo()) {
      throw new BusinessException("Restaurante não está disponível para entrega");
    }

    BigDecimal taxaBase = restaurante.getTaxaEntrega();
    // Handle null taxaEntrega by using a default value
    if (taxaBase == null) {
      taxaBase = BigDecimal.ZERO;
    }
    String primeirosDigitos = cep.substring(0, Math.min(2, cep.length()));
    try {
      int codigoRegiao = Integer.parseInt(primeirosDigitos);
      BigDecimal multiplicador;
      if (codigoRegiao == 1) {
        multiplicador = BigDecimal.ONE;
      } else if (codigoRegiao >= 2 && codigoRegiao <= 5) {
        multiplicador = new BigDecimal("1.20");
      } else if (codigoRegiao >= 6 && codigoRegiao <= 9) {
        multiplicador = new BigDecimal("1.50");
      } else {
        multiplicador = new BigDecimal("2.00");
      }
      return taxaBase.multiply(multiplicador).setScale(2, java.math.RoundingMode.HALF_UP);
    } catch (NumberFormatException e) {
      return taxaBase;
    }
  }

  @Override
  public Restaurante alterarStatus(Long id, Boolean ativo) {
    Restaurante restaurante =
        restauranteRepository
            .findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException("Restaurante", "ID", String.valueOf(id)));
    restaurante.setAtivo(ativo);
    return restauranteRepository.save(restaurante);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> buscarProximos(String cep) {
    List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrueAndExcluidoFalse();
    String primeirosDigitos = cep.substring(0, Math.min(2, cep.length()));
    try {
      int codigoRegiao = Integer.parseInt(primeirosDigitos);
      if (codigoRegiao <= 5) {
        return restaurantesAtivos;
      } else {
        return restaurantesAtivos.stream()
            .filter(r -> r.getTaxaEntrega().compareTo(new BigDecimal("10.00")) <= 0)
            .toList();
      }
    } catch (NumberFormatException e) {
      return restaurantesAtivos;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Restaurante> listarComFiltros(String categoria, Boolean ativo) {
    if (categoria == null && ativo == null) {
      return restauranteRepository.findByAtivoTrueAndExcluidoFalse();
    }
    if (categoria != null && ativo == null) {
      return restauranteRepository.findByCategoriaAndExcluidoFalse(categoria);
    }
    if (categoria == null && ativo != null) {
      return ativo
          ? restauranteRepository.findByAtivoTrueAndExcluidoFalse()
          : restauranteRepository.findByAtivoFalseAndExcluidoFalse();
    }
    return restauranteRepository.findByCategoriaAndAtivoAndExcluidoFalse(categoria, ativo);
  }

  // ===== NOVOS MÉTODOS COM DTOs =====

  @Override
  public RestauranteResponse cadastrarRestaurante(RestauranteRequest restauranteRequest) {
    Restaurante restaurante = restauranteMapper.toEntity(restauranteRequest);
    Restaurante salvo = restauranteRepository.save(restaurante);
    return restauranteMapper.toResponse(salvo);
  }

  @Override
  @Transactional(readOnly = true)
  public RestauranteResponse buscarRestaurantePorId(Long id) {
    Restaurante restaurante =
        restauranteRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

    // Validate not soft deleted
    if (Boolean.TRUE.equals(restaurante.getExcluido())) {
      throw new BusinessException("Restaurante foi excluído do sistema");
    }

    return restauranteMapper.toResponse(restaurante);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestauranteResponse> buscarRestaurantesPorCategoria(String categoria) {
    List<Restaurante> restaurantes =
        restauranteRepository.findByCategoriaAndExcluidoFalse(categoria);
    return restaurantes.stream().map(restauranteMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<RestauranteResponse> buscarRestaurantesPorCategoria(
      String categoria, org.springframework.data.domain.Pageable pageable) {
    var page = restauranteRepository.findByCategoriaAndExcluidoFalse(categoria, pageable);
    return page.map(restauranteMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestauranteResponse> buscarRestaurantesDisponiveis() {
    List<Restaurante> restaurantes = restauranteRepository.findByAtivoTrueAndExcluidoFalse();
    return restaurantes.stream().map(restauranteMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<RestauranteResponse> buscarRestaurantesDisponiveis(
      org.springframework.data.domain.Pageable pageable) {
    var page = restauranteRepository.findByAtivoTrueAndExcluidoFalse(pageable);
    return page.map(restauranteMapper::toResponse);
  }

  @Override
  public RestauranteResponse atualizarRestaurante(Long id, RestauranteRequest restauranteRequest) {
    Restaurante existente =
        restauranteRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

    // Validate not soft deleted
    if (Boolean.TRUE.equals(existente.getExcluido())) {
      throw new BusinessException("Não é possível atualizar restaurante excluído");
    }

    // Update fields
    existente.setNome(restauranteRequest.getNome());
    existente.setCategoria(restauranteRequest.getCategoria());
    existente.setEndereco(restauranteRequest.getEndereco());
    existente.setTaxaEntrega(restauranteRequest.getTaxaEntrega());
    existente.setTelefone(restauranteRequest.getTelefone());
    existente.setEmail(restauranteRequest.getEmail());
    existente.setTempoEntregaMinutos(restauranteRequest.getTempoEntregaMinutos());
    existente.setAvaliacao(restauranteRequest.getAvaliacao());

    Restaurante atualizado = restauranteRepository.save(existente);
    return restauranteMapper.toResponse(atualizado);
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<RestauranteResponse> buscarProximos(
      String cep, org.springframework.data.domain.Pageable pageable) {
    // Reuse buscarProximos logic but page the results by querying active restaurants pageable and
    // then filter by region
    var page = restauranteRepository.findByAtivoTrueAndExcluidoFalse(pageable);
    // For simplicity, if region filter reduces results it will be applied in-memory on the page
    // content
    String primeirosDigitos = cep.substring(0, Math.min(2, cep.length()));
    try {
      int codigoRegiao = Integer.parseInt(primeirosDigitos);
      if (codigoRegiao <= 5) {
        return page.map(restauranteMapper::toResponse);
      } else {
        var filteredEntities =
            page.getContent().stream()
                .filter(r -> r.getTaxaEntrega().compareTo(new java.math.BigDecimal("10.00")) <= 0)
                .toList();
        var filtered = filteredEntities.stream().map(restauranteMapper::toResponse).toList();
        return new org.springframework.data.domain.PageImpl<
            com.deliverytech.delivery_api.dto.response.RestauranteResponse>(
            filtered, pageable, filtered.size());
      }
    } catch (NumberFormatException e) {
      return page.map(restauranteMapper::toResponse);
    }
  }
}
