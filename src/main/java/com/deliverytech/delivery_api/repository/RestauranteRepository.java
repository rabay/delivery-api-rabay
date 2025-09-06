package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
  List<Restaurante> findByNomeContainingIgnoreCaseAndExcluidoFalse(String nome);

  List<Restaurante> findByCategoriaAndExcluidoFalse(String categoria);

  org.springframework.data.domain.Page<Restaurante> findByCategoriaAndExcluidoFalse(
      String categoria, org.springframework.data.domain.Pageable pageable);

  List<Restaurante> findByAtivoTrueAndExcluidoFalse();

  org.springframework.data.domain.Page<Restaurante> findByAtivoTrueAndExcluidoFalse(
      org.springframework.data.domain.Pageable pageable);

  List<Restaurante> findAllByExcluidoFalseOrderByAvaliacaoDesc();

  List<Restaurante> findByAvaliacaoGreaterThanEqualAndExcluidoFalse(BigDecimal minAvaliacao);

  List<Restaurante> findByTaxaEntregaLessThanEqualAndExcluidoFalse(BigDecimal taxa);

  List<Restaurante> findByAtivoFalseAndExcluidoFalse();

  List<Restaurante> findByCategoriaAndAtivoAndExcluidoFalse(String categoria, Boolean ativo);

  List<Restaurante> findTop5ByExcluidoFalseOrderByNomeAsc();
}
