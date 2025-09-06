package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.LoginRequest;
import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.dto.response.LoginResponse;
import com.deliverytech.delivery_api.dto.response.UserResponse;
import com.deliverytech.delivery_api.exception.EmailJaCadastradoException;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(
    name = "Autenticação",
    description = "Operações relacionadas com autenticação e cadastro de usuários")
public class AuthController {

  @Autowired private UsuarioService usuarioService;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtUtil jwtUtil;

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Login de usuário", description = "Realizar o login do usuário")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Login realizado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResult.class),
                examples = {
                  @io.swagger.v3.oas.annotations.media.ExampleObject(
                      name = "login-success",
                      value =
                          "{\"data\":{\"token\":\"eyJhbGciOi...\"},\"message\":\"Login realizado"
                              + " com sucesso\",\"success\":true}")
                })),
    @ApiResponse(
        responseCode = "401",
        description = "Login inválido",
        content = @Content(schema = @Schema(implementation = Void.class))),
  })
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    try {
      // Use username and password for authentication
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  request.getUsername(), request.getPassword()));

      // If authentication is successful, generate JWT token
      String username = authentication.getName();
      String token = jwtUtil.gerarToken(username);

      LoginResponse dto = new LoginResponse(token);
      com.deliverytech.delivery_api.dto.response.ApiResult<LoginResponse> body =
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              dto, "Login realizado com sucesso", true);
      return ResponseEntity.ok(body);
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Usuário ou senha inválidos", false));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Erro interno: " + e.getMessage(), false));
    }
  }

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Registrar um usuário",
      description = "Cadastrar um novo usuário na plataforma")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Registro salvo com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResult.class),
                examples = {
                  @io.swagger.v3.oas.annotations.media.ExampleObject(
                      name = "register-success",
                      value =
                          "{\"data\":{\"id\":1,\"nome\":\"Usuário\",\"email\":\"user@test.com\"},\"message\":\"Registro"
                              + " salvo com sucesso\",\"success\":true}")
                })),
    @ApiResponse(
        responseCode = "400",
        description = "Registro inválido",
        content = @Content(schema = @Schema(implementation = Void.class))),
    @ApiResponse(
        responseCode = "409",
        description = "Email já cadastrado",
        content = @Content(schema = @Schema(implementation = Void.class))),
  })
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<UserResponse>>
      register(@Valid @RequestBody RegisterRequest request) {
    try {
      Usuario novoUsuario = usuarioService.salvar(request);
      UserResponse response = UserResponse.fromEntity(novoUsuario);
      com.deliverytech.delivery_api.dto.response.ApiResult<UserResponse> body =
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              response, "Registro salvo com sucesso", true);
      return ResponseEntity.status(HttpStatus.CREATED).body(body);
    } catch (EmailJaCadastradoException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Email já cadastrado: " + e.getMessage(), false));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Requisição inválida: " + e.getMessage(), false));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Erro interno: " + e.getMessage(), false));
    }
  }
}
