package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Car;

public interface CarService {

	void addCar(Car car);

	void updateCar(Car car);

	void deleteCarById(String id);

	Car getById(String id);

}
