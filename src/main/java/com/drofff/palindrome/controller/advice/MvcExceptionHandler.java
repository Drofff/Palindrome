package com.drofff.palindrome.controller.advice;

import com.drofff.palindrome.exception.TwoStepAuthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.drofff.palindrome.constants.EndpointConstants.EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@ControllerAdvice(basePackages = "com.drofff.palindrome.controller.mvc")
public class MvcExceptionHandler {

	@ExceptionHandler
	public String twoStepAuthExceptionHandler(TwoStepAuthException e) {
		return redirectToWithMessage(EXTERNAL_AUTH_ENDPOINT, e.getMessage());
	}

}
