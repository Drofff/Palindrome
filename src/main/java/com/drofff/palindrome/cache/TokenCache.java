package com.drofff.palindrome.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.drofff.palindrome.document.User;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class TokenCache {

	private static final Cache<String, String> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(20, TimeUnit.MINUTES)
			.build();

	private TokenCache() {}

	public static Optional<String> getTokenForUser(User user) {
		String tokenForUser = CACHE.getIfPresent(user.getUsername());
		return Optional.ofNullable(tokenForUser);
	}

	public static void saveTokenForUser(String token, User user) {
		CACHE.put(user.getUsername(), token);
	}

	public static void removeTokenForUser(User user) {
		CACHE.invalidate(user.getUsername());
	}

}
