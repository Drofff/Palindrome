package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.constants.EndpointConstants.USER_IS_BLOCKED_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.UserBlockService;

public class BlockedUserInterceptor extends HandlerInterceptorAdapter {

	private final UserBlockService userBlockService;

	public BlockedUserInterceptor(UserBlockService userBlockService) {
		this.userBlockService = userBlockService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if(isRequestRedirectNeeded(request)) {
			response.sendRedirect(USER_IS_BLOCKED_ENDPOINT);
			return false;
		}
		return true;
	}

	private boolean isRequestRedirectNeeded(HttpServletRequest request) {
		return isApplicableToRequest(request) && isCurrentUserBlocked();
	}

	private boolean isApplicableToRequest(HttpServletRequest request) {
		String requestedUri = request.getRequestURI();
		return isAuthenticated() && isNotUserIsBlockedEndpoint(requestedUri);
	}

	private boolean isNotUserIsBlockedEndpoint(String uri) {
		return !isUserIsBlockedEndpoint(uri);
	}

	private boolean isUserIsBlockedEndpoint(String uri) {
		return USER_IS_BLOCKED_ENDPOINT.equals(uri);
	}

	private boolean isCurrentUserBlocked() {
		User currentUser = getCurrentUser();
		return userBlockService.isUserBlocked(currentUser);
	}

}
