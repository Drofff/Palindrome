package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.document.EngineType;
import com.drofff.palindrome.document.LicenceCategory;

public class CarFatDto extends CarDto {

	private String id;

	private Brand brand;

	private BodyType bodyType;

	private LicenceCategory licenceCategory;

	private EngineType engineType;

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

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public LicenceCategory getLicenceCategory() {
		return licenceCategory;
	}

	public void setLicenceCategory(LicenceCategory licenceCategory) {
		this.licenceCategory = licenceCategory;
	}

	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

}
