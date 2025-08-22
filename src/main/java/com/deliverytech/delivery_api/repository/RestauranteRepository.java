package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.math.BigDecimal;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);
    List<Restaurante> findByCategoria(String categoria);
    List<Restaurante> findByAtivoTrue();
    List<Restaurante> findAllByOrderByAvaliacaoDesc();
    List<Restaurante> findByAvaliacaoGreaterThanEqual(BigDecimal minAvaliacao);
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);
    List<Restaurante> findByAtivoFalse();
    List<Restaurante> findByCategoriaAndAtivo(String categoria, Boolean ativo);
    List<Restaurante> findTop5ByOrderByNomeAsc();
}
