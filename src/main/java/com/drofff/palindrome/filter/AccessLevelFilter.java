package com.drofff.palindrome.filter;

import com.drofff.palindrome.enums.AccessLevel;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.AuthorizationTokenService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.drofff.palindrome.utils.AuthenticationUtils.isAuthenticated;
import static com.drofff.palindrome.utils.AuthorizationUtils.getAuthorizationTokenFromHeader;
import static com.drofff.palindrome.utils.AuthorizationUtils.isOpenEndpoint;
import static java.util.Collections.singletonList;

public class AccessLevelFilter implements Filter {

    private static final List<String> READ_ONLY_ENDPOINT_PATTERNS = singletonList("/api/user-app/.*");

    private final AuthorizationTokenService authorizationTokenService;

    public AccessLevelFilter(AuthorizationTokenService authorizationTokenService) {
        this.authorizationTokenService = authorizationTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(isApplicableToRequest(httpServletRequest)) {
            String authorizationToken = getAuthorizationTokenFromHeader(httpServletRequest);
            AccessLevel accessLevel = authorizationTokenService.getAccessLevelOfAuthorizationToken(authorizationToken);
            validateUriIsOfAccessLevel(httpServletRequest.getRequestURI(), accessLevel);
        }
        chain.doFilter(request, response);
    }

    private boolean isApplicableToRequest(HttpServletRequest httpServletRequest) {
        return !shouldIgnoreRequest(httpServletRequest);
    }

    private boolean shouldIgnoreRequest(HttpServletRequest httpServletRequest) {
        return !isAuthenticated() || isOpenEndpoint(httpServletRequest.getRequestURI());
    }

    private void validateUriIsOfAccessLevel(String uri, AccessLevel accessLevel) {
        if(isUriNotOfAccessLevel(uri, accessLevel)) {
            throw new ValidationException("Permission denied");
        }
    }

    private boolean isUriNotOfAccessLevel(String uri, AccessLevel accessLevel) {
        return !isUriOfAccessLevel(uri, accessLevel);
    }

    private boolean isUriOfAccessLevel(String uri, AccessLevel accessLevel) {
        return accessLevel == AccessLevel.FULL || ( isReadOnlyEndpoint(uri) && accessLevel == AccessLevel.READ_ONLY );
    }

    private boolean isReadOnlyEndpoint(String uri) {
        return READ_ONLY_ENDPOINT_PATTERNS.stream()
                .anyMatch(uri::matches);
    }

}
