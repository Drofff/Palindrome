package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.type.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.drofff.palindrome.utils.MailUtils.getCarUpdateMail;

@Service
public class AdminServiceImpl implements AdminService {

	private final DriverService driverService;
	private final UserService userService;
	private final MailService mailService;

	@Autowired
	public AdminServiceImpl(DriverService driverService, UserService userService,
	                        MailService mailService) {
		this.driverService = driverService;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public void notifyCarUpdate(Car car) {
		Mail notificationMail = getCarUpdateMail(car.getNumber());
		Driver driverOwner = driverService.getOwnerOfCar(car);
		User userOwner = userService.getUserById(driverOwner.getUserId());
		mailService.sendMailTo(notificationMail, userOwner.getUsername());
	}

}
