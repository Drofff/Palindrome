package com.drofff.palindrome.service;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.type.ExternalAuthenticationOption;
import com.drofff.palindrome.type.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.drofff.palindrome.constants.EndpointConstants.COMPLETE_EXTERNAL_AUTH_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.OPTION_ID_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.TOKEN_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.FormattingUtils.uriWithQueryParams;
import static com.drofff.palindrome.utils.MailUtils.getTwoStepAuthMail;
import static java.util.Arrays.asList;

@Service
public class EmailAuthenticator extends TokenAuthenticator {

	private static final String EMAIL_OPTION_ID = "email_option";

	private final PoliceService policeService;
	private final MailService mailService;

	@Value("${application.url}")
	private String applicationUrl;

	@Value("${external.auth.max-token-wait-time}")
	private long maxTokenWaitTime;

	@Autowired
	public EmailAuthenticator(PoliceService policeService, MailService mailService) {
		this.policeService = policeService;
		this.mailService = mailService;
	}

	@Override
	public Set<ExternalAuthenticationOption> getAuthenticationOptions() {
		String email = getCurrentUser().getUsername();
		return new ExternalAuthenticationOption.Builder()
				.usingEmail()
				.withId(EMAIL_OPTION_ID)
				.withLabel(email)
				.singleton();
	}

	@Override
	protected void sendTokenToAuthenticatorOfOptionWithId(String token, String optionId) {
		User currentUser = getCurrentUser();
		Police police = policeService.getPoliceByUserId(currentUser.getId());
		String completeAuthLink = generateCompleteAuthLinkWithToken(token);
		Mail twoStepAuthMail = getTwoStepAuthMail(police.getFirstName(), completeAuthLink);
		mailService.sendMailTo(twoStepAuthMail, currentUser.getUsername());
	}

	private String generateCompleteAuthLinkWithToken(String token) {
		String completeAuthUrl = applicationUrl + COMPLETE_EXTERNAL_AUTH_ENDPOINT;
		Pair<String, String> tokenParam = Pair.of(TOKEN_PARAM, token);
		Pair<String, String> optionIdParam = Pair.of(OPTION_ID_PARAM, EMAIL_OPTION_ID);
		return uriWithQueryParams(completeAuthUrl, asList(tokenParam, optionIdParam));
	}

	@Override
	protected long getMaxTokenWaitTime() {
		return maxTokenWaitTime;
	}

}
