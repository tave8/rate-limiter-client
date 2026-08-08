package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.ServiceInfo;
import com.giuseppetavella.rate_limiter_client.TooManyRequestsException;
import com.giuseppetavella.rate_limiter_client.clients.email_api.payloads.EmailAPIPayloadToSendDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailAPIClient {
    private final ExecutorService executor;
    private final ServiceInfo serviceInfo;
    
    public EmailAPIClient(EmailAPIServiceInfo serviceInfo) { // Dependency injected
        this.serviceInfo = serviceInfo;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    public CompletableFuture<ResponseEntity<?>> sendEmail(String recipient, String subject, String body)  {
        var restTemplate = new RestTemplate();
        var payload = new EmailAPIPayloadToSendDTO(recipient, subject, body);

        return CompletableFuture.supplyAsync(() -> {
            var resp = restTemplate.postForEntity(serviceInfo.getServiceUrl(), payload, String.class);
            
            if(resp.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(429))) {
                throw new TooManyRequestsException(serviceInfo.getServiceName(), resp.getBody());
            }
            
            return resp;
        }, executor);
    }
    
}
