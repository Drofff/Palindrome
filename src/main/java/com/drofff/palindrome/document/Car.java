package com.drofff.palindrome.document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.repository.BodyTypeRepository;
import com.drofff.palindrome.repository.BrandRepository;
import com.drofff.palindrome.repository.EngineTypeRepository;
import com.drofff.palindrome.repository.LicenceCategoryRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Document
public class Car implements Entity {

	@Id
	private String id;

	@NotBlank(message = "Car number is required")
	@Pattern(regexp = "([a-zA-Z]{2})(\\d{4})([a-zA-Z]{2})", message = "Car number is of a wrong format")
	private String number;

	@NotBlank(message = "Body number is required")
	private String bodyNumber;

	@NotBlank(message = "Brand should be provided")
	@FromRepository(BrandRepository.class)
	private String brandId;

	@NotBlank(message = "Model should be provided")
	private String model;

	@NotBlank(message = "Body type should be specified")
	@FromRepository(BodyTypeRepository.class)
	private String bodyTypeId;

	@NotBlank(message = "Color is required")
	private String color;

	@NotNull(message = "Weight is required")
	@Min(value = 0, message = "0 is a minimal possible weight")
	private Float weight;

	@NotBlank(message = "Select licence category of your car")
	@FromRepository(LicenceCategoryRepository.class)
	private String licenceCategoryId;

	@NotNull(message = "Engine volume should be provided")
	@Min(value = 0, message = "Minimal engine volume is 0")
	private Float engineVolume;

	@NotBlank(message = "Engine type is required")
	@FromRepository(EngineTypeRepository.class)
	private String engineTypeId;

	@NotNull(message = "Registration date is required")
	@PastOrPresent(message = "Please, provide valid registration date")
	private LocalDate registrationDate;

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

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

}
