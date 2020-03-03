package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.constants.EndpointConstants.USER_IS_BLOCKED_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

import javax.servlet.http.HttpServletRequest;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.UserBlockService;

public class BlockedUserInterceptor extends RedirectInterceptor {

	private final UserBlockService userBlockService;

	public BlockedUserInterceptor(UserBlockService userBlockService) {
		this.userBlockService = userBlockService;
	}

	@Override
	protected boolean isRequestRedirectNeeded(HttpServletRequest request) {
		User currentUser = getCurrentUser();
		return userBlockService.isUserBlocked(currentUser);
	}

	@Override
	protected String redirectUri() {
		return USER_IS_BLOCKED_ENDPOINT;
	}

}
