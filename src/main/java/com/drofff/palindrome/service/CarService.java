package com.drofff.palindrome.service;

import com.drofff.palindrome.document.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CarService {

	void addCar(Car car);

	void updateCar(Car car);

	void deleteCarById(String id);

	List<Car> getCarsOfDriver(Driver driver);

	Car getOwnedCarById(String id);

	Car getCarById(String id);

	List<Car> getAllCars();

	Page<Car> getAllCarsAtPage(int page);

	Car getCarByNumber(String number);

	boolean hasAnyCarWithBodyType(BodyType bodyType);

	boolean hasAnyCarWithBrand(Brand brand);

	boolean hasAnyCarWithLicenceCategory(LicenceCategory licenceCategory);

	boolean hasAnyCarWithEngineType(EngineType engineType);

}
