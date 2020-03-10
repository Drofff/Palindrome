package com.drofff.palindrome.controller;

import static com.drofff.palindrome.cache.OnlineCache.getOnlineCounterValue;
import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.USER_IS_BLOCKED_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.CARS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.REQUESTS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.VIOLATIONS_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.putUserBlockIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserBlock;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.HomeViolationDto;
import com.drofff.palindrome.mapper.HomeViolationDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.ChangeRequestService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.UserBlockService;
import com.drofff.palindrome.service.ViolationService;

@Controller
public class BaseController {

	private static final int CARS_AT_HOME_PAGE_MAX_SIZE = 2;
	private static final int VIOLATIONS_AT_HOME_PAGE_MAX_SIZE = 4;

	private final DriverService driverService;
	private final CarService carService;
	private final ViolationService violationService;
	private final MappingsResolver mappingsResolver;
	private final UserBlockService userBlockService;
	private final AuthenticationService authenticationService;
	private final ChangeRequestService changeRequestService;
	private final HomeViolationDtoMapper homeViolationDtoMapper;

	@Autowired
	public BaseController(DriverService driverService, CarService carService,
	                      ViolationService violationService, MappingsResolver mappingsResolver,
	                      UserBlockService userBlockService, AuthenticationService authenticationService,
	                      ChangeRequestService changeRequestService, HomeViolationDtoMapper homeViolationDtoMapper) {
		this.driverService = driverService;
		this.carService = carService;
		this.violationService = violationService;
		this.mappingsResolver = mappingsResolver;
		this.userBlockService = userBlockService;
		this.authenticationService = authenticationService;
		this.changeRequestService = changeRequestService;
		this.homeViolationDtoMapper = homeViolationDtoMapper;
	}

	@GetMapping(HOME_ENDPOINT)
	public String getHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                          Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		putCurrentUserIntoModelIfAuthenticated(model);
		putHomeParamsIntoModelIfDriver(model);
		putAdminParamsIntoModelIfNeeded(model);
		return "homePage";
	}

	private void putCurrentUserIntoModelIfAuthenticated(Model model) {
		if(isAuthenticated()) {
			model.addAttribute(USER_PARAM, getCurrentUser());
		}
	}

	private void putHomeParamsIntoModelIfDriver(Model model) {
		if(isAuthenticatedDriver()) {
			putHomeParamsIntoModel(model);
		}
	}

	private boolean isAuthenticatedDriver() {
		return isAuthenticated() && getCurrentUser().isDriver();
	}

	private void putHomeParamsIntoModel(Model model) {
		Driver driver = driverService.getCurrentDriver();
		List<Car> cars = getDriverOwnedCars(driver);
		model.addAttribute(CARS_PARAM, cars);
		List<Violation> violations = getDriverViolations(driver);
		List<HomeViolationDto> homeViolationDtos = applyToEachListElement(this::toHomeViolationDto, violations);
		model.addAttribute(VIOLATIONS_PARAM, homeViolationDtos);
	}

	private List<Car> getDriverOwnedCars(Driver driver) {
		return carService.getCarsOfDriver(driver).stream()
				.limit(CARS_AT_HOME_PAGE_MAX_SIZE)
				.collect(Collectors.toList());
	}

	private List<Violation> getDriverViolations(Driver driver) {
		return violationService.getDriverViolations(driver).stream()
				.limit(VIOLATIONS_AT_HOME_PAGE_MAX_SIZE)
				.collect(Collectors.toList());
	}

	private HomeViolationDto toHomeViolationDto(Violation violation) {
		HomeViolationDto homeViolationDto = homeViolationDtoMapper.toDto(violation);
		return mappingsResolver.resolveMappings(violation, homeViolationDto);
	}

	private void putAdminParamsIntoModelIfNeeded(Model model) {
		if(isAuthenticatedAdmin()) {
			model.addAttribute("online_counter", getOnlineCounterValue());
			model.addAttribute("users_count", authenticationService.countUsers());
			model.addAttribute("blocked_users_count", userBlockService.countBlockedUsers());
			model.addAttribute(REQUESTS_PARAM, changeRequestService.countChangeRequests());
		}
	}

	private boolean isAuthenticatedAdmin() {
		return isAuthenticated() && getCurrentUser().isAdmin();
	}

	@GetMapping(USER_IS_BLOCKED_ENDPOINT)
	public String getUserIsBlockedPage(Model model) {
		User currentUser = getCurrentUser();
		Optional<UserBlock> userBlockOptional = userBlockService.getUserBlockForUserIfPresent(currentUser);
		if(userBlockOptional.isPresent()) {
			putUserBlockIntoModel(userBlockOptional.get(), model);
			return "userIsBlockedPage";
		}
		return redirectTo(HOME_ENDPOINT);
	}

	@GetMapping(ERROR_ENDPOINT)
	public String getErrorPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           Model model) {
		if(isAuthenticated()) {
			model.addAttribute(USER_PARAM, getCurrentUser());
		}
		model.addAttribute(MESSAGE_PARAM, message);
		return "errorPage";
	}

}
