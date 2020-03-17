package com.drofff.palindrome.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.palindrome.document.AuthorizationToken;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.AuthorizationTokenRepository;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Duration TOKEN_TIME_TO_LIVE = Duration.of(2, ChronoUnit.DAYS);

    private final AuthorizationTokenRepository authorizationTokenRepository;

    @Autowired
    public AuthorizationServiceImpl(AuthorizationTokenRepository authorizationTokenRepository) {
        this.authorizationTokenRepository = authorizationTokenRepository;
    }

    @Override
    public String generateAuthorizationTokenForUser(User user) {
        LocalDateTime tokenDueDateTime = generateNextTokenDueDateTime();
        AuthorizationToken authorizationToken = new AuthorizationToken.Builder()
		        .forUser(user)
		        .dueDateTime(tokenDueDateTime)
		        .build();
        authorizationTokenRepository.save(authorizationToken);
        return authorizationToken.getToken();
    }

    private LocalDateTime generateNextTokenDueDateTime() {
        return LocalDateTime.now().plus(TOKEN_TIME_TO_LIVE);
    }

    @Override
    public String getUserIdByAuthorizationToken(String token) {
        AuthorizationToken authorizationToken = loadAuthorizationToken(token);
        validateAuthorizationTokenDueDate(authorizationToken);
        return authorizationToken.getUserId();
    }

    private AuthorizationToken loadAuthorizationToken(String token) {
        return authorizationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("Invalid authorization token"));
    }

    private void validateAuthorizationTokenDueDate(AuthorizationToken authorizationToken) {
        if(isExpiredToken(authorizationToken)) {
            throw new ValidationException("Token is no longer valid");
        }
    }

    private boolean isExpiredToken(AuthorizationToken token) {
        LocalDateTime now = LocalDateTime.now();
        return token.getDueDateTime().isBefore(now);
    }

    @Override
    public void removeAllExpiredAuthorizationTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<AuthorizationToken> expiredTokens = authorizationTokenRepository.findByDueDateTimeLessThan(now);
        expiredTokens.forEach(authorizationTokenRepository::delete);
    }

}
