package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void registerMetrics() {
        registerActiveUsersGauge();
        registerPedidosPorStatusGauge();
    }

    private void registerActiveUsersGauge() {
        Gauge.builder("usuarios.ativos", this, MetricService::getActiveUsers)
                .description("Número de usuários ativos")
                .register(meterRegistry);
    }

    private void registerPedidosPorStatusGauge() {
        Arrays.stream(StatusPedido.values()).forEach(status ->
                Gauge.builder("pedidos.status", this, s -> s.getPedidosCountByStatus(status))
                        .description("Número de pedidos por status")
                        .tag("status", status.name())
                        .register(meterRegistry)
        );
    }

    public long getActiveUsers() {
        return usuarioRepository.countByAtivo(true);
    }

    public long getPedidosCountByStatus(StatusPedido status) {
        return pedidoRepository.countByStatus(status);
    }
}
