package com.drofff.palindrome.filter;

import static com.drofff.palindrome.constants.EndpointConstants.AUTHENTICATE_API_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.StringUtils.removePartFromStr;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.AuthorizationService;

public class AuthorizationFilter implements Filter {

    private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private static final List<String> OPEN_ENDPOINTS_PATTERNS = Arrays.asList(AUTHENTICATE_API_ENDPOINT,
		    "/api/drivers/.*/photo", "/api/polices/.*/photo");

    private final AuthorizationService authorizationService;

    public AuthorizationFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
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
    	return OPEN_ENDPOINTS_PATTERNS.stream()
			    .anyMatch(uri::matches);
    }

    private void authorizeUserByToken(HttpServletRequest httpServletRequest) {
        String token = getAuthorizationTokenFromHeader(httpServletRequest);
        User user = authorizationService.getUserByToken(token);
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
