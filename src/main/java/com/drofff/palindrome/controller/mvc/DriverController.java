package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.DriverDto;
import com.drofff.palindrome.dto.HomeViolationDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.DriverPattern;
import com.drofff.palindrome.mapper.DriverDtoMapper;
import com.drofff.palindrome.mapper.HomeViolationDtoMapper;
import com.drofff.palindrome.service.*;
import com.drofff.palindrome.type.ViolationsStatistic;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.RELATIVE_HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;
import static com.drofff.palindrome.utils.ViolationUtils.invertedViolationsDateTimeComparator;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/driver")
public class DriverController {

	private static final String DRIVER_CREATED_MESSAGE = "You have successfully created your driver profile! Welcome!";

	private static final String CREATE_DRIVER_VIEW = "createDriverPage";
	private static final String UPDATE_DRIVER_VIEW = "updateDriverPage";
	private static final String UPDATE_DRIVER_PHOTO_VIEW = "updateDriverPhotoPage";

	private static final int CARS_SHORT_LIST_SIZE = 2;
	private static final int VIOLATIONS_SHORT_LIST_SIZE = 4;

	private final DriverService driverService;
	private final PhotoService photoService;
	private final CarService carService;
	private final ViolationService violationService;
	private final DriverDtoMapper driverDtoMapper;
	private final HomeViolationDtoMapper homeViolationDtoMapper;
	private final MappingsResolver mappingsResolver;

	public DriverController(DriverService driverService, PhotoService photoService,
							CarService carService, ViolationService violationService,
							DriverDtoMapper driverDtoMapper, HomeViolationDtoMapper homeViolationDtoMapper,
							MappingsResolver mappingsResolver) {
		this.driverService = driverService;
		this.photoService = photoService;
		this.carService = carService;
		this.violationService = violationService;
		this.driverDtoMapper = driverDtoMapper;
		this.homeViolationDtoMapper = homeViolationDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getDriverProfilePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		Driver driver = driverService.getCurrentDriver();
		model.addAttribute(DRIVER_PARAM, driver);
		String photo = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, photo);
		String email = getCurrentUser().getUsername();
		model.addAttribute(EMAIL_PARAM, email);
		return "driverPage";
	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('POLICE')")
	@ResponseBody
	public List<Driver> getAllDrivers() {
		return driverService.getAllDrivers();
	}

	@GetMapping("/search")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getDriverSearchPage(@RequestParam(required = false) String name, DriverPattern driverPattern, Model model) {
		List<Driver> drivers = nonNull(name) ? driverService.getDriversByName(name) : driverService.getAllDrivers();
		List<Driver> filteredDrivers = grepByPattern(drivers, driverPattern);
		loadPhotosOfDrivers(filteredDrivers);
		model.addAttribute(DRIVERS_PARAM, filteredDrivers);
		model.addAttribute(PATTERN_PARAM, driverPattern);
		model.addAttribute("name", name);
		return "driverSearchPage";
	}

	private void loadPhotosOfDrivers(List<Driver> drivers) {
		drivers.forEach(driver -> {
			String encodedPhoto = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
			driver.setPhotoUri(encodedPhoto);
		});
	}

	@GetMapping("/{id}/statistic")
	public String getStatisticOfDriverWithId(@PathVariable String id, Model model) {
		Driver driver = driverService.getDriverById(id);
		model.addAttribute(DRIVER_PARAM, driver);
		ViolationsStatistic violationsStatistic = violationService.getViolationsStatisticForDriver(driver);
		model.addAttribute("statistic", violationsStatistic);
		return "driverStatisticPage";
	}

	@GetMapping(RELATIVE_HOME_ENDPOINT)
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getDriverHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
									Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		Driver driver = driverService.getCurrentDriver();
		List<Car> carShortList = getShortenOwnedCarsListForDriver(driver);
		model.addAttribute(CARS_PARAM, carShortList);
		List<HomeViolationDto> violationShortList = getShortenViolationsListForDriver(driver);
		model.addAttribute(VIOLATIONS_PARAM, violationShortList);
		return "driverHomePage";
	}

	private List<Car> getShortenOwnedCarsListForDriver(Driver driver) {
		return carService.getCarsOfDriver(driver).stream()
				.limit(CARS_SHORT_LIST_SIZE)
				.collect(toList());
	}

	private List<HomeViolationDto> getShortenViolationsListForDriver(Driver driver) {
		return violationService.getDriverViolations(driver).stream()
				.sorted(invertedViolationsDateTimeComparator())
				.limit(VIOLATIONS_SHORT_LIST_SIZE)
				.map(this::toHomeViolationDto)
				.collect(toList());
	}

	private HomeViolationDto toHomeViolationDto(Violation violation) {
		HomeViolationDto homeViolationDto = homeViolationDtoMapper.toDto(violation);
		return mappingsResolver.resolveMappings(violation, homeViolationDto);
	}

	@GetMapping("/create")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getCreateDriverProfilePage() {
		return CREATE_DRIVER_VIEW;
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String createDriver(DriverDto driverDto, MultipartFile photo, Model model) {
		Driver driver = driverDtoMapper.toEntity(driverDto);
		try {
			driverService.createDriverProfileWithPhoto(driver, photo);
			return redirectToWithMessage(HOME_ENDPOINT, DRIVER_CREATED_MESSAGE);
		} catch(ValidationException e) {
			model.addAttribute(DRIVER_PARAM, driver);
			putValidationExceptionIntoModel(e, model);
			return CREATE_DRIVER_VIEW;
		}
	}

	@GetMapping("/update")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getUpdateDriverProfilePage(Model model) {
		Driver driverProfile = driverService.getCurrentDriver();
		model.addAttribute(DRIVER_PARAM, driverProfile);
		String encodedPhoto = photoService.loadEncodedPhotoByUri(driverProfile.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, encodedPhoto);
		return UPDATE_DRIVER_VIEW;
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String updateDriver(DriverDto driverDto, Model model) {
		Driver driver = driverDtoMapper.toEntity(driverDto);
		try {
			driverService.updateDriverProfile(driver);
			return redirectToWithMessage("/driver", "Profile data has been successfully updated");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(DRIVER_PARAM, driver);
			model.addAttribute(PHOTO_PARAM, getCurrentDriverPhoto());
			return UPDATE_DRIVER_VIEW;
		}
	}

	@GetMapping("/update/photo")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getUpdateDriverPhotoPage() {
		return UPDATE_DRIVER_PHOTO_VIEW;
	}

	@PostMapping("/update/photo")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String updateDriverPhoto(MultipartFile photo, Model model) {
		try {
			driverService.updateDriverPhoto(photo);
			model.addAttribute(SUCCESS_PARAM, Boolean.TRUE);
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
		}
		return UPDATE_DRIVER_PHOTO_VIEW;
	}

	private String getCurrentDriverPhoto() {
		Driver driver = driverService.getCurrentDriver();
		return photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
	}

}
