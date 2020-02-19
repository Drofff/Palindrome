package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;

@Controller
public class BaseController {

	private static final int CARS_AT_HOME_PAGE_MAX_SIZE = 2;

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
		model.addAttribute("authenticated", isAuthenticated());
		putOwnedCarsIntoModelIfDriver(model);
		return "homePage";
	}

	private void putOwnedCarsIntoModelIfDriver(Model model) {
		if(isAuthenticatedDriver()) {
			model.addAttribute("cars", getOwnedByCurrentUserCars());
		}
	}

	private boolean isAuthenticatedDriver() {
		return isAuthenticated() && getCurrentUser().isDriver();
	}

	private List<Car> getOwnedByCurrentUserCars() {
		String currentUserId = getCurrentUser().getId();
		Driver driver = driverService.getDriverByUserId(currentUserId);
		return carService.getCarsOfDriver(driver).stream()
				.limit(CARS_AT_HOME_PAGE_MAX_SIZE)
				.collect(Collectors.toList());
	}

	@GetMapping(ERROR_ENDPOINT)
	public String getErrorPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		return "errorPage";
	}

}
