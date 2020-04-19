package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.dto.HomeViolationDto;
import com.drofff.palindrome.mapper.HomeViolationDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.drofff.palindrome.cache.OnlineCache.getOnlineCounterValue;
import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.putUserBlockIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectTo;
import static com.drofff.palindrome.utils.ViolationUtils.violationsByDateTimeComparator;

@Controller
public class BaseController {

	private static final int CARS_AT_HOME_PAGE_MAX_SIZE = 2;
	private static final int VIOLATIONS_AT_HOME_PAGE_MAX_SIZE = 4;

	private final DriverService driverService;
	private final CarService carService;
	private final ViolationService violationService;
	private final MappingsResolver mappingsResolver;
	private final UserBlockService userBlockService;
	private final UserService userService;
	private final ChangeRequestService changeRequestService;
	private final HomeViolationDtoMapper homeViolationDtoMapper;

	@Autowired
	public BaseController(DriverService driverService, CarService carService,
	                      ViolationService violationService, MappingsResolver mappingsResolver,
	                      UserBlockService userBlockService, UserService userService,
	                      ChangeRequestService changeRequestService, HomeViolationDtoMapper homeViolationDtoMapper) {
		this.driverService = driverService;
		this.carService = carService;
		this.violationService = violationService;
		this.mappingsResolver = mappingsResolver;
		this.userBlockService = userBlockService;
		this.userService = userService;
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
				.sorted(violationsByDateTimeComparator())
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
			model.addAttribute("users_count", userService.countUsers());
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
