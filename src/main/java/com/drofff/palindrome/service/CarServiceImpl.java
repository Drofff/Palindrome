package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.CarRepository;

@Service
public class CarServiceImpl implements CarService {

	private static final int ALL_CARS_PAGE_SIZE = 12;

	private final CarRepository carRepository;
	private final DriverService driverService;
	private final List<CarPropertyService> carPropertyServices;

	@Autowired
	public CarServiceImpl(CarRepository carRepository, DriverService driverService,
	                      List<CarPropertyService> carPropertyServices) {
		this.carRepository = carRepository;
		this.driverService = driverService;
		this.carPropertyServices = carPropertyServices;
	}

	@Override
	public void addCar(Car car) {
		validate(car);
		validateCarProperties(car);
		Driver driver = driverService.getCurrentDriver();
		carRepository.save(car);
		driverService.addToDriverOwnedCars(car, driver);
	}

	@Override
	public void updateCar(Car car) {
		validate(car);
		validateHasId(car);
		validateCarProperties(car);
		Driver driver = driverService.getCurrentDriver();
		validateDriverIsOwnerOfCar(driver, car);
		carRepository.save(car);
	}

	private void validateHasId(Car car) {
		if(hasNoId(car)) {
			throw new ValidationException("Id is required to update car");
		}
	}

	private boolean hasNoId(Car car) {
		return Objects.isNull(car.getId());
	}

	private void validateCarProperties(Car car) {
		carPropertyServices.forEach(service -> service.validateCarProperty(car));
	}

	@Override
	public void deleteCarById(String id) {
		validateCarId(id);
		Car car = getById(id);
		Driver driver = driverService.getCurrentDriver();
		validateDriverIsOwnerOfCar(driver, car);
		driverService.deleteFromDriverOwnedCars(car, driver);
		carRepository.delete(car);
	}

	private void validateDriverIsOwnerOfCar(Driver driver, Car car) {
		if(isDriverNotOwnerOfCar(driver, car)) {
			throw new ValidationException("Driver is not a owner of the car");
		}
	}

	private boolean isDriverNotOwnerOfCar(Driver driver, Car car) {
		return !isDriverOwnerOfCar(driver, car);
	}

	private boolean isDriverOwnerOfCar(Driver driver, Car car) {
		return driver.getOwnedCarIds().contains(car.getId());
	}

	@Override
	public List<Car> getCarsOfDriver(Driver driver) {
		return driver.getOwnedCarIds().stream()
				.map(this::getById)
				.collect(Collectors.toList());
	}

	@Override
	public Car getById(String id) {
		validateCarId(id);
		return carRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Car with such id doesn't exist"));
	}

	private void validateCarId(String id) {
		validateNotNull(id, "Car id is required");
	}

	@Override
	public Page<Car> getAllCarsAtPage(int page) {
		Pageable pageable = PageRequest.of(page, ALL_CARS_PAGE_SIZE);
		return carRepository.findAll(pageable);
	}

}
