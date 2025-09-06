package com.deliverytech.delivery_api.filter;

import com.deliverytech.delivery_api.service.LoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(1)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

  private final LoggingService loggingService;

  public RequestResponseLoggingFilter(LoggingService loggingService) {
    this.loggingService = loggingService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Wrap request and response to enable content caching
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    // Record start time
    long startTime = System.currentTimeMillis();
    String requestId = UUID.randomUUID().toString();

    try {
      // Continue with the filter chain
      filterChain.doFilter(wrappedRequest, wrappedResponse);
    } finally {
      // Record end time
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;

      // Log the request and response
      loggingService.logRequestResponse(wrappedRequest, wrappedResponse, requestId, duration);

      // Copy response body back to original response
      wrappedResponse.copyBodyToResponse();
    }
  }
}
