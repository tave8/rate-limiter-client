package com.giuseppetavella.rate_limiter_client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;


public class EmailAPIClient {
    private final String sender;
    private final String url;
    
    public EmailAPIClient() {
        this.sender = "sender@example.com";
        this.url = "http://localhost:9000/email-api";
    }
    
    public void sendEmail(String recipient, String subject, String body)  {
        var restTemplate = new RestTemplate();
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, new Object(), String.class);

        System.out.println(response);
        
        // Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        
    }
    
}
