package com.drofff.palindrome.component;

import com.drofff.palindrome.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public Scheduler(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void removeExpiredAuthenticationTokens() {
        authenticationTokenService.removeExpiredAuthenticationTokens();
    }

}
