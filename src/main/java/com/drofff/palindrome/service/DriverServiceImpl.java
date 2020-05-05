package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Entity;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.DriverRepository;
import com.drofff.palindrome.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.EntityUtils.copyNonEditableFields;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.stream.Collectors.toList;

@Service
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
		validateLicenceNumberIsUnique(driver.getLicenceNumber());
		validatePhoto(photo);
		User currentUser = getCurrentUser();
		validateHasNoDriverProfile(currentUser);
		String photoUri = photoService.savePhotoForUser(photo, currentUser);
		driver.setPhotoUri(photoUri);
		driver.setUserId(currentUser.getId());
		driverRepository.save(driver);
	}

	private void validateLicenceNumberIsUnique(String licenceNumber) {
		if(existsDriverWithLicenceNumber(licenceNumber)) {
			throw new ValidationException("Driver account with such a licence number already exists");
		}
	}

	private boolean existsDriverWithLicenceNumber(String licenceNumber) {
		return driverRepository.findByLicenceNumber(licenceNumber).isPresent();
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
		Driver actualDriver = getActualDriverProfile(driver);
		copyNonEditableFields(actualDriver, driver);
		driverRepository.save(driver);
	}

	private Driver getActualDriverProfile(Driver driver) {
		User currentUser = getCurrentUser();
		if(currentUser.isAdmin()) {
			return getDriverById(driver.getId());
		}
		return getDriverByUserId(currentUser.getId());
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
	public boolean isOwnerOfCarWithId(Driver driver, String id) {
		Driver carOwner = getOwnerOfCarWithId(id);
		return driver.equals(carOwner);
	}

	@Override
	public Driver getOwnerOfCar(Car car) {
		return getOwnerOfCarWithId(car.getId());
	}

	private Driver getOwnerOfCarWithId(String id) {
		return driverRepository.findByCarId(id)
				.orElseThrow(() -> new ValidationException("Can not find an owner of the car"));
	}

	@Override
	public List<Driver> getDriversByName(String name) {
		validateNotNull(name, "Name should be provided");
		return getAllDrivers().stream()
				.filter(driver -> matchesNameOfDriver(name, driver))
				.sorted(nameMatchComparator(name))
				.collect(toList());
	}

	private boolean matchesNameOfDriver(String namePattern, Driver driver) {
		return calculateNameMatchForDriver(namePattern, driver) > 0;
	}

	private Comparator<Driver> nameMatchComparator(String name) {
		return (d0, d1) -> {
			int d0NameMatch = calculateNameMatchForDriver(name, d0);
			int d1NameMatch = calculateNameMatchForDriver(name, d1);
			return d1NameMatch - d0NameMatch;
		};
	}

	private int calculateNameMatchForDriver(String namePattern, Driver driver) {
		List<String> patternNames = getNamesOfPattern(namePattern);
		return (int) driver.getNamesAsList().stream()
				.filter(patternNames::contains)
				.count();
	}

	private List<String> getNamesOfPattern(String namePattern) {
		String[] names = namePattern.split("\\s");
		return Stream.of(names)
				.filter(StringUtils::isNotBlank)
				.collect(toList());
	}

	@Override
	public List<Driver> getAllDrivers() {
		return driverRepository.findAll();
	}

	@Override
	public Driver getDriverById(String id) {
		validateNotNull(id, "Missing driver id");
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
