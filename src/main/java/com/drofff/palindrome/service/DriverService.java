package com.drofff.palindrome.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;

public interface DriverService {

	void createDriverProfileWithPhoto(Driver driver, MultipartFile photo);

	void updateDriverProfile(Driver driver);

	void updateDriverPhoto(MultipartFile photo);

	Driver getDriverByUserId(String userId);

	Optional<Driver> getDriverByUserIdIfPresent(String userId);

	void addToDriverOwnedCars(Car car, Driver driver);

	void deleteFromDriverOwnedCars(Car car, Driver driver);

}
