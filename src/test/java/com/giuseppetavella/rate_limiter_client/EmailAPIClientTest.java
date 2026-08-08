package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.TooManyRequestsException;
import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailAPIClientTest {
    @Test
    void test1() {
        var client = new EmailAPIClient();

        List<CompletableFuture<?>> cfs = new ArrayList<>();
        cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        
        CompletableFuture.allOf(
          cfs.toArray(new CompletableFuture[0])
        ).exceptionally(ex -> {
            assertEquals(TooManyRequestsException.class, ex.getClass());
            return null;
        }).join();

        for(var cf : cfs) {
            System.out.println(cf.join());            
        }
        
        // client.sendEmail("s","s","qq");
        
    }

    @Test
    void test2() {
        var client = new EmailAPIClient();
        client.sendEmail("s","s","qq");

    }
}