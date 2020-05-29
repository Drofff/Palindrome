package com.drofff.palindrome.document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.repository.CarRepository;
import com.drofff.palindrome.repository.PoliceRepository;
import com.drofff.palindrome.repository.ViolationTypeRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Document
public class Violation implements Entity, Hronable {

	@Id
	private String id;

	@NotBlank(message = "Location should be provided")
	private String location;

	private LocalDateTime dateTime;

	@NotBlank(message = "Violation type is required")
	@FromRepository(ViolationTypeRepository.class)
	private String violationTypeId;

	private boolean paid;

	@NotBlank(message = "Car should be specified")
	@FromRepository(CarRepository.class)
	private String carId;

	@NotBlank(message = "Violation is required")
	private String violatorId;

	@FromRepository(PoliceRepository.class)
	private String officerId;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
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

	public boolean isUnpaid() {
		return !paid;
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
