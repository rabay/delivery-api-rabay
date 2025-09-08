package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EmailDuplicadoException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.exception.ProdutoIndisponivelException;
import com.deliverytech.delivery_api.exception.TransactionException;
import com.deliverytech.delivery_api.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// OffsetDateTime não utilizado

@ControllerAdvice
@Tag(name = "Tratamento de Erros", description = "Tratamento global de exceções da aplicação")
public class GlobalExceptionHandler {
  @ExceptionHandler(EmailDuplicadoException.class)
  @Operation(
      summary = "Tratamento de email duplicado",
      description = "Retorna erro quando um email já está cadastrado")
  @ApiResponses({@ApiResponse(responseCode = "409", description = "Email já cadastrado")})
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleEmailDuplicado(EmailDuplicadoException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "EmailDuplicado", "Email duplicado: " + ex.getMessage(), HttpStatus.CONFLICT.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Email já cadastrado", false);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(EstoqueInsuficienteException.class)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleEstoqueInsuficiente(EstoqueInsuficienteException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "EstoqueInsuficiente", "Estoque insuficiente: " + ex.getMessage(), HttpStatus.CONFLICT.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Estoque insuficiente", false);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(ProdutoIndisponivelException.class)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleProdutoIndisponivel(ProdutoIndisponivelException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "ProdutoIndisponivel", "Produto indisponível: " + ex.getMessage(), HttpStatus.CONFLICT.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Produto indisponível", false);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @Operation(
      summary = "Tratamento de entidade não encontrada",
      description = "Retorna erro quando uma entidade não é encontrada")
  @ApiResponses({@ApiResponse(responseCode = "404", description = "Entidade não encontrada")})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleEntityNotFound(EntityNotFoundException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "EntityNotFound", "Entidade não encontrada: " + ex.getMessage(), HttpStatus.NOT_FOUND.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Recurso não encontrado", false);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleBusinessException(BusinessException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "BusinessError", "Erro de negócio: " + ex.getMessage(), HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Erro de negócio", false);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ValidationErrorResponse>>
      handleValidationException(ValidationException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("error", ex.getMessage());
    var error =
        new com.deliverytech.delivery_api.dto.response.ValidationErrorResponse(
            "ValidationFailed", errors, HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Dados de entrada inválidos", false);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(TransactionException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ErrorResponse>>
      handleTransactionException(TransactionException ex) {
    var error =
        new com.deliverytech.delivery_api.dto.response.ErrorResponse(
            "TransactionError", "Erro de transação: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Erro interno", false);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @Operation(
      summary = "Tratamento de validação de argumentos",
      description = "Retorna erros de validação quando argumentos não são válidos")
  @ApiResponses({@ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ValidationErrorResponse>>
      handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    var error =
        new com.deliverytech.delivery_api.dto.response.ValidationErrorResponse(
            "ValidationFailed", errors, HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Dados de entrada inválidos", false);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ValidationErrorResponse>>
      handleConstraintViolationException(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String fieldName = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();
      errors.put(fieldName, errorMessage);
    }
    var error =
        new com.deliverytech.delivery_api.dto.response.ValidationErrorResponse(
            "ValidationFailed", errors, HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Dados de entrada inválidos", false);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ValidationErrorResponse>>
      handleMissingParams(MissingServletRequestParameterException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("parameter", "Parâmetro obrigatório ausente: " + ex.getParameterName());
    var error =
        new com.deliverytech.delivery_api.dto.response.ValidationErrorResponse(
            "ValidationFailed", errors, HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Dados de entrada inválidos", false);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.ValidationErrorResponse>>
      handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("parameter", "Valor do parâmetro inválido para: " + ex.getName());
    var error =
        new com.deliverytech.delivery_api.dto.response.ValidationErrorResponse(
            "ValidationFailed", errors, HttpStatus.BAD_REQUEST.value(), java.time.OffsetDateTime.now());
    var body = new com.deliverytech.delivery_api.dto.response.ApiResult<>(error, "Dados de entrada inválidos", false);
    return ResponseEntity.badRequest().body(body);
  }
}
