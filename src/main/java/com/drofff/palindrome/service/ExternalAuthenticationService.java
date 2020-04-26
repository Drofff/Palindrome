package com.drofff.palindrome.service;

import java.util.Set;

import com.drofff.palindrome.type.ExternalAuthenticationOption;

public interface ExternalAuthenticationService {

	Set<ExternalAuthenticationOption> getExternalAuthenticationOptions();

	String authenticateUsingOptionWithId(String optionId);

	void completeAuthenticationWithOptionId(String optionId, String token);

}
