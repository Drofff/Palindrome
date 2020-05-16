package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserBlock;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.service.UserBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.ModelUtils.putUserBlockIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectTo;

@Controller
public class BaseController {

	private final UserBlockService userBlockService;

	@Autowired
	public BaseController(UserBlockService userBlockService) {
		this.userBlockService = userBlockService;
	}

	@GetMapping(HOME_ENDPOINT)
	public String getHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                          Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		if(isAuthenticated()) {
			String relativeHomeUri = getRelativeHomePageUri();
			return redirectTo(relativeHomeUri);
		}
		return "homePage";
	}

	private String getRelativeHomePageUri() {
		Role role = getCurrentUser().getRole();
		String roleSegment = role.name().toLowerCase();
		return "/" + roleSegment + RELATIVE_HOME_ENDPOINT;
	}

	@GetMapping(USER_IS_BLOCKED_ENDPOINT)
	public String getUserIsBlockedPage(Model model) {
		User currentUser = getCurrentUser();
		Optional<UserBlock> userBlockOptional = userBlockService.getUserBlockForUserIfPresent(currentUser);
		if(userBlockOptional.isPresent()) {
			putUserBlockIntoModel(userBlockOptional.get(), model);
			return "userIsBlockedPage";
		}
		return redirectTo(HOME_ENDPOINT);
	}

	@GetMapping(ERROR_ENDPOINT)
	public String getErrorPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           Model model) {
		if(isAuthenticated()) {
			model.addAttribute(USER_PARAM, getCurrentUser());
		}
		model.addAttribute(MESSAGE_PARAM, message);
		return "errorPage";
	}

}
