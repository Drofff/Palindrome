package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.constants.EndpointConstants.CREATE_DRIVER_PROFILE_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.StringUtils.areNotEqual;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.service.DriverServiceImpl;

public class DriverInterceptor extends HandlerInterceptorAdapter {

	private final DriverServiceImpl driverService;

	public DriverInterceptor(DriverServiceImpl driverService) {
		this.driverService = driverService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if(isApplicableToRequest(request)) {
			User currentUser = getCurrentUser();
			if(needsDriverProfile(currentUser)) {
				response.sendRedirect(CREATE_DRIVER_PROFILE_ENDPOINT);
				return false;
			}
		}
		return true;
	}

	private boolean isApplicableToRequest(HttpServletRequest request) {
		return isAuthenticated() && hasApplicableRequestUri(request);
	}

	private boolean hasApplicableRequestUri(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return areNotEqual(requestUri, CREATE_DRIVER_PROFILE_ENDPOINT);
	}

	private boolean needsDriverProfile(User user) {
		return isDriver(user) && driverService.hasNoDriverProfile(user);
	}

	private boolean isDriver(User user) {
		return user.getAuthorities().contains(Role.DRIVER);
	}

}
