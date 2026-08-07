package com.giuseppetavella.rate_limiter_client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailAPIClientTest {
    @Test
    void test1() {
        var client = new EmailAPIClient();
        client.sendEmail("s","s","qq");
        client.sendEmail("s","s","qq");
        client.sendEmail("s","s","qq");
        client.sendEmail("s","s","qq");
        client.sendEmail("s","s","qq");
        // client.sendEmail("s","s","qq");
        
    }

    @Test
    void test2() {
        var client = new EmailAPIClient();
        client.sendEmail("s","s","qq");

    }
}