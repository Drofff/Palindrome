package com.drofff.palindrome.exception;

public class TwoStepAuthException extends PalindromeException {

	public TwoStepAuthException() {
		super();
	}

	public TwoStepAuthException(String message) {
		super(message);
	}

	public TwoStepAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public TwoStepAuthException(Throwable cause) {
		super(cause);
	}

	protected TwoStepAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
