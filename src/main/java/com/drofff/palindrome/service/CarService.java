package com.drofff.palindrome.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;

public interface CarService {

	void addCar(Car car);

	void updateCar(Car car);

	void deleteCarById(String id);

	List<Car> getCarsOfDriver(Driver driver);

	Car getById(String id);

	Page<Car> getAllCarsAtPage(int page);

}
