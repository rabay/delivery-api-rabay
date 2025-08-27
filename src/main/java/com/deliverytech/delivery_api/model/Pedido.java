package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(unique = true, length = 50)
    private String numeroPedido;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(length = 500)
    private String observacoes;

    @Embedded
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean excluido = false;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ItemPedido> itens = new java.util.ArrayList<>();
    
    // Helper methods for bidirectional relationship management
    public void addItem(ItemPedido item) {
        if (itens == null) {
            itens = new java.util.ArrayList<>();
        }
        itens.add(item);
        item.setPedido(this);
    }
    
    public void removeItem(ItemPedido item) {
        if (itens != null) {
            itens.remove(item);
            item.setPedido(null);
        }
    }
}
