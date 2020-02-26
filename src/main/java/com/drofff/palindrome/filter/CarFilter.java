package com.drofff.palindrome.filter;

import com.drofff.palindrome.annotation.Filter;
import com.drofff.palindrome.annotation.Strategy;
import com.drofff.palindrome.dto.CarsCarFatDto;

@Filter(forClass = CarsCarFatDto.class)
public class CarFilter {

	@Strategy(StartsWithComparisonStrategy.class)
	private String number;

	@Strategy(StartsWithComparisonStrategy.class)
	private String bodyNumber;

	private String brandId;

	@Strategy(StartsWithComparisonStrategy.class)
	private String model;

	private String bodyTypeId;

	private String color;

	private String licenceCategoryId;

	private String engineTypeId;

	private String ownerId;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBodyNumber() {
		return bodyNumber;
	}

	public void setBodyNumber(String bodyNumber) {
		this.bodyNumber = bodyNumber;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBodyTypeId() {
		return bodyTypeId;
	}

	public void setBodyTypeId(String bodyTypeId) {
		this.bodyTypeId = bodyTypeId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLicenceCategoryId() {
		return licenceCategoryId;
	}

	public void setLicenceCategoryId(String licenceCategoryId) {
		this.licenceCategoryId = licenceCategoryId;
	}

	public String getEngineTypeId() {
		return engineTypeId;
	}

	public void setEngineTypeId(String engineTypeId) {
		this.engineTypeId = engineTypeId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
