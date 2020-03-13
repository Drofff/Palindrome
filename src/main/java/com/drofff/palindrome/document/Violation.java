package com.drofff.palindrome.document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.repository.CarRepository;
import com.drofff.palindrome.repository.ViolationTypeRepository;

@Document
public class Violation implements Entity {

	@Id
	private String id;

	@NotNull(message = "Location should be provided")
	@NotBlank(message = "Location should not be blank")
	private String location;

	private LocalDateTime dateTime;

	@NotNull(message = "Violation type is required")
	@FromRepository(ViolationTypeRepository.class)
	private String violationTypeId;

	private boolean paid;

	@NotNull(message = "Car should be specified")
	@FromRepository(CarRepository.class)
	private String carId;

	@NotNull(message = "Violation is required")
	private String violatorId;

	private String officerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getViolationTypeId() {
		return violationTypeId;
	}

	public void setViolationTypeId(String violationTypeId) {
		this.violationTypeId = violationTypeId;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getViolatorId() {
		return violatorId;
	}

	public void setViolatorId(String violatorId) {
		this.violatorId = violatorId;
	}

	public String getOfficerId() {
		return officerId;
	}

	public void setOfficerId(String officerId) {
		this.officerId = officerId;
	}

}
