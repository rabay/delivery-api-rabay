package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.projection.FaturamentoPorCategoriaProjection;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

  List<Produto> findByNomeContainingIgnoreCaseAndExcluidoFalse(String nome);

  org.springframework.data.domain.Page<Produto> findByNomeContainingIgnoreCaseAndExcluidoFalse(
      String nome, org.springframework.data.domain.Pageable pageable);

  List<Produto> findByDisponivelTrueAndExcluidoFalse();

  org.springframework.data.domain.Page<Produto> findByDisponivelTrueAndExcluidoFalse(
      org.springframework.data.domain.Pageable pageable);

  List<Produto> findByRestauranteIdAndExcluidoFalse(Long restauranteId);

  List<Produto> findByRestauranteAndExcluidoFalse(
      com.deliverytech.delivery_api.model.Restaurante restaurante);

  List<Produto> findByCategoriaAndExcluidoFalse(String categoria);

  org.springframework.data.domain.Page<Produto> findByCategoriaAndExcluidoFalse(
      String categoria, org.springframework.data.domain.Pageable pageable);

  org.springframework.data.domain.Page<Produto> findByRestauranteIdAndExcluidoFalse(
      Long restauranteId, org.springframework.data.domain.Pageable pageable);

  List<Produto> findByPrecoLessThanEqualAndExcluidoFalse(java.math.BigDecimal preco);

  // === CONSULTA NATIVA: Produtos mais vendidos ===
  @Query(
      value =
          "SELECT p.id as idProduto, p.nome as nomeProduto, SUM(ip.quantidade *"
              + " ip.preco_unitario) as totalVendas, COUNT(ip.id) as quantidadeItemPedido"
              + " FROM produto p LEFT JOIN item_pedido ip ON p.id = ip.produto_id LEFT"
              + " JOIN pedido ped ON ip.pedido_id = ped.id WHERE p.excluido = false AND"
              + " ped.data_pedido BETWEEN :inicio AND :fim GROUP BY p.id, p.nome ORDER BY"
              + " totalVendas DESC",
      nativeQuery = true)
  List<RelatorioVendasProdutos> produtosMaisVendidos(
      @Param("inicio") java.time.LocalDateTime inicio,
      @Param("fim") java.time.LocalDateTime fim,
      Pageable pageable);

  // === CONSULTA NATIVA: Faturamento por categoria ===
  @Query(
      value =
          "SELECT p.categoria as categoria, SUM(ip.quantidade * ip.preco_unitario) as totalFaturado"
              + " FROM produto p LEFT JOIN item_pedido ip ON p.id = ip.produto_id LEFT JOIN pedido"
              + " ped ON ip.pedido_id = ped.id WHERE p.excluido = false AND ped.data_pedido BETWEEN"
              + " :inicio AND :fim GROUP BY p.categoria ORDER BY totalFaturado DESC",
      nativeQuery = true)
  List<FaturamentoPorCategoriaProjection> faturamentoPorCategoria(
      @Param("inicio") java.time.LocalDateTime inicio, @Param("fim") java.time.LocalDateTime fim);
}
