package com.deliverytech.deliveryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para exceções da API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de argumentos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Trata exceções de argumento ilegal
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        
        // Tratar casos específicos do delivery conforme orientações do projeto
        String message = ex.getMessage().toLowerCase();
        if (message.contains("estoque insuficiente")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(error);
        } else if (message.contains("cancelamento após preparo") || message.contains("não pode ser cancelado")) {
            return ResponseEntity.status(HttpStatus.TOO_EARLY).body(error);
        }
        
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata exceções de recurso não encontrado
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        
        if (ex.getMessage().contains("não encontrado")) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Trata exceções genéricas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro interno do servidor");
        error.put("details", ex.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}
