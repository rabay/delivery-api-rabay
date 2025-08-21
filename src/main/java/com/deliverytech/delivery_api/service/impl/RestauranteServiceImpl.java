package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;

@Service
public class RestauranteServiceImpl implements RestauranteService {
    private final RestauranteRepository restauranteRepository;

    public RestauranteServiceImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public Restaurante cadastrar(RestauranteRequest restauranteRequest) {
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
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    @Override
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    @Override
    public List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao) {
        return restauranteRepository.findByAvaliacaoGreaterThanEqual(minAvaliacao);
    }

    @Override
    public List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa) {
        return restauranteRepository.findByTaxaEntregaLessThanEqual(maxTaxa);
    }

    @Override
    public Restaurante atualizar(Long id, RestauranteRequest atualizado) {
        return restauranteRepository.findById(id)
            .map(r -> {
                r.setNome(atualizado.getNome());
                r.setCategoria(atualizado.getCategoria());
                r.setEndereco(atualizado.getEndereco());
                r.setTaxaEntrega(atualizado.getTaxaEntrega());
                r.setTelefone(atualizado.getTelefone());
                r.setEmail(atualizado.getEmail());
                r.setTempoEntregaMinutos(atualizado.getTempoEntregaMinutos());
                r.setAvaliacao(atualizado.getAvaliacao());
                return restauranteRepository.save(r);
            }).orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
    }

    @Override
    public void inativar(Long id) {
        restauranteRepository.findById(id)
            .ifPresentOrElse(
                restaurante -> {
                    restaurante.setAtivo(false);
                    restauranteRepository.save(restaurante);
                },
                () -> {
                    throw new RuntimeException("Restaurante não encontrado - ID: " + id);
                }
            );
    }

    @Override
    public BigDecimal calcularTaxaEntrega(Long restauranteId, String cep) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado - ID: " + restauranteId));
        if (!restaurante.isAtivo()) {
            throw new RuntimeException("Restaurante não está disponível para entrega");
        }
        BigDecimal taxaBase = restaurante.getTaxaEntrega();
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
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado - ID: " + id));
        restaurante.setAtivo(ativo);
        return restauranteRepository.save(restaurante);
    }

    @Override
    public List<Restaurante> buscarProximos(String cep) {
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();
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
    public List<Restaurante> listarComFiltros(String categoria, Boolean ativo) {
        if (categoria == null && ativo == null) {
            return restauranteRepository.findAll();
        }
        if (categoria != null && ativo == null) {
            return restauranteRepository.findByCategoria(categoria);
        }
        if (categoria == null && ativo != null) {
            return ativo ? restauranteRepository.findByAtivoTrue() : restauranteRepository.findByAtivoFalse();
        }
        return restauranteRepository.findByCategoriaAndAtivo(categoria, ativo);
    }
}
