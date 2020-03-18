package com.drofff.palindrome.grep.pattern;

import com.drofff.palindrome.annotation.Pattern;
import com.drofff.palindrome.document.Violation;

@Pattern(forClass = Violation.class)
public class ViolationPattern {

	private String carId;

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

}
