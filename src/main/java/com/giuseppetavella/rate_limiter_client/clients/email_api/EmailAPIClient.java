package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.clients.ResponseWrapperDTO;
import com.giuseppetavella.rate_limiter_client.clients.TooManyRequestsException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EmailAPIClient {
    private final String url;
    private final ExecutorService executor;
    
    public EmailAPIClient() {
        this.url = "http://localhost:9000/email-api";
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    public CompletableFuture<ResponseEntity<?>> sendEmail(String recipient, String subject, String body)  {
        var restTemplate = new RestTemplate();
        var payload = new EmailAPIPayloadToSendDTO(recipient, subject, body);

        return CompletableFuture.supplyAsync(() -> {
            var resp = restTemplate.postForEntity(url, payload, String.class);
            
            if(resp.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(429))) {
                throw new TooManyRequestsException("Email API", resp.getBody());
            }
            
            return resp;
        }, executor);
    }
    
}
