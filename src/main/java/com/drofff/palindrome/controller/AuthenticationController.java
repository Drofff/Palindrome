package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.ACTIVATE_ACCOUNT_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.FORGOT_PASS_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.LOGIN_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.PASS_RECOVERY_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.REGISTRATION_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.ERROR_MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_ID_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.UserDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;

@Controller
public class AuthenticationController {

	private static final String INVALID_CREDENTIALS = "error";

	private static final String LOGIN_VIEW = "loginPage";
	private static final String REGISTRATION_VIEW = "registrationPage";
	private static final String FORGOT_PASS_VIEW = "forgotPasswordPage";
	private static final String PASS_RECOVERY_VIEW = "passwordRecoveryPage";

	private final AuthenticationService authenticationService;
	private final UserDtoMapper userDtoMapper;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService, UserDtoMapper userDtoMapper) {
		this.authenticationService = authenticationService;
		this.userDtoMapper = userDtoMapper;
	}

	@GetMapping(LOGIN_ENDPOINT)
	public String getLoginPage(HttpServletRequest request, Model model) {
		if(hasInvalidCredentialsMessage(request)) {
			model.addAttribute(ERROR_MESSAGE_PARAM, Boolean.TRUE);
		}
		return LOGIN_VIEW;
	}

	private boolean hasInvalidCredentialsMessage(HttpServletRequest request) {
		String queryStr = request.getQueryString();
		return INVALID_CREDENTIALS.equals(queryStr);
	}

	@GetMapping(REGISTRATION_ENDPOINT)
	public String getRegistrationPage() {
		return REGISTRATION_VIEW;
	}

	@PostMapping(REGISTRATION_ENDPOINT)
	public String registerUser(UserDto userDto, Model model) {
		User user = userDtoMapper.toEntity(userDto);
		try {
			authenticationService.registerDriverAccount(user);
			return "activateAccountPage";
		} catch(ValidationException e) {
			model.addAttribute(USER_PARAM, userDto);
			putValidationExceptionIntoModel(e, model);
			return REGISTRATION_VIEW;
		}
	}

	@GetMapping(ACTIVATE_ACCOUNT_ENDPOINT)
	public String activateAccount(String token, String userId, Model model) {
		try {
			authenticationService.activateUserAccountByToken(userId, token);
			return redirectToWithMessage(HOME_ENDPOINT, "Successfully activated account");
		} catch(PalindromeException e) {
			model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
			return errorPageWithMessage(e.getMessage());
		}
	}

	@GetMapping(FORGOT_PASS_ENDPOINT)
	public String getForgotPasswordPage() {
		return FORGOT_PASS_VIEW;
	}

	@PostMapping(FORGOT_PASS_ENDPOINT)
	public String remindPassword(String email, Model model) {
		try {
			authenticationService.remindPasswordToUserWithEmail(email);
			return "recoveryStartedPage";
		} catch(PalindromeException e) {
			model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
			return FORGOT_PASS_VIEW;
		}
	}

	@GetMapping(PASS_RECOVERY_ENDPOINT)
	public String verifyPasswordRecovery(String token, String userId, Model model) {
		try {
			authenticationService.verifyRecoveryAttemptForUserWithToken(userId, token);
			model.addAttribute(TOKEN_PARAM, token);
			model.addAttribute(USER_ID_PARAM, userId);
			return PASS_RECOVERY_VIEW;
		} catch(PalindromeException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@PostMapping(PASS_RECOVERY_ENDPOINT)
	public String recoverPassword(String userId, String token, String password, Model model) {
		try {
			authenticationService.recoverPasswordForUserWithToken(userId, token, password);
			return redirectToWithMessage(HOME_ENDPOINT, "Successfully changed password");
		} catch(ValidationException e) {
			model.addAttribute(TOKEN_PARAM, token);
			model.addAttribute(USER_ID_PARAM, userId);
			putValidationExceptionIntoModel(e, model);
			return PASS_RECOVERY_VIEW;
		} catch(PalindromeException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

}
