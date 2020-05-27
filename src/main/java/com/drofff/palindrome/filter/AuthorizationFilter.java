package com.drofff.palindrome.filter;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.AuthorizationTokenService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.*;
import static com.drofff.palindrome.constants.RegexConstants.ANY_SYMBOL;
import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;
import static java.util.Arrays.asList;

public class AuthorizationFilter implements Filter {

    private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private static final List<String> OPEN_ENDPOINT_PATTERNS = asList(AUTHENTICATE_API_ENDPOINT,
		    REFRESH_TOKEN_API_ENDPOINT, TWO_STEP_AUTH_API_ENDPOINT, "/api/drivers/.*/photo",
            "/api/polices/.*/photo", API_RESOURCE_ENDPOINTS_BASE + ANY_SYMBOL);

    private final AuthorizationTokenService authorizationTokenService;

    public AuthorizationFilter(AuthorizationTokenService authorizationTokenService) {
        this.authorizationTokenService = authorizationTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestedUri = httpServletRequest.getRequestURI();
        if(requiresAuthorization(requestedUri)) {
            authorizeUserByToken(httpServletRequest);
        }
        chain.doFilter(request, response);
    }

    private boolean requiresAuthorization(String uri) {
    	return !isOpenEndpoint(uri);
    }

    private boolean isOpenEndpoint(String uri) {
    	return OPEN_ENDPOINT_PATTERNS.stream()
			    .anyMatch(uri::matches);
    }

    private void authorizeUserByToken(HttpServletRequest httpServletRequest) {
        String token = getAuthorizationTokenFromHeader(httpServletRequest);
        User user = authorizationTokenService.getUserByAuthorizationToken(token);
        setCurrentUser(user);
    }

    private String getAuthorizationTokenFromHeader(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(AUTHORIZATION_TOKEN_HEADER);
        validateNotNull(token, "Missing authorization token");
        return removeTokenPrefix(token);
    }

    private String removeTokenPrefix(String token) {
        return removePartFromStr(AUTHORIZATION_TOKEN_PREFIX, token);
    }

}
