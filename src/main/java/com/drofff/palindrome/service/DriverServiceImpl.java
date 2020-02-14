package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.DriverRepository;

@Service
public class DriverServiceImpl implements DriverService {

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
	public void updateDriverProfile(Driver driver) {
		validate(driver);
		User currentUser = getCurrentUser();
		Driver originalDriver = getDriverByUserId(currentUser.getId());
		mergeDriverMappings(originalDriver, driver);
		driverRepository.save(driver);
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
		if(Objects.isNull(photo)) {
			throw new ValidationException("Photo is required");
		}
	}

	public boolean hasNoDriverProfile(User user) {
		return !hasDriverProfile(user);
	}

	private boolean hasDriverProfile(User user) {
		return getDriverByUserIdIfPresent(user.getId()).isPresent();
	}

	@Override
	public Driver getDriverByUserId(String userId) {
		return getDriverByUserIdIfPresent(userId)
				.orElseThrow(() -> new PalindromeException("User has no driver profile"));
	}

	@Override
	public Optional<Driver> getDriverByUserIdIfPresent(String userId) {
		return driverRepository.findByUserId(userId);
	}

}
