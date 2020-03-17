package com.drofff.palindrome.interceptor;

import static com.drofff.palindrome.utils.AuthenticationUtils.setCurrentUser;
import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthorizationService;

public class AuthorizationTokenInterceptor extends HandlerInterceptorAdapter {

    private static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";

    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;

    public AuthorizationTokenInterceptor(AuthorizationService authorizationService,
                                         AuthenticationService authenticationService) {
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            authorizeUserByToken(request);
            return true;
        } catch (ValidationException e) {
			writeMessageToResponse(e.getMessage(), response);
            return false;
        }
    }

    private void authorizeUserByToken(HttpServletRequest request) {
	    String token = getAuthorizationTokenFromHeader(request);
	    String userId = authorizationService.getUserIdByAuthorizationToken(token);
	    User user = authenticationService.getUserById(userId);
	    setCurrentUser(user);
    }

    private String getAuthorizationTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_TOKEN_HEADER);
        validateNotNull(token, "Missing authorization token");
        return token;
    }

    private void writeMessageToResponse(String message, HttpServletResponse response) {
    	try {
    		PrintWriter printWriter = response.getWriter();
    		printWriter.println(message);
	    } catch(IOException e) {
    		throw new PalindromeException(e.getMessage());
	    }
    }

}
