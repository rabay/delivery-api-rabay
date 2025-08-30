package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Implementa a interface para validar a anotação @ValidNome Valida strings que representam nomes
 * próprios
 */
public class NomeValidator implements ConstraintValidator<ValidNome, String> {

    // Padrão regex que permite letras (incluindo acentuadas), espaços, hífens e apostrofes
    private static final String NOME_PATTERN = "^[\\p{L}][\\p{L}\\s'-]*[\\p{L}]$|^[\\p{L}]$";

    private static final Pattern pattern = Pattern.compile(NOME_PATTERN);

    private int minLength;
    private int maxLength;

    /** Inicializa o validador com os parâmetros da anotação */
    @Override
    public void initialize(ValidNome constraintAnnotation) {
        this.minLength = constraintAnnotation.min();
        this.maxLength = constraintAnnotation.max();
    }

    /** Método que contém a regra de validação 'value' é o valor do campo anotado com @ValidNome */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Se o valor for nulo ou vazio, considera válido
        // Porque a anotação @NotNull deve ser usada para validar presença
        if (value == null || value.isEmpty()) {
            return true;
        }

        // Remove espaços em branco do início e fim
        String trimmedValue = value.trim();

        // Verifica se o valor original era diferente do trimmed (tinha espaços nas bordas)
        if (!value.equals(trimmedValue)) {
            return false;
        }

        // Verifica comprimento
        if (trimmedValue.length() < minLength || trimmedValue.length() > maxLength) {
            return false;
        }

        // Verifica se não há espaços consecutivos
        if (trimmedValue.contains("  ")) {
            return false;
        }

        // Verifica se não há hífens ou apostrofes consecutivos
        if (trimmedValue.contains("--") || trimmedValue.contains("''")) {
            return false;
        }

        // Verifica se não começa ou termina com hífen ou apostrofe
        if (trimmedValue.startsWith("-")
                || trimmedValue.endsWith("-")
                || trimmedValue.startsWith("'")
                || trimmedValue.endsWith("'")) {
            return false;
        }

        // Verifica se corresponde ao padrão regex
        if (!pattern.matcher(trimmedValue).matches()) {
            return false;
        }

        // Verifica se não contém números
        if (trimmedValue.matches(".*\\d.*")) {
            return false;
        }

        // Verifica se todas as palavras têm pelo menos 1 caractere (não há espaços seguidos de
        // hífen/apostrofe)
        String[] words = trimmedValue.split("\\s+");
        for (String word : words) {
            if (word.isEmpty() || word.matches("^[-']+$")) {
                return false;
            }
        }

        return true;
    }
}
