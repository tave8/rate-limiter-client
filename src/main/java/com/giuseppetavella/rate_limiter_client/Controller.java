package com.giuseppetavella.rate_limiter_client;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Controller {
    
    
    @GetMapping
    public String serverOk() {
        return "rater limiter: client. up and running.";
    }
    
}
