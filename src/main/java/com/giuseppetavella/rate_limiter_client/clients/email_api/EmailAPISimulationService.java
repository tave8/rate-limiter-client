package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.Config;
import com.giuseppetavella.rate_limiter_client.ResponseInfo;
import com.giuseppetavella.rate_limiter_client.SimulationPayload;
import com.giuseppetavella.rate_limiter_client.clients.email_api.payloads.EmailAPIPayloadToSendDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

@Service
public class EmailAPISimulationService {

    private final List<ScheduledExecutorService> schedulers;
    private volatile boolean running;

    public EmailAPISimulationService() {
        this.schedulers = new CopyOnWriteArrayList<>();
        this.running = false;
    }

    public static void main(String[] args) {
        var simulation = new EmailAPISimulationService();

        System.out.println("Starting simulation...");

        // Sends at most 11 requests every 1000ms per client, for a max of 10 seconds
        simulation.start(new SimulationPayload(1, 10, 1000L, 10_000L));

        // No manual sleep/stop needed — the schedulers shut themselves down at timeout.
        // Keep the JVM alive long enough to observe it, or block on your own condition here.
        try {
            Thread.sleep(10_000L + 500L); // small buffer past the timeout
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        System.out.println("Simulation finished.");
    }

    public synchronized void start(SimulationPayload payload) {
        stop(); // Stop any currently active simulation before launching a new one

        if (payload == null || payload.clients() <= 0) {
            throw new IllegalArgumentException("Payload must contain at least 1 client.");
        }
        if (payload.timeout() <= 0) {
            throw new IllegalArgumentException("Payload must contain a positive timeout.");
        }

        var config = new Config();
        var serviceInfo = config.loadEmailAPIServiceInfo();
        List<EmailAPIClient> clients = new ArrayList<>();

        for (int i = 0; i < payload.clients(); i++) {
            var client = new EmailAPIClient(serviceInfo);
            client.setClientName("client-" + i);
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
            System.err.println("[SIMULATION ERROR] " + (ex != null ? ex.getMessage() : "Unknown error"));
            return null;
        };

        double jitterPrc = 0.3;
        this.running = true;

        for (var client : clients) {
            var scheduler = client.sendEmailEvery(
                    payload.requests(),
                    payload.period(),
                    jitterPrc,
                    payload.timeout(),
                    TimeUnit.MILLISECONDS,
                    cb,
                    cbErr,
                    () -> new EmailAPIPayloadToSendDTO(
                            "giuseppetavella8@gmail.com",
                            "some subject",
                            "some body"
                    )
            );
            this.schedulers.add(scheduler);

            // Mark simulation as not running once this scheduler terminates (best-effort,
            // last one to finish wins if clients have staggered timeouts).
            scheduler.schedule(() -> {
                synchronized (this) {
                    boolean anyStillRunning = schedulers.stream().anyMatch(s -> !s.isShutdown());
                    if (!anyStillRunning) {
                        this.running = false;
                    }
                }
            }, payload.timeout() + 50L, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void stop() {
        this.running = false;
        for (var scheduler : schedulers) {
            try {
                scheduler.shutdownNow();
            } catch (Exception ignored) {}
        }
        this.schedulers.clear();
    }

    public boolean isRunning() {
        return running;
    }
}
