package com.giuseppetavella.rate_limiter_client;

public record SimulationPayload(int clients, int requests, long period, long timeout) {}
