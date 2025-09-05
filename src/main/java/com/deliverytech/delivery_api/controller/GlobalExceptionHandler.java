package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.exception.EmailDuplicadoException;
import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.exception.ProdutoIndisponivelException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.ValidationException;
import com.deliverytech.delivery_api.exception.TransactionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
// OffsetDateTime não utilizado

@ControllerAdvice
@Tag(name = "Tratamento de Erros", description = "Tratamento global de exceções da aplicação")
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailDuplicadoException.class)
    @Operation(summary = "Tratamento de email duplicado", 
               description = "Retorna erro quando um email já está cadastrado")
    @ApiResponses({
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleEmailDuplicado(EmailDuplicadoException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Email duplicado: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Estoque insuficiente: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    
    @ExceptionHandler(ProdutoIndisponivelException.class)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleProdutoIndisponivel(ProdutoIndisponivelException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Produto indisponível: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    @Operation(summary = "Tratamento de entidade não encontrada", 
               description = "Retorna erro quando uma entidade não é encontrada")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "Entidade não encontrada")
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Entidade não encontrada: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleBusinessException(BusinessException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Erro de negócio: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> handleValidationException(ValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(errors, "Erro de validação", false);
        return ResponseEntity.badRequest().body(body);
    }
    
    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Object>> handleTransactionException(TransactionException ex) {
        var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(null, "Erro de transação: " + ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Operation(summary = "Tratamento de validação de argumentos", 
               description = "Retorna erros de validação quando argumentos não são válidos")
    @ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(errors, "Erro de validação", false);
    return ResponseEntity.badRequest().body(body);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(errors, "Erro de validação", false);
    return ResponseEntity.badRequest().body(body);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> handleMissingParams(MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("parameter", "Parâmetro obrigatório ausente: " + ex.getParameterName());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(errors, "Erro de validação", false);
    return ResponseEntity.badRequest().body(body);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("parameter", "Valor do parâmetro inválido para: " + ex.getName());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(errors, "Erro de validação", false);
    return ResponseEntity.badRequest().body(body);
    }
}