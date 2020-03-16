package com.drofff.palindrome.component;

import com.drofff.palindrome.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    private final AuthorizationService authorizationService;

    @Autowired
    public Scheduler(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void removeExpiredAuthorizationTokens() {
        LOG.info("Removing expired authorization tokens");
        authorizationService.removeAllExpiredAuthorizationTokens();
    }

}
