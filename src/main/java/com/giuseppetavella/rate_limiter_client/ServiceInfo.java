package com.giuseppetavella.rate_limiter_client;

public class ServiceInfo {
    private final String serviceUrl;
    private final String serviceName;

    public ServiceInfo(String serviceUrl, String serviceName) {
        this.serviceUrl = serviceUrl;
        this.serviceName = serviceName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceUrl='" + serviceUrl + '\'' +
                '}';
    }
}
