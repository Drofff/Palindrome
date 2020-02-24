package com.drofff.palindrome.dto;

import java.time.LocalDateTime;

import com.drofff.palindrome.document.ViolationType;

public class HomeViolationDto {

	private String id;

	private ViolationType violationType;

	private LocalDateTime dateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ViolationType getViolationType() {
		return violationType;
	}

	public void setViolationType(ViolationType violationType) {
		this.violationType = violationType;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}
