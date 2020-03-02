package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.MailUtils.getCarUpdateMail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.type.Mail;

@Service
public class AdminServiceImpl implements AdminService {

	private final DriverService driverService;
	private final AuthenticationService authenticationService;
	private final MailService mailService;

	@Autowired
	public AdminServiceImpl(DriverService driverService, AuthenticationService authenticationService,
	                        MailService mailService) {
		this.driverService = driverService;
		this.authenticationService = authenticationService;
		this.mailService = mailService;
	}

	@Override
	public void notifyCarUpdate(Car car) {
		Mail notificationMail = getCarUpdateMail(car.getNumber());
		Driver driverOwner = driverService.getOwnerOfCar(car);
		User userOwner = authenticationService.getUserById(driverOwner.getUserId());
		mailService.sendMailTo(notificationMail, userOwner.getUsername());
	}

}
