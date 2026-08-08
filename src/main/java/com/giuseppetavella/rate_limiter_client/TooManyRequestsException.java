package com.giuseppetavella.rate_limiter_client;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String service, String details) {
        super("Too many requests for service '%s'. Details: %s".formatted(service, details));
    }
}
