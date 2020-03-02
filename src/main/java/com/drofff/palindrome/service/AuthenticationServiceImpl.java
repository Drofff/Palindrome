package com.drofff.palindrome.service;

import static com.drofff.palindrome.constants.EndpointConstants.ACTIVATE_ACCOUNT_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.PASS_RECOVERY_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_ID_PARAM;
import static com.drofff.palindrome.enums.Role.ADMIN;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.MailUtils.getActivationMailWithLinkAndUsername;
import static com.drofff.palindrome.utils.MailUtils.getCredentialsMail;
import static com.drofff.palindrome.utils.MailUtils.getRemindPasswordMailWithLink;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.ValidationUtils.validate;
import static com.drofff.palindrome.utils.ValidationUtils.validateCurrentUserHasRole;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.util.Arrays;
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
		List<Pair<String, String>> activationParams = tokenAndUserIdParams(user.getActivationToken(), user.getId());
		return uriWithQueryParams(activationEndpointUri, activationParams);
	}

	private void sendActivationLinkToUserByMail(String link, User user) {
		Mail activationMail = getActivationMailWithLinkAndUsername(link, user.getUsername());
		mailService.sendMailTo(activationMail, user.getUsername());
	}

	@Override
	public void activateUserAccountByToken(String userId, String token) {
		validateNotNull(token, "Token is required");
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

	private User getUserByUsername(String username) {
		validateNotNull(username, "Username should be provided");
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new PalindromeException("User with such username doesn't exist"));
	}

	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	private String generateRecoveryLinkForUser(String token, User user) {
		String recoveryUri = applicationUrl + PASS_RECOVERY_ENDPOINT;
		List<Pair<String, String>> recoveryParams = tokenAndUserIdParams(token, user.getId());
		return uriWithQueryParams(recoveryUri, recoveryParams);
	}

	private List<Pair<String, String>> tokenAndUserIdParams(String token, String userId) {
		Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, token);
		Pair<String, String> userIdParam = Pair.of(USER_ID_PARAM, userId);
		return Arrays.asList(tokenParam, userIdParam);
	}

	private void sendRecoveryLinkToUserByMail(String link, User user) {
		Mail remindPasswordMail = getRemindPasswordMailWithLink(link);
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

	public User getUserById(String id) {
		validateNotNull(id, "User id should be provided");
		return userRepository.findById(id)
				.orElseThrow(() -> new PalindromeException("User with such id doesn't exist"));
	}

	private void validateRecoveryVerificationTokenForUser(String token, User user) {
		validateNotNull(token, "Token is required");
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

}
