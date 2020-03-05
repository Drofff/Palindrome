package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.drofff.palindrome.annotation.FromRepository;
import com.drofff.palindrome.repository.ViolationRepository;

@Document
public class Ticket {

	@Id
	private String id;

	@FromRepository(ViolationRepository.class)
	private String violationId;

	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getViolationId() {
		return violationId;
	}

	public void setViolationId(String violationId) {
		this.violationId = violationId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
