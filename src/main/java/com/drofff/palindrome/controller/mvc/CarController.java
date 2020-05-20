package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.CarDto;
import com.drofff.palindrome.dto.CarFatDto;
import com.drofff.palindrome.dto.OwnedCarsCarDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.CarDtoMapper;
import com.drofff.palindrome.mapper.CarFatDtoMapper;
import com.drofff.palindrome.mapper.OwnedCarsCarDtoMapper;
import com.drofff.palindrome.service.*;
import com.drofff.palindrome.type.CollectionPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.*;
import static com.drofff.palindrome.utils.ViolationUtils.*;

@Controller
@RequestMapping("/car")
public class CarController {

	private static final String CREATE_CAR_VIEW = "createCarPage";
	private static final String UPDATE_CAR_VIEW = "updateCarPage";

	private static final String CAR_ENDPOINT_PREFIX = "/car/";

	private static final int OWNED_CARS_PAGE_SIZE = 3;

	private final CarService carService;
	private final BrandService brandService;
	private final BodyTypeService bodyTypeService;
	private final LicenceCategoryService licenceCategoryService;
	private final EngineTypeService engineTypeService;
	private final DriverService driverService;
	private final ViolationService violationService;
	private final CarDtoMapper carDtoMapper;
	private final OwnedCarsCarDtoMapper ownedCarsCarDtoMapper;
	private final CarFatDtoMapper carFatDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public CarController(CarService carService, BrandService brandService,
	                     BodyTypeService bodyTypeService, LicenceCategoryService licenceCategoryService,
	                     EngineTypeService engineTypeService, DriverService driverService,
	                     ViolationService violationService, CarDtoMapper carDtoMapper,
	                     OwnedCarsCarDtoMapper ownedCarsCarDtoMapper, CarFatDtoMapper carFatDtoMapper,
	                     MappingsResolver mappingsResolver) {
		this.carService = carService;
		this.brandService = brandService;
		this.bodyTypeService = bodyTypeService;
		this.licenceCategoryService = licenceCategoryService;
		this.engineTypeService = engineTypeService;
		this.driverService = driverService;
		this.violationService = violationService;
		this.carDtoMapper = carDtoMapper;
		this.ownedCarsCarDtoMapper = ownedCarsCarDtoMapper;
		this.carFatDtoMapper = carFatDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getOwnedCarsPage(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                               @RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                               Model model) {
		Driver driver = driverService.getCurrentDriver();
		List<Car> ownedCars = carService.getCarsOfDriver(driver);
		List<OwnedCarsCarDto> ownedCarsCarDtos = applyToEachListElement(this::toOwnedCarsCarDto, ownedCars);
		CollectionPage<OwnedCarsCarDto> ownedCarsPage = CollectionPage.Builder.ofCollection(ownedCarsCarDtos)
				.atPage(page)
				.withPageSize(OWNED_CARS_PAGE_SIZE)
				.build();
		putCollectionPageIntoModel(ownedCarsPage, model);
		model.addAttribute(MESSAGE_PARAM, message);
		return "ownedCarsPage";
	}

	private OwnedCarsCarDto toOwnedCarsCarDto(Car car) {
		OwnedCarsCarDto carDto = ownedCarsCarDtoMapper.toDto(car);
		return mappingsResolver.resolveMappings(car, carDto);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getCar(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                     @PathVariable String id, Model model) {
		Car car = carService.getOwnedCarById(id);
		model.addAttribute(CAR_PARAM, toCarFatDto(car));
		model.addAttribute(MESSAGE_PARAM, message);
		putCarViolationStatsIntoModel(car, model);
		return "carPage";
	}

	private CarFatDto toCarFatDto(Car car) {
		CarFatDto carFatDto = carFatDtoMapper.toDto(car);
		return mappingsResolver.resolveMappings(car, carFatDto);
	}

	private void putCarViolationStatsIntoModel(Car car, Model model) {
		List<Violation> violations = violationService.getCarViolations(car);
		model.addAttribute("violations_count", violations.size());
		LocalDateTime latestViolationDateTime = getLatestViolationDateTimeIfPresent(violations)
				.orElse(null);
		model.addAttribute("latest_violation_datetime", latestViolationDateTime);
		model.addAttribute("payed_violations_count", countPaidViolations(violations));
		model.addAttribute("not_payed_violations_count", countUnpaidViolations(violations));
	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('POLICE')")
	@ResponseBody
	public List<Car> getAllCars() {
		return carService.getAllCars();
	}

	@GetMapping("/create")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getCreateCarPage(Model model) {
		putCarPropertiesIntoModel(model);
		return CREATE_CAR_VIEW;
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String createCar(CarDto carDto, Model model) {
		Car car = carDtoMapper.toEntity(carDto);
		try {
			carService.addCar(car);
			model.addAttribute(SUCCESS_PARAM, Boolean.TRUE);
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(CAR_PARAM, car);
		}
		putCarPropertiesIntoModel(model);
		return CREATE_CAR_VIEW;
	}

	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getUpdateCarPage(@PathVariable String id, Model model) {
		Car car = carService.getOwnedCarById(id);
		model.addAttribute(CAR_PARAM, car);
		putCarPropertiesIntoModel(model);
		return UPDATE_CAR_VIEW;
	}

	@PostMapping("/update/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String updateCarPage(@PathVariable String id, CarDto carDto, Model model) {
		Car car = carDtoMapper.toEntity(carDto);
		car.setId(id);
		try {
			carService.updateCar(car);
			return redirectToWithMessage(CAR_ENDPOINT_PREFIX + id, "Saved changes");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(CAR_PARAM, car);
			putCarPropertiesIntoModel(model);
			return UPDATE_CAR_VIEW;
		}
	}

	@PostMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String deleteCarById(@PathVariable String id) {
		try {
			carService.deleteCarById(id);
		} catch(ValidationException e) {
			return redirectToWithMessage(CAR_ENDPOINT_PREFIX + id, e.getMessage());
		}
		return redirectToWithMessage("/car", "Successfully deleted car");
	}

	private void putCarPropertiesIntoModel(Model model) {
		model.addAttribute(BRANDS_PARAM, brandService.getAll());
		model.addAttribute(BODY_TYPES_PARAM, bodyTypeService.getAll());
		model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategoryService.getAll());
		model.addAttribute(ENGINE_TYPES_PARAM, engineTypeService.getAll());
	}

}
