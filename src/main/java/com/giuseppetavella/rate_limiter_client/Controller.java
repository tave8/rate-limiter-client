package com.giuseppetavella.rate_limiter_client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {

    @GetMapping
    public String serverOk() {
        return "rater limiter: client. up and running.";
    }
    
}
