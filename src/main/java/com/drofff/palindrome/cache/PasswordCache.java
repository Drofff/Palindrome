package com.drofff.palindrome.cache;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Optional;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class PasswordCache {

	private static final Cache<String, String> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(30, MINUTES)
			.build();

	private PasswordCache() {}

	public static void savePasswordForUser(String password, User user) {
		CACHE.put(user.getUsername(), password);
	}

	public static String popPasswordForUser(User user) {
		String password = CACHE.getIfPresent(user.getUsername());
		CACHE.invalidate(user.getUsername());
		return Optional.ofNullable(password)
				.orElseThrow(() -> new PalindromeException("No password for user " + user.getUsername()));
	}

}
