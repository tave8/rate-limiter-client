package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.JitterUtils;
import com.giuseppetavella.rate_limiter_client.ResponseInfo;
import com.giuseppetavella.rate_limiter_client.ServiceInfo;
import com.giuseppetavella.rate_limiter_client.TooManyRequestsException;
import com.giuseppetavella.rate_limiter_client.clients.email_api.payloads.EmailAPIPayloadToSendDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class EmailAPIClient {
    private String clientName;
    private final ExecutorService executor;
    private final ServiceInfo serviceInfo;
    
    public EmailAPIClient(EmailAPIServiceInfo serviceInfo) { // Dependency injected
        this.serviceInfo = serviceInfo;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    

    public CompletableFuture<ResponseEntity<?>> sendEmail(EmailAPIPayloadToSendDTO payload)  {
        var restTemplate = new RestTemplate();

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
    public ScheduledExecutorService sendEmailEvery(int howMany, long period, double jitterPrc,
                                                   long timeout, TimeUnit timeoutUnit,
                                                   Function<ResponseInfo, ResponseInfo> cb,
                                                   Function<Throwable, ResponseInfo> cbErr,
                                                   Supplier<EmailAPIPayloadToSendDTO> payloadSupplier)
    {
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        long baseDelayMs = Math.max(1, period / howMany);

        Runnable task = new Runnable() {
            private int sentInCurrentWindow = 0;
            private long windowStartMs = System.currentTimeMillis();
            @Override
            public void run() {
                if (scheduler.isShutdown()) {
                    return;
                }
                try {
                    long now = System.currentTimeMillis();
                    if (now - windowStartMs >= period) {
                        sentInCurrentWindow = 0;
                        windowStartMs = now;
                    }
                    if (sentInCurrentWindow < howMany) {
                        sentInCurrentWindow++;
                        try {
                            sendEmail(payloadSupplier.get())
                                    .thenApply(resp -> new EmailAPIResponseInfo(resp, EmailAPIClient.this))
                                    .thenApply(cb)
                                    .exceptionally(cbErr);
                        } catch (Throwable t) {
                            cbErr.apply(t);
                        }
                    }
                } catch (Throwable t) {
                    cbErr.apply(t);
                } finally {
                    if (!scheduler.isShutdown()) {
                        double factor = ThreadLocalRandom.current().nextDouble(-jitterPrc, jitterPrc);
                        long nextDelayMs = Math.max(1, Math.round(baseDelayMs + (baseDelayMs * factor)));
                        scheduler.schedule(this, nextDelayMs, TimeUnit.MILLISECONDS);
                    }
                }
            }
        };

        scheduler.submit(task);

        // Shut everything down once the timeout elapses
        scheduler.schedule(scheduler::shutdown, timeout, timeoutUnit);

        return scheduler;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }
    
}
