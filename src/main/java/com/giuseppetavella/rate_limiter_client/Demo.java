package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIClient;
import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIResponseInfo;
import com.giuseppetavella.rate_limiter_client.clients.email_api.payloads.EmailAPIPayloadToSendDTO;
import tools.jackson.databind.deser.jdk.UUIDDeserializer;

import java.util.UUID;
import java.util.function.Function;

public class Demo {
    static void main(String[] args) {
        var config = new Config();
        var serviceInfo = config.loadEmailAPIServiceInfo();
        var client1 = new EmailAPIClient(serviceInfo);
        var client2 = new EmailAPIClient(serviceInfo);

        client1.setClientName("client-1");
        client2.setClientName("client-2");
        
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

        client1.sendEmailEvery(100, 1000, cb, cbErr, () -> {
            return new EmailAPIPayloadToSendDTO(
                    "giuseppetavella8@gmail.com",
                    "some subject",
                    "some body"
            );
        });
        // client1.sendEmailEvery(5, 1000, cb, cbErr, "giuseppetavella8@gmail.com", "some subject", "some body");
        // client1.sendEmailEvery(5, 1000, cb, cbErr, "giuseppetavella8@gmail.com", "some subject", "some body");
        // client2.sendEmailEvery(2, 1000, cb, cbErr, "giuseppetavella8@gmail.com", "some subject", "some body");
    }
}
