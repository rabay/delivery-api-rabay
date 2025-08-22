package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.math.BigDecimal;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByNomeContainingIgnoreCaseAndExcluidoFalse(String nome);
    List<Restaurante> findByCategoriaAndExcluidoFalse(String categoria);
    List<Restaurante> findByAtivoTrueAndExcluidoFalse();
    List<Restaurante> findAllByExcluidoFalseOrderByAvaliacaoDesc();
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndExcluidoFalse(BigDecimal minAvaliacao);
    List<Restaurante> findByTaxaEntregaLessThanEqualAndExcluidoFalse(BigDecimal taxa);
    List<Restaurante> findByAtivoFalseAndExcluidoFalse();
    List<Restaurante> findByCategoriaAndAtivoAndExcluidoFalse(String categoria, Boolean ativo);
    List<Restaurante> findTop5ByExcluidoFalseOrderByNomeAsc();
}
