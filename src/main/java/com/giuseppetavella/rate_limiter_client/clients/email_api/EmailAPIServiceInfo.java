package com.giuseppetavella.rate_limiter_client.clients.email_api;

import com.giuseppetavella.rate_limiter_client.ServiceInfo;

public class EmailAPIServiceInfo extends ServiceInfo {
    public EmailAPIServiceInfo(String serviceUrl) {
        super(serviceUrl, "email-api");
    }
}
