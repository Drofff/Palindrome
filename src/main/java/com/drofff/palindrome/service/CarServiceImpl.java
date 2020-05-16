package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.isBlank;
import static com.drofff.palindrome.utils.ValidationUtils.*;

@Service
public class CarServiceImpl implements CarService, EntityManager {

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
		validateIsDriver();
		carRepository.save(car);
		addToDriverOwnedCars(car);
	}

	private void validateIsDriver() {
		if(isCurrentUserNotDriver()) {
			throw new ValidationException("User should obtain driver role");
		}
	}

	private boolean isCurrentUserNotDriver() {
		return !isCurrentUserDriver();
	}

	private void addToDriverOwnedCars(Car car) {
		Driver driver = driverService.getCurrentDriver();
		driverService.addToDriverOwnedCars(car, driver);
	}

	@Override
	public void update(Entity entity) {
		validateIsCarEntity(entity);
		updateCar((Car) entity);
	}

	private void validateIsCarEntity(Entity entity) {
		Class<? extends Entity> entityClass = entity.getClass();
		if(isNotCarClass(entityClass)) {
			throw new ValidationException("Unexpected entity of class " + entityClass.getName() + ". Car entity is expected");
		}
	}

	private boolean isNotCarClass(Class<? extends Entity> clazz) {
		return !isCarClass(clazz);
	}

	@Override
	public void updateCar(Car car) {
		validate(car);
		validateEntityHasId(car);
		validateCarProperties(car);
		validatePermissionsToUpdateCar(car);
		carRepository.save(car);
	}

	private void validateCarProperties(Car car) {
		carPropertyServices.forEach(service -> service.validateCarProperty(car));
	}

	private void validatePermissionsToUpdateCar(Car car) {
		if(userHasNoPermissionToUpdateCar(car)) {
			throw new ValidationException("Invalid permissions");
		}
	}

	private boolean userHasNoPermissionToUpdateCar(Car car) {
		return !userHasPermissionToUpdateCar(car);
	}

	private boolean userHasPermissionToUpdateCar(Car car) {
		return isCurrentUserAdmin() || isCurrentUserOwnerOfCar(car);
	}

	private boolean isCurrentUserAdmin() {
		return getCurrentUser().isAdmin();
	}

	private boolean isCurrentUserOwnerOfCar(Car car) {
		if(isCurrentUserDriver()) {
			Driver driver = driverService.getCurrentDriver();
			return isDriverOwnerOfCar(driver, car);
		}
		return false;
	}

	private boolean isCurrentUserDriver() {
		return getCurrentUser().isDriver();
	}

	@Override
	public void deleteCarById(String id) {
		Car car = getOwnedCarById(id);
		deleteFromDriverOwnedCars(car);
		carRepository.delete(car);
	}

	private void deleteFromDriverOwnedCars(Car car) {
		Driver driver = driverService.getCurrentDriver();
		driverService.deleteFromDriverOwnedCars(car, driver);
	}

	@Override
	public List<Car> getCarsOfDriver(Driver driver) {
		return driver.getOwnedCarIds().stream()
				.map(this::getCarById)
				.collect(Collectors.toList());
	}

	@Override
	public Car getOwnedCarById(String id) {
		Car car = getCarById(id);
		Driver driver = driverService.getCurrentDriver();
		validateDriverIsOwnerOfCar(driver, car);
		return car;
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
	public Car getCarById(String id) {
		validateNotNull(id, "Car id is required");
		return carRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Car with such id doesn't exist"));
	}

	@Override
	public List<Car> getAllCars() {
		return carRepository.findAll();
	}

	@Override
	public Page<Car> getAllCarsAtPage(int page) {
		Pageable pageable = PageRequest.of(page, ALL_CARS_PAGE_SIZE);
		return carRepository.findAll(pageable);
	}

	@Override
	public boolean isManagingEntityOfClass(Class<?> clazz) {
		return isCarClass(clazz);
	}

	private boolean isCarClass(Class<?> clazz) {
		return Car.class.isAssignableFrom(clazz);
	}

	@Override
	public Car getCarByNumber(String number) {
		validateCarNumber(number);
		return carRepository.findByNumber(number)
				.orElseThrow(() -> new ValidationException("Car with such number doesn't exist"));
	}

	private void validateCarNumber(String number) {
		validateNotNull(number, "Car number is required");
		validateCarNumberIsNotBlank(number);
	}

	private void validateCarNumberIsNotBlank(String number) {
		if(isBlank(number)) {
			throw new ValidationException("Car number should not be blank");
		}
	}

}
