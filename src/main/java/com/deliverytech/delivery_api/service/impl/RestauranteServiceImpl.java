package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class RestauranteServiceImpl implements RestauranteService {
    private final RestauranteRepository restauranteRepository;

    public RestauranteServiceImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public Restaurante cadastrar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    public List<Restaurante> buscarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Override
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    @Override
    public Restaurante ativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    @Override
    public Restaurante inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        restaurante.setAtivo(false);
        return restauranteRepository.save(restaurante);
    }
}
