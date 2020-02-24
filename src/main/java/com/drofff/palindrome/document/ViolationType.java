package com.drofff.palindrome.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ViolationType {

	@Id
	private String id;

	private String name;

	private Float feeAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Float feeAmount) {
		this.feeAmount = feeAmount;
	}

}
