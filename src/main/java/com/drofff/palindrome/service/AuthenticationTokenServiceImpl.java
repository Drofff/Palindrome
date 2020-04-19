package com.drofff.palindrome.service;

import com.drofff.palindrome.document.AuthenticationToken;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.AuthenticationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

@Service
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {

    private static final Period AUTHENTICATION_TOKEN_TIME_TO_LIVE = Period.ofMonths(2);

    private final AuthenticationTokenRepository authenticationTokenRepository;
    private final UserService userService;

    @Autowired
    public AuthenticationTokenServiceImpl(AuthenticationTokenRepository authenticationTokenRepository, UserService userService) {
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.userService = userService;
    }

    @Override
    public User authenticateUserWithIdByToken(String userId, String token) {
        validateNotNull(userId, "User id should be provided");
        validateNotNull(token, "Authentication token should not be null");
        AuthenticationToken authenticationToken = getAuthenticationTokenByValueAndUserId(token, userId);
        validateTokenIsNotExpired(authenticationToken);
        return userService.getUserById(userId);
    }

    private AuthenticationToken getAuthenticationTokenByValueAndUserId(String tokenValue, String userId) {
        return authenticationTokenRepository.findByValueAndUserId(tokenValue, userId)
                .orElseThrow(() -> new ValidationException("Invalid authentication token"));
    }

    private void validateTokenIsNotExpired(AuthenticationToken authenticationToken) {
        if(authenticationToken.isExpired()) {
            throw new ValidationException("Authentication token has expired");
        }
    }

    @Override
    public String generateAuthenticationTokenForUser(User user) {
        AuthenticationToken authenticationToken = new AuthenticationToken.Builder()
                .forUser(user)
                .expiresAt(nextAuthenticationTokenExpiresAt())
                .build();
        authenticationTokenRepository.save(authenticationToken);
        return authenticationToken.getValue();
    }

    private LocalDateTime nextAuthenticationTokenExpiresAt() {
        LocalDateTime now = LocalDateTime.now();
        return now.plus(AUTHENTICATION_TOKEN_TIME_TO_LIVE);
    }

    @Override
    public void removeExpiredAuthenticationTokens() {
        LocalDateTime now = LocalDateTime.now();
        authenticationTokenRepository.deleteByExpiresAtBefore(now);
    }

}
