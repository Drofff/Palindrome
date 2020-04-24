package com.drofff.palindrome.controller.advice;

import static com.drofff.palindrome.constants.EndpointConstants.EXTERNAL_AUTH_ENDPOINT;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.utils.ModelUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public String twoStepAuthExceptionHandler(TwoStepAuthException e) {
		return ModelUtils.redirectToWithMessage(EXTERNAL_AUTH_ENDPOINT, e.getMessage());
	}

}
