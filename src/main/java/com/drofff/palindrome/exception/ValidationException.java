package com.drofff.palindrome.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private final Map<String, String> fieldErrors;

	public ValidationException(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public ValidationException(String message) {
		super(message);
		this.fieldErrors = new HashMap<>();
	}

	public ValidationException(String message, Map<String, String> fieldErrors) {
		super(message);
		this.fieldErrors = fieldErrors;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

}
