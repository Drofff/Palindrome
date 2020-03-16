package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticationUtils {

	private AuthenticationUtils() {}

	public static boolean isAuthenticated() {
		return Optional.ofNullable(getAuthentication())
				.map(Authentication::getPrincipal)
				.filter(AuthenticationUtils::isUserPrincipal)
				.isPresent();
	}

	private static boolean isUserPrincipal(Object object) {
		return object instanceof User;
	}

	public static User getCurrentUser() {
		Authentication authentication = getAuthentication();
		return (User) authentication.getPrincipal();
	}

	private static Authentication getAuthentication() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return securityContext.getAuthentication();
	}

	public static void authenticateUser(User user) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = (Authentication) user;
		securityContext.setAuthentication(authentication);
	}

}
