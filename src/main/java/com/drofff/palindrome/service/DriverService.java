package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {

	void createDriverProfileWithPhoto(Driver driver, MultipartFile photo);

	void updateDriverProfile(Driver driver);

	void updateDriverPhoto(MultipartFile photo);

	void updateDriverLastNotificationDateTime(Driver driver);

	boolean hasNoDriverProfile(User user);

	Driver getCurrentDriver();

	Driver getDriverByUserId(String userId);

	void addToDriverOwnedCars(Car car, Driver driver);

	void deleteFromDriverOwnedCars(Car car, Driver driver);

	boolean isOwnerOfCarWithId(Driver driver, String carId);

	Driver getOwnerOfCar(Car car);

	List<Driver> getDriversByName(String name);

	List<Driver> getAllDrivers();

	Driver getDriverById(String id);

}
