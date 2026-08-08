package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.EmailAPIResponseInfo;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Service
public class EmailAPIClient {
    private String clientName;
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
     * @param cb callback that handles the async result
     * @param <T>
     */
    public <T> void sendEmailEvery(int howMany, 
                              long period,
                              Function<EmailAPIResponseInfo, EmailAPIResponseInfo> cb,
                              Function<Throwable, EmailAPIResponseInfo> cbErr,
                               String recipient,
                               String subject, 
                               String body) 
    {
        var scheduler = Executors.newSingleThreadScheduledExecutor(); 
            scheduler.scheduleAtFixedRate(
                    () -> {
                        sendEmail(recipient, subject, body)
                                .thenApply(resp -> new EmailAPIResponseInfo(resp, this))
                                .thenApply(cb)
                                .exceptionally(cbErr);
                    }, 
                    0, 
                    period / howMany, 
                    TimeUnit.MILLISECONDS
            );
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }
    
}
