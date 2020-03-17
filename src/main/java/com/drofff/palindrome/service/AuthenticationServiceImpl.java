package com.drofff.palindrome.service;

import static com.drofff.palindrome.constants.EndpointConstants.ACTIVATE_ACCOUNT_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.CONFIRM_PASS_CHANGE_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.PASS_RECOVERY_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_ID_PARAM;
import static com.drofff.palindrome.enums.Role.ADMIN;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.MailUtils.getActivationMail;
import static com.drofff.palindrome.utils.MailUtils.getCredentialsMail;
import static com.drofff.palindrome.utils.MailUtils.getPasswordChangeConfirmationMail;
import static com.drofff.palindrome.utils.MailUtils.getRemindPasswordMail;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateCurrentUserHasRole;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.cache.PasswordCache;
import com.drofff.palindrome.cache.TokenCache;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserRepository;
import com.drofff.palindrome.type.Mail;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final int ALL_USERS_PAGE_SIZE = 12;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;

	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
									 MailService mailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
	}

	@Override
	public void registerDriverAccount(User user) {
		validate(user);
		validateHasUniqueUsername(user);
		initDefaultDriverParams(user);
		encodeUserPassword(user);
		user.setActivationToken(generateToken());
		userRepository.save(user);
		String activationLink = generateActivationLinkForUser(user);
		sendActivationLinkToUserByMail(activationLink, user);
	}

	private void initDefaultDriverParams(User user) {
		user.setActive(Boolean.FALSE);
		user.setRole(Role.DRIVER);
	}

	private String generateActivationLinkForUser(User user) {
		String activationEndpointUri = applicationUrl + ACTIVATE_ACCOUNT_ENDPOINT;
		return tokenAndUserIdToEndpoint(user.getActivationToken(), user.getId(), activationEndpointUri);
	}

	private void sendActivationLinkToUserByMail(String link, User user) {
		Mail activationMail = getActivationMail(link, user.getUsername());
		mailService.sendMailTo(activationMail, user.getUsername());
	}

	@Override
	public void activateUserAccountByToken(String userId, String token) {
		validateToken(token);
		User user = getUserById(userId);
		validateUserIsNotActive(user);
		validateTokenForUser(token, user);
		user.setActive(true);
		userRepository.save(user);
	}

	private void validateUserIsNotActive(User user) {
		if(user.isEnabled()) {
			throw new PalindromeException("User account is already active");
		}
	}

	private void validateTokenForUser(String token, User user) {
		String originalToken = user.getActivationToken();
		if(areNotEqual(token, originalToken)) {
			throw new PalindromeException("Invalid activation token");
		}
	}

	@Override
	public void remindPasswordToUserWithEmail(String email) {
		User user = getUserByUsername(email);
		String token = generateToken();
		TokenCache.saveTokenForUser(token, user);
		String recoveryLink = generateRecoveryLinkForUser(token, user);
		sendRecoveryLinkToUserByMail(recoveryLink, user);
	}

	private String generateRecoveryLinkForUser(String token, User user) {
		String recoveryUri = applicationUrl + PASS_RECOVERY_ENDPOINT;
		return tokenAndUserIdToEndpoint(token, user.getId(), recoveryUri);
	}

	private String tokenAndUserIdToEndpoint(String token, String userId, String uri) {
		List<Pair<String, String>> params = tokenAndUserIdParams(token, userId);
		return uriWithQueryParams(uri, params);
	}

	private List<Pair<String, String>> tokenAndUserIdParams(String token, String userId) {
		Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, token);
		Pair<String, String> userIdParam = Pair.of(USER_ID_PARAM, userId);
		return Arrays.asList(tokenParam, userIdParam);
	}

	private void sendRecoveryLinkToUserByMail(String link, User user) {
		Mail remindPasswordMail = getRemindPasswordMail(link);
		mailService.sendMailTo(remindPasswordMail, user.getUsername());
	}

	@Override
	public void verifyRecoveryAttemptForUserWithToken(String userId, String token) {
		User user = getUserById(userId);
		validateRecoveryVerificationTokenForUser(token, user);
	}

	@Override
	public void recoverPasswordForUserWithToken(String userId, String token, String newPassword) {
		User user = getUserById(userId);
		validateRecoveryVerificationTokenForUser(token, user);
		user.setPassword(newPassword);
		validate(user);
		encodeUserPassword(user);
		userRepository.save(user);
		TokenCache.removeTokenForUser(user);
	}

	@Override
	public User getUserById(String id) {
		validateNotNull(id, "User id should be provided");
		return userRepository.findById(id)
				.orElseThrow(() -> new PalindromeException("User with such id doesn't exist"));
	}

	private void validateRecoveryVerificationTokenForUser(String token, User user) {
		validateToken(token);
		String originalToken = getVerificationTokenForUser(user);
		if(areNotEqual(originalToken, token)) {
			throw new PalindromeException("Invalid verification token");
		}
	}

	private String getVerificationTokenForUser(User user) {
		return TokenCache.getTokenForUser(user)
				.orElseThrow(() -> new PalindromeException("No recovery request detected for user"));
	}

	@Override
	public void changeUserPassword(String password, String newPassword) {
		User currentUser = getCurrentUser();
		validateUserHasPassword(currentUser, password);
		currentUser.setPassword(newPassword);
		validate(currentUser);
		encodeUserPassword(currentUser);
		userRepository.save(currentUser);
	}

	private void validateUserHasPassword(User user, String password) {
		if(hasNotPassword(user, password)) {
			throw new ValidationException("Invalid password");
		}
	}

	@Override
	public void changeUserPasswordByMail(String newPassword) {
		User currentUser = getCurrentUser();
		currentUser.setPassword(newPassword);
		validate(currentUser);
		encodeUserPassword(currentUser);
		PasswordCache.savePasswordForUser(currentUser.getPassword(), currentUser);
		String confirmationToken = generateToken();
		TokenCache.saveTokenForUser(confirmationToken, currentUser);
		String confirmationLink = generatePasswordChangeConfirmationLinkWithToken(confirmationToken);
		sendPasswordChangeConfirmationLinkByMail(confirmationLink, currentUser.getUsername());
	}

	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	private String generatePasswordChangeConfirmationLinkWithToken(String confirmationToken) {
		String uri = applicationUrl + CONFIRM_PASS_CHANGE_ENDPOINT;
		Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, confirmationToken);
		return uriWithQueryParams(uri, Collections.singletonList(tokenParam));
	}

	private void sendPasswordChangeConfirmationLinkByMail(String link, String username) {
		Mail confirmationMail = getPasswordChangeConfirmationMail(link);
		mailService.sendMailTo(confirmationMail, username);
	}

	@Override
	public void confirmUserPasswordChangeByToken(String token) {
		User currentUser = getCurrentUser();
		String originalToken = TokenCache.getTokenForUser(currentUser)
				.orElseThrow(() -> new ValidationException("No active change requests are present for user"));
		validatePasswordChangeConfirmationToken(originalToken, token);
		TokenCache.removeTokenForUser(currentUser);
		String newPassword = PasswordCache.popPasswordForUser(currentUser);
		currentUser.setPassword(newPassword);
		userRepository.save(currentUser);
	}

	private void validatePasswordChangeConfirmationToken(String originalToken, String token) {
		validateToken(token);
		if(areNotEqual(originalToken, token)) {
			throw new ValidationException("Invalid confirmation token");
		}
	}

	private void validateToken(String token) {
		validateNotNull(token, "Token is required");
	}

	@Override
	public Page<User> getAllUsersAtPage(int page) {
		Pageable pageable = PageRequest.of(page, ALL_USERS_PAGE_SIZE);
		return userRepository.findAll(pageable);
	}

	@Override
	public long countUsers() {
		return userRepository.count();
	}

	@Override
	public List<Role> getAllRoles() {
		Role[] roles = Role.values();
		return Arrays.asList(roles);
	}

	@Override
	public void createUser(User user) {
		validateCurrentUserHasRole(ADMIN);
		validateHasUniqueUsername(user);
		validateHasRole(user);
		user.setActive(Boolean.TRUE);
		String generatedPassword = generatePassword();
		user.setPassword(generatedPassword);
		encodeUserPassword(user);
		validate(user);
		userRepository.save(user);
		sendCredentialsByMail(user.getUsername(), generatedPassword);
	}

	private void validateHasUniqueUsername(User user) {
		if(existsUserWithUsername(user.getUsername())) {
			throw new ValidationException("User with such username already exists");
		}
	}

	private boolean existsUserWithUsername(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	private void validateHasRole(User user) {
		if(hasNoRole(user)) {
			throw new ValidationException("User role is required");
		}
	}

	private boolean hasNoRole(User user) {
		return Objects.isNull(user.getRole());
	}

	private String generatePassword() {
		return UUID.randomUUID().toString();
	}

	private void encodeUserPassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	private void sendCredentialsByMail(String username, String password) {
		Mail credentialsMail = getCredentialsMail(username, password);
		mailService.sendMailTo(credentialsMail, username);
	}

	@Override
	public User authenticateUser(String username, String password) {
		validateUserCredentials(username, password);
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new PalindromeException("Inconsistent user info"));
	}

	private void validateUserCredentials(String username, String password) {
		if(areInvalidCredentials(username, password)) {
			throw new ValidationException("Invalid credentials");
		}
	}

	private boolean areInvalidCredentials(String username, String password) {
		User user = getUserByUsername(username);
		return hasNotPassword(user, password);
	}

	private User getUserByUsername(String username) {
		validateNotNull(username, "Username should be provided");
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ValidationException("User with such username doesn't exist"));
	}

	private boolean hasNotPassword(User user, String password) {
		return !hasPassword(user, password);
	}

	private boolean hasPassword(User user, String password) {
		String originalPassword = user.getPassword();
		return passwordEncoder.matches(password, originalPassword);
	}

}