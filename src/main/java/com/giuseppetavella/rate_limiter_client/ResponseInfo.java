package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import org.springframework.http.ResponseEntity;

public class ResponseInfo {
    private final ResponseEntity<?> response;
    private final EmailAPIClient client;

    public ResponseInfo(ResponseEntity<?> response,
                                EmailAPIClient client)
    {
        this.response = response;
        this.client = client;
    }

    public EmailAPIClient getClient() {
        return client;
    }

    public ResponseEntity<?> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "client=" + client +
                ", response=" + response +
                '}';
    }
}
