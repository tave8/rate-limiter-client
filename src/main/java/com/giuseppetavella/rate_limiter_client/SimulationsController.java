package com.giuseppetavella.rate_limiter_client;

import com.giuseppetavella.rate_limiter_client.clients.email_api.EmailAPISimulationService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulations")
public class SimulationsController {

    private final EmailAPISimulationService emailAPISimulationService;
    
    public SimulationsController(EmailAPISimulationService emailAPISimulationService) { // Dependency injected
        this.emailAPISimulationService = emailAPISimulationService;
    }
    
    @PostMapping("/{service}/{command}")
    public String runSimulation(@PathVariable String service,
                                @PathVariable String command,
                                @RequestBody @Validated SimulationPayload payload,
                                BindingResult validation
    )
    {
        
        if(validation.hasErrors()) {
            throw new RuntimeException("payload is invalid. details: " + validation.getAllErrors());
        }
        
        if(service.equals("email-api")) {
            // Hardcoded for now - FIX
            if(command.equals("start")) {
                emailAPISimulationService.start(new SimulationPayload(payload.clients(), payload.requests(), payload.period()));
            } else if (command.equals("stop")) {
                
                emailAPISimulationService.stop();
                return "simulation stopped";
                
            } else {
                throw new RuntimeException("command %s not recognized.".formatted(command));
            }

        } else {
            throw new RuntimeException("service %s not registered".formatted(service));
        }
        return "new simulation started (and eventually stopped previous simulation)";
    }

}
