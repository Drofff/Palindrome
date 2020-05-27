package com.drofff.palindrome.service;

import com.drofff.palindrome.document.ActivationToken;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.ActivationTokenRepository;
import com.drofff.palindrome.type.Mail;
import com.drofff.palindrome.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.drofff.palindrome.cache.TokenCache.*;
import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.MessageConstants.INVALID_TOKEN_MESSAGE;
import static com.drofff.palindrome.document.User.MIN_PASSWORD_LENGTH;
import static com.drofff.palindrome.enums.Role.DRIVER;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.MailUtils.getActivationMail;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.*;
import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String TOKEN_IS_NULL_MESSAGE = "Token is required";

	private final ActivationTokenRepository activationTokenRepository;
	private final UserService userService;
	private final MailService mailService;

	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	public AuthenticationServiceImpl(ActivationTokenRepository activationTokenRepository, UserService userService,
									 MailService mailService) {
		this.activationTokenRepository = activationTokenRepository;
		this.userService = userService;
		this.mailService = mailService;
	}

	@Override
	public void registerDriverAccount(User user) {
		initDefaultDriverParams(user);
		userService.createUser(user);
		ActivationToken activationToken = generateActivationTokenForUser(user);
		String activationLink = activationLinkWithTokenForUser(activationToken, user);
		sendActivationLinkToUserByMail(activationLink, user);
	}

	private void initDefaultDriverParams(User user) {
		user.setActive(FALSE);
		user.setRole(DRIVER);
	}

	private ActivationToken generateActivationTokenForUser(User user) {
		ActivationToken activationToken = ActivationToken.forUser(user);
		return activationTokenRepository.save(activationToken);
	}

	private String activationLinkWithTokenForUser(ActivationToken activationToken, User user) {
		String activationEndpointUrl = applicationUrl + ACTIVATE_ACCOUNT_ENDPOINT;
		return tokenAndUserIdToEndpoint(activationToken.getValue(), user.getId(), activationEndpointUrl);
	}

	private void sendActivationLinkToUserByMail(String link, User user) {
		Mail activationMail = getActivationMail(link, user.getUsername());
		mailService.sendMailTo(activationMail, user.getUsername());
	}

	@Override
	public void activateUserAccountByToken(String userId, String token) {
		validateNotNull(token, "Activation token should not be null");
		User user = userService.getUserById(userId);
		validateUserIsNotActive(user);
		validateActivationTokenBelongsToUserWithId(token, userId);
		userService.markUserWithIdAsActive(userId);
		disposeActivationToken(token);
	}

	private void validateUserIsNotActive(User user) {
		if(user.isEnabled()) {
			throw new PalindromeException("User account is already active");
		}
	}

	private void validateActivationTokenBelongsToUserWithId(String token, String userId) {
		Optional<ActivationToken> activationTokenOptional = activationTokenRepository.findByValueAndUserId(token, userId);
		if(!activationTokenOptional.isPresent()) {
			throw new ValidationException("Invalid activation token");
		}
	}

	private void disposeActivationToken(String token) {
		activationTokenRepository.deleteByValue(token);
	}

	@Override
	public void requestPasswordRecovery(String email) {
		User user = userService.getUserByUsername(email);
		String token = randomString();
		sendPasswordRecoveryMailWithTokenToUser(token, user);
		doOnTokenReceive(token, (userId, params) -> {
			validateAreEqual(user.getId(), userId, INVALID_TOKEN_MESSAGE);
			String newPassword = params[0];
			userService.updatePasswordForUserWithId(newPassword, userId);
			removeTokenListener(token);
		});
	}

	private void sendPasswordRecoveryMailWithTokenToUser(String token, User user) {
		String recoveryLink = passwordRecoveryLinkOf(token, user.getId());
		Mail recoveryMail = MailUtils.getRemindPasswordMail(recoveryLink);
		mailService.sendMailTo(recoveryMail, user.getUsername());
	}

	private String passwordRecoveryLinkOf(String token, String userId) {
		String passwordRecoveryEndpoint = applicationUrl + PASS_RECOVERY_ENDPOINT;
		return tokenAndUserIdToEndpoint(token, userId, passwordRecoveryEndpoint);
	}

	@Override
	public void completePasswordRecoveryOfUserWithIdUsingToken(String userId, String token, String newPassword) {
		validateNotNull(userId, "User id is required");
		validateNotNull(token, TOKEN_IS_NULL_MESSAGE);
		validatePassword(newPassword);
		onReceive(token, userId, newPassword);
	}

	@Override
	public void requestPasswordChange(String newPassword) {
		validatePassword(newPassword);
		User currentUser = getCurrentUser();
		String token = randomString();
		sendConfirmPasswordChangeMailWithTokenToUser(token, currentUser);
		doOnTokenReceive(token, (userId, params) -> {
			validateAreEqual(currentUser.getId(), userId, INVALID_TOKEN_MESSAGE);
			userService.updatePasswordForUserWithId(newPassword, userId);
			removeTokenListener(token);
		});
	}

	private void sendConfirmPasswordChangeMailWithTokenToUser(String token, User user) {
		String confirmPasswordChangeLink = confirmPasswordChangeLinkOf(token, user.getId());
		Mail confirmPasswordMail = MailUtils.getPasswordChangeConfirmationMail(confirmPasswordChangeLink);
		mailService.sendMailTo(confirmPasswordMail, user.getUsername());
	}

	private String confirmPasswordChangeLinkOf(String token, String userId) {
		String confirmPasswordChangeUrl = applicationUrl + CONFIRM_PASS_CHANGE_ENDPOINT;
		return tokenAndUserIdToEndpoint(token, userId, confirmPasswordChangeUrl);
	}

	private String tokenAndUserIdToEndpoint(String token, String userId, String url) {
		Pair<String, String> tokenParam = Pair.of("token", token);
		Pair<String, String> userIdParam = Pair.of("userId", userId);
		return uriWithQueryParams(url, asList(tokenParam, userIdParam));
	}

	@Override
	public void confirmPasswordChangeUsingToken(String token) {
		validateNotNull(token, TOKEN_IS_NULL_MESSAGE);
		String userId = getCurrentUser().getId();
		onReceive(token, userId);
	}

	@Override
	public void changePasswordUsingOldPassword(String password, String newPassword) {
		validateNotNull(password, "Current password should be provided");
		validatePassword(newPassword);
		User currentUser = getCurrentUser();
		userService.validateIsPasswordOfUser(password, currentUser);
		userService.updatePasswordForUserWithId(newPassword, currentUser.getId());
	}

	private void validatePassword(String password) {
		validateNotNull(password, "Password is required");
		if(password.length() < MIN_PASSWORD_LENGTH) {
			throw new ValidationException("Minimal password length is " + MIN_PASSWORD_LENGTH);
		}
	}

}