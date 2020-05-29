package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.type.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.drofff.palindrome.utils.MailUtils.getUnpaidViolationsNotificationMail;
import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

@Service
public class EmailNotificationService implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationService.class);

    private static final long NOTIFICATIONS_INTERVAL_SECONDS = 60L * 60L * 24L * 4L; // 4 days

    private final DriverService driverService;
    private final ViolationService violationService;
    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public EmailNotificationService(DriverService driverService, ViolationService violationService,
                                    UserService userService, MailService mailService) {
        this.driverService = driverService;
        this.violationService = violationService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public void notifyDriversAboutUnpaidViolations() {
        driverService.getAllDrivers().stream()
                .filter(driver -> mayBeNotified(driver) && hasUnpaidViolations(driver))
                .forEach(this::notifyDriverAboutUnpaidViolations);
    }

    private boolean mayBeNotified(Driver driver) {
        LocalDateTime lastNotificationDateTime = driver.getLastNotificationDateTime();
        return lastNotificationDateTime == null || notificationIntervalPastFrom(lastNotificationDateTime);
    }

    private boolean notificationIntervalPastFrom(LocalDateTime from) {
        long fromSeconds = from.toEpochSecond(UTC);
        long nowSeconds = now().toEpochSecond(UTC);
        long intervalFrom = nowSeconds - fromSeconds;
        return intervalFrom >= NOTIFICATIONS_INTERVAL_SECONDS;
    }

    private boolean hasUnpaidViolations(Driver driver) {
        return violationService.getDriverViolations(driver).stream()
                .anyMatch(Violation::isUnpaid);
    }

    private void notifyDriverAboutUnpaidViolations(Driver driver) {
        long unpaidViolationsCount = violationService.countUnpaidViolationsOfDriver(driver);
        LOG.info("Notifying driver with id={} about {} unpaid violations", driver.getId(), unpaidViolationsCount);
        sendUnpaidViolationsOfCountNotificationMailToDriver(unpaidViolationsCount, driver);
        driverService.updateDriverLastNotificationDateTime(driver);
    }

    private void sendUnpaidViolationsOfCountNotificationMailToDriver(long unpaidViolationsCount, Driver driver) {
        Mail unpaidViolationsNotificationMail = getUnpaidViolationsNotificationMail(driver.getFirstName(), unpaidViolationsCount);
        User user = userService.getUserById(driver.getUserId());
        mailService.sendMailTo(unpaidViolationsNotificationMail, user.getUsername());
    }

}