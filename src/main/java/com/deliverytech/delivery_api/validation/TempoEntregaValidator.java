package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementa a interface para validar a anotação @ValidTempoEntrega
 * Valida inteiros que representam tempo de entrega em minutos
 */
public class TempoEntregaValidator implements ConstraintValidator<ValidTempoEntrega, Integer> {

    private int minTempo;
    private int maxTempo;
    private int intervalo;

    /**
     * Inicializa o validador com os parâmetros da anotação
     */
    @Override
    public void initialize(ValidTempoEntrega constraintAnnotation) {
        this.minTempo = constraintAnnotation.min();
        this.maxTempo = constraintAnnotation.max();
        this.intervalo = constraintAnnotation.interval();
    }

    /**
     * Método que contém a regra de validação
     * 'value' é o valor do campo anotado com @ValidTempoEntrega
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        // Se o valor for nulo, considera válido
        // Porque a anotação @NotNull deve ser usada para validar presença
        if (value == null) {
            return true;
        }

        // Verifica se está dentro do range mínimo e máximo
        if (value < minTempo || value > maxTempo) {
            return false;
        }

        // Verifica se é múltiplo do intervalo especificado
        if (intervalo > 0 && value % intervalo != 0) {
            return false;
        }

        // Validações específicas para tempos de entrega realistas
        
        // Tempos muito específicos que são comuns no mercado
        // 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60 minutos, etc.
        // Mas vamos permitir apenas múltiplos de 5 para padronizar
        
        // Verifica se não é um tempo muito específico (ex: 7, 13, 27 minutos)
        // que seria improvável em um contexto real de delivery
        return isRealisticDeliveryTime(value);
    }

    /**
     * Verifica se o tempo de entrega é realista para o contexto de delivery
     */
    private boolean isRealisticDeliveryTime(Integer tempo) {
        // Tempos de entrega típicos no mercado brasileiro
        // Permitimos flexibilidade mas mantemos dentro do razoável
        
        // Muito rápido (menos de 15 min) - só para estabelecimentos muito próximos
        if (tempo < 15) {
            return tempo >= 5; // Mínimo absoluto
        }
        
        // Tempo normal (15-60 min) - maioria dos casos
        if (tempo <= 60) {
            return true;
        }
        
        // Tempo longo (60-120 min) - distâncias maiores ou preparo complexo
        if (tempo <= 120) {
            return true;
        }
        
        // Tempo muito longo (120-240 min) - casos especiais
        if (tempo <= 240) {
            // Para tempos muito longos, deve ser múltiplo de 15 ou 30 minutos
            return tempo % 15 == 0 || tempo % 30 == 0;
        }
        
        return false;
    }
}