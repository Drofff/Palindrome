package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public abstract class RedirectInterceptor extends HandlerInterceptorAdapter {

	private static final boolean DEFAULT_SHOULD_BE_AUTHENTICATED = true;

	@Override
	public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if(redirectNeeded(request)) {
			response.sendRedirect(redirectUri());
			return false;
		}
		return true;
	}

	private boolean redirectNeeded(HttpServletRequest request) {
		return isApplicableToRequest(request) && isRequestRedirectNeeded(request);
	}

	private boolean isApplicableToRequest(HttpServletRequest request) {
		String requestedUri = request.getRequestURI();
		return isAuthenticatedIfNeeded() && areNotEqual(requestedUri, redirectUri());
	}

	private boolean isAuthenticatedIfNeeded() {
		return shouldNotBeAuthenticated() || isAuthenticated();
	}

	private boolean shouldNotBeAuthenticated() {
		return !shouldBeAuthenticated();
	}

	protected boolean shouldBeAuthenticated() {
		return DEFAULT_SHOULD_BE_AUTHENTICATED;
	}

	protected abstract boolean isRequestRedirectNeeded(HttpServletRequest request);

	protected abstract String redirectUri();

}