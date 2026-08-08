package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Demo {
    static void main(String[] args) {
        var config = new Config();
        var serviceInfo = config.loadEmailAPIServiceInfo();
        var client = new EmailAPIClient(serviceInfo);

        CompletableFuture<ResponseEntity<?>> cf = client.sendEmail("giuseppetavella8@gmail.com", "some subject", "some body");
        
        Function<ResponseEntity<?>, ResponseEntity<?>> cb = (resp) -> {
            System.out.println("processed response: " + resp);
            return resp;
        };
        
        client.sendEvery(10, 1000, cf, cb);
    }
}
