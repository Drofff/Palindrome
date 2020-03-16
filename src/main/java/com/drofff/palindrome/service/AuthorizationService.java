package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;

public interface AuthorizationService {

    String generateAuthorizationTokenForUser(User user);

    String getUserIdByAuthorizationToken(String token);

    void removeAllExpiredAuthorizationTokens();

}
