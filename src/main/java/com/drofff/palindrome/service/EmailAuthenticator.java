package com.drofff.palindrome.service;

import static com.drofff.palindrome.constants.EndpointConstants.COMPLETE_EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.OPTION_ID_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.MailUtils.getTwoStepAuthMail;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;
import static com.drofff.palindrome.utils.StringUtils.randomString;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import com.drofff.palindrome.type.Mail;

@Service
public class EmailAuthenticator implements ExternalAuthenticator {

	private static final Logger LOG = LoggerFactory.getLogger(EmailAuthenticator.class);

	private static final Map<String, String> TOKENS_BUFFER = new HashMap<>();

	private static final long MAX_TOKEN_WAIT_TIME_MILLIS = 3 * 60 * 1000L; // 3 minutes

	private static final String EMAIL_OPTION_ID = "email_option";

	private final PoliceService policeService;
	private final MailService mailService;

	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	public EmailAuthenticator(PoliceService policeService, MailService mailService) {
		this.policeService = policeService;
		this.mailService = mailService;
	}

	@Override
	public Set<ExternalAuthenticationOption> getAuthenticationOptions() {
		User currentUser = getCurrentUser();
		return new ExternalAuthenticationOption.Builder()
				.usingEmail()
				.withId(EMAIL_OPTION_ID)
				.withLabel(currentUser.getUsername())
				.singleton();
	}

	@Override
	public void authenticateUsingOptionWithId(String optionId) {
		validateIsEmailOptionId(optionId);
		User currentUser = getCurrentUser();
		removeUserTokenFromBuffer(currentUser);
		String token = randomString();
		sendTokenToUserViaMail(token, currentUser);
		waitTillUserAuthComplete(currentUser);
		validateUserAuthCompleted(currentUser);
		String receivedToken = getTokenReceivedForUser(currentUser);
		validateToken(receivedToken, token);
	}

	private void removeUserTokenFromBuffer(User user) {
		synchronized(TOKENS_BUFFER) {
			TOKENS_BUFFER.remove(user.getId());
		}
	}

	private void sendTokenToUserViaMail(String token, User user) {
		Police police = policeService.getPoliceByUserId(user.getId());
		String completeAuthLink = generateCompleteAuthLinkWithToken(token);
		Mail twoStepAuthMail = getTwoStepAuthMail(police.getFirstName(), completeAuthLink);
		mailService.sendMailTo(twoStepAuthMail, user.getUsername());
	}

	private String generateCompleteAuthLinkWithToken(String token) {
		String completeAuthUrl = applicationUrl + COMPLETE_EXTERNAL_AUTH_ENDPOINT;
		Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, token);
		Pair<String, String> optionIdParam = Pair.of(OPTION_ID_PARAM, EMAIL_OPTION_ID);
		return uriWithQueryParams(completeAuthUrl, asList(tokenParam, optionIdParam));
	}

	private void waitTillUserAuthComplete(User user) {
		try {
			waitForUserToken(user);
		} catch(InterruptedException e) {
			LOG.info("Token wait thread has been interrupted");
		}
	}

	private void waitForUserToken(User user) throws InterruptedException {
		long waitStartTime = currentTimeMillis();
		synchronized(TOKENS_BUFFER) {
			while(shouldWaitForUserToken(user, waitStartTime)) {
				TOKENS_BUFFER.wait(MAX_TOKEN_WAIT_TIME_MILLIS);
			}
		}
	}

	private boolean shouldWaitForUserToken(User user, long waitStartTime) {
		return hasNoTokenForUserId(user.getId()) && waitTimeIsNotOut(waitStartTime);
	}

	private boolean waitTimeIsNotOut(long waitStartTime) {
		return calculateTimeWaiting(waitStartTime) <= MAX_TOKEN_WAIT_TIME_MILLIS;
	}

	private long calculateTimeWaiting(long waitStartTime) {
		return currentTimeMillis() - waitStartTime;
	}

	private void validateUserAuthCompleted(User user) {
		if(hasNoTokenForUserId(user.getId())) {
			throw new TwoStepAuthException("Authentication confirmation has not been received");
		}
	}

	private boolean hasNoTokenForUserId(String userId) {
		synchronized(TOKENS_BUFFER) {
			return !TOKENS_BUFFER.containsKey(userId);
		}
	}

	private String getTokenReceivedForUser(User user) {
		synchronized(TOKENS_BUFFER) {
			return TOKENS_BUFFER.get(user.getId());
		}
	}

	private void validateToken(String receivedToken, String originalToken) {
		if(areNotEqual(originalToken, receivedToken)) {
			throw new TwoStepAuthException("Token is invalid");
		}
	}

	@Override
	public void completeAuthenticationWithOptionId(String optionId, String token) {
		validateNotNull(token, "Token is required");
		validateIsEmailOptionId(optionId);
		User currentUser = getCurrentUser();
		populateBufferWithTokenOfUser(token, currentUser);
	}

	private void validateIsEmailOptionId(String optionId) {
		validateNotNull(optionId, "Option id should not be null");
		if(isNotEmailOptionId(optionId)) {
			throw new TwoStepAuthException("Unknown option id");
		}
	}

	private boolean isNotEmailOptionId(String optionId) {
		return !hasOptionWithId(optionId);
	}

	@Override
	public boolean hasOptionWithId(String optionId) {
		return EMAIL_OPTION_ID.equals(optionId);
	}

	private void populateBufferWithTokenOfUser(String token, User user) {
		synchronized(TOKENS_BUFFER) {
			TOKENS_BUFFER.put(user.getId(), token);
			TOKENS_BUFFER.notifyAll();
		}
	}

}
