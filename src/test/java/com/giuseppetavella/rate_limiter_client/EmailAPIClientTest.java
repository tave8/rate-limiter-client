package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailAPIClientTest {
    @Test
    void test1() {
        // var client = new EmailAPIClient();
        //
        // List<CompletableFuture<?>> cfs = new ArrayList<>();
        // cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        // cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        // cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        // cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        // cfs.add(client.sendEmail("giuseppetavella8@gmail.com","a subject","writing you something"));
        //
        // CompletableFuture.allOf(
        //   cfs.toArray(new CompletableFuture[0])
        // ).join();
        //
        // for(var cf : cfs) {
        //     System.out.println(cf.join());            
        // }
        
        // client.sendEmail("s","s","qq");
        
    }

    @Test
    void test2() throws IOException {
        var config = new Config();
        var serviceInfo = config.loadEmailAPIServiceInfo();
        var client = new EmailAPIClient(serviceInfo);

        var resp = client.sendEmail("giuseppetavella8@gmail.com", "some subject", "some body").join();

        System.out.println(resp.getBody());
        // client.sendEmail("s","s","qq");

    }
}