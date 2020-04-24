package com.drofff.palindrome.service;

import static java.util.Collections.emptySet;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.drofff.palindrome.type.ExternalAuthenticationOption;

@Service
public class PushNotificationAuthenticator implements ExternalAuthenticator {

	private static final String OPTION_ID_PREFIX = "device_";

	@Override
	public Set<ExternalAuthenticationOption> getAuthenticationOptions() {
		return emptySet();
	}

	@Override
	public void authenticateUsingOptionWithId(String optionId) {

	}

	@Override
	public void completeAuthenticationWithOptionId(String optionId, String token) {

	}

	@Override
	public boolean hasOptionWithId(String optionId) {
		return false;
	}

}
