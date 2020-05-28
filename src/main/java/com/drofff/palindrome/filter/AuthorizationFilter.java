package com.drofff.palindrome.filter;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.service.AuthorizationTokenService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.AuthorizationUtils.getAuthorizationTokenFromHeader;
import static com.drofff.palindrome.utils.AuthorizationUtils.isOpenEndpoint;

public class AuthorizationFilter implements Filter {

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

    private void authorizeUserByToken(HttpServletRequest httpServletRequest) {
        String token = getAuthorizationTokenFromHeader(httpServletRequest);
        User user = authorizationTokenService.getUserByAuthorizationToken(token);
        setCurrentUser(user);
    }

}
