package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Implementa a interface para validar a anotação @ValidEmail
 * Valida strings que representam endereços de email
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    // Padrão regex para validação de email mais rigorosa
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    // Comprimento máximo permitido para emails (RFC 5321)
    private static final int MAX_EMAIL_LENGTH = 255;

    /**
     * Método que contém a regra de validação
     * 'value' é o valor do campo anotado com @ValidEmail
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Se o valor for nulo ou vazio, considera válido
        // Porque a anotação @NotNull deve ser usada para validar presença
        if (value == null || value.isEmpty()) {
            return true;
        }

        // Verifica se não excede o comprimento máximo
        if (value.length() > MAX_EMAIL_LENGTH) {
            return false;
        }

        // Verifica se o formato está correto usando regex
        if (!pattern.matcher(value).matches()) {
            return false;
        }

        // Validações adicionais de domínio
        String[] parts = value.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        // Local part não pode estar vazio ou ter mais de 64 caracteres
        if (localPart.isEmpty() || localPart.length() > 64) {
            return false;
        }

        // Domain part não pode estar vazio ou ter mais de 253 caracteres
        if (domainPart.isEmpty() || domainPart.length() > 253) {
            return false;
        }

        // Não pode começar ou terminar com ponto
        if (localPart.startsWith(".") || localPart.endsWith(".") ||
            domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        }

        // Não pode ter pontos consecutivos
        if (value.contains("..")) {
            return false;
        }

        return true;
    }
}