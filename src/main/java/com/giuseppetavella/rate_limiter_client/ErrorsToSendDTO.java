package com.giuseppetavella.rate_limiter_client;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public record ErrorsToSendDTO(
        // error message
        String message, 
        
        // when the error occurred
        OffsetDateTime timestamp, 
        
        // more error messages, if you want
        List<String> errors) 
{

    public ErrorsToSendDTO(String message,
                           List<String> errors) {
        this(message, OffsetDateTime.now(), errors);
    }

    public ErrorsToSendDTO(String message,
                           OffsetDateTime timestamp) {
        this(message, timestamp, new ArrayList<>());
    }


    public ErrorsToSendDTO(String message) {
        this(message, OffsetDateTime.now(), new ArrayList<>());
    }

}