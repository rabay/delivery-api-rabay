package com.deliverytech.delivery_api.security;

public enum JwtErrorReason {
    TOKEN_EXPIRED("token_expired"),
    INVALID_SIGNATURE("invalid_signature"),
    MALFORMED_TOKEN("malformed_token"),
    INVALID_TOKEN("invalid_token"),
    AUTH_ERROR("auth_error");

    private final String code;

    JwtErrorReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
