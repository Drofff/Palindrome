package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthenticationTokenService {

    User authenticateUserWithIdByToken(String userId, String token);

    String generateAuthenticationTokenForUser(User user);

    void removeExpiredAuthenticationTokens();

}
