package com.drofff.palindrome.filter;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.TwoStepAuthException;
import com.drofff.palindrome.service.PoliceService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.ParameterConstants.ACCESS_TOKEN_PARAM;
import static com.drofff.palindrome.constants.RegexConstants.ANY_SYMBOL;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class TwoStepAuthFilter implements Filter {

	private static final List<String> NON_APPLICABLE_URI_PATTERNS = asList(API_ENDPOINTS + ANY_SYMBOL,
			EXTERNAL_AUTH_ENDPOINT, COMPLETE_EXTERNAL_AUTH_ENDPOINT, ERROR_ENDPOINT);

	private final PoliceService policeService;

	public TwoStepAuthFilter(PoliceService policeService) {
		this.policeService = policeService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			processRequest((HttpServletRequest) request);
			chain.doFilter(request, response);
		} catch(TwoStepAuthException e) {
			redirectToTwoStepAuthPage((HttpServletResponse) response);
		}
	}

	private void processRequest(HttpServletRequest request) {
		if(shouldFilterRequest(request)) {
			String accessToken = getAccessTokenFromRequest(request);
			validateAccessToken(accessToken);
		}
	}

	private boolean shouldFilterRequest(HttpServletRequest request) {
		if(isApplicableToRequestUri(request.getRequestURI())) {
			return getCurrentUserIfPresent()
					.filter(this::hasTwoStepAuthEnabled)
					.isPresent();
		}
		return false;
	}

	private boolean isApplicableToRequestUri(String requestUri) {
		return NON_APPLICABLE_URI_PATTERNS.stream()
				.noneMatch(requestUri::matches);
	}

	private Optional<User> getCurrentUserIfPresent() {
		if(isAuthenticated()) {
			User currentUser = getCurrentUser();
			return Optional.of(currentUser);
		}
		return Optional.empty();
	}

	private boolean hasTwoStepAuthEnabled(User user) {
		return user.isPolice() && getCurrentPolice().isTwoStepAuthEnabled();
	}

	private String getAccessTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		return stream(cookies)
				.filter(this::isAccessTokenCookie)
				.map(Cookie::getValue)
				.findFirst()
				.orElseThrow(() -> new TwoStepAuthException("Missing access token"));
	}

	private boolean isAccessTokenCookie(Cookie cookie) {
		return cookie.getName().equals(ACCESS_TOKEN_PARAM);
	}

	private void validateAccessToken(String accessToken) {
		if(isInvalidAccessToken(accessToken)) {
			throw new TwoStepAuthException("Invalid access token");
		}
	}

	private boolean isInvalidAccessToken(String accessToken) {
		return !isValidAccessToken(accessToken);
	}

	private boolean isValidAccessToken(String accessToken) {
		Police police = getCurrentPolice();
		return police.getAccessTokens().contains(accessToken);
	}

	private Police getCurrentPolice() {
		User currentUser = getCurrentUser();
		return policeService.getPoliceByUserId(currentUser.getId());
	}

	private void redirectToTwoStepAuthPage(HttpServletResponse response) throws IOException {
		response.sendRedirect(EXTERNAL_AUTH_ENDPOINT);
	}

}
