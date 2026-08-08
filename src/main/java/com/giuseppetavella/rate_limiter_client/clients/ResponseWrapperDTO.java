package com.giuseppetavella.rate_limiter_client.clients;

import org.springframework.http.ResponseEntity;

public record ResponseWrapperDTO<T>(
        ResponseEntity<?> response,
        T payload
) {
}
