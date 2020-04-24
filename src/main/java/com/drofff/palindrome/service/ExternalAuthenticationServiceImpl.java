package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.type.ExternalAuthenticationOption;

@Service
public class ExternalAuthenticationServiceImpl implements ExternalAuthenticationService {

	private final PoliceService policeService;
	private final Set<ExternalAuthenticator> externalAuthenticators;

	@Autowired
	public ExternalAuthenticationServiceImpl(PoliceService policeService, Set<ExternalAuthenticator> externalAuthenticators) {
		this.policeService = policeService;
		this.externalAuthenticators = externalAuthenticators;
	}

	@Override
	public Set<ExternalAuthenticationOption> getExternalAuthenticationOptions() {
		return externalAuthenticators.stream()
				.flatMap(this::authenticatorOptionsStream)
				.collect(toSet());
	}

	private Stream<ExternalAuthenticationOption> authenticatorOptionsStream(ExternalAuthenticator authenticator) {
		return authenticator.getAuthenticationOptions().stream();
	}

	@Override
	public String authenticateUsingOptionWithId(String optionId) {
		validateNotNull(optionId, "Option id is required");
		ExternalAuthenticator desiredAuthenticator = getAuthenticatorOfOptionWithId(optionId);
		desiredAuthenticator.authenticateUsingOptionWithId(optionId);
		Police police = getCurrentPolice();
		String accessToken = generateAccessToken();
		policeService.assignAccessTokenToPoliceWithId(accessToken, police.getId());
		return accessToken;
	}

	private Police getCurrentPolice() {
		User currentUser = getCurrentUser();
		return policeService.getPoliceByUserId(currentUser.getId());
	}

	private String generateAccessToken() {
		return randomString();
	}

	@Override
	public void completeAuthenticationWithOptionId(String optionId, String token) {
		ExternalAuthenticator authenticator = getAuthenticatorOfOptionWithId(optionId);
		authenticator.completeAuthenticationWithOptionId(optionId, token);
	}

	private ExternalAuthenticator getAuthenticatorOfOptionWithId(String optionId) {
		return externalAuthenticators.stream()
				.filter(authenticator -> authenticator.hasOptionWithId(optionId))
				.findFirst()
				.orElseThrow(() -> new ValidationException("Option with such id doesn't exist"));
	}

}
