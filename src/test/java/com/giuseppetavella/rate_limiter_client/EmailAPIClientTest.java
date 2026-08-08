package com.giuseppetavella.rate_limiter_client;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class EmailAPIClientTest {
    @Test
    void test1() {
        var client = new EmailAPIClient();

        List<CompletableFuture<?>> cfs = new ArrayList<>();
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        cfs.add(client.sendEmail("s","s","qq"));
        
        CompletableFuture.allOf(
          cfs.toArray(new CompletableFuture[0])
        ).join();

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