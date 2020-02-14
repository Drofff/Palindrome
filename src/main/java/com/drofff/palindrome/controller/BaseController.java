package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.ERROR_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BaseController {

	@GetMapping(HOME_ENDPOINT)
	public String getHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                          Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		return "homePage";
	}

	@GetMapping(ERROR_ENDPOINT)
	public String getErrorPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		return "errorPage";
	}

}
