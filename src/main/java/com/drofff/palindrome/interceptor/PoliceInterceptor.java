package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.constants.EndpointConstants.CREATE_POLICE_PROFILE_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

import javax.servlet.http.HttpServletRequest;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.PoliceService;

public class PoliceInterceptor extends RedirectInterceptor {

	private final PoliceService policeService;

	public PoliceInterceptor(PoliceService policeService) {
		this.policeService = policeService;
	}

	@Override
	protected boolean isRequestRedirectNeeded(HttpServletRequest request) {
		User currentUser = getCurrentUser();
		return currentUser.isPolice() && policeService.hasNoPoliceProfile(currentUser);
	}

	@Override
	protected String redirectUri() {
		return CREATE_POLICE_PROFILE_ENDPOINT;
	}

}
