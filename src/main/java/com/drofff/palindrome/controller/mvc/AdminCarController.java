package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.CarDto;
import com.drofff.palindrome.dto.CarsCarFatDto;
import com.drofff.palindrome.dto.CarsDriverDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.CarPattern;
import com.drofff.palindrome.mapper.CarDtoMapper;
import com.drofff.palindrome.mapper.CarsCarFatDtoMapper;
import com.drofff.palindrome.mapper.CarsDriverDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.*;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

	private static final String ADMIN_CAR_UPDATE_VIEW = "adminUpdateCarPage";

	private final CarService carService;
	private final DriverService driverService;
	private final BrandService brandService;
	private final BodyTypeService bodyTypeService;
	private final EngineTypeService engineTypeService;
	private final LicenceCategoryService licenceCategoryService;
	private final AdminService adminService;
	private final PhotoService photoService;
	private final MappingsResolver mappingsResolver;
	private final CarsCarFatDtoMapper carsCarFatDtoMapper;
	private final CarsDriverDtoMapper carsDriverDtoMapper;
	private final CarDtoMapper carDtoMapper;

	@Autowired
	public AdminCarController(CarService carService, DriverService driverService,
	                          BrandService brandService, BodyTypeService bodyTypeService,
	                          EngineTypeService engineTypeService, LicenceCategoryService licenceCategoryService,
	                          AdminService adminService, PhotoService photoService, MappingsResolver mappingsResolver,
	                          CarsCarFatDtoMapper carsCarFatDtoMapper, CarsDriverDtoMapper carsDriverDtoMapper,
	                          CarDtoMapper carDtoMapper) {
		this.carService = carService;
		this.driverService = driverService;
		this.brandService = brandService;
		this.bodyTypeService = bodyTypeService;
		this.engineTypeService = engineTypeService;
		this.licenceCategoryService = licenceCategoryService;
		this.adminService = adminService;
		this.photoService = photoService;
		this.mappingsResolver = mappingsResolver;
		this.carsCarFatDtoMapper = carsCarFatDtoMapper;
		this.carsDriverDtoMapper = carsDriverDtoMapper;
		this.carDtoMapper = carDtoMapper;
	}

	@GetMapping
	public String getCarsPage(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                          @RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                          CarPattern carPattern, Model model) {
		Page<Car> allCarsPage = carService.getAllCarsAtPage(page);
		List<CarsCarFatDto> carsCarFatDtos = applyToPageContent(this::toCarsCarFatDto, allCarsPage);
		model.addAttribute(CARS_PARAM, grepByPattern(carsCarFatDtos, carPattern));
		model.addAttribute(PATTERN_PARAM, carPattern);
		model.addAttribute(MESSAGE_PARAM, message);
		putPageIntoModel(allCarsPage, model);
		putCarFiltersIntoModel(model);
		return "adminCarsPage";
	}

	private void putCarFiltersIntoModel(Model model) {
		putCarPropertiesIntoModel(model);
		List<Driver> drivers = driverService.getAllDrivers();
		List<CarsDriverDto> carsDriverDtos = applyToEachListElement(this::toCarsDriverDto, drivers);
 		model.addAttribute("drivers", carsDriverDtos);
	}

	private CarsDriverDto toCarsDriverDto(Driver driver) {
		CarsDriverDto carsDriverDto = carsDriverDtoMapper.toDto(driver);
		return mappingsResolver.resolveMappings(driver, carsDriverDto);
	}

	@GetMapping("/{id}")
	public String getCarPage(@PathVariable String id, Model model) {
		Car car = carService.getCarById(id);
		CarsCarFatDto carsCarFatDto = toCarsCarFatDto(car);
		model.addAttribute(CAR_PARAM, carsCarFatDto);
		Driver driver = driverService.getOwnerOfCar(car);
		loadDriverPhoto(driver);
		model.addAttribute(DRIVER_PARAM, driver);
		return "viewCarPage";
	}

	private CarsCarFatDto toCarsCarFatDto(Car car) {
		CarsCarFatDto carsCarFatDto = carsCarFatDtoMapper.toDto(car);
		Driver carOwner = driverService.getOwnerOfCar(car);
		carsCarFatDto.setOwnerId(carOwner.getId());
		return mappingsResolver.resolveMappings(car, carsCarFatDto);
	}

	private void loadDriverPhoto(Driver driver) {
		String photoUri = driver.getPhotoUri();
		String encodedPhoto = photoService.loadEncodedPhotoByUri(photoUri);
		driver.setPhotoUri(encodedPhoto);
	}

	@GetMapping("/update/{id}")
	public String getUpdateCarPage(@PathVariable String id, Model model) {
		try {
			Car car = carService.getCarById(id);
			model.addAttribute(CAR_PARAM, car);
			putCarPropertiesIntoModel(model);
			return ADMIN_CAR_UPDATE_VIEW;
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@PostMapping("/update/{id}")
	public String updateCar(@PathVariable String id, CarDto carDto, Model model) {
		Car car = carDtoMapper.toEntity(carDto);
		car.setId(id);
		try {
			carService.updateCar(car);
			adminService.notifyCarUpdate(car);
			return redirectToWithMessage("/admin/cars", "Successfully updated car info");
		} catch(ValidationException e) {
			model.addAttribute(CAR_PARAM, car);
			putValidationExceptionIntoModel(e, model);
			putCarPropertiesIntoModel(model);
			return ADMIN_CAR_UPDATE_VIEW;
		}
	}

	private void putCarPropertiesIntoModel(Model model) {
		model.addAttribute(BRANDS_PARAM, brandService.getAll());
		model.addAttribute(BODY_TYPES_PARAM, bodyTypeService.getAll());
		model.addAttribute(ENGINE_TYPES_PARAM, engineTypeService.getAll());
		model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategoryService.getAll());
	}

}
