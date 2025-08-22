package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmailAndExcluidoFalse(String email);
    List<Cliente> findByAtivoTrueAndExcluidoFalse();
    List<Cliente> findByNomeContainingIgnoreCaseAndExcluidoFalse(String nome);
    boolean existsByEmailAndExcluidoFalse(String email);

    // === CONSULTA NATIVA: Ranking de clientes por nº de pedidos ===
    @Query(value = "SELECT c.id as idCliente, c.nome as nomeCliente, SUM(COALESCE(p.valor_total,0)) as totalCompras, COUNT(p.id) as quantidadePedidos FROM cliente c LEFT JOIN pedido p ON c.id = p.cliente_id WHERE c.excluido = false GROUP BY c.id, c.nome ORDER BY quantidadePedidos DESC LIMIT 10", nativeQuery = true)
    List<RelatorioVendasClientes> rankingClientesPorPedidos();
}
