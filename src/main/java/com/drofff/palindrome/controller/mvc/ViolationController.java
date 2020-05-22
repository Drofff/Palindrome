package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.*;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.ViolationPattern;
import com.drofff.palindrome.mapper.*;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.*;
import static com.drofff.palindrome.utils.ViolationUtils.invertedViolationsDateTimeComparator;

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
	private final PoliceViolationDtoMapper policeViolationDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public ViolationController(ViolationService violationService, DriverService driverService,
							   PoliceService policeService, CarService carService,
							   ViolationTypeService violationTypeService, ViolationsViolationDtoMapper violationsViolationDtoMapper,
							   ViolationFatDtoMapper violationFatDtoMapper, CarFatDtoMapper carFatDtoMapper,
							   ViolationDtoMapper violationDtoMapper, PoliceViolationDtoMapper policeViolationDtoMapper,
							   MappingsResolver mappingsResolver) {
		this.violationService = violationService;
		this.driverService = driverService;
		this.policeService = policeService;
		this.carService = carService;
		this.violationTypeService = violationTypeService;
		this.violationsViolationDtoMapper = violationsViolationDtoMapper;
		this.violationFatDtoMapper = violationFatDtoMapper;
		this.carFatDtoMapper = carFatDtoMapper;
		this.violationDtoMapper = violationDtoMapper;
		this.policeViolationDtoMapper = policeViolationDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getMyViolationsPage(ViolationPattern violationPattern, Pageable pageRequest, Model model) {
		Driver driver = driverService.getCurrentDriver();
		Page<Violation> violationsPage = violationService.getPageOfDriverViolations(driver, pageRequest);
		List<Violation> violations = grepByPattern(violationsPage.getContent(), violationPattern);
		putViolationsIntoModel(violations, model);
		putPageIntoModel(violationsPage, model);
		putCarsOfDriverIntoModel(driver, model);
		model.addAttribute(PATTERN_PARAM, violationPattern);
		return "myViolationsPage";
	}

	private void putViolationsIntoModel(List<Violation> violations, Model model) {
		List<ViolationsViolationDto> violationsViolationDtos = violations.stream()
				.sorted(invertedViolationsDateTimeComparator())
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

	@GetMapping("/police")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getPoliceViolationsPage(Pageable pageable, Model model) {
		User currentUser = getCurrentUser();
		Police police = policeService.getPoliceByUserId(currentUser.getId());
		Page<Violation> violationsPage = violationService.getPageOfViolationsAddedByPolice(police, pageable);
		List<PoliceViolationDto> policeViolationDtos = applyToPageContent(this::toPoliceViolationDto, violationsPage);
		model.addAttribute(VIOLATIONS_PARAM, policeViolationDtos);
		putPageIntoModel(violationsPage, model);
		putViolationTypesIntoModel(model);
		return "policeViolationsPage";
	}

	private PoliceViolationDto toPoliceViolationDto(Violation violation) {
		PoliceViolationDto policeViolationDto = policeViolationDtoMapper.toDto(violation);
		Driver violator = driverService.getDriverByUserId(violation.getViolatorId());
		policeViolationDto.setViolator(violator);
		return mappingsResolver.resolveMappings(violation, policeViolationDto);
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
		return mappingsResolver.resolveMappings(violation, violationFatDto);
	}

	@GetMapping("/create")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getCreateViolationPage(@RequestParam(required = false) String number, Model model) {
		putViolationTypesIntoModel(model);
		model.addAttribute("number", number);
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
		List<ViolationType> violationTypes = violationTypeService.getAll();
		model.addAttribute(VIOLATION_TYPES_PARAM, violationTypes);
	}

}
