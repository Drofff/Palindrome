package com.drofff.palindrome.dto;

import java.util.List;

import com.drofff.palindrome.document.ViolationType;

public class RestViolationTypesDto implements RestResponseDto {

	private List<ViolationType> violationTypes;

	public RestViolationTypesDto(List<ViolationType> violationTypes) {
		this.violationTypes = violationTypes;
	}

	public List<ViolationType> getViolationTypes() {
		return violationTypes;
	}

	public void setViolationTypes(List<ViolationType> violationTypes) {
		this.violationTypes = violationTypes;
	}

}
