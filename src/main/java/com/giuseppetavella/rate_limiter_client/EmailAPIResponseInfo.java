package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import org.springframework.http.ResponseEntity;

public class EmailAPIResponseInfo {
    private final ResponseEntity<?> response;
    private final EmailAPIClient client;
    
    public EmailAPIResponseInfo(ResponseEntity<?> response, 
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
}
