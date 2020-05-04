package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.ExternalAuthenticationService;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static com.drofff.palindrome.constants.EndpointConstants.COMPLETE_EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;

@Controller
public class ExternalAuthenticationController {

	private static final int ACCESS_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 365; // 1 year

	private static final String EXTERNAL_AUTH_VIEW = "externalAuthPage";

	private final ExternalAuthenticationService externalAuthenticationService;

	@Autowired
	public ExternalAuthenticationController(ExternalAuthenticationService externalAuthenticationService) {
		this.externalAuthenticationService = externalAuthenticationService;
	}

	@GetMapping(EXTERNAL_AUTH_ENDPOINT)
	public String getExternalAuthenticationOptionsPage(@RequestParam(name = MESSAGE_PARAM, required = false) String message,
													   Model model) {
		Set<ExternalAuthenticationOption> authenticationOptions = externalAuthenticationService
				.getExternalAuthenticationOptions();
		model.addAttribute("options", authenticationOptions);
		model.addAttribute(MESSAGE_PARAM, message);
		return EXTERNAL_AUTH_VIEW;
	}

	@PostMapping(EXTERNAL_AUTH_ENDPOINT)
	public String authenticateUsingOption(String optionId, HttpServletResponse response, Model model) {
		try {
			String accessToken = externalAuthenticationService.authenticateUsingOptionWithId(optionId);
			addAccessTokenToCookies(accessToken, response);
			model.addAttribute(SUCCESS_PARAM, true);
			return EXTERNAL_AUTH_VIEW;
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	private void addAccessTokenToCookies(String accessToken, HttpServletResponse response) {
		Cookie cookie = new Cookie(ACCESS_TOKEN_PARAM, accessToken);
		cookie.setPath("/");
		cookie.setMaxAge(ACCESS_TOKEN_COOKIE_MAX_AGE);
		response.addCookie(cookie);
	}

	@GetMapping(COMPLETE_EXTERNAL_AUTH_ENDPOINT)
	public String completeExternalAuth(String optionId, String token) {
		try {
			externalAuthenticationService.completeAuthenticationWithOptionId(optionId, token);
			return "externalAuthCompletedPage";
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

}
