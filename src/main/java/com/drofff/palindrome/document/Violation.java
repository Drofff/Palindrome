package com.drofff.palindrome.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.repository.TicketRepository;
import com.drofff.palindrome.repository.ViolationTypeRepository;

@Document
public class Violation {

	@Id
	private String id;

	private String location;

	private LocalDateTime dateTime;

	@FromRepository(ViolationTypeRepository.class)
	private String violationTypeId;

	@FromRepository(TicketRepository.class)
	private String ticketId;

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

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
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
