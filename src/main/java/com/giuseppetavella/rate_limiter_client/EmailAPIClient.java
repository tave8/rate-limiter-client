package com.giuseppetavella.rate_limiter_client;

import java.util.concurrent.Future;


public class EmailAPIClient {
    private final String sender;
    
    public EmailAPIClient() {
        this.sender = "sender@example.com";
    }
    
    public String sendEmail(String recipient, String subject, String body) throws Exception {
        
        
        
        return "email successfully sent";
    }
    
}
