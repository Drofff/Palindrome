package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.Brand;

public class OwnedCarsCarDto extends CarDto {

	private String id;

	private Brand brand;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
