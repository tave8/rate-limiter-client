package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPIServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class Config {

    @Bean
    public EmailAPIServiceInfo loadEmailAPIServiceInfo() {
        var objectMapper = new ObjectMapper();
        
        // Load service info
        ClassPathResource resource = new ClassPathResource("service_infos/email_api.json");
        EmailAPIServiceInfo serviceInfo;

        try (InputStream inputStream = resource.getInputStream()) {
            serviceInfo = objectMapper.readValue(
                    inputStream,
                    new TypeReference<EmailAPIServiceInfo>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return serviceInfo;
    }

}
