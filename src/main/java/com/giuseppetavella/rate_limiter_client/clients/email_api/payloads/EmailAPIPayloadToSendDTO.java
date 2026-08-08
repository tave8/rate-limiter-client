package com.giuseppetavella.rate_limiter_client.clients.email_api.payloads;

public record EmailAPIPayloadToSendDTO(
        String recipient,
        String subject,
        String body
) {
}
