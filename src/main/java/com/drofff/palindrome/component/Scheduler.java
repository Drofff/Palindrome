package com.drofff.palindrome.component;

import com.drofff.palindrome.service.AuthenticationTokenService;
import com.drofff.palindrome.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    private static final int MINUTE_IN_MILLIS = 1000 * 60;
    private static final int DAY_IN_MILLIS = MINUTE_IN_MILLIS * 60 * 24;

    private final AuthenticationTokenService authenticationTokenService;
    private final NotificationService notificationService;

    @Autowired
    public Scheduler(AuthenticationTokenService authenticationTokenService, NotificationService notificationService) {
        this.authenticationTokenService = authenticationTokenService;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void removeExpiredAuthenticationTokens() {
        LOG.info("Removing expired authentication tokens");
        authenticationTokenService.removeExpiredAuthenticationTokens();
    }

    @Scheduled(initialDelay = MINUTE_IN_MILLIS, fixedDelay = DAY_IN_MILLIS)
    public void notifyDriversAboutUnpaidViolations() {
        LOG.info("Sending notifications about unpaid violations to drivers");
        notificationService.notifyDriversAboutUnpaidViolations();
    }

}