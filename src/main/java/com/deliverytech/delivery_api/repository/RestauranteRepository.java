package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findAllByOrderByAvaliacaoDesc();
}
