package com.drofff.palindrome.dto;

public class ViolationDto {

	private String carNumber;

	private String location;

	private String violationTypeId;

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getViolationTypeId() {
		return violationTypeId;
	}

	public void setViolationTypeId(String violationTypeId) {
		this.violationTypeId = violationTypeId;
	}

}
