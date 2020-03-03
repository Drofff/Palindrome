package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.constants.EndpointConstants.CREATE_DRIVER_PROFILE_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

import javax.servlet.http.HttpServletRequest;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.DriverService;

public class DriverInterceptor extends RedirectInterceptor {

	private final DriverService driverService;

	public DriverInterceptor(DriverService driverService) {
		this.driverService = driverService;
	}

	@Override
	protected boolean isRequestRedirectNeeded(HttpServletRequest request) {
		User currentUser = getCurrentUser();
		return currentUser.isDriver() && driverService.hasNoDriverProfile(currentUser);
	}

	@Override
	protected String redirectUri() {
		return CREATE_DRIVER_PROFILE_ENDPOINT;
	}

}
