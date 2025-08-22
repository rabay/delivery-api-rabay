package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestaurante(Restaurante restaurante);
    List<Produto> findByRestauranteId(Long restauranteId);
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByDisponivelTrue();
    List<Produto> findByPrecoLessThanEqual(java.math.BigDecimal preco);
}
