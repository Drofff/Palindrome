package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;

@Controller
public class BaseController {

	private final DriverService driverService;
	private final CarService carService;

	@Autowired
	public BaseController(DriverService driverService, CarService carService) {
		this.driverService = driverService;
		this.carService = carService;
	}

	@GetMapping(HOME_ENDPOINT)
	public String getHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                          Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		boolean authenticated = isAuthenticated();
		model.addAttribute("is_authenticated", authenticated);
		if(authenticated) {
			model.addAttribute("cars", getOwnedCarsIfDriver());
		}
		return "homePage";
	}

	private List<Car> getOwnedCarsIfDriver() {
		User currentUser = getCurrentUser();
		if(currentUser.isDriver()) {
			Driver driver = driverService.getDriverByUserId(currentUser.getId());
			return getOwnedCarsOfDriver(driver);
		}
		return new ArrayList<>();
	}

	private List<Car> getOwnedCarsOfDriver(Driver driver) {
		return driver.getOwnedCarIds().stream()
				.map(carService::getById)
				.collect(Collectors.toList());
	}

	@GetMapping(ERROR_ENDPOINT)
	public String getErrorPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		return "errorPage";
	}

}
