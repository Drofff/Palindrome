package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.SUCCESS_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putPageOfCollectionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.CarDto;
import com.drofff.palindrome.dto.OwnedCarsCarDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.CarDtoMapper;
import com.drofff.palindrome.mapper.OwnedCarsCarDtoMapper;
import com.drofff.palindrome.service.BodyTypeService;
import com.drofff.palindrome.service.BrandService;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.EngineTypeService;
import com.drofff.palindrome.service.LicenceCategoryService;

@Controller
@RequestMapping("/car")
@PreAuthorize("hasAuthority('DRIVER')")
public class CarController {

	private static final String CREATE_CAR_VIEW = "createCarPage";

	private static final String BRANDS_PARAM = "brands";
	private static final String BODY_TYPES_PARAM = "body_types";
	private static final String LICENCE_CATEGORIES_PARAM = "licence_categories";
	private static final String ENGINE_TYPES_PARAM = "engine_types";
	private static final String CAR_PARAM = "car";

	private static final int OWNED_CARS_PAGE_SIZE = 3;

	private final CarService carService;
	private final BrandService brandService;
	private final BodyTypeService bodyTypeService;
	private final LicenceCategoryService licenceCategoryService;
	private final EngineTypeService engineTypeService;
	private final DriverService driverService;
	private final CarDtoMapper carDtoMapper;
	private final OwnedCarsCarDtoMapper ownedCarsCarDtoMapper;

	@Autowired
	public CarController(CarService carService, BrandService brandService,
	                     BodyTypeService bodyTypeService, LicenceCategoryService licenceCategoryService,
	                     EngineTypeService engineTypeService, DriverService driverService,
	                     CarDtoMapper carDtoMapper, OwnedCarsCarDtoMapper ownedCarsCarDtoMapper) {
		this.carService = carService;
		this.brandService = brandService;
		this.bodyTypeService = bodyTypeService;
		this.licenceCategoryService = licenceCategoryService;
		this.engineTypeService = engineTypeService;
		this.driverService = driverService;
		this.carDtoMapper = carDtoMapper;
		this.ownedCarsCarDtoMapper = ownedCarsCarDtoMapper;
	}

	@GetMapping
	public String getOwnedCarsPage(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                               Model model) {
		User currentUser = getCurrentUser();
		Driver driver = driverService.getDriverByUserId(currentUser.getId());
		List<Car> ownedCars = carService.getCarsOfDriver(driver);
		List<OwnedCarsCarDto> ownedCarsCarDtos = toOwnedCarsCarDtos(ownedCars);
		putPageOfCollectionIntoModel(page, OWNED_CARS_PAGE_SIZE, ownedCarsCarDtos, model);
		return "ownedCarsPage";
	}

	private List<OwnedCarsCarDto> toOwnedCarsCarDtos(List<Car> cars) {
		return cars.stream()
				.map(this::toOwnedCarsCarDto)
				.collect(Collectors.toList());
	}

	private OwnedCarsCarDto toOwnedCarsCarDto(Car car) {
		OwnedCarsCarDto carDto = ownedCarsCarDtoMapper.toDto(car);
		Brand brand = brandService.getById(car.getBrandId());
		carDto.setBrand(brand);
		return carDto;
	}

	@GetMapping("/create")
	public String getCreateCarPage(Model model) {
		putCarPropertiesIntoModel(model);
		return CREATE_CAR_VIEW;
	}

	@PostMapping("/create")
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

	private void putCarPropertiesIntoModel(Model model) {
		model.addAttribute(BRANDS_PARAM, brandService.getAll());
		model.addAttribute(BODY_TYPES_PARAM, bodyTypeService.getAll());
		model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategoryService.getAll());
		model.addAttribute(ENGINE_TYPES_PARAM, engineTypeService.getAll());
	}

}
