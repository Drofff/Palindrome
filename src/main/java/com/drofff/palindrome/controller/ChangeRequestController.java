package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.ParameterConstants.BODY_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.BRANDS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.CAR_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.DRIVER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.ENGINE_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.LICENCE_CATEGORIES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PHOTO_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.REQUESTS_PARAM;
import static com.drofff.palindrome.utils.DateUtils.dateTimeToEpochSeconds;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;
import static java.util.Comparator.comparingInt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.CarDto;
import com.drofff.palindrome.dto.CarFatDto;
import com.drofff.palindrome.dto.UpdateDriverDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.CarDtoMapper;
import com.drofff.palindrome.mapper.CarFatDtoMapper;
import com.drofff.palindrome.mapper.UpdateDriverDtoMapper;
import com.drofff.palindrome.service.BodyTypeService;
import com.drofff.palindrome.service.BrandService;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.ChangeRequestService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.EngineTypeService;
import com.drofff.palindrome.service.LicenceCategoryService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PhotoService;

@Controller
@RequestMapping("/change-request")
public class ChangeRequestController {

	private static final String DRIVER_CHANGE_REQUEST_VIEW = "driverSendChangeRequestPage";
	private static final String CAR_CHANGE_REQUEST_VIEW = "carSendChangeRequestPage";

	private static final String REQUEST_ID_PARAM = "request_id";
	private static final String COMMENT_PARAM = "comment";

	private static final String CHANGE_REQUESTS_ENDPOINT = "/change-request";

