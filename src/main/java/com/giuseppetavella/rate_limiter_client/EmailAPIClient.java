package com.giuseppetavella.rate_limiter_client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class EmailAPIClient {
    private final String url;
    private final ExecutorService executor;
    
    public EmailAPIClient() {
        this.url = "http://localhost:9000/email-api";
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    public CompletableFuture<ResponseEntity<String>> sendEmail(String recipient, String subject, String body)  {
        var restTemplate = new RestTemplate();
        // Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new Object(), String.class);
            return response;
        }, executor);
    }
    
}
