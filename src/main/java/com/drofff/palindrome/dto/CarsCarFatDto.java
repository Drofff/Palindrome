package com.drofff.palindrome.dto;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.document.EngineType;
import com.drofff.palindrome.document.LicenceCategory;

public class CarsCarFatDto {

	private String id;

	private String number;

	private String bodyNumber;

	private String brandId;

	private String model;

	private String bodyTypeId;

	private String color;

	private Float weight;

	private String licenceCategoryId;

	private Float engineVolume;

	private String engineTypeId;

	private String registrationDate;

	private Brand brand;

	private BodyType bodyType;

	private LicenceCategory licenceCategory;

	private EngineType engineType;

	private String ownerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getLicenceCategoryId() {
		return licenceCategoryId;
	}

	public void setLicenceCategoryId(String licenceCategoryId) {
		this.licenceCategoryId = licenceCategoryId;
	}

	public Float getEngineVolume() {
		return engineVolume;
	}

	public void setEngineVolume(Float engineVolume) {
		this.engineVolume = engineVolume;
	}

	public String getEngineTypeId() {
		return engineTypeId;
	}

	public void setEngineTypeId(String engineTypeId) {
		this.engineTypeId = engineTypeId;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
