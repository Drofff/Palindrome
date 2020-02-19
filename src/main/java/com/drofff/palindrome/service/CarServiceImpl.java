package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.CarRepository;

@Service
public class CarServiceImpl implements CarService {

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
		User currentUser = getCurrentUser();
		Driver driver = driverService.getDriverByUserId(currentUser.getId());
		carRepository.save(car);
		driverService.addToDriverOwnedCars(car, driver);
	}

	@Override
	public void updateCar(Car car) {
		validate(car);
		validateHasId(car);
		validateCarProperties(car);
		User currentUser = getCurrentUser();
		Driver driver = driverService.getDriverByUserId(currentUser.getId());
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
		Car car = getById(id);
		User currentUser = getCurrentUser();
		Driver driver = driverService.getDriverByUserId(currentUser.getId());
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

	private Car getById(String id) {
		return carRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Car with such id doesn't exist"));
	}

}
