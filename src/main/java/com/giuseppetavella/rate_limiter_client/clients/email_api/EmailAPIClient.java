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
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

    /**
     * 
     * 
     * @param howMany how many times to call the given task
     * @param period in milliseconds
     * @param cf 
     * @param cb callback that handles the async result
     * @param <T>
     */
    public <T> void sendEvery(int howMany, 
                              long period, 
                              CompletableFuture<T> cf,
                              Function<T, T> cb) 
    {
        var scheduler = Executors.newSingleThreadScheduledExecutor(); 
            scheduler.scheduleAtFixedRate(
                    () -> {
                        // Register callback without blocking
                        cf.thenApply(cb).exceptionally(e -> {
                            System.out.println(e);
                            return null;
                        }).join();
                    }, 
                    0, 
                    period / howMany, 
                    TimeUnit.MILLISECONDS
            );
    }
    
}
