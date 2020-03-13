package com.drofff.palindrome.filter;

import com.drofff.palindrome.annotation.Filter;
import com.drofff.palindrome.document.Violation;

@Filter(forClass = Violation.class)
public class ViolationFilter {

	private String carId;

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

}
