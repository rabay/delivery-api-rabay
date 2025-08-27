# Security Testing Guide

## Overview
This guide documents the security test scenarios and best practices for testing JWT authentication in the Delivery API application.

## Test Configuration Patterns

### 1. Full Integration Tests (@SpringBootTest)
Use for testing complete security flow with actual Spring Security configuration:

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {
    // Full context loading with JWT security enabled
}
```

**When to use:**
- Testing complete authentication flows
- Testing authorization with real security filters
- Integration testing with JWT tokens

### 2. Slice Tests (@WebMvcTest) - Avoid for Security
❌ **Not recommended** for security testing as it doesn't load the complete security context:

```java
// DON'T DO THIS for security tests
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    // Limited context, security issues
}
```

## Custom Test Annotations

### @WithMockJwtUser
Use our custom annotation for authenticated test scenarios:

```java
@Test
@WithMockJwtUser(email = "test@example.com", role = Role.ADMIN)
void adminShouldAccessProtectedEndpoint() {
    // Test with authenticated admin user
}
```

## Security Test Scenarios

### 1. Authentication Tests
- ✅ Valid credentials return JWT token
- ✅ Invalid credentials return 401 Unauthorized  
- ✅ Missing credentials return 400 Bad Request
- ✅ Malformed requests return 400 Bad Request

### 2. Authorization Tests
- ✅ Unauthenticated requests return 403 Forbidden
- ✅ Authenticated users access allowed endpoints
- ✅ Role-based access control works correctly
- ✅ Users can only access their own resources

### 3. JWT Token Tests
- ✅ Tokens are properly generated with different timestamps
- ✅ Valid tokens are accepted
- ✅ Invalid/expired tokens are rejected
- ✅ Token claims contain correct user information

### 4. Public Endpoint Tests
- ✅ Authentication endpoints (/api/auth/**) are public
- ✅ Health endpoints are accessible
- ✅ Swagger documentation is accessible

## Test Utilities

### JwtTestUtils
Provides helper methods for JWT testing:

```java
// Create test users
Usuario adminUser = JwtTestUtils.createAdminUser();
Usuario clienteUser = JwtTestUtils.createClienteUser();

// Setup authentication context
JwtTestUtils.setupAuthenticationContext(user);
```

### WithMockJwtUserSecurityContextFactory
Custom security context factory for @WithMockJwtUser annotation.

## Best Practices

### 1. Test Context Configuration
- Use @SpringBootTest for security tests
- Add @ActiveProfiles("test") for test-specific configuration
- Include @AutoConfigureMockMvc for MockMvc support

### 2. Mock Management
- Mock external dependencies, not security components
- Use @MockBean for service layer dependencies
- Avoid mocking AuthenticationManager in integration tests

### 3. Error Handling Tests
- Test all error scenarios (401, 403, 400)
- Verify proper exception types are thrown
- Ensure error responses have correct HTTP status codes

### 4. Token Testing
- Test token generation with time delays for uniqueness
- Verify token validation logic
- Test token expiration scenarios

## Common Issues and Solutions

### Issue: ApplicationContext fails to load
**Solution:** Use @SpringBootTest instead of @WebMvcTest for security tests

### Issue: Tests expect 401 but get 403
**Solution:** Spring Security returns 403 for protected resources without authentication (correct behavior)

### Issue: JWT tokens are identical
**Solution:** Add sufficient delay (1 second) between token generations

### Issue: Unnecessary stubbing errors
**Solution:** Remove mocks that aren't actually used by the method under test

## Test Data Management

### Default Test Users
The application creates default users in test environment:
- admin@deliveryapi.com (ADMIN)
- cliente@test.com (CLIENTE)  
- restaurante@test.com (RESTAURANTE)
- entregador@test.com (ENTREGADOR)

### Custom Test Users
Create specific test users using JwtTestUtils for test isolation:

```java
Usuario customUser = JwtTestUtils.createTestUsuario("custom@example.com", Role.CLIENTE);
```

## Conclusion

Following these patterns and practices ensures robust security testing that accurately reflects the real application behavior while maintaining test reliability and maintainability.