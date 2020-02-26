package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.BODY_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.BRANDS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.CARS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.ENGINE_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.FILER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.LICENCE_CATEGORIES_PARAM;
import static com.drofff.palindrome.utils.FilterUtils.filter;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.putPageIntoModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.CarsCarFatDto;
import com.drofff.palindrome.dto.CarsDriverDto;
import com.drofff.palindrome.filter.CarFilter;
import com.drofff.palindrome.mapper.CarsCarFatDtoMapper;
import com.drofff.palindrome.mapper.CarsDriverDtoMapper;
import com.drofff.palindrome.service.BodyTypeService;
import com.drofff.palindrome.service.BrandService;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.EngineTypeService;
import com.drofff.palindrome.service.LicenceCategoryService;
import com.drofff.palindrome.service.MappingsResolver;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

	private final CarService carService;
	private final DriverService driverService;
	private final BrandService brandService;
	private final BodyTypeService bodyTypeService;
	private final EngineTypeService engineTypeService;
	private final LicenceCategoryService licenceCategoryService;
	private final MappingsResolver mappingsResolver;
	private final CarsCarFatDtoMapper carsCarFatDtoMapper;
	private final CarsDriverDtoMapper carsDriverDtoMapper;

	@Autowired
	public AdminCarController(CarService carService, DriverService driverService,
	                          BrandService brandService, BodyTypeService bodyTypeService,
	                          EngineTypeService engineTypeService, LicenceCategoryService licenceCategoryService,
	                          MappingsResolver mappingsResolver, CarsCarFatDtoMapper carsCarFatDtoMapper,
	                          CarsDriverDtoMapper carsDriverDtoMapper) {
		this.carService = carService;
		this.driverService = driverService;
		this.brandService = brandService;
		this.bodyTypeService = bodyTypeService;
		this.engineTypeService = engineTypeService;
		this.licenceCategoryService = licenceCategoryService;
		this.mappingsResolver = mappingsResolver;
		this.carsCarFatDtoMapper = carsCarFatDtoMapper;
		this.carsDriverDtoMapper = carsDriverDtoMapper;
	}

	@GetMapping
	public String getCarsPage(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                          CarFilter filter, Model model) {
		Page<Car> allCarsPage = carService.getAllCarsAtPage(page);
		List<CarsCarFatDto> carsCarFatDtos = applyToEachListElement(this::toCarsCarFatDto, allCarsPage.getContent());
		model.addAttribute(CARS_PARAM, filter(carsCarFatDtos, filter));
		model.addAttribute(FILER_PARAM, filter);
		putPageIntoModel(allCarsPage, model);
		putCarPropertiesIntoModel(model);
		return "adminCarsPage";
	}

	private CarsCarFatDto toCarsCarFatDto(Car car) {
		CarsCarFatDto carsCarFatDto = carsCarFatDtoMapper.toDto(car);
		Driver carOwner = driverService.getOwnerOfCar(car);
		carsCarFatDto.setOwnerId(carOwner.getId());
		return mappingsResolver.resolveMappings(car, carsCarFatDto);
	}

	private void putCarPropertiesIntoModel(Model model) {
		model.addAttribute(BRANDS_PARAM, brandService.getAll());
		model.addAttribute(BODY_TYPES_PARAM, bodyTypeService.getAll());
		model.addAttribute(ENGINE_TYPES_PARAM, engineTypeService.getAll());
		model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategoryService.getAll());
		List<Driver> drivers = driverService.getAllDrivers();
		List<CarsDriverDto> carsDriverDtos = applyToEachListElement(this::toCarsDriverDto, drivers);
 		model.addAttribute("drivers", carsDriverDtos);
	}

	private CarsDriverDto toCarsDriverDto(Driver driver) {
		CarsDriverDto carsDriverDto = carsDriverDtoMapper.toDto(driver);
		return mappingsResolver.resolveMappings(driver, carsDriverDto);
	}

}
