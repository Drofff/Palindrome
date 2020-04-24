package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.ExternalAuthenticationService;
import com.drofff.palindrome.type.ExternalAuthenticationOption;

@Controller
@RequestMapping(EXTERNAL_AUTH_ENDPOINT)
public class ExternalAuthenticationController {

	private final ExternalAuthenticationService externalAuthenticationService;

	@Autowired
	public ExternalAuthenticationController(ExternalAuthenticationService externalAuthenticationService) {
		this.externalAuthenticationService = externalAuthenticationService;
	}

	@GetMapping
	public String getExternalAuthenticationOptionsPage(Model model) {
		Set<ExternalAuthenticationOption> authenticationOptions = externalAuthenticationService
				.getExternalAuthenticationOptions();
		model.addAttribute("options", authenticationOptions);
		return "externalAuthPage";
	}

	@PostMapping
	public String authenticateUsingOption(String optionId) {
		try {
			externalAuthenticationService.authenticateUsingOptionWithId(optionId);
			return redirectToWithMessage(HOME_ENDPOINT, "Successfully authenticated user");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

}
