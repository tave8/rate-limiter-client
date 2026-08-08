package com.giuseppetavella.rate_limiter_client.clients.email_api;

public record EmailAPIPayloadToSendDTO(
        String recipient,
        String subject,
        String body
) {
}
