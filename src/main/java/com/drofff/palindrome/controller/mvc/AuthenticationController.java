package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.ChangePasswordDto;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.UserDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
public class AuthenticationController {

	private static final String INVALID_CREDENTIALS = "error";

	private static final String LOGIN_VIEW = "loginPage";
	private static final String REGISTRATION_VIEW = "registrationPage";
	private static final String FORGOT_PASS_VIEW = "forgotPasswordPage";
	private static final String PASS_RECOVERY_VIEW = "passwordRecoveryPage";
	private static final String CHANGE_PASS_VIEW = "changePasswordPage";

	private static final String CHANGE_PASS_ENDPOINT = "/change-password";

	private static final String ROLE_PARAM = "role";

	private static final String PASS_CHANGED_MESSAGE = "Password has been successfully changed";

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
	public String activateAccount(String token, String userId) {
		authenticationService.activateUserAccountByToken(userId, token);
		return redirectToWithMessage(HOME_ENDPOINT, "Successfully activated account");
	}

	@GetMapping(FORGOT_PASS_ENDPOINT)
	public String getForgotPasswordPage() {
		return FORGOT_PASS_VIEW;
	}

	@PostMapping(FORGOT_PASS_ENDPOINT)
	public String remindPassword(String email, Model model) {
		try {
			authenticationService.requestPasswordRecovery(email);
			return "recoveryStartedPage";
		} catch(PalindromeException e) {
			model.addAttribute(ERROR_MESSAGE_PARAM, e.getMessage());
			return FORGOT_PASS_VIEW;
		}
	}

	@GetMapping(PASS_RECOVERY_ENDPOINT)
	public String verifyPasswordRecovery(String token, String userId, Model model) {
		model.addAttribute(TOKEN_PARAM, token);
		model.addAttribute(USER_ID_PARAM, userId);
		return PASS_RECOVERY_VIEW;
	}

	@PostMapping(PASS_RECOVERY_ENDPOINT)
	public String recoverPassword(String userId, String token, String password, Model model) {
		try {
			authenticationService.completePasswordRecoveryOfUserWithIdUsingToken(userId, token, password);
			return redirectToWithMessage(HOME_ENDPOINT, "Successfully changed password");
		} catch(ValidationException e) {
			model.addAttribute(TOKEN_PARAM, token);
			model.addAttribute(USER_ID_PARAM, userId);
			putValidationExceptionIntoModel(e, model);
			return PASS_RECOVERY_VIEW;
		}
	}

	@GetMapping(CHANGE_PASS_ENDPOINT)
	public String getChangePasswordPage(Model model) {
		model.addAttribute(ROLE_PARAM, getCurrentUser().getRole());
		return CHANGE_PASS_VIEW;
	}

	@PostMapping(CHANGE_PASS_ENDPOINT)
	public String changePassword(ChangePasswordDto changePasswordDto, Model model) {
		try {
			if(changePasswordDto.isByMail()) {
				authenticationService.requestPasswordChange(changePasswordDto.getNewPassword());
				model.addAttribute(MESSAGE_PARAM, "Confirmation token has been sent to you by mail");
			} else {
				authenticationService.changePasswordUsingOldPassword(changePasswordDto.getPassword(), changePasswordDto.getNewPassword());
				model.addAttribute(MESSAGE_PARAM, PASS_CHANGED_MESSAGE);
			}
		} catch(ValidationException e) {
			model.addAttribute("passwords", changePasswordDto);
			putValidationExceptionIntoModel(e, model);
		}
		model.addAttribute(ROLE_PARAM, getCurrentUser().getRole());
		return CHANGE_PASS_VIEW;
	}

	@GetMapping(CONFIRM_PASS_CHANGE_ENDPOINT)
	public String confirmPasswordChange(String token) {
		authenticationService.confirmPasswordChangeUsingToken(token);
		return redirectToWithMessage(HOME_ENDPOINT, PASS_CHANGED_MESSAGE);
	}

}
