package com.drofff.palindrome.dto;

import java.time.LocalDateTime;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.ViolationType;

public class ViolationFatDto {

	private String id;

	private String location;

	private LocalDateTime dateTime;

	private ViolationType violationType;

	private boolean paid;

	private Car car;

	private Police officer;

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

	public ViolationType getViolationType() {
		return violationType;
	}

	public void setViolationType(ViolationType violationType) {
		this.violationType = violationType;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Police getOfficer() {
		return officer;
	}

	public void setOfficer(Police officer) {
		this.officer = officer;
	}

}
