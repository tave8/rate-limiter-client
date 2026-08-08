package com.giuseppetavella.rate_limiter_client.clients.email_api;

import java.util.List;

public record EmailAPIPayloadReceivedOnErrorDTO(
        String message,
        String timestamp,
        List<String> errors
) {
}
