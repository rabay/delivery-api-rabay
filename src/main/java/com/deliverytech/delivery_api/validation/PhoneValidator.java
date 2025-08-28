package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Implementa a interface para validar a anotação @ValidPhone
 * Valida strings que representam números de telefone brasileiros
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    // Padrões regex para diferentes formatos de telefone brasileiro
    private static final String PHONE_PATTERN_FORMATTED_MOBILE = 
        "^\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}$";  // (11) 99999-9999
    
    private static final String PHONE_PATTERN_FORMATTED_LANDLINE = 
        "^\\(\\d{2}\\)\\s?[2-5]\\d{3}-\\d{4}$";   // (11) 3333-4444
    
    private static final String PHONE_PATTERN_UNFORMATTED_MOBILE = 
        "^\\d{2}9\\d{8}$";                   // 11999999999
    
    private static final String PHONE_PATTERN_UNFORMATTED_LANDLINE = 
        "^\\d{2}[2-5]\\d{7}$";                   // 1133334444
    
    private static final String PHONE_PATTERN_INTERNATIONAL = 
        "^\\+55\\d{2}9?\\d{8}$";             // +5511999999999
    
    private static final Pattern[] patterns = {
        Pattern.compile(PHONE_PATTERN_FORMATTED_MOBILE),
        Pattern.compile(PHONE_PATTERN_FORMATTED_LANDLINE),
        Pattern.compile(PHONE_PATTERN_UNFORMATTED_MOBILE),
        Pattern.compile(PHONE_PATTERN_UNFORMATTED_LANDLINE),
        Pattern.compile(PHONE_PATTERN_INTERNATIONAL)
    };

    /**
     * Método que contém a regra de validação
     * 'value' é o valor do campo anotado com @ValidPhone
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Se o valor for nulo ou vazio, considera válido
        // Porque a anotação @NotNull deve ser usada para validar presença
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Remove espaços em branco extras
        String cleanValue = value.trim();

        // Testa contra todos os padrões válidos
        for (Pattern pattern : patterns) {
            if (pattern.matcher(cleanValue).matches()) {
                return isValidDDD(extractDDD(cleanValue));
            }
        }

        return false;
    }

    /**
     * Extrai o DDD do número de telefone
     */
    private String extractDDD(String phone) {
        // Remove caracteres não numéricos e extrai os dois primeiros dígitos
        String numbersOnly = phone.replaceAll("[^\\d]", "");
        
        // Para números internacionais, pula o código do país (+55)
        if (phone.startsWith("+55")) {
            numbersOnly = numbersOnly.substring(2);
        }
        
        if (numbersOnly.length() >= 2) {
            return numbersOnly.substring(0, 2);
        }
        
        return "";
    }

    /**
     * Valida se o DDD é válido no Brasil
     */
    private boolean isValidDDD(String ddd) {
        // DDDs válidos no Brasil
        String[] validDDDs = {
            "11", "12", "13", "14", "15", "16", "17", "18", "19", // São Paulo
            "21", "22", "24",                                     // Rio de Janeiro
            "27", "28",                                           // Espírito Santo
            "31", "32", "33", "34", "35", "37", "38",            // Minas Gerais
            "41", "42", "43", "44", "45", "46",                  // Paraná
            "47", "48", "49",                                     // Santa Catarina
            "51", "53", "54", "55",                              // Rio Grande do Sul
            "61",                                                 // Distrito Federal
            "62", "64",                                           // Goiás
            "63",                                                 // Tocantins
            "65", "66",                                           // Mato Grosso
            "67",                                                 // Mato Grosso do Sul
            "68",                                                 // Acre
            "69",                                                 // Rondônia
            "71", "73", "74", "75", "77",                        // Bahia
            "79",                                                 // Sergipe
            "81", "87",                                           // Pernambuco
            "82",                                                 // Alagoas
            "83",                                                 // Paraíba
            "84",                                                 // Rio Grande do Norte
            "85", "88",                                           // Ceará
            "86", "89",                                           // Piauí
            "91", "93", "94",                                     // Pará
            "92", "97",                                           // Amazonas
            "95",                                                 // Roraima
            "96",                                                 // Amapá
            "98", "99"                                            // Maranhão
        };

        for (String validDDD : validDDDs) {
            if (validDDD.equals(ddd)) {
                return true;
            }
        }

        return false;
    }
}