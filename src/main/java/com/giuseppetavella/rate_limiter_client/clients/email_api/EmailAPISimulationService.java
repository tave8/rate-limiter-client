package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.Config;
import com.giuseppetavella.rate_limiter_client.ResponseInfo;
import com.giuseppetavella.rate_limiter_client.SimulationPayload;
import com.giuseppetavella.rate_limiter_client.clients.email_api.payloads.EmailAPIPayloadToSendDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

@Service
public class EmailAPISimulationService {
    
    private final List<ScheduledExecutorService> schedulers;
    private boolean running;
    
    public EmailAPISimulationService() {
        this.schedulers = new ArrayList<>();
        this.running = false;
    }

    static void main(String[] args) {
        
        // var simulation = new EmailAPISimulationService();
        // simulation.start(new SimulationPayload(1, 3, 1000L));
        //
        // try {
        //     Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        //
        // System.out.println("shutting down simulation...");
        // simulation.stop();

    }
    
    public void start(SimulationPayload payload) {
        stop(); // Stop the current simulation before running a new one
        
        var config = new Config();
        var serviceInfo = config.loadEmailAPIServiceInfo();
        List<EmailAPIClient> clients = new ArrayList<>();

        for (int i = 0; i < payload.clients(); i++) {
            var client = new EmailAPIClient(serviceInfo);
            client.setClientName("client-"+i);
            clients.add(client);
        }
        
        Function<ResponseInfo, ResponseInfo> cb = (respInfo) -> {
            System.out.println("[%s] status: %s, body: %s".formatted(
                    respInfo.getClient().getClientName(), 
                    respInfo.getResponse().getStatusCode(),
                    respInfo.getResponse().getBody())
            );
            return respInfo;
        };
        
        Function<Throwable, ResponseInfo> cbErr = (ex) -> {
            System.out.println(ex.getMessage());
            return null;
        };
        
        for(var client : clients) {
            var scheduler = client.sendEmailEvery(payload.requests(), payload.period(), cb, cbErr, () -> {
                return new EmailAPIPayloadToSendDTO(
                        "giuseppetavella8@gmail.com",
                        "some subject",
                        "some body"
                );
            });
            schedulers.add(scheduler);
        }
        
        this.running = true;

    }
    
    
    public void stop() {
        for(var scheduler : schedulers) {
            scheduler.shutdownNow();
        }
        this.running = false;
    }
    
}
