package com.giuseppetavella.rate_limiter_client;

public record SimulationPayload(
        // @NotNull ADD VALIDATION DEPENDENCY IN SPRING, SO we can validate paylaod
        Integer clients,
        Integer requests,
        Long period
) {
}