	private final ChangeRequestService changeRequestService;
	private final DriverService driverService;
	private final CarService carService;
	private final PhotoService photoService;
	private final BrandService brandService;
	private final BodyTypeService bodyTypeService;
	private final EngineTypeService engineTypeService;
	private final LicenceCategoryService licenceCategoryService;
	private final UpdateDriverDtoMapper updateDriverDtoMapper;
	private final CarDtoMapper carDtoMapper;
	private final CarFatDtoMapper carFatDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public ChangeRequestController(ChangeRequestService changeRequestService, DriverService driverService,
	                               CarService carService, PhotoService photoService,
	                               BrandService brandService, BodyTypeService bodyTypeService,
	                               EngineTypeService engineTypeService, LicenceCategoryService licenceCategoryService,
	                               UpdateDriverDtoMapper updateDriverDtoMapper, CarDtoMapper carDtoMapper,
	                               CarFatDtoMapper carFatDtoMapper, MappingsResolver mappingsResolver) {
		this.changeRequestService = changeRequestService;
		this.driverService = driverService;
		this.carService = carService;
		this.photoService = photoService;
		this.brandService = brandService;
		this.bodyTypeService = bodyTypeService;
		this.engineTypeService = engineTypeService;
		this.licenceCategoryService = licenceCategoryService;
		this.updateDriverDtoMapper = updateDriverDtoMapper;
		this.carDtoMapper = carDtoMapper;
		this.carFatDtoMapper = carFatDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public String getChangeRequests(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
		List<ChangeRequest> changeRequestList = getAllChangeRequestsSortByDateTime();
		model.addAttribute(REQUESTS_PARAM, changeRequestList);
		model.addAttribute(MESSAGE_PARAM, message);
		return "changeRequestsPage";
	}

	private List<ChangeRequest> getAllChangeRequestsSortByDateTime() {
		return changeRequestService.getAllChangeRequests().stream()
				.sorted(comparingInt(request -> dateTimeToEpochSeconds(request.getDateTime())))
				.collect(Collectors.toList());
	}

	@GetMapping("/driver/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String getDriverChangeRequestPage(@PathVariable String id, Model model) {
		ChangeRequest changeRequest = changeRequestService.getDriverChangeRequestById(id);
		Driver changedDriver = (Driver) changeRequest.getTargetValue();
		model.addAttribute("changed_driver", changedDriver);
		Driver originalDriver = driverService.getDriverById(changedDriver.getId());
		model.addAttribute(DRIVER_PARAM, originalDriver);
		model.addAttribute(REQUEST_ID_PARAM, id);
		return "driverChangeRequestPage";
	}

	@GetMapping("/car/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String getCarChangeRequestPage(@PathVariable String id, Model model) {
		ChangeRequest changeRequest = changeRequestService.getCarChangeRequestById(id);
		Car changedCar = (Car) changeRequest.getTargetValue();
		model.addAttribute("changed_car", toCarFatDto(changedCar));
		Car originalCar = carService.getCarById(changedCar.getId());
		model.addAttribute(CAR_PARAM, toCarFatDto(originalCar));
		model.addAttribute(REQUEST_ID_PARAM, id);
		return "carChangeRequestPage";
	}

	private CarFatDto toCarFatDto(Car car) {
		CarFatDto carFatDto = carFatDtoMapper.toDto(car);
		return mappingsResolver.resolveMappings(car, carFatDto);
	}

	@PostMapping("/approve")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String approveChangeRequestById(String id) {
		try {
			changeRequestService.approveChangeById(id);
			return redirectToWithMessage(CHANGE_REQUESTS_ENDPOINT, "Change request has been approved successfully");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@PostMapping("/refuse")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String refuseChangeRequestById(String id) {
		try {
			changeRequestService.refuseChangeById(id);
			return redirectToWithMessage(CHANGE_REQUESTS_ENDPOINT, "Change request has been refused successfully");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@GetMapping("/send/driver/{id}")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getSendDriverChangeRequestPage(@PathVariable String id, Model model) {
		Driver driver = driverService.getDriverById(id);
		model.addAttribute(DRIVER_PARAM, driver);
		String photo = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, photo);
		return DRIVER_CHANGE_REQUEST_VIEW;
	}

	@PostMapping("/send/driver")
	@PreAuthorize("hasAuthority('POLICE')")
	public String sendDriverChangeRequest(UpdateDriverDto updateDriverDto, @RequestParam(required = false) String comment,
	                                      Model model) {
		Driver driver = updateDriverDtoMapper.toEntity(updateDriverDto);
		try {
			changeRequestService.requestDriverInfoChangeWithComment(driver, comment);
			model.addAttribute(MESSAGE_PARAM, "Change request has been sent successfully");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
		}
		model.addAttribute(DRIVER_PARAM, driver);
		Driver originalDriver = driverService.getDriverById(driver.getId());
		String photo = photoService.loadEncodedPhotoByUri(originalDriver.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, photo);
		model.addAttribute(COMMENT_PARAM, comment);
		return DRIVER_CHANGE_REQUEST_VIEW;
	}

	@GetMapping("/send/car/{id}")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getSendCarChangeRequestPage(@PathVariable String id, Model model) {
		Car car = carService.getCarById(id);
		model.addAttribute(CAR_PARAM, car);
		putCarPropertiesIntoModel(model);
		return CAR_CHANGE_REQUEST_VIEW;
	}

	@PostMapping("/send/car/{id}")
	@PreAuthorize("hasAuthority('POLICE')")
	public String sendChangeRequest(@PathVariable String id, CarDto carDto, @RequestParam(required = false) String comment,
	                                Model model) {
		Car car = carDtoMapper.toEntity(carDto);
		car.setId(id);
		try {
			changeRequestService.requestCarInfoChangeWithComment(car, comment);
			model.addAttribute(MESSAGE_PARAM, "Change request has been sent successfully");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
		}
		model.addAttribute(CAR_PARAM, car);
		model.addAttribute(COMMENT_PARAM, comment);
		putCarPropertiesIntoModel(model);
		return CAR_CHANGE_REQUEST_VIEW;
	}

	private void putCarPropertiesIntoModel(Model model) {
		model.addAttribute(BRANDS_PARAM, brandService.getAll());
		model.addAttribute(BODY_TYPES_PARAM, bodyTypeService.getAll());
		model.addAttribute(ENGINE_TYPES_PARAM, engineTypeService.getAll());
		model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategoryService.getAll());
	}

}
