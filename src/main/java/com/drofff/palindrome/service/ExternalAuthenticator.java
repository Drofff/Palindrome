package com.drofff.palindrome.service;

import java.util.Set;

import com.drofff.palindrome.type.ExternalAuthenticationOption;

public interface ExternalAuthenticator {

	Set<ExternalAuthenticationOption> getAuthenticationOptions();

	void authenticateUsingOptionWithId(String optionId);

	void completeAuthenticationWithOptionId(String optionId, String token);

	boolean hasOptionWithId(String optionId);

}
