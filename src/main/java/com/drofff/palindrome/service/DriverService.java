package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;

public interface DriverService {

	void createDriverProfileWithPhoto(Driver driver, MultipartFile photo);

	void updateDriverProfile(Driver driver);

	void updateDriverPhoto(MultipartFile photo);

	Driver getCurrentDriver();

	Driver getDriverByUserId(String userId);

	void addToDriverOwnedCars(Car car, Driver driver);

	void deleteFromDriverOwnedCars(Car car, Driver driver);

	Driver getOwnerOfCar(Car car);

	List<Driver> getAllDrivers();

	Driver getDriverById(String id);

}
