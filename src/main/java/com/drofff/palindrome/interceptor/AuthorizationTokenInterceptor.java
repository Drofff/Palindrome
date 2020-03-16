package com.drofff.palindrome.interceptor;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.drofff.palindrome.utils.AuthenticationUtils.authenticateUser;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

public class AuthorizationTokenInterceptor extends HandlerInterceptorAdapter {

    private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";

    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthorizationTokenInterceptor(AuthorizationService authorizationService,
                                         AuthenticationService authenticationService) {
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String token = getAuthorizationTokenFromHeader(request);
            String userId = authorizationService.getUserIdByAuthorizationToken(token);
            User user = authenticationService.getUserById(userId);
            authenticateUser(user);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    private String getAuthorizationTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_TOKEN_HEADER);
        validateNotNull(token, "Missing authorization token");
        return token;
    }

}
