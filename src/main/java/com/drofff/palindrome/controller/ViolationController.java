package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.CARS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.FILER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.VIOLATIONS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.VIOLATION_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FilterUtils.filter;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.putPageIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;
import static com.drofff.palindrome.utils.ViolationUtils.violationsByDateTimeComparator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.CarFatDto;
import com.drofff.palindrome.dto.ViolationDto;
import com.drofff.palindrome.dto.ViolationFatDto;
import com.drofff.palindrome.dto.ViolationsViolationDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.filter.ViolationFilter;
import com.drofff.palindrome.mapper.CarFatDtoMapper;
import com.drofff.palindrome.mapper.ViolationDtoMapper;
import com.drofff.palindrome.mapper.ViolationFatDtoMapper;
import com.drofff.palindrome.mapper.ViolationsViolationDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PoliceService;
import com.drofff.palindrome.service.ViolationService;
import com.drofff.palindrome.service.ViolationTypeService;

@Controller
@RequestMapping("/violation")
public class ViolationController {

	private static final String CREATE_VIOLATION_VIEW = "createViolationPage";

	private static final String VIOLATION_TYPES_PARAM = "violation_types";

	private final ViolationService violationService;
	private final DriverService driverService;
	private final PoliceService policeService;
	private final CarService carService;
	private final ViolationTypeService violationTypeService;
	private final ViolationsViolationDtoMapper violationsViolationDtoMapper;
	private final ViolationFatDtoMapper violationFatDtoMapper;
	private final CarFatDtoMapper carFatDtoMapper;
	private final ViolationDtoMapper violationDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public ViolationController(ViolationService violationService, DriverService driverService,
	                           PoliceService policeService, CarService carService,
	                           ViolationTypeService violationTypeService, ViolationsViolationDtoMapper violationsViolationDtoMapper,
	                           ViolationFatDtoMapper violationFatDtoMapper, CarFatDtoMapper carFatDtoMapper,
	                           ViolationDtoMapper violationDtoMapper, MappingsResolver mappingsResolver) {
		this.violationService = violationService;
		this.driverService = driverService;
		this.policeService = policeService;
		this.carService = carService;
		this.violationTypeService = violationTypeService;
		this.violationsViolationDtoMapper = violationsViolationDtoMapper;
		this.violationFatDtoMapper = violationFatDtoMapper;
		this.carFatDtoMapper = carFatDtoMapper;
		this.violationDtoMapper = violationDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getMyViolationsPage(ViolationFilter violationFilter, Pageable pageRequest, Model model) {
		Driver driver = driverService.getCurrentDriver();
		Page<Violation> violationsPage = violationService.getPageOfDriverViolations(driver, pageRequest);
		List<Violation> violations = filter(violationsPage.getContent(), violationFilter);
		putViolationsIntoModel(violations, model);
		putPageIntoModel(violationsPage, model);
		putCarsOfDriverIntoModel(driver, model);
		model.addAttribute(FILER_PARAM, violationFilter);
		return "myViolationsPage";
	}

	private void putViolationsIntoModel(List<Violation> violations, Model model) {
		List<ViolationsViolationDto> violationsViolationDtos = violations.stream()
				.sorted(violationsByDateTimeComparator())
				.map(this::toViolationsViolationDto)
				.collect(Collectors.toList());
		model.addAttribute(VIOLATIONS_PARAM, violationsViolationDtos);
	}

	private ViolationsViolationDto toViolationsViolationDto(Violation violation) {
		ViolationsViolationDto dto = violationsViolationDtoMapper.toDto(violation);
		return mappingsResolver.resolveMappings(violation, dto);
	}

	private void putCarsOfDriverIntoModel(Driver driver, Model model) {
		List<Car> cars = carService.getCarsOfDriver(driver);
		List<CarFatDto> carFatDtos = applyToEachListElement(this::toCarFatDto, cars);
		model.addAttribute(CARS_PARAM, carFatDtos);
	}

	private CarFatDto toCarFatDto(Car car) {
		CarFatDto carFatDto = carFatDtoMapper.toDto(car);
		return mappingsResolver.resolveMappings(car, carFatDto);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getViolationPage(@PathVariable String id, Model model) {
		User currentUser = getCurrentUser();
		Violation violation = violationService.getViolationOfUserById(currentUser, id);
		ViolationFatDto violationFatDto = toViolationFatDto(violation);
		model.addAttribute(VIOLATION_PARAM, violationFatDto);
		Driver driver = driverService.getDriverByUserId(currentUser.getId());
		boolean isCarOwned = driverService.isOwnerOfCarWithId(driver, violation.getCarId());
		model.addAttribute("is_car_owned", isCarOwned);
		return "violationPage";
	}

	private ViolationFatDto toViolationFatDto(Violation violation) {
		ViolationFatDto violationFatDto = violationFatDtoMapper.toDto(violation);
		Police officer = policeService.getPoliceById(violation.getOfficerId());
		violationFatDto.setOfficer(officer);
		return mappingsResolver.resolveMappings(violation, violationFatDto);
	}

	@GetMapping("/create")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getCreateViolationPage(Model model) {
		putViolationTypesIntoModel(model);
		return CREATE_VIOLATION_VIEW;
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('POLICE')")
	public String createViolation(ViolationDto violationDto, Model model) {
		Violation violation = violationDtoMapper.toEntity(violationDto);
		try {
			Car car = carService.getCarByNumber(violationDto.getCarNumber());
			violation.setCarId(car.getId());
			Driver carOwner = driverService.getOwnerOfCar(car);
			violation.setViolatorId(carOwner.getUserId());
			violationService.addViolation(violation);
			return redirectToWithMessage(HOME_ENDPOINT, "Violation has been successfully added");
		} catch(ValidationException e) {
			putViolationTypesIntoModel(model);
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(VIOLATION_PARAM, violationDto);
			return CREATE_VIOLATION_VIEW;
		}
	}

	private void putViolationTypesIntoModel(Model model) {
		List<ViolationType> violationTypes = violationTypeService.getAllViolationTypes();
		model.addAttribute(VIOLATION_TYPES_PARAM, violationTypes);
	}

}
