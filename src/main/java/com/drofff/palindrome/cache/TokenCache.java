package com.drofff.palindrome.cache;

import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.listener.TokenListener;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Optional;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.concurrent.TimeUnit.MINUTES;

public class TokenCache {

	private static final Cache<String, TokenListener> LISTENERS_CACHE = Caffeine.newBuilder()
			.expireAfterWrite(20, MINUTES)
			.build();

	private static final String TOKEN_IS_NULL_MESSAGE = "Token should not be null";

	private TokenCache() {}

	public static void doOnTokenReceive(String token, TokenListener tokenListener) {
		validateNotNull(token, TOKEN_IS_NULL_MESSAGE);
		validateNotNull(tokenListener);
		LISTENERS_CACHE.put(token, tokenListener);
	}

	public static void onReceive(String token, String userId, String ... params) {
		validateNotNull(token, TOKEN_IS_NULL_MESSAGE);
		TokenListener tokenListener = LISTENERS_CACHE.getIfPresent(token);
		Optional.ofNullable(tokenListener)
				.orElseThrow(() -> new ValidationException("Invalid token"))
				.onTokenReceived(userId, params);
	}

	public static void removeTokenListener(String token) {
		LISTENERS_CACHE.invalidate(token);
	}

}
