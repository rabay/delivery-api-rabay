package com.deliverytech.delivery_api.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Service
@Slf4j
public class LoggingService {

  private final String logDirectory;

  public LoggingService(@Value("${logging.directory:entregaveis/logs}") String logDirectory) {
    this.logDirectory = logDirectory;
    initializeLogDirectory();
  }

  public void logRequestResponse(
      ContentCachingRequestWrapper request,
      ContentCachingResponseWrapper response,
      String requestId,
      long duration) {

    // Determine log file name based on endpoint
    String logFileName = determineLogFileName(request.getRequestURI());

    // Extract request and response data
    String requestData = extractRequestData(request);
    String responseData = extractResponseData(response);

    // Format log entry
    String logEntry =
        formatLogEntry(
            requestId,
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            duration,
            requestData,
            responseData);

    // Write to log file
    writeLogEntry(logFileName, logEntry);
  }

  private String determineLogFileName(String requestURI) {
    if (requestURI.startsWith("/api/clientes")) {
      return "clientes.log";
    } else if (requestURI.startsWith("/api/restaurantes")) {
      return "restaurantes.log";
    } else if (requestURI.startsWith("/api/produtos")) {
      return "produtos.log";
    } else if (requestURI.startsWith("/api/pedidos")) {
      return "pedidos.log";
    } else if (requestURI.startsWith("/api/auth")) {
      return "auth.log";
    } else if (requestURI.startsWith("/api/relatorios")) {
      return "relatorios.log";
    } else if (requestURI.startsWith("/health")
        || requestURI.startsWith("/info")
        || requestURI.startsWith("/actuator/health")
        || requestURI.startsWith("/actuator/info")) {
      return "health.log";
    } else if (requestURI.startsWith("/db")) {
      return "db.log";
    } else {
      return "outros.log";
    }
  }

  private String extractRequestData(ContentCachingRequestWrapper request) {
    StringBuilder requestData = new StringBuilder();

    // Add headers (excluding sensitive ones)
    requestData.append("HEADERS:\n");
    Collections.list(request.getHeaderNames())
        .forEach(
            headerName -> {
              if (!isSensitiveHeader(headerName)) {
                requestData
                    .append(headerName)
                    .append(": ")
                    .append(request.getHeader(headerName))
                    .append("\n");
              } else {
                requestData.append(headerName).append(": ***MASKED***\n");
              }
            });

    // Add body content
    requestData.append("BODY:\n");
    byte[] content = request.getContentAsByteArray();
    if (content.length > 0) {
      String body = new String(content, StandardCharsets.UTF_8);
      requestData.append(maskSensitiveData(body));
    }

    return requestData.toString();
  }

  private String extractResponseData(ContentCachingResponseWrapper response) {
    StringBuilder responseData = new StringBuilder();

    // Add headers
    responseData.append("HEADERS:\n");
    response
        .getHeaderNames()
        .forEach(
            headerName -> {
              responseData
                  .append(headerName)
                  .append(": ")
                  .append(response.getHeader(headerName))
                  .append("\n");
            });

    // Add body content
    responseData.append("BODY:\n");
    byte[] content = response.getContentAsByteArray();
    if (content.length > 0) {
      String body = new String(content, StandardCharsets.UTF_8);
      responseData.append(maskSensitiveData(body));
    }

    return responseData.toString();
  }

  private boolean isSensitiveHeader(String headerName) {
    return "Authorization".equalsIgnoreCase(headerName)
        || "X-Auth-Token".equalsIgnoreCase(headerName)
        || "Cookie".equalsIgnoreCase(headerName);
  }

  private String maskSensitiveData(String data) {
    // Mask passwords
    data = data.replaceAll("(\"password\"\\s*:\\s*\")([^\"]*)(\")", "$1***MASKED***$3");
    data = data.replaceAll("(\"token\"\\s*:\\s*\")([^\"]*)(\")", "$1***MASKED***$3");
    return data;
  }

  private String formatLogEntry(
      String requestId,
      String method,
      String uri,
      int status,
      long duration,
      String requestData,
      String responseData) {
    StringBuilder logEntry = new StringBuilder();

    // Add timestamp and basic info
    logEntry
        .append("[")
        .append(Instant.now().toString())
        .append("] [")
        .append(requestId)
        .append("] [")
        .append(method)
        .append("] [")
        .append(uri)
        .append("] [")
        .append(status)
        .append("] [")
        .append(duration)
        .append("ms]\n");

    // Add request and response data
    logEntry
        .append("REQUEST:\n")
        .append(requestData)
        .append("\nRESPONSE:\n")
        .append(responseData)
        .append("\n----------------------------------------\n");

    return logEntry.toString();
  }

  private void writeLogEntry(String logFileName, String logEntry) {
    try {
      Path logDir = Paths.get(logDirectory);
      if (!Files.exists(logDir)) {
        Files.createDirectories(logDir);
      }

      Path logFile = logDir.resolve(logFileName);
      Files.write(
          logFile,
          logEntry.getBytes(StandardCharsets.UTF_8),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      log.error("Failed to write log entry to file: {}", logFileName, e);
    }
  }

  private void initializeLogDirectory() {
    try {
      Path logDir = Paths.get(logDirectory);
      if (!Files.exists(logDir)) {
        Files.createDirectories(logDir);
      }
    } catch (IOException e) {
      log.error("Failed to create log directory: {}", logDirectory, e);
    }
  }
}
