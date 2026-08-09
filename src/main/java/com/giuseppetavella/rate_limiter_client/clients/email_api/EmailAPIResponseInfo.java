package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.ResponseInfo;
import org.springframework.http.ResponseEntity;

public class EmailAPIResponseInfo extends ResponseInfo {
    public EmailAPIResponseInfo(ResponseEntity<?> response, EmailAPIClient client) {
        super(response, client);
    }
}
