package com.drofff.palindrome.controller.advice;

import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.drofff.palindrome.constants.EndpointConstants.EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@ControllerAdvice(basePackages = "com.drofff.palindrome.controller.mvc")
public class MvcExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(MvcExceptionHandler.class);

	@ExceptionHandler
	public String twoStepAuthExceptionHandler(TwoStepAuthException e) {
		return redirectToWithMessage(EXTERNAL_AUTH_ENDPOINT, e.getMessage());
	}

	@ExceptionHandler
	public String validationExceptionHandler(ValidationException e) {
		return errorPageWithMessage(e.getMessage());
	}

	@ExceptionHandler
	public String palindromeExceptionHandler(PalindromeException e) {
		LOG.error(e.getMessage());
		return errorPageWithMessage("Some error has occurred.. Don't worry, our team will fix it in a moment!");
	}

}