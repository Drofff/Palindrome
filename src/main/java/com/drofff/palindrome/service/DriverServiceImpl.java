package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.DriverRepository;

@Service
@Qualifier("driver")
public class DriverServiceImpl implements DriverService, EntityManager {

	private final DriverRepository driverRepository;
	private final PhotoService photoService;

	@Autowired
	public DriverServiceImpl(DriverRepository driverRepository, PhotoService photoService) {
		this.driverRepository = driverRepository;
		this.photoService = photoService;
	}

	@Override
	public void createDriverProfileWithPhoto(Driver driver, MultipartFile photo) {
		validate(driver);
		validatePhoto(photo);
		User currentUser = getCurrentUser();
		validateHasNoDriverProfile(currentUser);
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		driver.setPhotoUri(photoUri);
		driver.setUserId(currentUser.getId());
		driverRepository.save(driver);
	}

	private void validateHasNoDriverProfile(User user) {
		if(hasDriverProfile(user)) {
			throw new ValidationException("User already has a driver profile");
		}
	}

	@Override
	public void update(Entity entity) {
		validateIsDriverEntity(entity);
		updateDriverProfile((Driver) entity);
	}

	private void validateIsDriverEntity(Entity entity) {
		Class<? extends Entity> entityClass = entity.getClass();
		if(isNotDriverClass(entityClass)) {
			throw new ValidationException("Unexpected entity of class " + entityClass.getName() + ". Driver entity should be provided");
		}
	}

	private boolean isNotDriverClass(Class<? extends Entity> clazz) {
		return !isDriverClass(clazz);
	}

	@Override
	public void updateDriverProfile(Driver driver) {
		validate(driver);
		validateHasId(driver);
		User currentUser = getCurrentUser();
		Driver originalDriver = getDriverByUserId(currentUser.getId());
		mergeDriverMappings(originalDriver, driver);
		driverRepository.save(driver);
	}

	private void validateHasId(Driver driver) {
		if(hasNoId(driver)) {
			throw new ValidationException("Id is required to update driver profile");
		}
	}

	private boolean hasNoId(Driver driver) {
		return Objects.isNull(driver.getId());
	}

	private void mergeDriverMappings(Driver from, Driver to) {
		String userId = from.getUserId();
		to.setUserId(userId);
		Set<String> ownedCars = from.getOwnedCarIds();
		to.setOwnedCarIds(ownedCars);
		String photoUri = from.getPhotoUri();
		to.setPhotoUri(photoUri);
	}

	@Override
	public void updateDriverPhoto(MultipartFile photo) {
		validatePhoto(photo);
		User currentUser = getCurrentUser();
		Driver driverProfile = getDriverByUserId(currentUser.getId());
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		driverProfile.setPhotoUri(photoUri);
		driverRepository.save(driverProfile);
	}

	private void validatePhoto(MultipartFile photo) {
		validateNotNull(photo, "Photo is required");
	}

	@Override
	public boolean hasNoDriverProfile(User user) {
		return !hasDriverProfile(user);
	}

	private boolean hasDriverProfile(User user) {
		return getDriverByUserIdIfPresent(user.getId()).isPresent();
	}

	@Override
	public Driver getCurrentDriver() {
		User currentUser = getCurrentUser();
		return getDriverByUserId(currentUser.getId());
	}

	@Override
	public Driver getDriverByUserId(String userId) {
		return getDriverByUserIdIfPresent(userId)
				.orElseThrow(() -> new PalindromeException("User has no driver profile"));
	}

	private Optional<Driver> getDriverByUserIdIfPresent(String userId) {
		return driverRepository.findByUserId(userId);
	}

	public void addToDriverOwnedCars(Car car, Driver driver) {
		driver.getOwnedCarIds().add(car.getId());
		driverRepository.save(driver);
	}

	@Override
	public void deleteFromDriverOwnedCars(Car car, Driver driver) {
		driver.getOwnedCarIds().remove(car.getId());
		driverRepository.save(driver);
	}

	@Override
	public Driver getOwnerOfCar(Car car) {
		return driverRepository.findByCarId(car.getId())
				.orElseThrow(() -> new ValidationException("Can not find an owner of the car"));
	}

	@Override
	public List<Driver> getAllDrivers() {
		return driverRepository.findAll();
	}

	@Override
	public Driver getDriverById(String id) {
		return driverRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Driver with such id doesn't exist"));
	}

	@Override
	public boolean isManagingEntityOfClass(Class<?> clazz) {
		return isDriverClass(clazz);
	}

	private boolean isDriverClass(Class<?> clazz) {
		return Driver.class.isAssignableFrom(clazz);
	}

}
